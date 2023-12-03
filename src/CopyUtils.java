import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CopyUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:src/db/LibraryDatabase.db";

    /**
     * Retrieves all copies from the Copy table.
     */
    public static void getAllCopies() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT * FROM Copy";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                // Process each row in the result set
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    int copyNumber = resultSet.getInt("CopyNumber");

                    System.out.println("ISBN: " + isbn + ", CopyNumber: " + copyNumber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a copy into the Copy table.
     *
     * @param isbn       ISBN of the book.
     * @param copyNumber Copy number of the book.
     */
    public static void insertCopy(String isbn, int copyNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO Copy (ISBN, CopyNumber) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                preparedStatement.setInt(2, copyNumber);
                preparedStatement.executeUpdate();
                System.out.println("Copy inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a copy from the Copy table.
     *
     * @param isbn       ISBN of the book.
     * @param copyNumber Copy number of the book.
     */
    public static void deleteCopy(String isbn, int copyNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for deletion
            String sql = "DELETE FROM Copy WHERE ISBN = ? AND CopyNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                preparedStatement.setInt(2, copyNumber);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Copy deleted successfully!");
                } else {
                    System.out.println("Copy not found or deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
