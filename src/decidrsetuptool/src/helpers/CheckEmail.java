package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckEmail {
	
	/**
	 * Checks weather an email address is valid or not.
	 * 
	 * @param mail
	 * @return true if valid, else false
	 */
	public static boolean validateEmail(String mail){
				
		  //Set the email pattern string
	      Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

	      //Match the given string with the pattern
	      Matcher m = p.matcher(mail);

	      //check whether match is found 
	      boolean matchFound = m.matches();
	      
	      return matchFound;

	}

}
