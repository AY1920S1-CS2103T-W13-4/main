package seedu.address.model.account;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.attributes.Name;
import seedu.address.model.tag.Tag;
import seedu.address.model.transaction.Transaction;

/**
 * Represents an account in the account book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Account {

    // Identity fields
    private final Name name;
    private final List<Transaction> transactions;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Account(Name name, List<Transaction> transactions, Set<Tag> tags) {
        requireAllNonNull(name, transactions, tags);
        this.name = name;
        this.transactions = transactions;
        this.tags.addAll(tags);
    }

    public static Account getDefaultAccount() {
        //TODO implement getDefaultAccount which returns the default account
        return new Account(new Name("DEFAULT"), new ArrayList<Transaction>(), new HashSet<>());
    }

    public Name getName() {
        return name;
    }

    public List<Transaction> getTransaction() {
        return transactions;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
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
                && otherAccount.getTransaction().equals(getTransaction())
                && otherAccount.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, transactions, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}

