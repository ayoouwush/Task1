package ui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancellationForm extends JFrame {

    JTextField pnrField;
    JTextArea detailsArea;

    public CancellationForm() {
        setTitle("Cancel Reservation");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JLabel pnrLabel = new JLabel("Enter PNR:");
        pnrField = new JTextField();

        topPanel.add(pnrLabel);
        topPanel.add(pnrField);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);

        JButton fetchBtn = new JButton("Fetch Details");
        fetchBtn.addActionListener(e -> fetchDetails());

        JButton cancelBtn = new JButton("Confirm Cancellation");
        cancelBtn.addActionListener(e -> cancelTicket());

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(fetchBtn);
        bottomPanel.add(cancelBtn);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Step 1: Fetch details by PNR
    private void fetchDetails() {
        String pnr = pnrField.getText();

        try {
            Connection conn = DBConnection.getConnection();
            String q = "SELECT * FROM reservations WHERE pnr_no=?";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setString(1, pnr);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String info =
                        "PNR: " + rs.getInt("pnr_no") + "\n" +
                                "Passenger: " + rs.getString("passenger_name") + "\n" +
                                "Train No: " + rs.getInt("train_no") + "\n" +
                                "Class: " + rs.getString("class_type") + "\n" +
                                "Journey Date: " + rs.getString("journey_date") + "\n" +
                                "From: " + rs.getString("from_place") + "\n" +
                                "To: " + rs.getString("to_place") + "\n" +
                                "Status: " + rs.getString("status");

                detailsArea.setText(info);

            } else {
                JOptionPane.showMessageDialog(this, "PNR not found!");
                detailsArea.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Step 2: Confirm cancellation
    private void cancelTicket() {
        String pnr = pnrField.getText();

        try {
            Connection conn = DBConnection.getConnection();
            String q = "UPDATE reservations SET status='cancelled' WHERE pnr_no=?";
            PreparedStatement pst = conn.prepareStatement(q);
            pst.setString(1, pnr);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Ticket Cancelled Successfully!");
                detailsArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation Failed! Invalid PNR");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
