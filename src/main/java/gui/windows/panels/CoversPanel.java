package gui.windows.panels;

import entities.Cover;
import entities.Song;
import gui.guiTools.ButtonEditor;
import gui.guiTools.ButtonRenderer;
import gui.windows.dialogs.AddCoverWindow;
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

public class CoversPanel extends JPanel {
    private MainWindow mainWindow;
    private JScrollPane scrollPane;

    private String[] columnNames = {"Title","Type","Length","Rating","UpdateCover","DeleteCover"};
    private JTable coversTable;

    public java.util.List<Cover> actualCoversList = new ArrayList<>();

    public CoversPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setCustomLayout();
        add(generateScrollPane(), "wrap");
        add(generateAddCoverButton());
    }

    private JButton generateAddCoverButton() {
        ImageIcon img = new ImageIcon("add_icon.jpg");
        JButton addCoverButton = new JButton("Add cover",img);
        addCoverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddCoverWindow addCoverWindow = new AddCoverWindow(mainWindow);
            }
        });
        return addCoverButton;
    }

    private JScrollPane generateScrollPane() {
        scrollPane = new JScrollPane(generateTable());
        return scrollPane;
    }

    private JTable generateTable() {
        coversTable = new JTable(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
        return coversTable;
    }

    private void setTableButtons() {
        coversTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        coversTable.getColumn("UpdateCover").setCellRenderer(new ButtonRenderer());
        coversTable.getColumn("UpdateCover").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));
        coversTable.getColumn("DeleteCover").setCellRenderer(new ButtonRenderer());
        coversTable.getColumn("DeleteCover").setCellEditor(new ButtonEditor(new JCheckBox(),mainWindow));


        coversTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        coversTable.setPreferredScrollableViewportSize(new Dimension(500, 120));
    }

    private Object[][] generateDataFromDataBase() {

        actualCoversList.clear();
        EntityTransaction transaction = mainWindow.entityManager.getTransaction();
        try {
            transaction.begin();
            Collection coverCollection = mainWindow.entityManager.createQuery("SELECT e FROM Cover e").getResultList();
            Object[][] data = new Object[numberOfCovers(coverCollection)][6];

            int j = 0;
            for (Iterator i = coverCollection.iterator(); i.hasNext();) {
                Cover e = (Cover) i.next();
                actualCoversList.add(e);
                data[j][0] = e.getTitle();
                data[j][1] = e.getType();
                data[j][2] = e.getLength();
                data[j][3] = e.getRating();
                data[j][4] = "UpdateCover";
                data[j][5] = "DeleteCover";
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

    public int numberOfCovers(Collection coverCollection) {
        int counter = 0;
        for (Iterator i = coverCollection.iterator(); i.hasNext();) {
            Cover e = (Cover) i.next();
            counter++;
        }
        return counter;
    }

    public void refreshCoversTable(){
        coversTable.setModel(new DefaultTableModel(generateDataFromDataBase(), columnNames));
        setTableButtons();
    }

    private void setCustomLayout() {
        setLayout(new MigLayout());
    }
}
