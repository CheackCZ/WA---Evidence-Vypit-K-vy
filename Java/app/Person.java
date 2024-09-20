package Java.app;

/**
 * Represents a person with name and an ID.
 */
public class Person {

    private int id;
    private String name;
    private String email;

    /**
     * Constructs a Person with the given ID and name.
     * @param id - person's ID
     * @param name - person's name
     */
    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the person's name.
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the person's ID.
     * @return the ID of the person
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the person's email.
     * @return the email of the person
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns a formatted string showing the person's id and name.
     * @return String representing the person's id and name.
     */
    @Override
    public String toString() {
        return "| " + id + " | " + name;  // Ensures that the person's name is shown in the JComboBox
    }
}
