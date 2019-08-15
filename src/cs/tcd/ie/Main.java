package cs.tcd.ie;
//17331565 Anton Yamkovoy
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel container = new JPanel();
        JScrollPane jsp = new JScrollPane(container);
        container.setPreferredSize(new Dimension(500, 250));
        container.setLayout(null);

        JLabel lblHelloWorld = new JLabel("Hello World");
        lblHelloWorld.setBounds(10, 11, 101, 14);
        container.add(lblHelloWorld);

        frame.getContentPane().add(jsp);
    }
}