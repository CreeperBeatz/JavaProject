public class InvalidContentException extends Exception {
	static final long serialVersionUID = 0;

	public InvalidContentException() {
		super();
	}

	@Override
	public String getMessage() {
		return "File contains characters what are not alphanumeric!";
	}
}
