import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:src/db/LibraryDatabase.db";

    /**
     * Retrieves all persons from the Person table.
     */
    public static void getAllPersons() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT * FROM Person";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                // Process each row in the result set
                while (resultSet.next()) {
                    int personID = resultSet.getInt("PersonID");
                    String name = resultSet.getString("Name");
                    String address = resultSet.getString("Address");
                    String title = resultSet.getString("Title");

                    System.out.println("PersonID: " + personID +
                            ", Name: " + name +
                            ", Address: " + address +
                            ", Title: " + title);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a person into the Person table.
     *
     * @param personID Person ID.
     * @param name     Name of the person.
     * @param address  Address of the person.
     * @param title    Title of the person.
     */
    public static void insertPerson(int personID, String name, String address, String title) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO Person (PersonID, Name, Address, Title) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setInt(1, personID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, address);
                preparedStatement.setString(4, title);

                preparedStatement.executeUpdate();
                System.out.println("Person inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a person from the Person table and their corresponding LibraryCard
     * (if it exists).
     *
     * @param personID Person ID of the person to be deleted.
     */
    public static void deletePerson(int personID) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Check if LibraryCard exists for the person
            boolean libraryCardExists = checkLibraryCardExists(connection, personID);

            // Delete Person
            String deletePersonSql = "DELETE FROM Person WHERE PersonID = ?";
            try (PreparedStatement deletePersonStatement = connection.prepareStatement(deletePersonSql)) {
                deletePersonStatement.setInt(1, personID);
                int rowsAffectedPerson = deletePersonStatement.executeUpdate();

                if (rowsAffectedPerson > 0) {
                    System.out.println("Person deleted successfully!");
                } else {
                    System.out.println("Person not found or deletion failed.");
                }
            }

            // Delete LibraryCard if it exists
            if (libraryCardExists) {
                String deleteLibraryCardSql = "DELETE FROM LibraryCard WHERE LibraryID = ?";
                try (PreparedStatement deleteLibraryCardStatement = connection.prepareStatement(deleteLibraryCardSql)) {
                    deleteLibraryCardStatement.setInt(1, personID);
                    int rowsAffectedLibraryCard = deleteLibraryCardStatement.executeUpdate();

                    if (rowsAffectedLibraryCard > 0) {
                        System.out.println("LibraryCard deleted successfully!");
                    } else {
                        System.out.println("LibraryCard not found or deletion failed.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkLibraryCardExists(Connection connection, int personID) throws SQLException {
        String checkLibraryCardSql = "SELECT 1 FROM LibraryCard WHERE LibraryID = ?";
        try (PreparedStatement checkLibraryCardStatement = connection.prepareStatement(checkLibraryCardSql)) {
            checkLibraryCardStatement.setInt(1, personID);
            try (var resultSet = checkLibraryCardStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public static void printStudentNames() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String sql = "SELECT Name FROM Person WHERE Title = 'Student'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String studentName = resultSet.getString("Name");
                    System.out.println("Student Name: " + studentName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Print the names of all professors from the Person table.
     */
    public static void printProfessorNames() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String sql = "SELECT Name FROM Person WHERE Title = 'Professor'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String professorName = resultSet.getString("Name");
                    System.out.println("Professor Name: " + professorName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
