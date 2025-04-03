package com.diagnosis.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class DiagnosisScreen {

    private List<String> selectedSymptoms;
    private JTabbedPane tabbedPane;
    private JTextArea doctorQuestionArea;
    private int currentQuestionIndex;
    private String[] questions = {
        "Mắt bạn có bị đỏ không?",
        "Bạn có bị sốt không?",
        "Bạn có bị đau đầu không?",
        "Bạn có bị đau họng không?",
        "Bạn có bị ho không?",
        "Bạn có bị chóng mặt không?",
        "Bạn có bị mệt mỏi không?",
        "Bạn có bị buồn nôn không?",
        "Bạn có bị đau bụng không?",
        "Bạn có bị khó thở không?"
    };
    private String[] symptoms = {
        "Đỏ mắt",
        "Sốt",
        "Đau đầu",
        "Đau họng",
        "Ho",
        "Chóng mặt",
        "Mệt mỏi",
        "Buồn nôn",
        "Đau bụng",
        "Khó thở"
    };

    public DiagnosisScreen(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        selectedSymptoms = new ArrayList<>();
        currentQuestionIndex = 0;
    }

    public JPanel createDiagnosisPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        // Panel chính chứa bác sĩ, câu hỏi, và bệnh nhân
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Bác sĩ (bên trái)
        JLabel doctorLabel = new JLabel("Bác sĩ", JLabel.CENTER);
        doctorLabel.setFont(new Font("Arial", Font.BOLD, 16));
        doctorLabel.setForeground(new Color(74, 144, 226));
        try {
            java.net.URL doctorUrl = getClass().getClassLoader().getResource("images/doctor.png");
            if (doctorUrl != null) {
                ImageIcon doctorIcon = new ImageIcon(doctorUrl);
                Image doctorImage = doctorIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                doctorLabel.setIcon(new ImageIcon(doctorImage));
                doctorLabel.setText(""); // Xóa chữ "Bác sĩ" nếu đã có hình ảnh
            } else {
                System.out.println("Không tìm thấy hình ảnh bác sĩ");
                doctorLabel.setText("Bác sĩ");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải hình ảnh bác sĩ: " + e.getMessage());
            doctorLabel.setText("Bác sĩ");
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(doctorLabel, gbc);

        // Câu hỏi của bác sĩ (ở giữa)
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(new Color(200, 220, 255)); // Màu nền nhạt cho textbox
        questionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        doctorQuestionArea = new JTextArea(questions[currentQuestionIndex]);
        doctorQuestionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        doctorQuestionArea.setWrapStyleWord(true);
        doctorQuestionArea.setLineWrap(true);
        doctorQuestionArea.setEditable(false);
        doctorQuestionArea.setBackground(new Color(200, 220, 255));
        doctorQuestionArea.setBorder(null);
        questionPanel.add(doctorQuestionArea, BorderLayout.CENTER);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(questionPanel, gbc);

        // Bệnh nhân (bên phải)
        JLabel patientLabel = new JLabel("Bệnh nhân", JLabel.CENTER);
        patientLabel.setFont(new Font("Arial", Font.BOLD, 16));
        patientLabel.setForeground(new Color(40, 167, 69));
        try {
            java.net.URL patientUrl = getClass().getClassLoader().getResource("images/patient.png");
            if (patientUrl != null) {
                ImageIcon patientIcon = new ImageIcon(patientUrl);
                Image patientImage = patientIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                patientLabel.setIcon(new ImageIcon(patientImage));
                patientLabel.setText(""); // Xóa chữ "Bệnh nhân" nếu đã có hình ảnh
            } else {
                System.out.println("Không tìm thấy hình ảnh bệnh nhân");
                patientLabel.setText("Bệnh nhân");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi tải hình ảnh bệnh nhân: " + e.getMessage());
            patientLabel.setText("Bệnh nhân");
        }

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(patientLabel, gbc);

        // Panel chứa hai nút "Có" và "Không"
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton yesButton = new JButton("Có");
        yesButton.setFont(new Font("Arial", Font.BOLD, 14));
        yesButton.setBackground(new Color(200, 220, 255)); // Màu nhạt
        yesButton.setForeground(Color.BLACK);
        yesButton.setPreferredSize(new Dimension(100, 40));
        yesButton.addActionListener(e -> handleAnswer(true));

        JButton noButton = new JButton("Không");
        noButton.setFont(new Font("Arial", Font.BOLD, 14));
        noButton.setBackground(new Color(200, 220, 255)); // Màu nhạt
        noButton.setForeground(Color.BLACK);
        noButton.setPreferredSize(new Dimension(100, 40));
        noButton.addActionListener(e -> handleAnswer(false));

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.4;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(buttonPanel, gbc);

        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }

    private void handleAnswer(boolean answer) {
        if (answer) {
            selectedSymptoms.add(symptoms[currentQuestionIndex]);
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.length) {
            doctorQuestionArea.setText(questions[currentQuestionIndex]);
        } else {
            if (selectedSymptoms.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Bạn không có triệu chứng nào được chọn!");
                currentQuestionIndex = 0;
                selectedSymptoms.clear();
                doctorQuestionArea.setText(questions[currentQuestionIndex]);
            } else {
                tabbedPane.setComponentAt(0, createResultPanel());
            }
        }
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel resultTitle = new JLabel("Kết quả chẩn đoán", JLabel.LEFT);
        resultTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel symptomsLabel = new JLabel("Triệu chứng bạn: " + String.join(", ", selectedSymptoms));
        symptomsLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel diseasesLabel = new JLabel("Bệnh có thể mắc phải:");
        diseasesLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel diseasesPanel = new JPanel();
        diseasesPanel.setLayout(new BoxLayout(diseasesPanel, BoxLayout.Y_AXIS));
        diseasesPanel.setBackground(Color.WHITE);
        diseasesPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        diseasesPanel.setPreferredSize(new Dimension(300, 100));

        JPanel fluPanel = new JPanel(new BorderLayout(5, 5));
        fluPanel.setBackground(Color.WHITE);
        JLabel fluLabel = new JLabel("Cảm cúm");
        JProgressBar fluBar = new JProgressBar(0, 100);
        fluBar.setValue(80);
        fluBar.setStringPainted(true);
        fluBar.setForeground(new Color(135, 206, 250));
        fluBar.setBackground(Color.WHITE);
        JLabel fluPercentage = new JLabel("80%");
        fluPanel.add(fluLabel, BorderLayout.WEST);
        fluPanel.add(fluBar, BorderLayout.CENTER);
        fluPanel.add(fluPercentage, BorderLayout.EAST);

        JPanel throatPanel = new JPanel(new BorderLayout(5, 5));
        throatPanel.setBackground(Color.WHITE);
        JLabel throatLabel = new JLabel("Viêm họng");
        JProgressBar throatBar = new JProgressBar(0, 100);
        throatBar.setValue(65);
        throatBar.setStringPainted(true);
        throatBar.setForeground(new Color(135, 206, 250));
        throatBar.setBackground(Color.WHITE);
        JLabel throatPercentage = new JLabel("65%");
        throatPanel.add(throatLabel, BorderLayout.WEST);
        throatPanel.add(throatBar, BorderLayout.CENTER);
        throatPanel.add(throatPercentage, BorderLayout.EAST);

        JPanel sinusPanel = new JPanel(new BorderLayout(5, 5));
        sinusPanel.setBackground(Color.WHITE);
        JLabel sinusLabel = new JLabel("Viêm xoang");
        JProgressBar sinusBar = new JProgressBar(0, 100);
        sinusBar.setValue(45);
        sinusBar.setStringPainted(true);
        sinusBar.setForeground(new Color(135, 206, 250));
        sinusBar.setBackground(Color.WHITE);
        JLabel sinusPercentage = new JLabel("45%");
        sinusPanel.add(sinusLabel, BorderLayout.WEST);
        sinusPanel.add(sinusBar, BorderLayout.CENTER);
        sinusPanel.add(sinusPercentage, BorderLayout.EAST);

        diseasesPanel.add(fluPanel);
        diseasesPanel.add(Box.createVerticalStrut(5));
        diseasesPanel.add(throatPanel);
        diseasesPanel.add(Box.createVerticalStrut(5));
        diseasesPanel.add(sinusPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBackground(Color.WHITE);

        JPanel reasonsPanel = new JPanel();
        reasonsPanel.setLayout(new BoxLayout(reasonsPanel, BoxLayout.Y_AXIS));
        reasonsPanel.setBackground(Color.WHITE);
        reasonsPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel reasonsLabel = new JLabel("Lý do có thể dẫn tới bệnh:");
        reasonsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        reasonsPanel.add(reasonsLabel);
        reasonsPanel.add(Box.createVerticalStrut(5));

        String[] reasons = {
            "Thời tiết thay đổi đột ngột",
            "Tiếp xúc với nguồn bệnh",
            "Suy giảm hệ miễn dịch",
            "Thiếu ngủ hoặc mệt mỏi kéo dài"
        };
        for (String reason : reasons) {
            JLabel reasonLabel = new JLabel("• " + reason);
            reasonLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            reasonsPanel.add(reasonLabel);
        }

        JPanel advicePanel = new JPanel();
        advicePanel.setLayout(new BoxLayout(advicePanel, BoxLayout.Y_AXIS));
        advicePanel.setBackground(Color.WHITE);
        advicePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        JLabel adviceLabel = new JLabel("Gợi ý xử lý:");
        adviceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        advicePanel.add(adviceLabel);
        advicePanel.add(Box.createVerticalStrut(5));

        JLabel shouldLabel = new JLabel("Nên:");
        shouldLabel.setFont(new Font("Arial", Font.BOLD, 12));
        advicePanel.add(shouldLabel);
        String[] shouldAdvice = {
            "Nghỉ ngơi đầy đủ",
            "Uống nhiều nước",
            "Đến gặp bác sĩ nếu triệu chứng nặng"
        };
        for (String advice : shouldAdvice) {
            JLabel adviceItem = new JLabel("• " + advice);
            adviceItem.setFont(new Font("Arial", Font.PLAIN, 12));
            advicePanel.add(adviceItem);
        }

        advicePanel.add(Box.createVerticalStrut(10));
        JLabel shouldNotLabel = new JLabel("Không nên:");
        shouldNotLabel.setFont(new Font("Arial", Font.BOLD, 12));
        advicePanel.add(shouldNotLabel);
        String[] shouldNotAdvice = {
            "Làm việc quá sức"
        };
        for (String advice : shouldNotAdvice) {
            JLabel adviceItem = new JLabel("• " + advice);
            adviceItem.setFont(new Font("Arial", Font.PLAIN, 12));
            advicePanel.add(adviceItem);
        }

        bottomPanel.add(reasonsPanel);
        bottomPanel.add(advicePanel);

        JButton backButton = new JButton("Trở về");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(74, 144, 226));
        backButton.setForeground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(100, 30));
        backButton.addActionListener(e -> {
            currentQuestionIndex = 0;
            selectedSymptoms.clear();
            tabbedPane.setComponentAt(0, createDiagnosisPanel());
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(resultTitle);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(symptomsLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(diseasesLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(diseasesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(bottomPanel); // Sửa thành bottomPanel
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(backButton); // Đưa nút "Trở về" trở lại mainPanel

        panel.add(mainPanel, BorderLayout.NORTH); // Đặt mainPanel ở NORTH để khôi phục bố cục
        return panel;
    }
}