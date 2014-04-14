package domain;

import java.util.ArrayList;
import domain.WorkingDayStatistics;
import domain.Registrar;

//TODO new in iteration 2
public class StatisticsLogger {
	public ArrayList<WorkingDayStatistics> statistics = new ArrayList<WorkingDayStatistics>();
	public ArrayList<Registrar> registrars = new ArrayList<Registrar>();

	public void addStatistics(ProcedureStatistics statistics) {
		throw new UnsupportedOperationException();
	}
}