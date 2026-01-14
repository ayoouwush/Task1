package ui;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class MainSystem extends JFrame {

    public MainSystem() {
        FlatLightLaf.setup();

        setTitle("Online Reservation System");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton reservationBtn = new JButton("Make Reservation");
        JButton cancelBtn = new JButton("Cancel Reservation");

        reservationBtn.addActionListener(e -> new ReservationForm());
        cancelBtn.addActionListener(e -> new CancellationForm());

        JPanel panel = new JPanel();
        panel.add(reservationBtn);
        panel.add(cancelBtn);

        add(panel);
        setVisible(true);
    }
}
