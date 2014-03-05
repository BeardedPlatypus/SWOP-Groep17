package domain;

public class DateTime {
	public final int days;
	public final int hours;
	public final int minutes;
	
	public int getDays() {
		return days;
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public DateTime(int days, int hours, int minutes){
		this.days = days;
		this.hours = hours;
		this.minutes = minutes;
	}
	
	public DateTime progressTimeBy(int days, int hours, int minutes){
		int newDays = this.days + days + ((this.hours + hours)/24);
		int newHours = ((this.hours + hours) % 24) + ((this.minutes + minutes)/60);
		int newMinutes = ((this.minutes + minutes) % 60);
		return new DateTime(newDays, newHours, newMinutes);
	}
	
}
