package gui.windows.panels;

import entities.Song;
import gui.guiTools.ButtonEditor;
import gui.guiTools.ButtonRenderer;
import gui.windows.dialogs.AddSongWindow;
import gui.windows.frames.MainWindow;
import net.miginfocom.swing.MigLayout;

import javax.persistence.EntityTransaction;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class SongsPanel extends JPanel {
    private MainWindow mainWindow;
    private JScrollPane scrollPane;

    private String[] columnNames = {"Title","Type","Length","Rating","UpdateSong","DeleteSong"};
    private JTable songsTable;

    public java.util.List<Song> actualSongsList = new ArrayList<>();

    public SongsPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setCustomLayout();
        add(generateScrollPane(), "wrap");
        add(generateAddSongButton());
    }

    private JButton generateAddSongButton() {
        ImageIcon img = new ImageIcon("add_icon.jpg");
        JButton addSongButton = new JButton("Add song",img);
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddSongWindow addSongWindow = new AddSongWindow(mainWindow);
            }
        });
        return addSongButton;
    }

    private JScrollPane generateScrollPane() {
        scrollPane = new JScrollPane(generateTable());
        return scrollPane;
    }

    private JTable generateTable() {
        songsTable = new JTable(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
        return songsTable;
    }

    private void setTableButtons() {
        songsTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        songsTable.getColumn("UpdateSong").setCellRenderer(new ButtonRenderer());
        songsTable.getColumn("UpdateSong").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));
        songsTable.getColumn("DeleteSong").setCellRenderer(new ButtonRenderer());
        songsTable.getColumn("DeleteSong").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));


        songsTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        songsTable.setPreferredScrollableViewportSize(new Dimension(500, 120));
    }

    private Object[][] generateDataFromDataBase() {

        actualSongsList.clear();
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection songCollection = mainWindow.entityManager.createQuery("SELECT e FROM Song e").getResultList();
            Object[][] data = new Object[numberOfSongs(songCollection)][6];

            int j = 0;
            for (Iterator i = songCollection.iterator(); i.hasNext();) {
                Song e = (Song) i.next();
                actualSongsList.add(e);
                data[j][0] = e.getTitle();
                data[j][1] = e.getType();
                data[j][2] = e.getLength();
                data[j][3] = e.getRating();
                data[j][4] = "UpdateSong";
                data[j][5] = "DeleteSong";
                j++;
            }
            transaction.commit();
            return data;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return null;
    }

    public int numberOfSongs(Collection songCollection) {
        int counter = 0;
        for (Iterator i = songCollection.iterator(); i.hasNext();) {
            Song e = (Song) i.next();
            counter++;
        }
        return counter;
    }

    public void refreshSongsTable(){
        songsTable.setModel(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
    }

    private void setCustomLayout() {
        setLayout(new MigLayout());
    }
}
