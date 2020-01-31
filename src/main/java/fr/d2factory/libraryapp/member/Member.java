package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.library.Library;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */

@Getter
@Setter
@AllArgsConstructor
public abstract class Member {

	/**
	 * An initial sum of money the member has
	 */
	private float wallet;

	/**
	 * An initial number of days numbers can member borrow
	 */
	protected int numberDaysKeeping;

	/**
	 * the last book borrow
	 */
	protected Book borrowBook;

	/**
	 * The member should pay their books when they are returned to the library
	 *
	 * @param numberOfDays the number of days they kept the book
	 */
	public abstract void payBook(int numberOfDays);

	public abstract int costOfBorrowBook(int numberOfDays);

	public Member(float wallet) {
		super();
		this.wallet = wallet;
	}

}