package budgetbuddy.model.util;

import budgetbuddy.model.AddressBook;
import budgetbuddy.model.ReadOnlyAddressBook;
import budgetbuddy.model.attributes.Name;
import budgetbuddy.model.person.Person;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh")),
            new Person(new Name("Bernice Yu")),
            new Person(new Name("Charlotte Oliveiro")),
            new Person(new Name("David Li")),
            new Person(new Name("Irfan Ibrahim")),
            new Person(new Name("Roy Balakrishnan"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

}
