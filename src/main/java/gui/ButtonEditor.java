package gui;

import entities.Song;
import utils.SongService;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
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
            if(Objects.equals(label, "delete")){
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    mainWindow.songService.removeSong(mainWindow.actualSongsList.get(buttonRow).getId());
                    mainWindow.revalidateMainWindow();
                    System.out.println("usuwam row:"+ buttonRow);
                }else{
                    System.out.println("nie usuwam");
                }
            }else{
                updateSongWindow = new UpdateSongWindow(mainWindow, mainWindow.actualSongsList.get(buttonRow));
            }
        }
        isPushed = false;
        return label;
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
