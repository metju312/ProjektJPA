package main;

import gui.windows.frames.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainWindow.getInstance().setVisible(true);
            }
        });
    }
}
