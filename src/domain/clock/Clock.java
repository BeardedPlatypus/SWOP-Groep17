package domain.clock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import domain.DateTime;

/**
 * The ClockManager is the object that manages the internal Clock of the whole
 * system.
 * When the time gets updated it notifies all its TimeObservers, through the 
 * TimeSubject interface. 
 * 
 * @author Thomas Vochten, Martinus Wilhelmus Tegelaers
 * 
 * @invariant | this.getCurrentTime != null
 * @invariant For each actor in the set of registered actors, there is at most
 *  			one event scheduled in the event queue.
 * @invariant There are at most getRegisteredActors().size() events scheduled
 * 				at any one time.  
 *
 */
//TODO refactor event queue management into new class?
public class Clock implements TimeSubject, EventConsumer{

	//--------------------------------------------------------------------------
	// Constructor
	//--------------------------------------------------------------------------
	/**
	 * Initialises a new clock.
	 * 
	 * @param initialTime
	 * 		The initial time of the system
	 * @throws IllegalArgumentException
	 * 		initialTime is null
	 */
	public Clock(DateTime initialTime) throws IllegalArgumentException {
		if (initialTime == null) {
			throw new IllegalArgumentException("Cannot initialise Clock with null InitialTime");
		}
		this.observers = new ArrayList<TimeObserver>();
		this.time = initialTime;
	}
	
	//--------------------------------------------------------------------------
	// Observer-related variables and methods
	//--------------------------------------------------------------------------
	/** Objects observing this Clock */
	private List<TimeObserver> observers;
	
	//--------------------------------------------------------------------------
	// Time-related methods
	//--------------------------------------------------------------------------
	/** Global system time */
	private DateTime time;

	@Override
	public void attachTimeObserver(TimeObserver t)
			throws IllegalArgumentException {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		
		if (!this.observers.contains(t)) { 
			this.observers.add(t); 
		}
		t.update(this.getCurrentTime());
	}
	
	/**
	 * @return The observers, for internal use
	 */
	private List<TimeObserver> getObserversPrivate() {
		return this.observers;
	}

	@Override
	public List<TimeObserver> getTimeObservers() {
		return Collections.unmodifiableList(this.observers);
	}

	@Override
	public void detachTimeObserver(TimeObserver t) throws IllegalArgumentException {
		if (t == null) {
			throw new IllegalArgumentException("Observer cannot be null");
		}
		this.observers.remove(t);
	}

	@Override
	public void notifyTime() {
		for (TimeObserver observer : this.getObserversPrivate()) {
			observer.update(this.getCurrentTime());
		}
	}
	
	/**
	 * @return The current time of the system.
	 */
	public DateTime getCurrentTime() {
		return this.time;
	}

	//--------------------------------------------------------------------------
	// Event queue related methods and variables
	//--------------------------------------------------------------------------
	/** Contains events that must be executed in the future */
	private PriorityQueue<TimeEvent> eventQueue = new PriorityQueue<TimeEvent>();
	
	/**
	 * @return The event queue
	 */
	private PriorityQueue<TimeEvent> getEventQueue() {
		return this.eventQueue;
	}
	
	/** The actors that have registered themselves as event suppliers */
	private Set<EventActor> registeredActors = new HashSet<EventActor>();
	
	/**
	 * @return The registered actors
	 */
	private Set<EventActor> getRegisteredActors() {
		return this.registeredActors;
	}
	
	/**
	 * @return The number of registered actors
	 */
	private int getNumRegisteredActors() {
		return this.getRegisteredActors().size();
	}
	
	/**
	 * Register the specified actor as an object that can supply TimeEvents
	 * 
	 * @param actor
	 * 		The actor that can supply TimeEvents
	 * @throws IllegalArgumentException
	 * 		actor is null
	 * 		
	 */
	@Override
	public void register(EventActor actor) throws IllegalArgumentException {
		if (actor == null) {
			throw new IllegalArgumentException("Cannot register null actor in Clock");
		}
		
		this.getRegisteredActors().add(actor);
	}
	
	/**
	 * Unregister the specified EventActor
	 * 
	 * @param actor
	 * 		The actor to unregister
	 * @throws IllegalArgumentException
	 * 		actor is null
	 */
	@Override
	public void unregister(EventActor actor) throws IllegalArgumentException {
		if (actor == null) {
			throw new IllegalArgumentException("Cannot unregister null actor from Clock");
		}
		
		this.getRegisteredActors().remove(actor);
		this.purgeEventsFromActor(actor);
		
		this.checkFireEvent();
	}
	
	/**
	 * When unregistering an EventActor, purge all events originating from the
	 * actor from the event queue
	 * 
	 * @param actor
	 * 		The actor to unregister
	 */
	private void purgeEventsFromActor(EventActor actor) {
		Iterator<TimeEvent> it = this.getEventQueue().iterator();
		
		while (it.hasNext()) {
			TimeEvent event = it.next();
			if (event.hasActor(actor)) {
				it.remove();
			}
		}
	}
	
	/**
	 * Set the current system time to the specified time
	 * 
	 * @param time
	 * 		The new time
	 */
	//Package Protected for manipulator
	void setCurrentTime(DateTime time) {
		this.time = time;
		this.notifyTime();
	}

	@Override
	public void constructEvent(DateTime timeToElapse, EventActor actor) throws IllegalArgumentException {
		if (timeToElapse == null) {
			throw new IllegalArgumentException("Cannot construct event with null elapsedTime");
		}
		if (actor == null) {
			throw new IllegalArgumentException("Cannot construct event with null actor");
		}
		if (! this.getRegisteredActors().contains(actor)) {
			throw new IllegalArgumentException("Cannot construct event with unregistered actor");
		}
		if (this.hasEventForActor(actor)) {
			throw new IllegalArgumentException("Cannot add event for actor"
					+ "if event queue already contains event scheduled for later for the same actor");
		}
		
		DateTime timeOfExecution = this.getCurrentTime().addTime(timeToElapse);
		this.addEvent(new TimeEvent(timeOfExecution, actor));
	}
	
	@Override
	public void removeEventForActor(EventActor actor) {
		if (! this.hasEventForActor(actor)) {
			throw new IllegalArgumentException("Tried to remove event for actor"
					+ ", but there was no event queued for actor");
		}
		Iterator<TimeEvent> it = this.getEventQueue().iterator();
		while (it.hasNext()) {
			TimeEvent event = it.next();
			if (event.hasActor(actor)) {
				it.remove();
			}
		}
	}
	
	@Override
	public boolean hasEventForActor(EventActor actor) {
		for (TimeEvent eventInQueue : this.getEventQueue()) {
			if (eventInQueue.hasActor(actor)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add the specified event to the event queue and check if an event
	 * must be fired.
	 * 
	 * @param event
	 * 		The event to add
	 */
	private void addEvent(TimeEvent event) {
		this.getEventQueue().add(event);
		this.checkFireEvent();
	}
	
	/**
	 * Fire at least one event if the size of the queue has reached the number
	 * of updaters.
	 */
	private void checkFireEvent() {
		if (this.mustFireEvent()) {
			List<TimeEvent> toFire = this.nextToFireEvents();
			this.setCurrentTime(toFire.get(0).getGlobalTime());
			for (TimeEvent event : toFire) {
				event.activate();
			}
		}
	}
	
	/**
	 * @return Whether an event must be fired.
	 */
	private boolean mustFireEvent() {
		return this.getEventQueue().size() >= this.getNumRegisteredActors();
	}
	
	/**
	 * @return The events that must be fired. All events contained
	 * in the list have the same global time.
	 */
	private List<TimeEvent> nextToFireEvents() {
		List<TimeEvent> toReturn = new ArrayList<TimeEvent>();
		
		toReturn.add(this.getEventQueue().poll());
		
		boolean addNoMore = false;
		
		do {
			TimeEvent potentialToFire = this.getEventQueue().peek();
			if (potentialToFire == null || toReturn.get(0).compareTo(potentialToFire) != 0) {
				addNoMore = true;
			} else {
				toReturn.add(this.getEventQueue().poll());
			}
		} while(! addNoMore);
		
		return toReturn;
	}

}
