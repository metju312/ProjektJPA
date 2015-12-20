package gui;

import entities.Song;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

public class MainWindow extends JFrame{
    private static MainWindow instance = null;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDB");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    public int mainWindowWidth = 400;
    public int mainWindowHeight = 400;
    private	JPanel panel1;
    private	JPanel panel2;
    private	JPanel panel3;

    private JTabbedPane tabbedPane;
    private JScrollPane scrollPane;
    private JTable table;

    private JFrame addSongWindow;

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    public MainWindow(){
        setMainWindowValues();
        centerWindow();
        generateTabbedPanePages();
        add(generateTabbedPane());
        addExitProcedures();
        validate();
    }

    private void generateTabbedPanePages() {
        generatePage1();
        generatePage2();
        generatePage3();
    }

    private void addExitProcedures() {
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (entityManager != null) {
                    entityManager.close();
                }
                if (entityManagerFactory != null) {
                    entityManagerFactory.close();
                }
                System.exit(0);
            }
        });
    }

    private void persistSong() {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Song song = new Song();
            song.setTitle("Tribute");
            song.setType("Rock");
            song.setLength(273);
            song.setRating(4.3);
            entityManager.persist(song);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    private JTabbedPane generateTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "Songs", panel1 );
        tabbedPane.addTab( "Covers", panel2 );
        tabbedPane.addTab( "Authors", panel3 );
        return tabbedPane;
    }


    private void setMainWindowValues() {
        setSize(mainWindowWidth, mainWindowHeight);
        setLayout(new FlowLayout());
        centerWindow();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    public void generatePage1()
    {
        panel1 = new JPanel();
        panel1.setLayout( new FlowLayout() );

        scrollPane = new JScrollPane(generateTable());
        //scrollPane.setLayout(new FlowLayout());


        panel1.add(scrollPane, new FlowLayout());
        panel1.add(generateAddSongButton(), new FlowLayout());

    }

    private JTable generateTable() {
        String[] columnNames = {"Title","Type","Length","Rating","Update","Delete"};
        Object[][] data = generateDataFromDataBase();

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table = new JTable(model);
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.getColumn("Update").setCellRenderer(new ButtonRenderer());
        table.getColumn("Update").setCellEditor(new ButtonEditor(new JCheckBox()));
        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));


        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        return table;
    }

    private JButton generateAddSongButton() {
        ImageIcon img = new ImageIcon("add_icon.jpg");
        JButton addSongButton = new JButton("Add song",img);
        MainWindow mainWindow = this;
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSongWindow = new AddSongWindow(entityManager, mainWindow);
            }
        });
        return addSongButton;
    }

    private Object[][] generateDataFromDataBase() {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Collection songCollection = entityManager.createQuery("SELECT e FROM Song e").getResultList();
            Object[][] data = new Object[numberOfSongs(songCollection)][6];

            int j = 0;
            for (Iterator i = songCollection.iterator(); i.hasNext();) {
                Song e = (Song) i.next();
                data[j][0] = e.getTitle();
                data[j][1] = e.getType();
                data[j][2] = e.getLength();
                data[j][3] = e.getRating();
                data[j][4] = "update";
                data[j][5] = "delete";
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

    private int numberOfSongs(Collection songCollection) {
        int counter = 0;
        for (Iterator i = songCollection.iterator(); i.hasNext();) {
            Song e = (Song) i.next();
            counter++;
        }
        return counter;
    }

    public void revalidateMainWindow(){
        System.out.println("revalidateMainWindow");



        tabbedPane.remove(panel1);
        tabbedPane.remove(panel2);
        tabbedPane.remove(panel3);
        tabbedPane.removeAll();


        tabbedPane.addTab("Songs2", panel1);
        tabbedPane.addTab( "Covers", panel2 );
        tabbedPane.addTab( "Authors", panel3 );
        tabbedPane.invalidate();
        tabbedPane.validate();
        tabbedPane.repaint();


        table.revalidate();
        table.repaint();
        scrollPane.remove(table);
        table.revalidate();
        table.repaint();
        //scrollPane.add(generateTable());
        setSize(600,600);
        revalidate();
        repaint();
        tabbedPane.revalidate();
        tabbedPane.repaint();
        panel1.revalidate();
        panel1.repaint();
    }

    public void generatePage2()
    {
        panel2 = new JPanel();
        panel2.setLayout( new BorderLayout() );
    }

    public void generatePage3()
    {
        panel3 = new JPanel();
        panel3.setLayout( new GridLayout( 3, 2 ) );
    }
}
