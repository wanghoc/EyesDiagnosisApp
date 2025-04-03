package com.diagnosis.swing;

import javax.swing.*;
import java.awt.*;

public class MainApp {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    public MainApp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Ứng dụng Chẩn đoán Bệnh");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Ứng dụng Chẩn đoán Bệnh", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBackground(new Color(74, 144, 226)); // #4A90E2
        titleLabel.setOpaque(true);
        frame.add(titleLabel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(74, 144, 226)); // #4A90E2
        tabbedPane.setForeground(Color.WHITE);

        // Khởi tạo các màn hình
        DiagnosisScreen diagnosisScreen = new DiagnosisScreen(tabbedPane);
        HistoryScreen historyScreen = new HistoryScreen();
        ForumScreen forumScreen = new ForumScreen(frame);

        // Thêm các tab
        tabbedPane.addTab("Chẩn đoán", diagnosisScreen.createDiagnosisPanel());
        tabbedPane.addTab("Lịch sử", historyScreen.createHistoryPanel());
        tabbedPane.addTab("Diễn đàn", forumScreen.createForumPanel());

        tabbedPane.setBackgroundAt(0, new Color(47, 107, 180)); // #2F6BB4
        tabbedPane.setForegroundAt(0, Color.WHITE);
        tabbedPane.setBackgroundAt(1, new Color(47, 107, 180)); // #2F6BB4
        tabbedPane.setForegroundAt(1, Color.WHITE);
        tabbedPane.setBackgroundAt(2, new Color(47, 107, 180)); // #2F6BB4 khi tab "Diễn đàn" được chọn
        tabbedPane.setForegroundAt(2, Color.WHITE);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}