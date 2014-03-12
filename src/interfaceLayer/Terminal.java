package interfaceLayer;

import java.io.IOException;

/**
 * @author simon
 *
 * This class provides the first layer of indirection for the interface
 * layer. It is first determined whether the user is a garage holder, car
 * mechanic or manager. The user is then redirected to the correct sub-interface.
 */
public class Terminal {
	
	/**
	 * These are the users that are currently defined in the user interface.
	 * This list is used to show the user his options, and to determine
	 * whether the option entered by the user is 'valid'.
	 * 
	 * Keep this list up to date when adding/removing interfaces for other
	 * users.
	 */
	static final String[][] users = {
		{"[G]arage holder","G"},
		{"[C]ar mechanic","C"},
		{"[M]anager","M"}
	};

	/**
	 * Prints a welcome message and redirects the user.
	 * 
	 * @param 	args
	 *		  	None are expected.
	 * @post	The user feels welcome and is redirected to a specialized interface.
	 * @throws 	IOException
	 * 			If there is a problem with reading the user's
	 * 			options from the System input stream.
	 */
	public static void main(String[] args) {
		showHeader();
		String user = "";
		try {
			user = selectUser();
			showLoginSuccess();
			redirectUser(user);
		} catch(IOException e) {
			System.err.println("Critical error: Unable to read user input.");
			e.printStackTrace();
		} catch (UndefinedUserException e) {
			System.err.println("User type identifier valid, but not implemented.");
		}
	}
	
	/**
	 * Redirects the user to a specialized interface.
	 * 
	 * @post	The user is redirected to a specialized interface.
	 * @throws  UndefinedUserException 
	 * 			If a user type is not implemented.
	 */
	private static void redirectUser(String user) throws UndefinedUserException {
		switch (user.toUpperCase()) {
		case "G":	GarageHolderTerminal.login();
					break;
		case "C":	CarMechanicTerminal.login();
					break;
		case "M":	ManagerTerminal.login();
					break;
		
		default: 	throw new UndefinedUserException(user);
		
		}
		
	}

	/**
	 * Prints a logo and corporate information.
	 * 
	 * @post	The user knows he's in the correct company.
	 */
	public static void showHeader(){
		
		System.out.println("              .,-:;//;:=,");
		System.out.println("          . :H@@@MM@M#H/.,+%;,");
		System.out.println("       ,/X+ +M@@M@MM%=,-%HMMM@X/,");
		System.out.println("     -+@MM; $M@@MH+-,;XMMMM@MMMM@+-");
		System.out.println("    ;@M@@M- XM@X;. -+XXXXXHHH@M@M#@/.");
		System.out.println("  ,%MM@@MH ,@%=             .---=-=:=,.");
		System.out.println("  =@#@@@MX.,                -%HX$$%%%:;");
		System.out.println(" =-./@M@M$                   .;@MMMM@MM:");
		System.out.println(" X@/ -$MM/         .--.       . +MM@@@M$");
		System.out.println(",@M@H: :@:   .----'   '--.    . =X#@@@@-");
		System.out.println(",@@@MMX, .   '-()-----()-'    /H- ;@M@M=");
		System.out.println(".H@@@@M@+,                    %MM+..%#$.");
		System.out.println(" /MMMM@MMH/.                  XM@MH; =;");
		System.out.println("  /%+%$XHH@$=              , .H@@@@MX,");
		System.out.println("   .=--------.           -%H.,@@@@@MX,");
		System.out.println("   .%MM@@@HHHXX$$$%+- .:$MMX =M@@MM%.");
		System.out.println("     =XMMM@MM@MM#H;,-+HMM@M+ /MMMX=");
		System.out.println("       =%@M@M#@$-.=$@MM@@@M; %M%=");
		System.out.println("         ,:+$+-,/H#MMMMMMM@= =,");
		System.out.println("               =++%%%%+/:-.");
		System.out.println();
		System.out.println("   ==================================");
		System.out.println("   = Aperture Car Assembly Terminal =");
		System.out.println("   ==================================");
		System.out.println();
		System.out.println();
		
		/**
		System.out.println("--------------------------------");
		System.out.println("~ Aperture Car Assembly System ~");
		System.out.println("--------------------------------");
		System.out.println();
		**/
	}
	
	public static void showLoginSuccess(){
		System.out.println("Thank you for logging in, you will be redirected momentarily...");
		System.out.println();
	}
	
	/**
	 * The user is shown the available user types and asked to indicate
	 * his own type by entering a character. This response is validated
	 * and returned.
	 * 
	 * @return	A one-character string representing a valid user type.
	 * @throws 	IOException
	 * 			If we're unable to read user input.
	 * 			
	 */
	public static String selectUser() throws IOException {
		System.out.println("Welcome, user!");
		System.out.println();
		System.out.println("Biometrics systems are offline. Please select your role manually.");
		
		String res = "";
		boolean firstTry = true;
			
		while(firstTry || !isValidUser(res)){
			if(!firstTry){
				System.out.println();
				System.out.println("Sorry, but \"" + res + "\" is not a valid user type. Please try again.");
			}
			firstTry = false;
			
			System.out.println();
			for(int i = 0; i < users.length; i++){
				System.out.println((i+1) + ". " + users[i][0]);
			}
			System.out.println();
			
			// We'll only read in the first character.
			res = String.valueOf((char) System.in.read());
			// Don't forget to clear the input buffer.
			System.in.skip(System.in.available());
			
		}
		
		return res;
	}
	
	/**
	 * Checks if we know a user corresponding to this identifier string.
	 * 
	 * @param	input
	 * 			A string potentially identifying a user type.
	 * @return	true if a user type associated with this string is known,
	 * 			false otherwise.
	 */
	public static boolean isValidUser(String input){
		for(String[] user : users){
			if (user[1].equalsIgnoreCase(input)) return true;
		}
		return false;
	}
	


}
