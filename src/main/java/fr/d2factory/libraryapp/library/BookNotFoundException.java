package fr.d2factory.libraryapp.library;

import lombok.AllArgsConstructor;

/**
 * This exception is thrown when a book is not found in the book Repository
 */

@AllArgsConstructor
public class BookNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 12154879996L;

}