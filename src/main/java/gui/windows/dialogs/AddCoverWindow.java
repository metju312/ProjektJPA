package gui.windows.dialogs;

import entities.Song;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;
import utils.CoverService;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

public class AddCoverWindow extends JDialog implements ActionListener {
    private int addSongWindowWidth = 320;
    private int addSongWindowHeight = 270;

    private MainWindow mainWindow;

    private String newTitle;
    private String newType;
    private Integer newLength;
    private Double newRating;

    private JTextField newTitleTextField;
    private JTextField newTypeTextField;
    private JTextField newLengthTextField;
    private JTextField newRatingTextField;

    private JButton addCoverButton;

    private Song chosenSong;

    public AddCoverWindow(MainWindow mainWindow){
        super(mainWindow, "Add new cover relative to chosen song", true);
        this.mainWindow = mainWindow;
        setMainWindowValues();
        setChosenSongFromFirstSong();
        add(generateComboBox(), "wrap");
        add(titlePanel(), "wrap");
        add(typePanel(), "wrap");
        add(lengthPanel(), "wrap");
        add(ratingPanel(), "wrap");
        add(buttonPanel());
        setVisible(true);
    }

    private JComboBox generateComboBox() {
        String[] allSongsTitles = generateAllSongsTitles();
        JComboBox comboBox = new JComboBox(allSongsTitles);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(this);
        return comboBox;
    }

    private String[] generateAllSongsTitles() {
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection songCollection = mainWindow.entityManager.createQuery("SELECT e FROM Song e").getResultList();
            String[] titles = new String[mainWindow.songsPanel.numberOfSongs(songCollection)];
            int j = 0;
            for (Iterator i = songCollection.iterator(); i.hasNext();) {
                Song e = (Song) i.next();
                titles[j] = e.getTitle();
                j++;
            }
            transaction.commit();
            return titles;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        addCoverButton = new JButton("Add cover");
        addCoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTextFields();
                mainWindow.coverService.createCover(newTitle, newType, newLength, newRating, chosenSong);
                mainWindow.coversPanel.refreshCoversTable();
                dispose();
            }
        });
        panel.add(addCoverButton);
        return panel;
    }

    private void fillTextFields() {
        newTitle = newTitleTextField.getText();
        newType = newTypeTextField.getText();
        newLength = Integer.parseInt(newLengthTextField.getText());
        newRating = Double.parseDouble(newRatingTextField.getText());
    }

    private JPanel ratingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Rating: "));
        newRatingTextField = new JTextField(20);
        panel.add(newRatingTextField);
        return panel;
    }

    private JPanel lengthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Length:"));
        newLengthTextField = new JTextField(20);
        panel.add(newLengthTextField);
        return panel;
    }

    private JPanel typePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Type:    "));
        newTypeTextField = new JTextField(20);
        panel.add(newTypeTextField);
        return panel;
    }

    private JPanel titlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Title:     "));
        newTitleTextField = new JTextField(20);
        panel.add(newTitleTextField);
        return panel;
    }


    private void setMainWindowValues() {
        setLayout(new MigLayout());
        setSize(addSongWindowWidth, addSongWindowHeight);
        centerWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox)e.getSource();
        setChosenSongFromComboBox(comboBox);
    }

    public void setChosenSongFromComboBox(JComboBox comboBox) {
        chosenSong = mainWindow.songsPanel.actualSongsList.get(comboBox.getSelectedIndex());
    }

    public void setChosenSongFromFirstSong() {
        chosenSong = mainWindow.songsPanel.actualSongsList.get(0);
    }
}
