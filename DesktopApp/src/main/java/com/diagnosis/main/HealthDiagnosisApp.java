package com.diagnosis.main;

import javax.swing.SwingUtilities;

import com.diagnosis.swing.MainApp;

/**
 *
 * @author quang
 */
public class HealthDiagnosisApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainApp());
    }
}
