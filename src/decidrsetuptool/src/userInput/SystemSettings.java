package userInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * Returns the system settings data as array of strings. The data will be
 * created by using user interaction.
 * 
 * @author Markus Fischer
 * 
 * @version 0.1
 */
public class SystemSettings {

	public static String[] getUserInput() throws IOException {

		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));
		String[] result = new String[6];
		String accept = "moep";
		String number = "moep";
		String log = null;
		String logInput;

		// print header
		System.out
				.println("###################################################");
		System.out.println("Set up system settings");
		System.out
				.println("###################################################");

		// get autoAcceptTenants
		System.out
				.println("Should tenant be accepted autamitcaly by the system (y/n):");
		accept = console.readLine();

		while (!((accept.equals("y")) || (accept.equals("n")))) {
			System.out.println("Invalid input please enter \"y\" or \"n\":");
			accept = console.readLine();
		}

		if (accept.equals("y")) {
			result[0] = accept;
		} else if (accept.equals("n")) {
			result[0] = accept;
		}

		// get lifetimes
		System.out
				.println("Please enter the lifetime of a password reset request (in sec):");
		number = console.readLine();

		while (!(helpers.CheckNumber.validateNumber(number))) {
			System.out
					.println("Invalid input please enter a number e.g. 1337:");
			number = console.readLine();
		}

		result[1] = number;

		System.out
				.println("Please enter the lifetime of a registration request (in sec):");
		number = console.readLine();

		while (!(helpers.CheckNumber.validateNumber(number))) {
			System.out
					.println("Invalid input please enter a number e.g. 1337:");
			number = console.readLine();
		}

		result[2] = number;

		System.out
				.println("Please enter the lifetime od an email change request (in sec):");
		number = console.readLine();

		while (!(helpers.CheckNumber.validateNumber(number))) {
			System.out
					.println("Invalid input please enter a number e.g. 1337:");
			number = console.readLine();
		}

		result[3] = number;

		System.out
				.println("Please enter the lifetime of an invitation (in sec)");
		number = console.readLine();

		while (!(helpers.CheckNumber.validateNumber(number))) {
			System.out
					.println("Invalid input please enter a number e.g. 1337:");
			number = console.readLine();
		}

		result[4] = number;
		
		// get loglevel
		System.out.println("Please chose the global loglevel:");
		System.out.println("1) ALL");
		System.out.println("2) DEBUG");
		System.out.println("3) ERROR");
		System.out.println("4) FATAL");
		System.out.println("5) INFO");
		System.out.println("6) OFF");
		System.out.println("7) WARN\n\n");

		logInput = console.readLine();

		while (log ==null){
			if ("1".equals(log)) {
				log="all";
			} else if ("2".equals(logInput)) {
				log="debug";
			} else if ("3".equals(logInput)) {
				log="error";
			} else if ("4".equals(logInput)) {
				log="fatal";
			} else if ("5".equals(logInput)) {
				log="info";
			} else if ("6".equals(logInput)) {
				log="off";
			} else if ("7".equals(logInput)) {
				log="warn";
			}
			else{
				System.out.println("Undefined input, please enter a number between 1 and 7:");
				logInput=console.readLine();
			}
		}
		result[5] = log;
		
		return result;
	}

}
