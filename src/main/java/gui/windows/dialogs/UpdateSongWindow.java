package gui.windows.dialogs;

import entities.Song;
import gui.windows.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateSongWindow extends JDialog {
    private int addSongWindowWidth = 400;
    private int addSongWindowHeight = 300;
    private MainWindow mainWindow;

    private String newTitle;
    private String newType;
    private Integer newLength;
    private Double newRating;

    private JTextField newTitleTextField;
    private JTextField newTypeTextField;
    private JTextField newLengthTextField;
    private JTextField newRatingTextField;

    private JButton addSongButton;

    private Song songToUpdate;
    private Song newSong;

    public UpdateSongWindow(MainWindow mainWindow, Song songToUpdate) {
        super(mainWindow, "Update song", true);
        this.mainWindow = mainWindow;
        this.songToUpdate = songToUpdate;
        setMainWindowValues();
        add(titlePanel());
        add(typePanel());
        add(lengthPanel());
        add(ratingPanel());
        add(buttonPanel());
        fillTextFields();
        setVisible(true);
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        addSongButton = new JButton("Update song");
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTextFields();
                mainWindow.songService.updateSong(songToUpdate.getId(), newTitle, newType, newLength, newRating);
                mainWindow.mainWindowHeight = 20;
                mainWindow.songsPanel.refreshSongsTable();
                dispose();
            }
        });
        panel.add(addSongButton);
        return panel;
    }

    private void fillTextFields() {
        newTitle = songToUpdate.getTitle();
        newType = songToUpdate.getType();
        newLength = songToUpdate.getLength();
        newRating = songToUpdate.getRating();
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
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Rating:"));
        newRatingTextField = new JTextField(20);
        panel.add(newRatingTextField);
        return panel;
    }

    private JPanel lengthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Length:"));
        newLengthTextField = new JTextField(20);
        panel.add(newLengthTextField);
        return panel;
    }

    private JPanel typePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Type:"));
        newTypeTextField = new JTextField(20);
        panel.add(newTypeTextField);
        return panel;
    }

    private JPanel titlePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("Title:"));
        newTitleTextField = new JTextField(20);
        panel.add(newTitleTextField);
        return panel;
    }


    private void setMainWindowValues() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        //setLocationRelativeTo(null);
        setSize(addSongWindowWidth, addSongWindowHeight);
        centerWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
}