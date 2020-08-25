package org.example.windows;

import com.mysql.cj.protocol.Resultset;
import org.example.App;
import org.example.Category;
import org.example.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.example.Visuals.*;

public class EntryWindow extends JFrame implements ActionListener {

    PreparedStatement saveStatement = App.connection.prepareStatement("insert into allentries (name, category, price, timestamp) values (?, ?, ?, ?)");

    private JLabel titleLabel = new JLabel("Entry register");
    private JLabel name = new JLabel("Name");
    private JTextField nameTextField = new JTextField(20);
    private JRadioButton fruitRadioButton = new JRadioButton("Fruit");
    private JRadioButton fishRadioButton = new JRadioButton("Fish");
    private JRadioButton insectsRadioButton = new JRadioButton("Insect");
    private JRadioButton flowersRadioButton = new JRadioButton("Flower");
    private JRadioButton shellsRadioButton = new JRadioButton("Shell");
    private JRadioButton fossilRadioButton = new JRadioButton("Fossil");
    private JTextField priceTextField = new JTextField(7);
    private JButton saveButton = new JButton("Save");
    private JButton viewRecordsButton = new JButton("View Records");
    private JLabel priceLabel = new JLabel("Price");
    private JLabel categoryLabel = new JLabel("Category");
    private ButtonGroup radioButtonGroup;

    public EntryWindow() throws SQLException {
        setLayout(new GridLayout(0, 1));
        getContentPane().setBackground(minnaSpecial);
        add(titleLabel);
        titleLabel.setFont(new Font("Constantia", Font.ITALIC, 19));
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        titleLabel.setPreferredSize(new Dimension(100, 20));

        add(Box.createVerticalStrut(verticalSpacerSmall));
        add(name);
        name.setFont(smallConstantia);
        add(nameTextField);
        nameTextField.setHorizontalAlignment(SwingConstants.LEFT);
        add(Box.createVerticalStrut(verticalSpacerSmall));
        add(categoryLabel);
        categoryLabel.setFont(smallConstantia);
        add(insectsRadioButton);
        insectsRadioButton.setFont(miniConstantia);
        insectsRadioButton.setActionCommand("INSECT");
        add(fruitRadioButton);
        fruitRadioButton.setFont(miniConstantia);
        fruitRadioButton.setActionCommand("FRUIT");
        add(flowersRadioButton);
        flowersRadioButton.setFont(miniConstantia);
        flowersRadioButton.setActionCommand("FLOWER");
        add(fishRadioButton);
        fishRadioButton.setFont(miniConstantia);
        fishRadioButton.setActionCommand("FISH");
        add(shellsRadioButton);
        shellsRadioButton.setFont(miniConstantia);
        shellsRadioButton.setActionCommand("SHELL");
        add(fossilRadioButton);
        fossilRadioButton.setFont(miniConstantia);
        fossilRadioButton.setActionCommand("FOSSIL");
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(insectsRadioButton);
        radioButtonGroup.add(fruitRadioButton);
        radioButtonGroup.add(flowersRadioButton);
        radioButtonGroup.add(fishRadioButton);
        radioButtonGroup.add(shellsRadioButton);
        radioButtonGroup.add(fossilRadioButton);
        fruitRadioButton.setBackground(minnaSpecial);
        fishRadioButton.setBackground(minnaSpecial);
        fossilRadioButton.setBackground(minnaSpecial);
        add(Box.createVerticalStrut(verticalSpacerSmall));
        add(priceLabel);
        priceLabel.setFont(smallConstantia);
        add(priceTextField);
        add(Box.createVerticalStrut(7));
        JPanel buttonPanel = new JPanel();
        add(buttonPanel);
        buttonPanel.add(saveButton);
        buttonPanel.add(viewRecordsButton);

        saveButton.addActionListener(this);
        viewRecordsButton.addActionListener(this);

        setPreferredSize(new Dimension(250, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //if save then retrieve information, then save it to DB/file
        if (e.getSource().equals(saveButton)){
            int price;
            try {
                price = Integer.parseInt(priceTextField.getText());
            } catch (NumberFormatException e1){
                price = 0;
            }
            Entry entry = new Entry(nameTextField.getText(), Category.valueOf(radioButtonGroup.getSelection().getActionCommand()), price, LocalDateTime.now());
            System.out.println(entry);
            try {
                saveStatement.setString(1, entry.getName());
                saveStatement.setString(2, entry.getCategory().toString());
                saveStatement.setInt(3, entry.getPrice());
                saveStatement.setTimestamp(4, Timestamp.valueOf(entry.getTimestamp()));
                saveStatement.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            nameTextField.setText("");
            radioButtonGroup.clearSelection();
            priceTextField.setText("");
        } else { //else then view, open view window
            try {
                new RecordWindow();
                this.dispose();
            } catch (SQLException throwables) {
                System.out.println("Problem in RecordWindow class");
                throwables.printStackTrace();
            }
        }
    }

}
