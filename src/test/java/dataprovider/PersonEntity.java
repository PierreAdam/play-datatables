package dataprovider;

import com.github.javafaker.Name;

/**
 * PersonEntity.
 *
 * @author Pierre Adam
 * @since 21.03.02
 */
public class PersonEntity {

    private final String firstName;

    private final String lastName;

    private final String title;

    private final String bloodGroup;

    /**
     * Instantiates a new Person entity.
     *
     * @param name the name
     */
    public PersonEntity(final Name name) {
        this.firstName = name.firstName();
        this.lastName = name.lastName();
        this.title = name.title();
        this.bloodGroup = name.bloodGroup();
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Gets blood group.
     *
     * @return the blood group
     */
    public String getBloodGroup() {
        return this.bloodGroup;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %s, %s", this.getFirstName(), this.getLastName(), this.getTitle(), this.getBloodGroup());
    }
}
