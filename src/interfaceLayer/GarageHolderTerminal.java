package interfaceLayer;

import java.util.List;

import domain.*;

public class GarageHolderTerminal {

	public static void login() {
		showHeader();
		//TODO goeie contructor
		NewOrderSessionHandler handler = null;
		showOrderOverview(handler);
		
	}

	private static void showOrderOverview(NewOrderSessionHandler handler) {
		showCompletedOrders(handler);
		showPendingOrders(handler);
	}

	private static void showCompletedOrders(NewOrderSessionHandler handler) {
		List<OrderContainer> completedOrders = handler.getCompletedOrders();
		OrderContainer o = completedOrders.get(0);
		//erc
		
	}

	private static void showPendingOrders(NewOrderSessionHandler handler) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Prints a welcome message for the user.
	 * 
	 * @post	The user feels welcome.
	 */
	public static void showHeader(){
		System.out.println("   ==================================");
		System.out.println("   = Aperture Car Assembly Terminal =");
		System.out.println("   ==================================");
		System.out.println("   \\\\   Garage Holder Subsystem    //");
		System.out.println("    ================================");
		System.out.println();
		System.out.println();
	}

}