import java.sql.*;

public class DatabaseConnection {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {

            String url = "jdbc:sqlite:src/db/users.db";
            connection = DriverManager.getConnection(url);

            String sql = "select * from users";
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                String name = result.getString("name");
                String email = result.getString("email");
                System.out.println(name + "|" + email);
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
