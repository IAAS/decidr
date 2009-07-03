package de.decidr.test.database.sql;

import javax.swing.JFrame;

public class DecidrMain {
    public static void main(String[] args) {
        DecidrSQLClient decidrFrame = new DecidrSQLClient();
        decidrFrame.setSize(600,400);
        decidrFrame.setTitle("DecidR SQL Tool");
        decidrFrame.setVisible(true);
        decidrFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
