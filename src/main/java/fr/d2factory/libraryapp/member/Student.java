package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.CostBorrowException;
import fr.d2factory.libraryapp.library.WalletNotEnoughException;
import fr.d2factory.libraryapp.utility.LibraryUtlity;

public class Student extends Member {

	private boolean firstYear;

	public Student(boolean firstYear, int wallet) {
		super(wallet);
		this.firstYear = firstYear;
		// init days of keeping for Student
		numberDaysKeeping = LibraryUtlity.DAYS_BEFORE_LATE_STUDENT;
	}

	public boolean isFirstYear() {
		return firstYear;
	}

	@Override
	public void payBook(int numberOfDays) {
		int cost = 0;

		if (firstYear)
			numberOfDays -= LibraryUtlity.FIRST_YEAR_FREE;
		cost = costOfBorrowBook(numberOfDays);
		if (cost < 0) {
			throw new CostBorrowException("negative cost value: " + cost);
		}
		if (cost > getWallet()) {
			throw new WalletNotEnoughException("Insufficient wallet balance: " + getWallet() + ", cost: " + cost);
		}
		setWallet(getWallet() - cost);
	}

	@Override
	public int costOfBorrowBook(int numberOfDays) {
		int costOfBorrow;
		if (numberOfDays <= LibraryUtlity.DAYS_BEFORE_LATE_STUDENT) {
			costOfBorrow = numberOfDays * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE;
		} else {
			costOfBorrow = LibraryUtlity.DAYS_BEFORE_LATE_STUDENT * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE
					+ LibraryUtlity.STUDENT_PRICE_AFTER_LATE * (numberOfDays - LibraryUtlity.DAYS_BEFORE_LATE_STUDENT);
		}
		return costOfBorrow;
	}

}
