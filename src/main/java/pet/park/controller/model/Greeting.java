package pet.park.controller.model;

public class Greeting {

	// Defines the data/attributes if a Greeting
	private final long id;
	private final String content;
	
	// Constructor - used to create an instance of this class, requires the data to be provided to create the instance.
	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	// Getter methods
	public long getId() {
		return id;
	}
	
	public String getContent() {
		return content;
	}	
}

