package ui;

import db.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservationForm extends JFrame {

    JComboBox<Integer> trainNoBox;
    JTextField trainNameField, classTypeField, passengerField, fromField, toField;
    JFormattedTextField dateField;

    public ReservationForm() {
        setTitle("Reservation Form");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        passengerField = new JTextField();

        trainNoBox = new JComboBox<>();
        loadTrainNumbers(); // loads train numbers from DB

        trainNameField = new JTextField();
        trainNameField.setEditable(false);

        classTypeField = new JTextField();
        classTypeField.setEditable(false);

        dateField = new JFormattedTextField("YYYY-MM-DD");
        fromField = new JTextField();
        toField = new JTextField();

        JButton insertBtn = new JButton("Insert");

        // when user selects a train number → auto-fill train name + class
        trainNoBox.addActionListener(e -> fillTrainDetails());

        insertBtn.addActionListener(e -> insertReservation());

        add(new JLabel("Passenger Name:"));
        add(passengerField);

        add(new JLabel("Train Number:"));
        add(trainNoBox);

        add(new JLabel("Train Name:"));
        add(trainNameField);

        add(new JLabel("Class Type:"));
        add(classTypeField);

        add(new JLabel("Date (YYYY-MM-DD):"));
        add(dateField);

        add(new JLabel("From:"));
        add(fromField);

        add(new JLabel("To:"));
        add(toField);

        add(new JLabel(""));
        add(insertBtn);

        setVisible(true);
    }

    // Load all train numbers from trains table
    private void loadTrainNumbers() {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT train_no FROM trains";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                trainNoBox.addItem(rs.getInt("train_no"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Auto-fill train name + class when train no is selected
    private void fillTrainDetails() {
        int trainNo = (int) trainNoBox.getSelectedItem();

        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT train_name, class_type FROM trains WHERE train_no=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setInt(1, trainNo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                trainNameField.setText(rs.getString("train_name"));
                classTypeField.setText(rs.getString("class_type"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Insert reservation into DB
    private void insertReservation() {
        String passenger = passengerField.getText();
        int trainNo = (int) trainNoBox.getSelectedItem();
        String classType = classTypeField.getText();
        String date = dateField.getText();
        String from = fromField.getText();
        String to = toField.getText();

        try {
            Connection conn = DBConnection.getConnection();

            String query = "INSERT INTO reservations (user_id, passenger_name, train_no, class_type, journey_date, from_place, to_place) VALUES (1, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, passenger);
            pst.setInt(2, trainNo);
            pst.setString(3, classType);
            pst.setString(4, date);
            pst.setString(5, from);
            pst.setString(6, to);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Reservation Inserted Successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
