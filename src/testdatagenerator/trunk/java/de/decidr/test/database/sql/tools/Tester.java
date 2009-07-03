package de.decidr.test.database.sql.tools;

public class Tester {

    /**
     * TODO: add comment
     * 
     * @param args
     */
    public static void main(String[] args) {
        SQLFormatter m1 = new SQLFormatter(" fgsdfgdsf ");
        System.out.println(m1.insert(new InsertInto("'field'","'dfsd'"),new InsertValues("'hallo'", "12")));

    }

}
