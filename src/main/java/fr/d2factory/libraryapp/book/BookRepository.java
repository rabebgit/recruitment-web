package fr.d2factory.libraryapp.book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository {

	/**
	 * Add books to the list of library books
	 *
	 * @param books the list of book to add to library
	 */

	public void addBooks(List<Book> books);

	/**
	 * find book in library
	 *
	 * @param isbnCode the isbnCode fo the book to find
	 * 
	 * @return book the found book
	 * 
	 */

	public Optional<Book> findBook(long isbnCode);

	/**
	 * add book in list of borrwoed books
	 * 
	 * @param book the borrowed book to add in list
	 * 
	 * @param borrowedAt the date of borrow book
	 * 
	 */

	public void saveBookBorrow(Book book, LocalDate borrowedAt);

	/**
	 * find date of borrowing books
	 *
	 * @param book the borrowed book
	 * 
	 * @return LocalDate the date of borrowed book
	 * 
	 */

	public Optional<LocalDate> findBorrowedBookDate(Book book);
}
