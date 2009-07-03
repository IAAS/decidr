package de.decidr.test.database.sql.tools;

public class InsertValues {
    private String[] values;

    public InsertValues(String... colNames) {
        values = new String[colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            values[i] = colNames[i];
        }
    }
    public String toString() {
            String result = "";
            for (int i = 0; i < values.length; i++) {
                // if is not last element add the element and comma
                if(i!=values.length-1) {
                result += values[i]+ ","; 
                } else {
                    result += values[i];
                }
            }
            return result;
    }
}

