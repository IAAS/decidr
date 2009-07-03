import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
	
	public static void main(String[] args){
		
		String[] input= null;
		String[] saDescription={"e-mail:","username:","passwort:","first name:","last name:","street:","postal code:","city:"};
		String[] ssDescription={"auto accept tenants:","lifetime password request(in sec):","lifetime registration request(in sec):","life time email change request(in sec):","lifetime invitation(in sec)","loglevel:"};
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String uInput = "n";
		
		
		//get super admin data
		while (!((uInput.equals("y")) || (uInput.equals("yes")))){
		
			try{
				input = userInput.SuperAdmin.getUserInput();
			}	
			catch(Exception e){
				System.out.println("An error occoured while reading super admin information:" +e);
			}
			
			
			// check if input is correct
			System.out.println("\n\nPlease check input:\n\n");
			
			if (input != null){
				int j = 0;
				for(String i:input){
					System.out.printf("%-20s",saDescription[j]);
					System.out.printf("%-20s",i);
					System.out.println();
					j++;
				}
			}
			else{
				System.out.println("An error occoured while reading super admin information.");
			}
			
			System.out.println("\nIs this correct? (y/n)");
			try {
				uInput = console.readLine();
			} catch (IOException e) {
				System.out.println("An error occoured while reading super admin information:" +e);
			}
			
		}
		
		
		//get system settings
		uInput="n";
		while (!uInput.equals("y") || !uInput.equals("yes")){
		
			try{
				input = userInput.SystemSettings.getUserInput();
			}	
			catch(Exception e){
				System.out.println("An error occoured while reading system settings information:" +e);
			}
			
			
			// check if input is correct
			System.out.println("\n\nPlease check input:\n\n");
			
			if (input != null){
				int j = 0;
				for(String i:input){
					//FIXME Style anpassen
					System.out.printf("%-20s",ssDescription[j]);
					System.out.printf("%-20s",i);
					System.out.println();
					j++;
				}
			}
			else{
				System.out.println("An error occoured while reading system settings information.");
			}
			
			System.out.println("\nIs this correct? (y/n)");
			try {
				uInput = console.readLine();
			} catch (IOException e) {
				System.out.println("An error occoured while reading system settings information:" +e);
			}
			
			
			
		}
		
		
		
		
	}
}
