package fr.d2factory.libraryapp.library;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepositoryImpl;
import fr.d2factory.libraryapp.member.Member;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import fr.d2factory.libraryapp.utility.LibraryUtlity;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {
	private Library library;
	private BookRepositoryImpl bookRepository;
	private static List<Book> books = new ArrayList<>();

	@BeforeEach
	void setup() throws JsonParseException, JsonMappingException, IOException {

		// We create an intance via hard code new () but we hoped we use IOC Spring
		// Feature not recommended in readme
		bookRepository = new BookRepositoryImpl();
		// create a Library Service
		library = new LibraryImpl(bookRepository);

		// Load books from json file and map to POJO
		ObjectMapper mapper = new ObjectMapper();
		File booksJson = new File("src/test/resources/books.json");
		books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
		});

		// Init a list of books
		bookRepository.addBooks(books);
	}

	@Test
	void member_can_borrow_a_book_if_book_is_available() {
		Member member1 = new Resident(2000);
		Book borrowedBook = library.borrowBook(46578964513L, member1, LocalDate.now());
		Assertions.assertNotNull(borrowedBook);
	}

	@Test()
	void borrowed_book_is_no_longer_available() {

		final long notFoundIsbn = 202020192018L;
		Assertions.assertThrows(BookNotFoundException.class, () -> {
			// init Student as he is in his first year
			Member member = new Student(true, 1000000);
			library.borrowBook(notFoundIsbn, member, LocalDate.now());
		});

	}

	@Test
	void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
		Member member1 = new Resident(1000);
		// The Resident borrowed the book 10 ays ago : LocalDate.now().minusDays(10)
		library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member1, LocalDate.now().minusDays(10));
		library.returnBook(books.get(0), member1);
		Assertions.assertEquals(900, member1.getWallet(), 0);
	}

	@Test
	void students_pay_10_cents_the_first_30days() {
		Member member1 = new Student(false, 1000);
		// assume books is not empty 
		library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member1, LocalDate.now().minusDays(30));
		library.returnBook(books.get(0), member1);
		Assertions.assertEquals(700, member1.getWallet(), 0);
	}

	@Test
	void students_in_1st_year_are_not_taxed_for_the_first_15days() {
		Member member1 = new Student(true, 900);
		// assume books is not empty 
		library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member1,
				LocalDate.now().minusDays(LibraryUtlity.FIRST_YEAR_FREE));
		library.returnBook(books.get(0), member1);
		Assertions.assertEquals(900, member1.getWallet(), 0);
	}

	// added
	@Test
	public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
		Member member1 = new Student(false, 2500);
		
		// delay of 6 days 
		int days = LibraryUtlity.DAYS_BEFORE_LATE_STUDENT + 6;

		// assume books is not empty 
		library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member1, LocalDate.now().minusDays(days));
		library.returnBook(books.get(0), member1);

		// calculate the price 
		
		int price = LibraryUtlity.DAYS_BEFORE_LATE_STUDENT * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE
				+ (days - LibraryUtlity.DAYS_BEFORE_LATE_STUDENT) * LibraryUtlity.STUDENT_PRICE_AFTER_LATE;
		
		Assertions.assertEquals(2500 - price, member1.getWallet(), 0);
	}

	@Test
	void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
		Member member20 = new Resident(2500);
		int days = LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT + 5;
		library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member20, LocalDate.now().minusDays(days));
		library.returnBook(books.get(0), member20);

		// calculate the price 
		int price = LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE
				+ (days - LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT) * LibraryUtlity.RESIDENT_PRICE_AFTER_LATE;

		Assertions.assertEquals((2500 - price), member20.getWallet(), 1);
	}

	@Test()
	public void members_cannot_borrow_book_if_they_have_late_books() {

		Assertions.assertThrows(HasLateBooksException.class, () -> {
			Member member1 = new Resident(3000);

			library.borrowBook(books.get(0).getIsbn().getIsbnCode(), member1,
					LocalDate.now().minusDays(LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT + 1));
			library.borrowBook(books.get(1).getIsbn().getIsbnCode(), member1, LocalDate.now());
		});

	}
}
