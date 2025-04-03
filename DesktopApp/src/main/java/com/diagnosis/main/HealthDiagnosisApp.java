package com.diagnosis.main;

import javax.swing.SwingUtilities;

import com.diagnosis.swing.MainApp;
// Classifier model = loadModel("SharedLibrary/models/decision_tree_model.j48");

/**
 *
 * @author quang
 */
public class HealthDiagnosisApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}
