import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private static boolean libraryTransactionExists(Connection connection, String isbn, int libraryID, int copyNumber)
            throws SQLException {
        String query = "SELECT 1 FROM LibraryTransaction WHERE ISBN = ? AND LibraryID = ? AND CopyNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, isbn);
            preparedStatement.setInt(2, libraryID);
            preparedStatement.setInt(3, copyNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if a record is found
            }
        }
    }

    public static void insertLibraryTransaction(String isbn, int libraryID, int copyNumber, String personTitle,
            String borrowDate, String returnDate, int penalty, String libraryStaffTransactee) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Check if the record already exists
            if (!libraryTransactionExists(connection, isbn, libraryID, copyNumber)) {
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
            } else {
                System.out.println("LibraryTransaction with the same ISBN, LibraryID, and CopyNumber already exists.");
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

    public static List<String> getOverdueStudentNames() {
        List<String> overdueStudentNames = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT Person.Name, LibraryTransaction.ReturnDate\n" + //
                    "FROM Person\n" + //
                    "JOIN LibraryTransaction ON Person.PersonID = LibraryTransaction.LibraryID\n" + //
                    "WHERE Person.Title = 'Student' AND (LibraryTransaction.ReturnDate < ? OR LibraryTransaction.ReturnDate IS NULL);\n"
                    + //
                    "";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameter for current date
                preparedStatement.setString(1, LocalDate.now().toString());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        // Add the name to the list
                        String studentName = resultSet.getString("Name");
                        overdueStudentNames.add(studentName);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return overdueStudentNames;
    }

    public static double calculateOutstandingBalance(String isbn, int libraryID, int copyNumber, double newBalance) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Retrieve the existing penalty for the given library transaction
            String sql = "SELECT Penalty FROM LibraryTransaction " +
                    "WHERE ISBN = ? AND LibraryID = ? AND CopyNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, isbn);
                preparedStatement.setInt(2, libraryID);
                preparedStatement.setInt(3, copyNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        double existingPenalty = resultSet.getDouble("Penalty");

                        // Calculate the outstanding balance by adding the existing penalty to the new
                        // balance
                        return existingPenalty + newBalance;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Return 0.0 if an error occurs or no result is found
        return 0.0;
    }
}
