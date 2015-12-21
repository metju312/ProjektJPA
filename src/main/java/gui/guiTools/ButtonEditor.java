package gui.guiTools;

import gui.windows.dialogs.UpdateAuthorWindow;
import gui.windows.dialogs.UpdateCoverWindow;
import gui.windows.frames.MainWindow;
import gui.windows.dialogs.UpdateSongWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;


public class ButtonEditor extends DefaultCellEditor  {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private MainWindow mainWindow;
    private int buttonRow;
    private int buttonColumn;

    private UpdateSongWindow updateSongWindow;
    private UpdateCoverWindow updateCoverWindow;
    private UpdateAuthorWindow updateAuthorWindow;

    public ButtonEditor(JCheckBox checkBox, MainWindow mainWindow) {
        super(checkBox);
        this.mainWindow = mainWindow;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        buttonRow=row;
        buttonColumn=column;
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }

        ImageIcon img = new ImageIcon("images/add_icon.jpg");
        button.setIcon(img);
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            onPushAction();
        }
        isPushed = false;
        return label;
    }

    private void onPushAction() {
        if(Objects.equals(label, "DeleteSong")) {
            deleteSong();
        }else if(Objects.equals(label, "UpdateSong")){
            updateSong();
        }else if(Objects.equals(label, "DeleteCover")){
            deleteCover();
        }else if(Objects.equals(label, "UpdateCover")){
            updateCover();
        }else if(Objects.equals(label, "DeleteAuthor")){
            deleteAuthor();
        }else if(Objects.equals(label, "UpdateAuthor")){
            updateAuthor();
        }
    }

    private void updateAuthor() {
        updateAuthorWindow = new UpdateAuthorWindow(mainWindow, mainWindow.authorsPanel.actualAuthorsList.get(buttonRow));
    }

    private void deleteAuthor() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            mainWindow.authorService.removeAuthor(mainWindow.authorsPanel.actualAuthorsList.get(buttonRow).getId());
            mainWindow.authorsPanel.refreshAuthorsTable();
            System.out.println("usuwam row:"+ buttonRow);
        }else{
            System.out.println("nie usuwam");
        }
    }

    private void updateCover() {
        updateCoverWindow = new UpdateCoverWindow(mainWindow, mainWindow.coversPanel.actualCoversList.get(buttonRow));
    }

    private void deleteCover() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            mainWindow.coverService.removeCover(mainWindow.coversPanel.actualCoversList.get(buttonRow).getId());
            mainWindow.coversPanel.refreshCoversTable();
            System.out.println("usuwam row:"+ buttonRow);
        }else{
            System.out.println("nie usuwam");
        }
    }

    private void updateSong() {
        updateSongWindow = new UpdateSongWindow(mainWindow, mainWindow.songsPanel.actualSongsList.get(buttonRow));
    }

    private void deleteSong() {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
            mainWindow.songService.removeSong(mainWindow.songsPanel.actualSongsList.get(buttonRow).getId());
            mainWindow.songsPanel.refreshSongsTable();
            System.out.println("usuwam row:"+ buttonRow);
        }else{
            System.out.println("nie usuwam");
        }
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
