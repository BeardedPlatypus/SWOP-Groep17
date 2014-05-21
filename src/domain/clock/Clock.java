package domain.clock;

import java.util.List;

import domain.DateTime;
import domain.productionSchedule.TimeObserver;
import domain.productionSchedule.TimeSubject;

public class Clock implements TimeSubject, EventConsumer{
	
	//TODO: queue, int met aantal updators, echte globaltime

	@Override
	public void attachTimeObserver(TimeObserver t)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void detachTimeObserver(TimeObserver t) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyTime() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<TimeObserver> getTimeObservers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void constructEvent(DateTime elapseTime, EventActor actor) {
		// TODO Auto-generated method stub
		
	}

}
