package gui.windows.dialogs;

import entities.Author;
import entities.Cover;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class AddAuthorWindow extends JDialog implements ActionListener {
    private int addAuthorWindowWidth = 320;
    private int addAuthorWindowHeight = 270;

    private MainWindow mainWindow;

    private String newFirstName;
    private String newLastName;
    private Integer newAge;
    private String newGenre;

    private JTextField newFirstNameTextField;
    private JTextField newLastNameTextField;
    private JTextField newAgeTextField;
    private JTextField newGenreTextField;

    private JButton addAuthorButton;

    private Cover chosenCover;

    private JComboBox comboBox;

    public AddAuthorWindow(MainWindow mainWindow){
        super(mainWindow, "Add new author", true);
        this.mainWindow = mainWindow;
        setMainWindowValues();
        add(generateComboBox(), "wrap");
        add(firstNamePanel(), "wrap");
        add(lastNamePanel(), "wrap");
        add(agePanel(), "wrap");
        add(genrePanel(), "wrap");
        add(buttonPanel());
        setVisible(true);
    }

    private JComboBox generateComboBox() {
        String[] allCoversTitles = generateAllCoversTitles();
        comboBox = new JComboBox(allCoversTitles);
        comboBox.setSelectedIndex(0);
        comboBox.addActionListener(this);
        return comboBox;
    }

    private String[] generateAllCoversTitles() {
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection coverCollection = mainWindow.entityManager.createQuery("SELECT e FROM Cover e").getResultList();
            String[] titles = new String[mainWindow.coversPanel.numberOfCovers(coverCollection)+1];
            titles[0] = "Without cover";
            int j = 1;
            for (Iterator i = coverCollection.iterator(); i.hasNext();) {
                Cover e = (Cover) i.next();
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
        addAuthorButton = new JButton("Add author");
        addAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTextFields();
                if(comboBox.getSelectedIndex()==0){
                    mainWindow.authorService.createAuthor(newFirstName, newLastName, newAge, newGenre);
                }else{
                    addAuthorToDB();
                }
                mainWindow.authorsPanel.refreshAuthorsTable();
                dispose();
            }
        });
        panel.add(addAuthorButton);
        return panel;
    }

    private void addAuthorToDB() {
        List<Cover> resultList = mainWindow.entityManager.createQuery("from Cover g where g.title = :t", Cover.class).setParameter("t", chosenCover.getTitle()).getResultList();
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        transaction.begin();
        Author author = new Author();
        author.setFirstName(newFirstName);
        author.setLastName(newLastName);
        author.setAge(newAge);
        author.setGenre(newGenre);
        for (Cover cover : resultList) {
            author.getCoverList().add(cover);
            cover.getAuthorList().add(author);
        }
        mainWindow.entityManager.persist(author);
        transaction.commit();
    }

    private void fillTextFields() {
        newFirstName = newFirstNameTextField.getText();
        newLastName = newLastNameTextField.getText();
        newAge = Integer.parseInt(newAgeTextField.getText());
        newGenre = newGenreTextField.getText();
    }

    private JPanel genrePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Genre:  "));
        newGenreTextField = new JTextField(20);
        panel.add(newGenreTextField);
        return panel;
    }

    private JPanel agePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("Age:      "));
        newAgeTextField = new JTextField(20);
        panel.add(newAgeTextField);
        return panel;
    }

    private JPanel lastNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("LastName:"));
        newLastNameTextField = new JTextField(20);
        panel.add(newLastNameTextField);
        return panel;
    }

    private JPanel firstNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.add(new JLabel("FirstName:"));
        newFirstNameTextField = new JTextField(20);
        panel.add(newFirstNameTextField);
        return panel;
    }


    private void setMainWindowValues() {
        setLayout(new MigLayout());
        setSize(addAuthorWindowWidth, addAuthorWindowHeight);
        centerWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComboBox comboBox = (JComboBox)e.getSource();
        setChosenCoverFromComboBox(comboBox);
    }

    public void setChosenCoverFromComboBox(JComboBox comboBox) {
        chosenCover = mainWindow.coversPanel.actualCoversList.get(comboBox.getSelectedIndex()-1);
    }

    public void setChosenCoverFromFirstCover() {
        chosenCover = mainWindow.coversPanel.actualCoversList.get(0);
    }
}
