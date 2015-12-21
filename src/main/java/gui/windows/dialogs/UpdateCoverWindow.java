package gui.windows.dialogs;

import entities.Cover;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateCoverWindow extends JDialog {
    private int addCoverWindowWidth = 320;
    private int addCoverWindowHeight = 240;
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

    private Cover coverToUpdate;
    private Cover newCover;

    public UpdateCoverWindow(MainWindow mainWindow, Cover coverToUpdate) {
        super(mainWindow, "Update cover", true);
        this.mainWindow = mainWindow;
        this.coverToUpdate = coverToUpdate;
        setMainWindowValues();
        add(titlePanel(), "wrap");
        add(typePanel(), "wrap");
        add(lengthPanel(), "wrap");
        add(ratingPanel(), "wrap");
        add(buttonPanel());
        fillTextFields();
        setVisible(true);
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        addCoverButton = new JButton("Update cover");
        addCoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTextFields();
                mainWindow.coverService.updateCover(coverToUpdate.getId(), newTitle, newType, newLength, newRating);
                mainWindow.mainWindowHeight = 20;
                mainWindow.coversPanel.refreshCoversTable();
                dispose();
            }
        });
        panel.add(addCoverButton);
        return panel;
    }

    private void fillTextFields() {
        newTitle = coverToUpdate.getTitle();
        newType = coverToUpdate.getType();
        newLength = coverToUpdate.getLength();
        newRating = coverToUpdate.getRating();
        newTitleTextField.setText(newTitle);
        newTypeTextField.setText(newType);
        newLengthTextField.setText(newLength.toString());
        newRatingTextField.setText(newRating.toString());
    }

    private void updateTextFields() {
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
        setSize(addCoverWindowWidth, addCoverWindowHeight);
        centerWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
}