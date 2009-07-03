package de.decidr.test.database.sql.tools;

public class InsertInto {
    private String[] tblColNames;

    public InsertInto(String... colNames) {
        tblColNames = new String[colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            tblColNames[i] = colNames[i];
        }
    }
    public String toString() {
            String result = "";
            for (int i = 0; i < tblColNames.length; i++) {
                // if is not last element add the element and comma
                if(i!=tblColNames.length-1) {
                result += tblColNames[i]+ ","; 
                } else {
                    result += tblColNames[i];
                }
            }
            return result;
    }
}
