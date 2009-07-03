package de.decidr.test.database.sql.tools;

public class SQLFormatter {

    private  String tblName;
    private String[] tblColNames;
    private String[] tblRow;

    public SQLFormatter(String tableName) {
        tblName = tableName;
    }

    public  String insert(InsertInto into, InsertValues values){
        String result = "INSERT INTO " + tblName + "("  +
        		into.toString() +
        		")VALUES(" +
        		values.toString() +
        		")"
        		;
                
        return result;
    }
}

