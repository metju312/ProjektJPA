package gui.windows.dialogs;

import entities.Author;
import entities.Cover;
import entities.Song;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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

    private JComboBox comboBoxSong;
    private JComboBox comboBoxAuthor;

    private Song chosenSong;
    private Author chosenAuthor;

    private boolean firstLaunch = true;

    public AddCoverWindow(MainWindow mainWindow){
        super(mainWindow, "Add new cover relative to chosen song", true);
        this.mainWindow = mainWindow;
        setMainWindowValues();
        setChosenSongFromFirstSong();
        setChosenAuthorFromFirstAuthor();
        add(generateComboBoxSong(), "wrap");
        add(generateComboBoxAuthor(), "wrap");
        add(titlePanel(), "wrap");
        add(typePanel(), "wrap");
        add(lengthPanel(), "wrap");
        add(ratingPanel(), "wrap");
        add(buttonPanel());
        setVisible(true);
    }

    private JComboBox generateComboBoxAuthor() {
        String[] allAuthorsTitles = generateAllAuthorsTitles();
        comboBoxAuthor = new JComboBox(allAuthorsTitles);
        comboBoxAuthor.setSelectedIndex(0);
        comboBoxAuthor.addActionListener(this);
        return comboBoxAuthor;
    }

    private String[] generateAllAuthorsTitles() {
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection authorCollection = mainWindow.entityManager.createQuery("SELECT e FROM Author e").getResultList();
            String[] titles = new String[mainWindow.authorsPanel.numberOfAuthors(authorCollection)+1];
            titles[0] = "Without author";
            int j = 1;
            for (Iterator i = authorCollection.iterator(); i.hasNext();) {
                Author e = (Author) i.next();
                titles[j] = e.getFirstName() + " " + e.getLastName();
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

    private JComboBox generateComboBoxSong() {
        String[] allSongsTitles = generateAllSongsTitles();
        comboBoxSong = new JComboBox(allSongsTitles);
        comboBoxSong.setSelectedIndex(1);
        comboBoxSong.addActionListener(this);
        return comboBoxSong;
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
                if(comboBoxAuthor.getSelectedIndex()==0){
                    mainWindow.coverService.createCover(newTitle, newType, newLength, newRating, chosenSong);
                }else{
                    addCoverToDB();
                }

                mainWindow.coversPanel.refreshCoversTable();
                dispose();
            }
        });
        panel.add(addCoverButton);
        return panel;
    }

    private void addCoverToDB() {
        List<Author> resultList = mainWindow.entityManager.createQuery("from Author g where g.firstName = :f and g.lastName = :l", Author.class).setParameter("f", chosenAuthor.getFirstName()).setParameter("l", chosenAuthor.getLastName()).getResultList();
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        transaction.begin();
        Cover cover = new Cover();
        cover.setTitle(newTitle);
        cover.setType(newType);
        cover.setLength(newLength);
        cover.setRating(newRating);
        cover.setSong(chosenSong);
        for (Author author : resultList) {
            cover.getAuthorList().add(author);
            author.getCoverList().add(cover);
        }
        mainWindow.entityManager.persist(cover);
        transaction.commit();
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
        //JComboBox comboBox = (JComboBox)e.getSource();
        setChosenSongFromComboBox();
        setChosenAuthorFromComboBox();
    }

    private void setChosenAuthorFromComboBox() {
        if(firstLaunch){
            chosenAuthor = mainWindow.authorsPanel.actualAuthorsList.get(comboBoxAuthor.getSelectedIndex());
            firstLaunch=false;
        }else{
            chosenAuthor = mainWindow.authorsPanel.actualAuthorsList.get(comboBoxAuthor.getSelectedIndex()-1);
        }

    }

    public void setChosenAuthorFromFirstAuthor() {
        chosenAuthor = mainWindow.authorsPanel.actualAuthorsList.get(0);
    }

    public void setChosenSongFromComboBox() {
        chosenSong = mainWindow.songsPanel.actualSongsList.get(comboBoxSong.getSelectedIndex());
    }

    public void setChosenSongFromFirstSong() {
        chosenSong = mainWindow.songsPanel.actualSongsList.get(0);
    }
}
