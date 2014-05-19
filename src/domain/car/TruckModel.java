package domain.car;

import java.util.List;

import domain.assemblyLine.TaskType;

/**
 * TruckModel is a subclass of Model that represents models of trucks.
 * 
 * @author Thomas Vochten
 *
 */
public class TruckModel extends Model {

	/**
	 * Initialise a TruckModel with the specified name, option categories,
	 * default expected amount of minutes on a work post, expected amount
	 * of minutes on the body post and expected amount of minutes on the 
	 * certification post
	 * 
	 * @param modelName
	 * 		The name of the new TruckModel.
	 * @param optionCategories
	 * 		The option categories of the new TruckModel.
	 * @param minsPerWorkPost
	 * 		The default amount of minutes trucks of this TruckModel are expected to
	 * 		spend on a work post.
	 * @param minsOnBodyPost
	 * 		The amount of minutes trucks of this TruckModel are expected to
	 * 		spend on the body post.
	 * @param minsOnCertificationPost
	 * 		The amount of minutes trucks of this TruckModel are expected to
	 * 		spend on the certification post.
	 */
	public TruckModel(String modelName, List<OptionCategory> optionCategories,
			int minsPerWorkPost, int minsOnBodyPost, int minsOnCertificationPost) {
		super(modelName, optionCategories, minsPerWorkPost);
		this.minsOnBodyPost = minsOnBodyPost;
		this.minsOnCertificationPost = minsOnCertificationPost;
	}
	
	/** The amount of minutes trucks of this TruckModel are expected
	 * to spend on the body post. */
	private final int minsOnBodyPost;
	
	/** Get the amount of minutes trucks of this TruckModel are expected to
	 * spend on the body post. */
	private int getMinsOnBodyPost() {
		return this.minsOnBodyPost;
	}
	
	/** The amount of minutes trucks of this TruckModel are expected
	 * to spend on the certification post. */
	private final int minsOnCertificationPost;
	
	/** Get the amount of minutes trucks of this TruckModel are expected to
	 * spend on the certification post. */
	private int getMinsOnCertificationPost() {
		return this.minsOnCertificationPost;
	}

	@Override
	public int getMinsOnWorkPostOfType(TaskType workPostType) {
		switch(workPostType) {
			case BODY:
				return this.getMinsOnBodyPost();
			case CERTIFICATION:
				return this.getMinsOnCertificationPost();
			default:
				return super.minsPerWorkPost;
		}
	}

}
