package fr.d2factory.libraryapp.library;


/**
 * This exception is thrown when a member who owns late books tries to borrow
 * another book
 */

public class HasLateBooksException extends RuntimeException {

	private static final long serialVersionUID = 122225558889L;

	public HasLateBooksException() {
		super();
	}

	public HasLateBooksException(String message, Throwable cause) {
		super(message, cause);
	}

	public HasLateBooksException(String message) {
		super(message);
	}

	public HasLateBooksException(Throwable cause) {
		super(cause);
	}


}