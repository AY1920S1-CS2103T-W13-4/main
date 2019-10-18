package budgetbuddy.model.account;

import static budgetbuddy.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Objects;

import budgetbuddy.model.transaction.Transaction;

/**
 * Represents an account in the account book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Account {

    // Identity fields
    private final Name name;
    private final ArrayList<Transaction> transaction;

    /**
     * Every field must be present and not null.
     */
    public Account(Name name, ArrayList<Transaction> transaction) {
        requireAllNonNull(name, transaction);
        this.name = name;
        this.transaction = transaction;
    }

    public Name getName() {
        return name;
    }

    public ArrayList<Transaction> getTransaction() {
        return transaction;
    }


    /**
     * Returns true if both accounts of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two accounts.
     */
    public boolean isSameAccount(Account otherAccount) {
        if (otherAccount == this) {
            return true;
        }

        return otherAccount != null
                && otherAccount.getName().equals(getName())
                && (otherAccount.getTransaction().equals(getTransaction()));
    }

    /**
     * Returns true if both accounts have the same identity and data fields.
     * This defines a stronger notion of equality between two accounts.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Account)) {
            return false;
        }

        Account otherAccount = (Account) other;
        return otherAccount.getName().equals(getName())
                && otherAccount.getTransaction().equals(getTransaction());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transaction);
    }

    @Override
    public String toString() {
        return getName().toString();
    }

}

