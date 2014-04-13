package domain;

//TODO new in iteration 2
public class DomainFacade {
	public PerformAssemblyTaskHandler performAssemblyTaskHandler;
	public NewOrderSessionHandler newOrderSessionHandler;
	public OrderSingleTaskHandler orderSingleTaskHandler;
	public OrderDetailsHandler orderDetailsHandler;
	public SchedulingAlgorithmHandler schedulingAlgorithmHandler;

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