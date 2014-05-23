package domain.assembly_line;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import domain.car.Model;

/**
 * Given a list of Models that an AssemblyLine is expected to handle,
 * a LayoutFactory can initialise the WorkPosts that the AssemblyLine needs to
 * fulfill its tasks.
 * 
 * @author Thomas Vochten
 *
 */
public class LayoutFactory {
	
	/**
	 * Make a list of WorkPosts that together are capable of producing
	 * vehicles of the specified models.
	 * 
	 * @param models
	 * 		The models the AssemblyLine must handle.
	 * @return
	 * 		The list of WorkPosts.
	 */
	public List<WorkPost> makeLayout(List<Model> models) {
		List<WorkPost> toReturn = new ArrayList<WorkPost>();
		int workPostNum = 0;
		
		for (TaskType type : TaskType.values()) {
			for (Model model : models) {
				//FIXME
				if (model.getMinsOnWorkPostOfType(type) > 0) {
					toReturn.add(new WorkPost(type, workPostNum));
					workPostNum++;
					break;
				}
			}
		}
		
		return toReturn;
	}
	
	public List<WorkPost> makeLayout(Model... models) {
		return this.makeLayout(Arrays.asList(models));
	}
}
