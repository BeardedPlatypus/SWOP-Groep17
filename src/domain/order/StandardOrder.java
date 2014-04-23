package domain.order;

import domain.DateTime;
import domain.Model;
import domain.Specification;

public class StandardOrder extends Order {
	public StandardOrder(Model model, Specification specification,
			int orderNumber, DateTime submissionTime)
			throws NullPointerException {
		super(model, specification, orderNumber, submissionTime);
	}
}
