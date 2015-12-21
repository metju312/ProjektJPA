package gui.windows.dialogs;

import entities.Author;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateAuthorWindow extends JDialog {
    private int addAuthorWindowWidth = 320;
    private int addAuthorWindowHeight = 240;
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

    private Author authorToUpdate;
    private Author newAuthor;

    public UpdateAuthorWindow(MainWindow mainWindow, Author authorToUpdate) {
        super(mainWindow, "Update author", true);
        this.mainWindow = mainWindow;
        this.authorToUpdate = authorToUpdate;
        setMainWindowValues();
        add(firstNamePanel(), "wrap");
        add(lastNamePanel(), "wrap");
        add(agePanel(), "wrap");
        add(genrePanel(), "wrap");
        add(buttonPanel());
        fillTextFields();
        setVisible(true);
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        addAuthorButton = new JButton("Update author");
        addAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTextFields();
                mainWindow.authorService.updateAuthor(authorToUpdate.getId(), newFirstName, newLastName, newAge, newGenre);
                mainWindow.mainWindowHeight = 20;
                mainWindow.authorsPanel.refreshAuthorsTable();
                dispose();
            }
        });
        panel.add(addAuthorButton);
        return panel;
    }

    private void fillTextFields() {
        newFirstName = authorToUpdate.getFirstName();
        newLastName = authorToUpdate.getLastName();
        newAge = authorToUpdate.getAge();
        newGenre = authorToUpdate.getGenre();
        newFirstNameTextField.setText(newFirstName);
        newLastNameTextField.setText(newLastName);
        newAgeTextField.setText(newAge.toString());
        newGenreTextField.setText(newGenre.toString());
    }

    private void updateTextFields() {
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
}