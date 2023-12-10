import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException {
        try {
            // Book Methods
            BookUtils bookUtils = new BookUtils();
            bookUtils.getAllBooks();
            System.out.println("\n");
            bookUtils.insertBook("5234527890117", "Ahnaf Shamim", "Introduction to Life", 2, "English", "Therapy");
            System.out.println("\n");
            bookUtils.deleteBook("5234527890117");
            System.out.println("\n");
            bookUtils.getAllBooks();

            System.out.println("\n\n\n\n");

            // Copy Methods
            CopyUtils copyUtils = new CopyUtils();
            copyUtils.insertCopy("7244565892164", 1);
            System.out.println("\n");
            copyUtils.getAllCopies();
            System.out.println("\n");
            copyUtils.deleteCopy("7244565892164", 1);
            System.out.println("\n");
            copyUtils.getAllCopies();

            // Person Methods
            PersonUtils personUtils = new PersonUtils();
            personUtils.insertPerson(11, "Jonny Doe", "123 Main St", "Student");
            System.out.println("\n");
            personUtils.getAllPersons();
            System.out.println("\n");
            personUtils.deletePerson(11);
            System.out.println("\n");
            personUtils.getAllPersons();
            System.out.println("\n Print student Names");
            personUtils.printStudentNames();
            System.out.println("\n Print professor Names");
            personUtils.printProfessorNames();

            // LibraryCard Methods
            LibraryCardUtils libraryCardUtils = new LibraryCardUtils();
            libraryCardUtils.insertLibraryCard(11, "2023-12-31");
            System.out.println("\n");
            libraryCardUtils.getAllLibraryCards();
            System.out.println("\n");
            libraryCardUtils.deleteLibraryCard(11);
            System.out.println("\n");
            libraryCardUtils.getAllLibraryCards();

            // Library Transaction Method
            LibraryTransactionUtils libraryTransactionUtils = new LibraryTransactionUtils();
            libraryTransactionUtils.insertLibraryTransaction("1234567890123", 1, 1, "Student", "2023-01-01",
                    "2023-01-15", 0, "John Doe");
            System.out.println("\n");
            libraryTransactionUtils.getAllLibraryTransactions();
            System.out.println("\n");
            System.out.println(libraryTransactionUtils.getOverdueStudentNames());
            System.out.println("\n");
            libraryTransactionUtils.deleteLibraryTransaction("1234567890123", 1, 1);
            try {
                libraryTransactionUtils.insertLibraryTransaction("1234567890123", 1, 1, "Student",
                        "2023-01-01", "2023-01-15", 0, "John Doe");

                // Display all library transactions (including outstanding balances)
                libraryTransactionUtils.getAllLibraryTransactions();

                // Update outstanding balance for a specific transaction
                libraryTransactionUtils.calculateOutstandingBalance("1234567890123", 1, 1, 25.0);

                // Display all library transactions again (including updated outstanding
                // balances)
                libraryTransactionUtils.getAllLibraryTransactions();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
