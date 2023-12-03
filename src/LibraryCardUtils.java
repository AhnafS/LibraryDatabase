import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryCardUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:src/db/LibraryDatabase.db";

    /**
     * Retrieves all library cards from the LibraryCard table.
     */
    public static void getAllLibraryCards() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT * FROM LibraryCard";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                // Process each row in the result set
                while (resultSet.next()) {
                    int libraryID = resultSet.getInt("LibraryID");
                    String expireDate = resultSet.getString("ExpireDate");

                    System.out.println("LibraryID: " + libraryID + ", ExpireDate: " + expireDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a library card into the LibraryCard table.
     *
     * @param libraryID  Library ID.
     * @param expireDate Expiry date of the library card.
     */
    public static void insertLibraryCard(int libraryID, String expireDate) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO LibraryCard (LibraryID, ExpireDate) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setInt(1, libraryID);
                preparedStatement.setString(2, expireDate);

                preparedStatement.executeUpdate();
                System.out.println("LibraryCard inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a library card from the LibraryCard table.
     *
     * @param libraryID Library ID of the library card to be deleted.
     */
    public static void deleteLibraryCard(int libraryID) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for deletion
            String sql = "DELETE FROM LibraryCard WHERE LibraryID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setInt(1, libraryID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("LibraryCard deleted successfully!");
                } else {
                    System.out.println("LibraryCard not found or deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
