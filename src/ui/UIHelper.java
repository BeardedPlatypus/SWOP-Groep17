package ui;

import java.util.Scanner;

/**
 * A helper class for the UI containing methods that need to be accessible to all
 * parts of the UI.
 * 
 * @author Frederik Goovaerts
 *
 */
public class UIHelper {

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------

	/**
	 * Make a new UIHelper with given scanner as scanner.
	 * 
	 * @param scanner
	 * 		The scanner for this class
	 * @throws IllegalArgumentException
	 * 		If given scanner is null
	 */
	public UIHelper(Scanner scanner) throws IllegalArgumentException{
		if(scanner == null)
			throw new IllegalArgumentException("Scanner should not be null.");
		this.input = scanner;
	}
	
	//--------------------------------------------------------------------------
	// Properties and fields
	//--------------------------------------------------------------------------
	
	/** scanner this class uses for interfacing with the user */
	private final Scanner input;
	
	/** Seperator for printing */
	public final String SEPERATOR = "-------------------------";
	
	/** CRLF for printing */
	public final String CRLF = "\r\n";
	
	//--------------------------------------------------------------------------
	// Helper Methods
	//--------------------------------------------------------------------------
		
	/**
	 * Method to get integer input from a user.
	 * A lower and upper bound are supplied, between which the choice has to be, both bounds inclusive.
	 * If an integer outside of the bounds is supplied, or wrong input is given, an error is displayed and the user
	 * is asked for new input.
	 * @param lowerBound
	 * 		The lower bound for allowed input, inclusive
	 * @param upperBound
	 * 		The upper bound for allowed input, inclusive
	 * @return
	 * 		The legal input between the bounds
	 */
	public int getIntFromUser(int lowerBound, int upperBound){
		int choice = 0;
		boolean decided = false;
		while(!decided){
			try{
				choice = Integer.parseInt(input.nextLine());
				if(choice >= lowerBound && choice <= upperBound){
					decided = true;
				} else{
					System.out.println("That is not a valid choice.\r\nTry again:");
				}
			} catch (NumberFormatException e){
				System.out.println("That is not a valid choice.\r\nTry again:");
			}
		}
		return choice;
	}
}
