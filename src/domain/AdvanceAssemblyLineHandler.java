package domain;

import java.util.ArrayList;

public class AdvanceAssemblyLineHandler {
	private AssemblyLine assemblyLine;

	public AdvanceAssemblyLineHandler(AssemblyLine assemblyLine){
		this.assemblyLine = assemblyLine;
	}
	
	public ArrayList<AssemblyProcedureContainer> getCurrentAssemblyLineStatus() {
		return assemblyLine.getCurrentAssembly();
	}
	
	public ArrayList<AssemblyProcedureContainer> getFutureAssemblyLineStatus() {
		return assemblyLine.getFutureAssembly();
	}
	
	public void tryAdvance(int Time) {
		assemblyLine.tryAdvance(Time);
	}
}