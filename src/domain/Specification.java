package domain;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.*;

import domain.Option;

/**
 * Class representing the choices made for a certain model's options.
 * A Specification is tailored for a certain Model, since it depends on the models order of its options, and those options' orders of their choices.
 * 
 * This class is instatiated by a model object, probably in the UI layer.
 * 
 * @author Frederik Goovaerts
 */
public class Specification {
	
	/** The Option objects contained in this Specification. */
	public List<Option> options = new ArrayList<Option>();
	
	/** Get the Option objects contained in this Specification. */
	public List<Option> getOptions() {
		return new ArrayList<Option>(this.options);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Specification other = (Specification) obj;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (! CollectionUtils.isEqualCollection(this.getOptions(), other.getOptions()))
			return false;
		return true;
	}
	
	
}