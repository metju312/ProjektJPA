package gui.windows.frames;

import gui.windows.panels.AuthorsPanel;
import gui.windows.panels.CoversPanel;
import gui.windows.panels.SongsPanel;
import utils.AuthorService;
import utils.CoverService;
import utils.SongService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame{
    private static MainWindow instance = null;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myDB");
    public EntityManager entityManager = entityManagerFactory.createEntityManager();

    public JTabbedPane tabbedPane;

    public SongService songService = new SongService(entityManager);
    public CoverService coverService = new CoverService(entityManager);
    public AuthorService authorService = new AuthorService(entityManager);

    public int mainWindowWidth = 560;
    public int mainWindowHeight = 256;

    public SongsPanel songsPanel;
    public CoversPanel coversPanel;
    public AuthorsPanel authorsPanel;

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
        closeDatabaseConnectionOnCloseAplicationButton();
    }

    private void generateTabbedPanePages() {
        generatePage1();
        generatePage2();
        generatePage3();
    }

    private JTabbedPane generateTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Songs", songsPanel);
        tabbedPane.addTab("Covers", coversPanel);
        tabbedPane.addTab("Authors", authorsPanel);

        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                songsPanel.refreshSongsTable();
                coversPanel.refreshCoversTable();
                authorsPanel.refreshAuthorsTable();
            }
        };
        tabbedPane.addChangeListener(changeListener);
        return tabbedPane;
    }

    private void closeDatabaseConnectionOnCloseAplicationButton() {
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

    private void setMainWindowValues() {
        setSize(mainWindowWidth, mainWindowHeight);
        centerWindow();
        getContentPane().setLayout( new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void centerWindow() {
        setLocationRelativeTo(null);
    }

    public void generatePage1() {
        songsPanel = new SongsPanel(this);
    }

    public void generatePage2() {
        coversPanel = new CoversPanel(this);
    }

    public void generatePage3() {
        authorsPanel = new AuthorsPanel(this);
    }
}
