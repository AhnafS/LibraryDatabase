import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookUtils {
    private static final String DATABASE_URL = "jdbc:sqlite:src/db/LibraryDatabase.db";

    /**
     * Retrieves all books from the Book table.
     */
    public static void getAllBooks() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for retrieval
            String sql = "SELECT * FROM Book";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                // Process each row in the result set
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    String author = resultSet.getString("Author");
                    String title = resultSet.getString("Title");
                    int edition = resultSet.getInt("Edition");
                    String language = resultSet.getString("Language");
                    String subjectField = resultSet.getString("SubjectField");

                    System.out.println("ISBN: " + isbn +
                            ", Author: " + author +
                            ", Title: " + title +
                            ", Edition: " + edition +
                            ", Language: " + language +
                            ", SubjectField: " + subjectField);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts a book into the Book table.
     *
     * @param isbn         ISBN of the book.
     * @param author       Author of the book.
     * @param title        Title of the book.
     * @param edition      Edition of the book.
     * @param language     Language of the book.
     * @param subjectField Subject field of the book.
     */
    public static void insertBook(String isbn, String author, String title, int edition, String language,
            String subjectField) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for insertion
            String sql = "INSERT INTO Book (ISBN, Author, Title, Edition, Language, SubjectField) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, title);
                preparedStatement.setInt(4, edition);
                preparedStatement.setString(5, language);
                preparedStatement.setString(6, subjectField);

                preparedStatement.executeUpdate();
                System.out.println("Book inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a book from the Book table.
     *
     * @param isbn ISBN of the book to be deleted.
     */
    public static void deleteBook(String isbn) {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Prepare SQL statement for deletion
            String sql = "DELETE FROM Book WHERE ISBN = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set parameters and execute the statement
                preparedStatement.setString(1, isbn);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Book deleted successfully!");
                } else {
                    System.out.println("Book not found or deletion failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void csBooks() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            String sql = "SELECT ISBN, Author, Title, Language FROM Book WHERE SubjectField = 'Computer Science'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    String author = resultSet.getString("Author");
                    String title = resultSet.getString("Title");
                    String language = resultSet.getString("language");
                    System.out.println(
                            "ISBN: " + isbn + " Author: " + author + " Title: " + title + " Language: " + language);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void foreignLanguageBook() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL)) {
            // Assuming 'Language' is the column representing the language in the database
            String sql = "SELECT ISBN, Author, Title, Language FROM Book WHERE Language <> 'English'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    String isbn = resultSet.getString("ISBN");
                    String author = resultSet.getString("Author");
                    String title = resultSet.getString("Title");
                    String language = resultSet.getString("language");

                    System.out.print(
                            "ISBN: " + isbn + ", Author: " + author + ", Title: " + title + ", Language: " + language);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
