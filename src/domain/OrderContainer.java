package domain;

/**
 * Container Interface that provides get methods of the container, for inspection
 * purposes.
 * 
 * @author Martinus Wilhelmus Tegelaers
 */
public interface OrderContainer {

	public Model getModel();

	public Specification getSpecifications();
}