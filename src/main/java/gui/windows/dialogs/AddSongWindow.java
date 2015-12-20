package gui.windows.dialogs;

import gui.windows.frames.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddSongWindow extends JDialog {
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

    public AddSongWindow(MainWindow mainWindow){
        super(mainWindow, "Add new song", true);
        this.mainWindow = mainWindow;
        setMainWindowValues();
        add(titlePanel());
        add(typePanel());
        add(lengthPanel());
        add(ratingPanel());
        add(buttonPanel());
        setVisible(true);
    }

    private JPanel buttonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        addSongButton = new JButton("Add song");
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillTextFields();
                mainWindow.songService.createSong(newTitle,newType,newLength,newRating);
                mainWindow.mainWindowHeight=20;
                mainWindow.songsPanel.refreshSongsTable();
                dispose();
            }
        });
        panel.add(addSongButton);
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
        setSize(addSongWindowWidth, addSongWindowHeight);
        centerWindow();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }
}
