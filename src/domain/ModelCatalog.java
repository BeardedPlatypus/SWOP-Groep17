package domain;

import java.util.ArrayList;
import domain.Model;

/**
 * Provides an overview of the models that a manufacturer can produce.
 */
public class ModelCatalog {
	private ArrayList<Model> models = new ArrayList<Model>();

	public void getCarModels() {
		throw new UnsupportedOperationException();
	}

	public void getModels() {
		throw new UnsupportedOperationException();
	}

	public Model[] toModelsArray() {
		Model[] lModels_Temp = new Model[this.models.size()];
		this.models.toArray(lModels_Temp);
		return lModels_Temp;
	}
}