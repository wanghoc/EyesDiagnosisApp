package com.diagnosis.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

import weka.classifiers.Classifier;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

public class DiagnosisScreen {

    private List<String> selectedSymptoms;
    private JTabbedPane tabbedPane;
    private JTextArea doctorQuestionArea;
    private int currentQuestionIndex;
    private Classifier model;
    private Instances attributesStructure;
    private Instance currentInstance;
    private ArrayList<Double> instanceValues;

    public DiagnosisScreen(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        selectedSymptoms = new ArrayList<>();
        currentQuestionIndex = 1; // Bắt đầu từ thuộc tính thứ 2 (sau Disease)
        instanceValues = new ArrayList<>();

        try {
            model = (Classifier) SerializationHelper.read("SharedLibrary/models/random_forest_model.rf");
            attributesStructure = loadFeatureStructure("SharedLibrary/models/features.arff");
            // Khởi tạo instanceValues với giá trị đầu tiên là 0.0 cho Disease
            instanceValues.add(0.0);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải model: " + e.getMessage());
        }
    }

    private Instances loadFeatureStructure(String filePath) throws Exception {
        ConverterUtils.DataSource source = new ConverterUtils.DataSource(filePath);
        Instances structure = source.getStructure();
        structure.setClassIndex(0);
        return structure;
    }

    private ImageIcon loadImage(String imageName, int width, int height) {
        try {
            // Tải hình ảnh từ resources
            java.net.URL imageUrl = DiagnosisScreen.class.getResource("/images/" + imageName);
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                Image image = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(image);
            } else {
                System.err.println("Không tìm thấy hình ảnh: " + imageName);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải hình ảnh " + imageName + ": " + e.getMessage());
            return null;
        }
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
        JPanel doctorPanel = new JPanel();
        doctorPanel.setLayout(new BoxLayout(doctorPanel, BoxLayout.Y_AXIS));
        doctorPanel.setBackground(Color.WHITE);

        JLabel doctorImageLabel = new JLabel();
        ImageIcon doctorIcon = loadImage("doctor.png", 100, 100);
        if (doctorIcon != null) {
            doctorImageLabel.setIcon(doctorIcon);
        }

        JLabel doctorTextLabel = new JLabel("Bác sĩ", JLabel.CENTER);
        doctorTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        doctorTextLabel.setForeground(new Color(74, 144, 226));

        doctorPanel.add(doctorImageLabel);
        doctorPanel.add(doctorTextLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(doctorPanel, gbc);

        // Câu hỏi của bác sĩ (ở giữa)
        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setBackground(new Color(200, 220, 255)); // Màu nền nhạt cho textbox
        questionPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        doctorQuestionArea = new JTextArea(getQuestionText(currentQuestionIndex));
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
        JPanel patientPanel = new JPanel();
        patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
        patientPanel.setBackground(Color.WHITE);

        JLabel patientImageLabel = new JLabel();
        ImageIcon patientIcon = loadImage("patient.png", 100, 100);
        if (patientIcon != null) {
            patientImageLabel.setIcon(patientIcon);
        }

        JLabel patientTextLabel = new JLabel("Bệnh nhân", JLabel.CENTER);
        patientTextLabel.setFont(new Font("Arial", Font.BOLD, 16));
        patientTextLabel.setForeground(new Color(40, 167, 69));

        patientPanel.add(patientImageLabel);
        patientPanel.add(patientTextLabel);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(patientPanel, gbc);

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

    private String getQuestionText(int attributeIndex) {
        if (attributeIndex == attributesStructure.numAttributes() - 1) {
            return "Vui lòng nhập tuổi của bạn:";
        }
        String attributeName = attributesStructure.attribute(attributeIndex).name();
        return "Bạn có " + attributeName + " không?";
    }

    private void handleAnswer(boolean answer) {
        if (currentQuestionIndex < attributesStructure.numAttributes() - 1) {
            // Thêm câu trả lời vào instanceValues
            instanceValues.add(answer ? 1.0 : 0.0);

            // Chuyển sang câu hỏi tiếp theo
            currentQuestionIndex++;

            // Nếu là câu hỏi cuối cùng (về tuổi)
            if (currentQuestionIndex == attributesStructure.numAttributes() - 1) {
                // Hiển thị dialog nhập tuổi
                String ageStr = JOptionPane.showInputDialog(null, "Vui lòng nhập tuổi của bạn:", "Nhập tuổi",
                        JOptionPane.QUESTION_MESSAGE);
                try {
                    double age = Double.parseDouble(ageStr);
                    instanceValues.add(age);
                    showDiagnosisResult();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số tuổi hợp lệ!");
                    currentQuestionIndex--;
                    instanceValues.remove(instanceValues.size() - 1);
                }
            } else {
                // Cập nhật câu hỏi tiếp theo
                doctorQuestionArea.setText(getQuestionText(currentQuestionIndex));
            }
        }
    }

    private void showDiagnosisResult() {
        try {
            Instance instance = new DenseInstance(1.0,
                    instanceValues.stream().mapToDouble(Double::doubleValue).toArray());
            instance.setDataset(attributesStructure);

            double[] probabilities = model.distributionForInstance(instance);
            String[] diseases = new String[probabilities.length];
            for (int i = 0; i < probabilities.length; i++) {
                diseases[i] = attributesStructure.classAttribute().value(i);
            }

            tabbedPane.setComponentAt(0, createResultPanel(diseases[0], probabilities, diseases));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi dự đoán: " + e.getMessage());
        }

        // Reset lại trạng thái
        currentQuestionIndex = 1;
        instanceValues.clear();
        instanceValues.add(0.0); // Thêm lại giá trị đầu tiên cho Disease
    }

    private JPanel createResultPanel(String diagnosis, double[] probabilities, String[] diseases) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);

        JLabel resultTitle = new JLabel("Kết quả chẩn đoán: " + diagnosis, JLabel.LEFT);
        resultTitle.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel diseasesLabel = new JLabel("Bệnh có thể mắc phải:");
        diseasesLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel diseasesPanel = new JPanel();
        diseasesPanel.setLayout(new BoxLayout(diseasesPanel, BoxLayout.Y_AXIS));
        diseasesPanel.setBackground(Color.WHITE);
        diseasesPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        diseasesPanel.setPreferredSize(new Dimension(300, 100));

        // Hiển thị các bệnh và tỷ lệ dự đoán
        for (int i = 0; i < diseases.length; i++) {
            JPanel diseasePanel = new JPanel(new BorderLayout(5, 5));
            diseasePanel.setBackground(Color.WHITE);

            JLabel diseaseLabel = new JLabel(diseases[i]);
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue((int) (probabilities[i] * 100));
            progressBar.setStringPainted(true);
            progressBar.setForeground(new Color(135, 206, 250));
            progressBar.setBackground(Color.WHITE);

            JLabel percentageLabel = new JLabel(String.format("%.2f%%", probabilities[i] * 100));
            diseasePanel.add(diseaseLabel, BorderLayout.WEST);
            diseasePanel.add(progressBar, BorderLayout.CENTER);
            diseasePanel.add(percentageLabel, BorderLayout.EAST);

            diseasesPanel.add(diseasePanel);
            diseasesPanel.add(Box.createVerticalStrut(5));
        }

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
        mainPanel.add(diseasesLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(diseasesPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(backButton);

        panel.add(mainPanel, BorderLayout.NORTH);
        return panel;
    }
}