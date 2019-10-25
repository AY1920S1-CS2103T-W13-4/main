package budgetbuddy.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import budgetbuddy.commons.core.index.Index;
import budgetbuddy.model.account.Account;
import budgetbuddy.model.account.UniqueAccountList;
import budgetbuddy.model.account.exception.AccountNotFoundException;
import budgetbuddy.model.transaction.Transaction;
import javafx.collections.FXCollections;
import budgetbuddy.model.attributes.Name;
import budgetbuddy.model.transaction.Transaction;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

/**
 * Manages the loans of each person in a list of persons.
 */
public class AccountsManager {

    private final UniqueAccountList accounts;

    private final FilteredList<Account> filteredAccounts;

    private final ObservableList<Account> internalList = FXCollections.observableArrayList();
    private final ObservableList<Account> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    public AccountsManager() {
        this.accounts = new UniqueAccountList();
        filteredAccounts = new FilteredList<>(this.getAccounts(), s -> true);
    }

    /**
     * Creates and fills a new list of accounts.
     * @param accounts A list of accounts with which to fill the new list.
     */
    public AccountsManager(List<Account> accounts) {
        requireNonNull(accounts);
        this.accounts = new UniqueAccountList(accounts);
        filteredAccounts = new FilteredList<>(this.getAccounts(), s -> true);
        this.internalList.setAll(accounts);
    }

    /**
     * Retrieves the list of accounts.
     */
    public ObservableList<Account> getAccounts() {
        return internalUnmodifiableList;
    }

    /**
     * Returns an unmodifiable view of the list of Account
     */
    public ObservableList<Account> getFilteredAccountList() {
        return filteredAccounts;
    }


    /**
     * Adds a given account to its specified account in the list.
     * @param toAdd The account to add.
     */
    public void addAccount(Account toAdd) {
        internalList.add(toAdd);
    }

    /**
     * Replaces a target account with the given account.
     * @param toEdit The index of the target account to replace.
     * @param editedAccount The edited account to replace the target account with.
     */
    public void editAccount(Index toEdit, Account editedAccount) throws AccountNotFoundException {
        checkIndexValidity(toEdit);
        internalList.set(toEdit.getZeroBased(), editedAccount);
    }

    /**
     * Deletes an account.
     * @param toDelete The target account for deletion.
     */
    public void deleteAccount (Account toDelete) {
        if (internalList.contains(toDelete)) {
            internalList.remove(toDelete);
        } else {
            throw new AccountNotFoundException();
        }
    }

    /**
     * Returns the current number of accounts in the list.
     * @return The current number of accounts in the list as an {@code int}.
     */
    public int getAccountsCount() {
        return getAccounts().size();
    }


    /**
     * Checks if a given index exceeds the number of accounts currently in the list.
     * @param toCheck The index to check.
     * @throws AccountNotFoundException If the index exceeds the current number of accounts.
     */
    private void checkIndexValidity(Index toCheck) throws AccountNotFoundException {
        if (toCheck.getOneBased() > getAccountsCount()) {
            throw new AccountNotFoundException();
        }
    }

    /**
     * Updates the filter of the filtered account list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    public void updateFilteredAccountList(Predicate<Account> predicate) {
        requireNonNull(predicate);
        filteredAccounts.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the transaction list.
     */
    public ObservableList<Transaction> getTransactionList() {
        return accounts.getTransactionList();
    }

    /**
     * Adds a transaction to the AccountBook
     */
    //TODO implement addTransaction
    public void addTransaction(Transaction toAdd) {
        Account accountToCheck = toAdd.getAccount();
        if (internalList.contains(accountToCheck)) {

        }
    }

    //TODO implement removeTransaction
    public void removeTransaction(Transaction toDelete){

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AccountsManager)) {
            return false;
        }

        AccountsManager otherAccountsManager = (AccountsManager) other;
        return internalList.equals(otherAccountsManager.internalList);
    }

    /**
     * Returns the account at the specified index in the list.
     * @param toGet The index of the target account.
     * @throws AccountNotFoundException If the account is not in the list.
     */
    public Account getAccount(Index toGet) throws AccountNotFoundException {
        checkIndexValidity(toGet);
        return getAccounts().get(toGet.getZeroBased());
    }
}
