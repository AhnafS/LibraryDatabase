import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static void renewLibraryCard(int libraryID) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Renew the library card by adding one year to the expiration date
            String sql = "UPDATE LibraryCard SET ExpireDate = DATE(ExpireDate, '+1 year') WHERE LibraryID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, libraryID);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Library card renewed successfully!");
                } else {
                    System.out.println("Library card renewal failed. Library ID not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disableExpiredLibraryCards() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Disable library cards that have expired by setting ExpireDate to null
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedCurrentDate = sdf.format(currentDate);

            String sql = "UPDATE LibraryCard SET ExpireDate = null WHERE ExpireDate < ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, formattedCurrentDate);

                int rowsUpdated = preparedStatement.executeUpdate();

                System.out.println(rowsUpdated + " expired library cards disabled.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
