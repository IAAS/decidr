package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckNumber {
	
	/**
	 * Checks weather String is a number or not.
	 * 
	 * @param mail
	 * @return true if valid, else false
	 */
	public static boolean validateNumber(String num){
				
		  //Set the email pattern string
	      Pattern p = Pattern.compile("[0-9]+");

	      //Match the given string with the pattern
	      Matcher m = p.matcher(num);

	      //check whether match is found 
	      boolean matchFound = m.matches();
	      
	      return matchFound;

	}

}
