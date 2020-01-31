package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The book repository emulates a database via 2 HashMaps
 */
public class BookRepositoryImpl implements BookRepository {

	// List of books in library
	private Map<ISBN, Book> availableBooks = new HashMap<>();

	// List of borrowed books from library
	private Map<Book, LocalDate> borrowedBooks = new HashMap<>();

	public void addBooks(List<Book> books) {
		if (books != null) {
			books.forEach(book -> availableBooks.computeIfAbsent(book.getIsbn(), key -> book));
		}
	}

	public Optional<Book> findBook(long isbnCode) {
		
		// return new feature Java 8 Optional to avoid NullPointerException
		return Optional.of(new ISBN(isbnCode)).map(availableBooks::get);
	}

	public void saveBookBorrow(Book book, LocalDate borrowedAt) {
		if (book != null && borrowedAt != null) {
			// add to lis
			borrowedBooks.put(book, borrowedAt);
			availableBooks.remove(book.getIsbn());
		}
	}

	public Optional<LocalDate> findBorrowedBookDate(Book book) {
		
		// return new feature Java 8 Optional to avoid NullPointerException
		return Optional.ofNullable(book).map(borrowedBooks::get);
	}
}
