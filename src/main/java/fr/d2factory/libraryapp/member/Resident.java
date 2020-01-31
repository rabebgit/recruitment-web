package fr.d2factory.libraryapp.member;

import fr.d2factory.libraryapp.library.CostBorrowException;
import fr.d2factory.libraryapp.library.WalletNotEnoughException;
import fr.d2factory.libraryapp.utility.LibraryUtlity;

public class Resident extends Member {

	public Resident(int wallet) {
		super(wallet);
		numberDaysKeeping = LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT;
	}

	@Override
	public void payBook(int numberOfDays) {
		int cost = 0;
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
		if (numberOfDays <= LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT) {
			costOfBorrow = numberOfDays * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE;
		} else {
			costOfBorrow = LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT * LibraryUtlity.MEMBRE_PRICE_BEFORE_LATE
					+ LibraryUtlity.RESIDENT_PRICE_AFTER_LATE * (numberOfDays - LibraryUtlity.DAYS_BEFORE_LATE_RESIDENT);
		}
		return costOfBorrow;
	}

}