package budgetbuddy.testutil;

import java.util.HashSet;
import java.util.Set;

import budgetbuddy.model.attributes.Name;
import budgetbuddy.model.loan.LoanList;
import budgetbuddy.model.person.Person;
import budgetbuddy.model.tag.Tag;
import budgetbuddy.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Kurtz";

    private Name name;
    private LoanList loans;
    private Set<Tag> tags;

    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        loans = new LoanList();
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        tags = new HashSet<>(personToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code LoanList} of the {@code Person} that we are building.
     */
    public PersonBuilder withLoans(LoanList loans) {
        this.loans.replaceList(loans.asUnmodifiableObservableList());
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Person build() {
        return new Person(name, tags);
    }

}
