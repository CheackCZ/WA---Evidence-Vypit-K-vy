package Java.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CoffeeApp extends JFrame {
    
    private JPanel panel;
    
    private JComboBox<String> userComboBox;
    private JSlider coffeeSlider;
    private JLabel coffeeAmountLabel;
    private JButton submitButton;
    private JTextArea summaryTextArea;
    
    private ApiClient apiClient;

    // Constructor to initialize the GUI
    public CoffeeApp() {

        // Initialize API client
        apiClient = new ApiClient();

        setTitle("Evidence Kávy");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        // User dropdown
        userComboBox = new JComboBox<>();
        panel.add(new JLabel("Vyber osobu:"));
        panel.add(userComboBox);

        // Coffee slider
        coffeeSlider = new JSlider(1, 10, 5);
        coffeeAmountLabel = new JLabel("Množství kávy: 5");
        panel.add(coffeeAmountLabel);
        coffeeSlider.addChangeListener(e -> coffeeAmountLabel.setText("Množství kávy: " + coffeeSlider.getValue()));
        panel.add(coffeeSlider);

        // Submit button
        submitButton = new JButton("Odeslat");
        submitButton.addActionListener(new SubmitButtonListener());
        panel.add(submitButton);

        // Summary text area
        summaryTextArea = new JTextArea(10, 30);
        summaryTextArea.setEditable(false);
        panel.add(new JScrollPane(summaryTextArea));

        // Load initial data
        loadPeople();

        // Add panel to frame
        add(panel);
    }

    // Load user data from the server
    private void loadPeople() {
        try {
            List<Person> people = apiClient.getPeopleList();
            for (Person person : people) {
                userComboBox.addItem(person.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Action listener for the submit button
    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedUser = (String) userComboBox.getSelectedItem();
            int coffeeAmount = coffeeSlider.getValue();

            // Send data using the API client
            try {
                apiClient.saveCoffeeData(selectedUser, coffeeAmount);
                summaryTextArea.setText("Data odeslána: " + selectedUser + " vypil " + coffeeAmount + " káv.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}