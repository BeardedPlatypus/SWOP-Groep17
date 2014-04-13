package domain;

public class InitialisationHandler {
	private NewOrderSessionHandler newOrderHandler;
	private PerformAssemblyTaskHandler taskHandler;
	public DomainFacade domainFacade;

	public NewOrderSessionHandler getNewOrderHandler() {
		return this.newOrderHandler;
	}

	public PerformAssemblyTaskHandler getTaskHandler() {
		return this.taskHandler;
	}
}