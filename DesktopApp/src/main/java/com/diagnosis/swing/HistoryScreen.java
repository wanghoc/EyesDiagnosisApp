package com.diagnosis.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HistoryScreen {
    public JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Tiêu đề "Lịch sử chẩn đoán"
        JLabel historyTitle = new JLabel("Lịch sử chẩn đoán", JLabel.LEFT);
        historyTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(historyTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
        String[] columnNames = {"Ngày", "Triệu chứng", "Chẩn đoán", "Độ chính xác", "Hành động"};
        Object[][] data = {
            {"24/02/2025", "Sốt, Đau đầu, Đau họng", "Cảm cúm", "80%", createActionButtons("24/02/2025")},
            {"20/02/2025", "Đau bụng, Buồn nôn", "Viêm dạ dày", "75%", createActionButtons("20/02/2025")},
            {"15/02/2025", "Ho, Sốt, Khó thở", "Viêm phế quản", "85%", createActionButtons("15/02/2025")}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Object.class : getValueAt(0, columnIndex).getClass();
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        table.setBackground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setEnabled(true); // Đảm bảo bảng có thể tương tác
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Phân trang
        JPanel paginationPanel = new JPanel();
        paginationPanel.setBackground(Color.WHITE);
        paginationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        String[] pageNumbers = {"1", "2", "3", ">"};
        for (String page : pageNumbers) {
            JButton pageButton = new JButton(page);
            pageButton.setFont(new Font("Arial", Font.PLAIN, 12));
            pageButton.setBackground(new Color(74, 144, 226)); // #4A90E2
            pageButton.setForeground(Color.WHITE);
            pageButton.setPreferredSize(new Dimension(40, 25));
            pageButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Chuyển đến trang " + page));
            paginationPanel.add(pageButton);
        }
        panel.add(paginationPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createActionButtons(String date) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new JButton("Xem");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewButton.setBackground(new Color(74, 144, 226)); // #4A90E2
        viewButton.setForeground(Color.WHITE);
        viewButton.setPreferredSize(new Dimension(50, 25));
        viewButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Xem chi tiết chẩn đoán ngày " + date));

        JButton deleteButton = new JButton("Xóa");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setBackground(new Color(220, 53, 69)); // #DC3545
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setPreferredSize(new Dimension(50, 25));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa chẩn đoán ngày " + date + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Logic xóa (giả lập)
                JOptionPane.showMessageDialog(null, "Đã xóa chẩn đoán ngày " + date);
            }
        });

        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }

    // Renderer cho nút trong bảng
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JPanel panel;

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JPanel) {
                panel = (JPanel) value;
                panel.setOpaque(true); // Đảm bảo panel hiển thị đúng màu nền
                return panel;
            }
            return this;
        }
    }

    // Editor cho nút trong bảng
    class ButtonEditor extends DefaultCellEditor {
        private JPanel buttonPanel;
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            setClickCountToStart(1); // Cho phép click ngay lập tức
            button = new JButton();
            button.setFocusable(false); // Đảm bảo nút không chiếm focus
            button.addActionListener(e -> {
                if (buttonPanel != null) {
                    fireEditingStopped(); // Kết thúc quá trình chỉnh sửa để đóng Editor
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof JPanel) {
                buttonPanel = (JPanel) value;
                button.setText(((JButton) ((JPanel) value).getComponent(0)).getText());
                button.setBackground(new Color(74, 144, 226)); // #4A90E2
                button.setForeground(Color.WHITE);
                return button;
            }
            return null;
        }

        @Override
        public Object getCellEditorValue() {
            return buttonPanel;
        }
    }
}