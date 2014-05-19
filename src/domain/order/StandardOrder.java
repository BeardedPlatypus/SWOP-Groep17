package domain.order;

import domain.DateTime;
import domain.car.Model;
import domain.car.Specification;
import domain.car.Model;

/**
 * Class for Orders for whole new cars.
 * 
 * @author Martinus Wilhelmus Tegelaers
 *
 */
public class StandardOrder extends Order {
	/**
	 * Initialises a new StandardOrder with the specified Model,
	 * Specification, order number and submission time.
	 * 
	 * @param model
	 * 		The model of the car
	 * @param specification
	 * 		The specification of the car
	 * @param orderNumber
	 * 		The number of the new Order
	 * @param submissionTime
	 * 		The time at which the Order was submitted
	 * @throws NullPointerException
	 * 		See {@link Order#Order(Model, Specification, int, DateTime)
	 * 				Order(Model, Specification, int, DateTime)}
	 */
	public StandardOrder(Model model, Specification specification,
			int orderNumber, DateTime submissionTime)
			throws NullPointerException {
		super(model, specification, orderNumber, submissionTime);
	}
}
