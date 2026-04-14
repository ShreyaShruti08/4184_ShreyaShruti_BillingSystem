package gui;

import service.BillingService;
import util.FileHandler;
import model.Item;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillingUI extends JFrame {

    private JTextField nameField, qtyField, priceField;
    private JTextArea outputArea;
    private BillingService service;

    public BillingUI() {
        service = new BillingService();

        setTitle("Grocery Billing System");
        setSize(550, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Background color
        getContentPane().setBackground(new Color(240, 248, 255));

        // Title
        JLabel title = new JLabel("GROCERY BILLING SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(50, 10, 450, 30);
        add(title);

        // Labels
        JLabel nameLabel = new JLabel("Item Name:");
        nameLabel.setBounds(50, 60, 100, 25);
        add(nameLabel);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setBounds(50, 100, 100, 25);
        add(qtyLabel);

        JLabel priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 140, 100, 25);
        add(priceLabel);

        // Fields
        nameField = new JTextField();
        nameField.setBounds(150, 60, 150, 25);
        add(nameField);

        qtyField = new JTextField();
        qtyField.setBounds(150, 100, 150, 25);
        add(qtyField);

        priceField = new JTextField();
        priceField.setBounds(150, 140, 150, 25);
        add(priceField);

        // Buttons
        JButton addBtn = new JButton("Add");
        styleButton(addBtn);
        addBtn.setBounds(330, 60, 120, 30);
        add(addBtn);

        JButton removeBtn = new JButton("Remove");
        styleButton(removeBtn);
        removeBtn.setBounds(330, 100, 120, 30);
        add(removeBtn);

        JButton billBtn = new JButton("Generate Bill");
        styleButton(billBtn);
        billBtn.setBounds(150, 190, 150, 35);
        add(billBtn);

        JButton clearBtn = new JButton("Clear");
        styleButton(clearBtn);
        clearBtn.setBounds(320, 190, 100, 35);
        add(clearBtn);

        JButton saveBtn = new JButton("Save");
        styleButton(saveBtn);
        saveBtn.setBounds(50, 190, 80, 35);
        add(saveBtn);

        JButton loadBtn = new JButton("Load");
        styleButton(loadBtn);
        loadBtn.setBounds(50, 230, 80, 35);
        add(loadBtn);

        // Output Area with Scroll
        outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(50, 280, 450, 250);
        add(scrollPane);

        // ADD ITEM
        addBtn.addActionListener(e -> {
            String name = nameField.getText();
            String qtyText = qtyField.getText();
            String priceText = priceField.getText();

            if (name.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!");
                return;
            }

            try {
                int qty = Integer.parseInt(qtyText);
                double price = Double.parseDouble(priceText);

                service.addItem(name, qty, price);
                refreshDisplay();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
            }
        });

        // REMOVE ITEM
        removeBtn.addActionListener(e -> {
            String name = nameField.getText();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter item name!");
                return;
            }

            boolean removed = service.removeItem(name);

            if (removed) {
                refreshDisplay();
            } else {
                JOptionPane.showMessageDialog(this, "Item not found!");
            }
        });

        // GENERATE BILL
        billBtn.addActionListener(e -> {
            double total = service.calculateTotal();
            double gst = service.getGST();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            StringBuilder bill = new StringBuilder();
            bill.append("===== GROCERY BILL =====\n");
            bill.append("Date: ").append(dtf.format(now)).append("\n\n");

            bill.append("Item\tQty\tPrice\n");
            bill.append("--------------------------\n");

            for (Item item : service.getItems()) {
                bill.append(item.getName()).append("\t")
                    .append(item.getQuantity()).append("\t")
                    .append(item.getPrice()).append("\n");
            }

            bill.append("\n--------------------------\n");
            bill.append("GST (5%): ").append(gst).append("\n");
            bill.append("Total: ").append(total);

            outputArea.setText(bill.toString());
        });

        // SAVE
        saveBtn.addActionListener(e -> {
            FileHandler.saveToFile(outputArea.getText());
            JOptionPane.showMessageDialog(this, "Saved!");
        });

        // LOAD
        loadBtn.addActionListener(e -> {
            outputArea.setText(FileHandler.loadFromFile());
        });

        // CLEAR
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            qtyField.setText("");
            priceField.setText("");
            outputArea.setText("");
        });

        setVisible(true);
    }

    // Button Styling Method
    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
    }

    // Refresh display
    private void refreshDisplay() {
        outputArea.setText("Item\tQty\tPrice\n");
        outputArea.append("--------------------------\n");

        for (Item item : service.getItems()) {
            outputArea.append(
                item.getName() + "\t" +
                item.getQuantity() + "\t" +
                item.getPrice() + "\n"
            );
        }
    }
}
