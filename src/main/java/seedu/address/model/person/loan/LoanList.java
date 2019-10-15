package seedu.address.model.person.loan;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import seedu.address.model.person.loan.exceptions.LoanNotFoundException;

/**
 * A list of loans that does not allow nulls.
 *
 * Supports a minimal set of list operations.
 */
public class LoanList implements Iterable<Loan> {

    private final ObservableList<Loan> internalList = FXCollections.observableArrayList();
    private final ObservableList<Loan> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Checks if the list contains a loan equivalent to the given loan.
     * @param toCheck The given loan to check the list for.
     * @return True if the list contains the given loan, false otherwise.
     */
    public boolean contains(Loan toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a loan to the list.
     * @param toAdd
     */
    public void add(Loan toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    /**
     * Retrieves a loan from the list equivalent to the given loan.
     * @param toGet The equivalent loan (identical attributes to the target loan).
     * @return The retrieved loan.
     */
    public Loan getLoan(Loan toGet) {
        requireNonNull(toGet);

        Loan targetLoan = null;
        for (Loan loan : internalUnmodifiableList) {
            if (loan.equals(toGet)) {
                targetLoan = loan;
            }
        }

        if (targetLoan == null) {
            throw new LoanNotFoundException();
        }

        return targetLoan;
    }

    /**
     * Replaces the loan {@code target} in the list with {@code editedLoan}.
     * {@code target} must exist in the list.
     * @param target The target loan to be replaced.
     * @param editedLoan The edited loan to replace the target loan with.
     */
    public void setLoan(Loan target, Loan editedLoan) {
        requireAllNonNull(target, editedLoan);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new LoanNotFoundException();
        }

        internalList.set(index, editedLoan);
    }

    /**
     * Replaces the content of this list with the content of {@code replacementList}.
     * @param replacementList The list to replace the current list with.
     */
    public void replaceList(List<Loan> replacementList) {
        requireNonNull(replacementList);
        internalList.setAll(replacementList);
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    /**
     * Deletes the given loan from the list.
     * The loan must exist in the list.
     * @param toRemove The loan to be removed from the list.
     */
    public void delete(Loan toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new LoanNotFoundException();
        }
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Loan> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Loan> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof LoanList)) {
            return false;
        }

        LoanList otherLoanList = (LoanList) other;
        return internalList.equals(otherLoanList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
