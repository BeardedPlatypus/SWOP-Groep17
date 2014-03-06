package interfaceLayer;

import java.io.IOException;

public class Terminal {
	
	static final String[][] users = {
		{"[G]arage holder","G"},
		{"[C]ar mechanic","C"},
		{"[M]anager","M"}
	};

	public static void main(String[] args) throws IOException {
		typeHeader();
		loginUser();
	}
	
	private static void loginUser() throws IOException {
		String user = selectUser();
		switch (user) {
		case "G":	GarageHolderTerminal.login();
					break;
		case "C":	CarMechanicTerminal.login();
					break;
		case "M":	ManagerTerminal.login();
					break;
		
		default: 	if(isValidUser(user)){
						// THROW UNDEFINED ERROR
					} else {
						// The user hasn't been defined in the String[][] users.
						System.out.println();
						System.out.println("Sorry, but \"" + user + "\" is not a valid user. Please try again.");
						System.out.println();
						loginUser();
					}
		}
		
	}

	public static void typeHeader(){
		System.out.println("--------------------------------");
		System.out.println("~ Aperture Car Assembly System ~");
		System.out.println("--------------------------------");
		System.out.println();
	}
	
	public static String selectUser() throws IOException{
		System.out.println("Welcome, user! Please select your role.");
		
		System.out.println();
		for(int i = 0; i < users.length; i++){
			System.out.println((i+1) + ". " + users[i][0]);
		}
		System.out.println();
		
		// We'll only read in the first character.
		char firstChar = (char) System.in.read();
		// Don't forget to clear the input buffer.
		System.in.skip(System.in.available());
		
		return String.valueOf(firstChar);
	}
	
	public static boolean isValidUser(String input){
		for(String[] user : users){
			if (user[1].equalsIgnoreCase(input)) return true;
		}
		return false;
	}
	


}
