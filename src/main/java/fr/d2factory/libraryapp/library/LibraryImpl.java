package fr.d2factory.libraryapp.library;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

public class LibraryImpl implements Library {

	BookRepository bookRepository;

	public LibraryImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Book borrowBook(final long isbnCode, final Member member, final LocalDate borrowedAt) {
		
		// check if the Member has already a borrowed book
		if (member.getBorrowBook() != null) {
			bookRepository.findBorrowedBookDate(member.getBorrowBook()).ifPresent(borrowedDate -> {
				if (ChronoUnit.DAYS.between(borrowedDate, borrowedAt) > member.getNumberDaysKeeping()) {
					//The member has already a book borrowed for more than the allowed period
					throw new HasLateBooksException();
				}
			});
		}

		Book bookToBorrow = bookRepository.findBook(isbnCode).orElseThrow(BookNotFoundException::new);
		bookRepository.saveBookBorrow(bookToBorrow, borrowedAt);
		// affect the book to the member
		member.setBorrowBook(bookToBorrow);
		return bookToBorrow;
	}

	@Override
	public void returnBook(final Book book, final Member member) {

		bookRepository.findBorrowedBookDate(book).ifPresent(borrowedDate -> {
			// calculate how long the Member kept the Book 
			int keepingDays = (int) ChronoUnit.DAYS.between(borrowedDate, LocalDate.now());
			// charge The Member  based on keepingDays
			member.payBook(keepingDays);
		});
	}

}
