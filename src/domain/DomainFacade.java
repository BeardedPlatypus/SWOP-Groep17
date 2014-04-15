package domain;

//TODO new in iteration 2
public class DomainFacade {
	private PerformAssemblyTaskHandler performAssemblyTaskHandler;
	private NewOrderSessionHandler newOrderSessionHandler;
	private OrderSingleTaskHandler orderSingleTaskHandler;
	private OrderDetailsHandler orderDetailsHandler;
	private SchedulingAlgorithmHandler schedulingAlgorithmHandler;

	public void getAlgorithms() {
		throw new UnsupportedOperationException();
	}

	public void getCurrentScheduleAlgorithm() {
		throw new UnsupportedOperationException();
	}

	public void setFifoAlgorithm() {
		throw new UnsupportedOperationException();
	}
}