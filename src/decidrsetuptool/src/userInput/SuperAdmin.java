package userInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Returns the super admin data as array of strings.
 * The data will be created by using user interaction.
 * 
 * @author Markus Fischer
 *
 * @version 0.1
 */
public class SuperAdmin {
	
	public static String[] getUserInput() throws IOException{
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String[] result = new String[8];
		String mail = "";
		
		// print header
		System.out.println("###################################################");
		System.out.println("Set up superadmin information");
		System.out.println("###################################################");
		
		//get email address
		System.out.println("Please enter email-adress:");
		mail=console.readLine();
		
		while(helpers.CheckEmail.validateEmail(mail)==false){
			System.out.println("\n\nAddress not valid. Please try again:");
			mail=console.readLine();
		}
		
		result[0]=mail;
		
		
		// get others
		System.out.println("Please enter username:");
		result[1]=console.readLine();
		System.out.println("Please enter password:");
		result[2]=console.readLine();
		System.out.println("Please enter first name:");
		result[3]=console.readLine();
		System.out.println("Please enter last name:");
		result[4]=console.readLine();
		System.out.println("Please enter street:");
		result[5]=console.readLine();
		System.out.println("Please enter postal code:");
		result[6]=console.readLine();
		System.out.println("Please enter city");
		result[7]=console.readLine();
	
		return result;
	}

}
