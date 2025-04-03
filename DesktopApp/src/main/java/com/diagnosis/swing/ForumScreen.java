package com.diagnosis.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ForumScreen {
    private JFrame frame;

    public ForumScreen(JFrame frame) {
        this.frame = frame;
    }

    public JPanel createForumPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Tiêu đề "Diễn đàn"
        JLabel forumTitle = new JLabel("Diễn đàn", JLabel.LEFT);
        forumTitle.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(forumTitle, BorderLayout.NORTH);

        // Nút "Đăng bài mới"
        JButton newPostButton = new JButton("Đăng bài mới");
        newPostButton.setFont(new Font("Arial", Font.BOLD, 14));
        newPostButton.setBackground(new Color(74, 144, 226)); // #4A90E2
        newPostButton.setForeground(Color.WHITE);
        newPostButton.setPreferredSize(new Dimension(150, 30));
        newPostButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Chức năng đăng bài mới đang được phát triển!"));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(newPostButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Bảng thảo luận
        String[] columnNames = {"Tiêu đề", "Người đăng", "Ngày đăng", "Hành động"};
        Object[][] data = {
            {"Cách giảm đau đầu hiệu quả?", "NguyenVanA", "05/03/2025", createForumActionButton("Cách giảm đau đầu hiệu quả?", "NguyenVanA", "Tôi hay bị đau đầu vào buổi tối, có cách nào giảm đau hiệu quả không?")},
            {"Có ai bị viêm họng mãn tính không?", "TranThiB", "03/03/2025", createForumActionButton("Có ai bị viêm họng mãn tính không?", "TranThiB", "Tôi bị viêm họng mãn tính đã 2 năm, có ai có kinh nghiệm chữa trị không?")},
            {"Chia sẻ kinh nghiệm chữa cảm cúm", "LeMinhC", "01/03/2025", createForumActionButton("Chia sẻ kinh nghiệm chữa cảm cúm", "LeMinhC", "Mỗi lần cảm cúm tôi rất mệt, mọi người có cách nào chữa nhanh không?")}
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Chỉ cột "Hành động" được chỉnh sửa để kích hoạt Editor
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Object.class : getValueAt(0, columnIndex).getClass();
            }
        };
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
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
            pageButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Chuyển đến trang " + page));
            paginationPanel.add(pageButton);
        }
        panel.add(paginationPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createForumActionButton(String title, String author, String content) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setBackground(Color.WHITE);

        JButton viewButton = new JButton("Xem");
        viewButton.setFont(new Font("Arial", Font.PLAIN, 12));
        viewButton.setBackground(new Color(74, 144, 226)); // #4A90E2
        viewButton.setForeground(Color.WHITE);
        viewButton.setPreferredSize(new Dimension(50, 25));
        // Không cần ActionListener trực tiếp ở đây, để Editor xử lý
        buttonPanel.add(viewButton);

        // Lưu thông tin để Editor sử dụng
        buttonPanel.putClientProperty("title", title);
        buttonPanel.putClientProperty("author", author);
        buttonPanel.putClientProperty("content", content);

        return buttonPanel;
    }

    private void showDiscussionDialog(String title, String author, String content) {
        // Tạo cửa sổ pop-up
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(frame);
        dialog.getContentPane().setBackground(Color.WHITE);

        // Tiêu đề và nội dung chính
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea("Người đăng: " + author + "\n\n" + content);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 12));
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setEditable(false);
        contentArea.setBackground(Color.WHITE);
        contentArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JScrollPane contentScrollPane = new JScrollPane(contentArea);
        contentScrollPane.setPreferredSize(new Dimension(450, 80));
        topPanel.add(contentScrollPane, BorderLayout.CENTER);

        dialog.add(topPanel, BorderLayout.NORTH);

        // Phần bình luận
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
        commentsPanel.setBackground(Color.WHITE);
        commentsPanel.setBorder(BorderFactory.createTitledBorder("Bình luận"));

        // Dữ liệu mẫu cho bình luận
        List<String> comments = new ArrayList<>();
        if (title.equals("Cách giảm đau đầu hiệu quả?")) {
            comments.add("TranThiB: Tôi hay uống trà gừng, rất hiệu quả!");
            comments.add("LeMinhC: Bạn thử massage thái dương xem, mình thấy đỡ lắm.");
        } else if (title.equals("Có ai bị viêm họng mãn tính không?")) {
            comments.add("NguyenVanA: Tôi cũng bị, bạn đã thử súc miệng nước muối chưa?");
            comments.add("LeMinhC: Bạn nên đi khám chuyên khoa tai mũi họng nhé!");
        } else if (title.equals("Chia sẻ kinh nghiệm chữa cảm cúm")) {
            comments.add("TranThiB: Tôi thường xông hơi với lá bạc hà, rất dễ chịu.");
            comments.add("NguyenVanA: Uống nhiều nước cam và nghỉ ngơi là cách mình hay làm.");
        }

        JTextArea commentsArea = new JTextArea();
        commentsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        commentsArea.setEditable(false);
        commentsArea.setBackground(Color.WHITE);
        for (String comment : comments) {
            commentsArea.append(comment + "\n\n");
        }
        JScrollPane commentsScrollPane = new JScrollPane(commentsArea);
        commentsScrollPane.setPreferredSize(new Dimension(450, 150));
        commentsPanel.add(commentsScrollPane);

        dialog.add(commentsPanel, BorderLayout.CENTER);

        // Khu vực nhập bình luận
        JPanel commentInputPanel = new JPanel(new BorderLayout(5, 5));
        commentInputPanel.setBackground(Color.WHITE);
        commentInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField commentField = new JTextField("Nhập bình luận của bạn...");
        commentField.setForeground(Color.GRAY);
        commentField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (commentField.getText().equals("Nhập bình luận của bạn...")) {
                    commentField.setText("");
                    commentField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (commentField.getText().isEmpty()) {
                    commentField.setText("Nhập bình luận của bạn...");
                    commentField.setForeground(Color.GRAY);
                }
            }
        });

        JButton submitButton = new JButton("Gửi");
        submitButton.setFont(new Font("Arial", Font.BOLD, 12));
        submitButton.setBackground(new Color(74, 144, 226)); // #4A90E2
        submitButton.setForeground(Color.WHITE);
        submitButton.setPreferredSize(new Dimension(80, 25));
        submitButton.addActionListener(e -> {
            String comment = commentField.getText().trim();
            if (!comment.isEmpty() && !comment.equals("Nhập bình luận của bạn...")) {
                JOptionPane.showMessageDialog(dialog, "Bình luận của bạn đã được gửi: " + comment);
                commentField.setText(""); // Xóa ô nhập sau khi gửi
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập bình luận trước khi gửi!");
            }
        });

        commentInputPanel.add(commentField, BorderLayout.CENTER);
        commentInputPanel.add(submitButton, BorderLayout.EAST);

        dialog.add(commentInputPanel, BorderLayout.SOUTH);

        // Hiển thị cửa sổ
        dialog.setVisible(true);
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
                    String title = (String) buttonPanel.getClientProperty("title");
                    String author = (String) buttonPanel.getClientProperty("author");
                    String content = (String) buttonPanel.getClientProperty("content");
                    showDiscussionDialog(title, author, content);
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