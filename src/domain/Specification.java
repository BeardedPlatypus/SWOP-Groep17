package domain;

import java.util.ArrayList;
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
	public ArrayList<Option> options = new ArrayList<Option>();
}