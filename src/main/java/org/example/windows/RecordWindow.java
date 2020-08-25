package org.example.windows;

import org.example.App;
import org.example.Category;
import org.example.Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.example.Visuals.*;

public class RecordWindow extends JFrame implements ActionListener {

    PreparedStatement allQuery = App.connection.prepareStatement("select * from allentries");
    ResultSet entries = allQuery.executeQuery();
    List<Entry> entriesList = compileEntries();
    List<Entry> selectedCategory;
    Category currentCategory = Category.ALL;
    Comparator<Entry> currentSorting = Comparator.comparing(Entry::getName);

    JPanel leftPanel = new JPanel();
    JLabel categoryLabel = new JLabel("Category");
    ButtonGroup categoryGroup = new ButtonGroup();
    JToggleButton categoryInsect = new JToggleButton("Insects");
    JToggleButton categoryFruit = new JToggleButton("Fruits");
    JToggleButton categoryFlower = new JToggleButton("Flowers");
    JToggleButton categoryFish = new JToggleButton("Fish");
    JToggleButton categoryShell = new JToggleButton("Shells");
    JToggleButton categoryFossil = new JToggleButton("Fossils");
    JToggleButton categoryAll = new JToggleButton("All entries");
    JLabel sortingLabel = new JLabel("Sorting mode");
    ButtonGroup sortingGroup = new ButtonGroup();
    JRadioButton sortingName = new JRadioButton("Sorted alphabetically");
    JRadioButton sortingPriceLoHi = new JRadioButton("Sorted by price, high to low");
    JRadioButton sortingPriceHiLo = new JRadioButton("Sorted by price, low to high");
    JButton registerEntry = new JButton("Register new entry");

    JTextArea entryViewer = new JTextArea(30, 25);
    JScrollPane scrollPane = new JScrollPane(entryViewer);

    public RecordWindow() throws SQLException {
        setLayout(new FlowLayout());
        add(leftPanel);
        //leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setLayout(new GridLayout(0, 1));
        leftPanel.setBackground(minnaSpecial);
        leftPanel.add(categoryLabel);
        categoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        categoryLabel.setFont(smallConstantia);
        leftPanel.add(Box.createVerticalStrut(verticalSpacerSmall));
        categoryGroup.add(categoryInsect);
        categoryGroup.add(categoryFruit);
        categoryGroup.add(categoryFlower);
        categoryGroup.add(categoryFish);
        categoryGroup.add(categoryShell);
        categoryGroup.add(categoryFossil);
        categoryGroup.add(categoryAll);

        leftPanel.add(categoryInsect);
        categoryInsect.setActionCommand("INSECT");
        categoryInsect.addActionListener(this);

        leftPanel.add(categoryFruit);
        categoryFruit.setActionCommand("FRUIT");
        categoryFruit.addActionListener(this);

        leftPanel.add(categoryFlower);
        categoryFlower.setActionCommand("FLOWER");
        categoryFlower.addActionListener(this);

        leftPanel.add(categoryFish);
        categoryFish.setActionCommand("FISH");
        categoryFish.addActionListener(this);

        leftPanel.add(categoryShell);
        categoryShell.setActionCommand("SHELL");
        categoryShell.addActionListener(this);

        leftPanel.add(categoryFossil);
        categoryFossil.setActionCommand("FOSSIL");
        categoryFossil.addActionListener(this);

        leftPanel.add(categoryAll);
        categoryAll.setActionCommand("ALL");
        categoryAll.addActionListener(this);

        categoryAll.setSelected(true);
        leftPanel.add(Box.createVerticalStrut(verticalSpacerSmall));
        leftPanel.add(sortingLabel);
        leftPanel.add(Box.createVerticalStrut(verticalSpacerSmall));
        leftPanel.add(sortingName);
        leftPanel.add(sortingPriceHiLo);
        leftPanel.add(sortingPriceLoHi);

        sortingGroup.add(sortingName);
        sortingName.setActionCommand("NAME");
        sortingName.addActionListener(this);

        sortingGroup.add(sortingPriceHiLo);
        sortingPriceHiLo.setActionCommand("HILO");
        sortingPriceHiLo.addActionListener(this);

        sortingGroup.add(sortingPriceLoHi);
        sortingPriceLoHi.setActionCommand("LOHI");
        sortingPriceLoHi.addActionListener(this);

        sortingName.setSelected(true);
        leftPanel.add(Box.createVerticalStrut(verticalSpacerSmall));
        leftPanel.add(registerEntry);
        registerEntry.addActionListener(this);
        registerEntry.setActionCommand("new");
        leftPanel.add(Box.createVerticalGlue());

        //scrollPane.add(entryViewer);
        add(scrollPane);
        compileList();
        entryViewer.setFont(smallConstantia);
        entryViewer.setBackground(minnaSpecial);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "new":
                this.dispose();
                try {
                    new EntryWindow();
                } catch (SQLException throwables) {
                    System.out.println("Problem in entry window class");
                    throwables.printStackTrace();
                }
                break;
            case "INSECT":
                changeCategory(Category.INSECT);
                break;
            case "FRUIT":
                changeCategory(Category.FRUIT);
                break;
            case "FLOWER":
                changeCategory(Category.FLOWER);
                break;
            case "FISH":
                changeCategory(Category.FISH);
                break;
            case "SHELL":
                changeCategory(Category.SHELL);
                break;
            case "FOSSIL":
                changeCategory(Category.FOSSIL);
                break;
            case "ALL":
                changeCategory(Category.ALL);
                break;
            case "NAME":
                changeSorting(Entry::getName);
                break;
            case "HILO":
                changeSorting(Entry::getPrice);
                break;
            case "LOHI":
                changeSorting(Entry::getInvertedPrice);
                break;
            default:
        }
        compileList();
    }

    public ArrayList<Entry> compileEntries() throws SQLException {
        ArrayList<Entry> entriesList = new ArrayList<>();
        entries.next();
        System.out.println(entries.getRow());
        if (!entries.isFirst()){
            return entriesList;
        }
        while (!entries.isAfterLast()){
            Timestamp timestamp;
            try {
                 timestamp = entries.getTimestamp("timestamp");
            } catch (NullPointerException e){
                System.out.println("null for timestamp");
                timestamp = null;
            }
            Entry entry = new Entry(entries.getString("name"), Category.valueOf(entries.getString("category")), entries.getInt("price"), timestamp.toLocalDateTime());
            entriesList.add(entry);
            entries.next();
        }
        entriesList.forEach(System.out::println);
        selectedCategory = new ArrayList<>(entriesList);
        selectedCategory.sort(Comparator.comparing(Entry::getName));
        return entriesList;
    }

    public void compileList(){
        StringBuilder compiled = new StringBuilder();
        selectedCategory.stream()
                .peek(a->System.out.println("SelectedCategory:  " + a))
                .filter(a -> currentCategory == Category.ALL || a.getCategory().equals(currentCategory))
                .peek(a->System.out.println("Filtered by category:  " + a))
                .sorted(currentSorting)
                .peek(a->System.out.println("Sorted:  " + a))
                .forEach(a->compiled.append(a).append("\n"));
        entryViewer.setText(compiled.toString());
        System.out.println("Compiled\n" + compiled.toString());
        entryViewer.repaint();
    }

    private void changeSorting(Function<? super Entry, Comparable> sorting){
        currentSorting = Comparator.comparing(sorting);
        selectedCategory.sort(currentSorting);
    }

    private void changeCategory(Category category){
        currentCategory = category;
        selectedCategory = entriesList.stream().filter(a -> currentCategory == Category.ALL || a.getCategory().equals(currentCategory)).collect(Collectors.toList());
    }
}
