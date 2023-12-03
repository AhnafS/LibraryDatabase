import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryTransactionUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:src/db/LibraryDatabase.db";

    /**
     * Retrieves all library transactions from the LibraryTransaction table.
     */
    public static void getAllLibraryTransactions() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT * FROM LibraryTransaction";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                // Process each row in the result set
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    int libraryID = resultSet.getInt("LibraryID");
                    int copyNumber = resultSet.getInt("CopyNumber");
                    String personTitle = resultSet.getString("PersonTitle");
                    String borrowDate = resultSet.getString("BorrowDate");
                    String returnDate = resultSet.getString("ReturnDate");
                    int penalty = resultSet.getInt("Penalty");
                    String staffTransactee = resultSet.getString("LibraryStaffTransactee");

                    System.out.println("ISBN: " + isbn +
                            ", LibraryID: " + libraryID +
                            ", CopyNumber: " + copyNumber +
                            ", PersonTitle: " + personTitle +
                            ", BorrowDate: " + borrowDate +
                            ", ReturnDate: " + returnDate +
                            ", Penalty: " + penalty +
                            ", LibraryStaffTransactee: " + staffTransactee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a library transaction into the LibraryTransaction table.
     *
     * @param isbn                   ISBN of the book.
     * @param libraryID              Library ID of the person.
     * @param copyNumber             Copy number of the book.
     * @param personTitle            Title of the person.
     * @param borrowDate             Borrow date of the book.
     * @param returnDate             Return date of the book.
     * @param penalty                Penalty associated with the transaction.
     * @param libraryStaffTransactee Library staff involved in the transaction.
     */
    public static void insertLibraryTransaction(String isbn, int libraryID, int copyNumber, String personTitle,
            String borrowDate, String returnDate, int penalty, String libraryStaffTransactee) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO LibraryTransaction (ISBN, LibraryID, CopyNumber, PersonTitle, " +
                    "BorrowDate, ReturnDate, Penalty, LibraryStaffTransactee) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                preparedStatement.setInt(2, libraryID);
                preparedStatement.setInt(3, copyNumber);
                preparedStatement.setString(4, personTitle);
                preparedStatement.setString(5, borrowDate);
                preparedStatement.setString(6, returnDate);
                preparedStatement.setInt(7, penalty);
                preparedStatement.setString(8, libraryStaffTransactee);

                preparedStatement.executeUpdate();
                System.out.println("LibraryTransaction inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a library transaction from the LibraryTransaction table.
     *
     * @param isbn       ISBN of the book.
     * @param libraryID  Library ID of the person.
     * @param copyNumber Copy number of the book.
     */
    public static void deleteLibraryTransaction(String isbn, int libraryID, int copyNumber) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for deletion
            String sql = "DELETE FROM LibraryTransaction WHERE ISBN = ? AND LibraryID = ? AND CopyNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                preparedStatement.setInt(2, libraryID);
                preparedStatement.setInt(3, copyNumber);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("LibraryTransaction deleted successfully!");
                } else {
                    System.out.println("LibraryTransaction not found or deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
