package domain;

import java.util.ArrayList;
import java.util.Iterator;

public class Option {
	private final String optionName;
	private final ArrayList<String> choices;
	
	public Option(String name, String...choicesArgs){
		this.optionName = name;
        this.choices = new ArrayList<String>();
        for (String choice : choicesArgs) {
            this.choices.add(choice);
        }
	}
	
	public String getOptionName() {
		return this.optionName;
	}
	
	public String getChoiceName(int choiceNb){
		if(choiceNb < 0 || choiceNb > getAmountOfChoices())
			throw new IllegalArgumentException("Not a valid choice number.");
		return choices.get(choiceNb);
	}
	
	public int getAmountOfChoices() {
		return choices.size();
	}

	public Iterator<String> getChoicesIterator(){
		return choices.iterator();
	}
	
}