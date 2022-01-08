package answers;

import answers.dal.UserDAL;
import answers.model.Order;
import answers.model.User;
import answers.model.enums.UserType;
import answers.util.JDBCUtil;
import java.sql.SQLException;

public class DatabaseTester {

    public static void main(String[] args) {
        try {
            deleteDatabase();
            setupDatabase();
            injectData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println(UserDAL.instance.findUserOrder(4L));
        System.out.println(UserDAL.instance.findUserOrder(3L));
        System.out.println(UserDAL.instance.findUserOrder(2L));
        System.out.println(UserDAL.instance.findUserOrder(1L));
    }

    private static void deleteDatabase() {
        try {
            System.out.println("Dropping tables");
            String query = "DROP TABLE `exercise_db_1`.`user`, `exercise_db_1`.`order`";
            JDBCUtil.getConnection().prepareStatement(query).execute();
        } catch (SQLException e) {
            System.err.println("Failed to drop schema");
        }
    }

    // Using DDL (Data Definition Language) to create a db
    public static void setupDatabase() throws SQLException {
        JDBCUtil.getConnection().prepareStatement("CREATE TABLE `exercise_db_1`.`user` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `email` VARCHAR(45) NOT NULL,\n" +
                "  `hashed_password` INT NOT NULL,\n" +
                "  `type` ENUM('ADMIN', 'REGULAR') NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `idusers_UNIQUE` (`id` ASC) VISIBLE,\n" +
                "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);\n"
        ).execute();

        JDBCUtil.getConnection().prepareStatement("CREATE TABLE `exercise_db_1`.`order` (\n" +
                "  `id` BIGINT NOT NULL AUTO_INCREMENT,\n" +
                "  `price` FLOAT NOT NULL,\n" +
                "  `product_name` VARCHAR(45) NOT NULL,\n" +
                "  `purchased_by` BIGINT NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);\n"
        ).execute();
    }

    // Using DML (Data manipulation language) to inject data into the database
    public static void injectData() throws SQLException {
        createUsers();
        createOrders();
    }

    private static void createUsers() {
        String[] names = { "yair", "oz", "tanya", "meital", "or", "emil", "idan", "yvgeni", "dor", "omer",
                           "david1", "david2", "daniel", "olga", "noam", "amit", "lior", "nitay" };

        for (String name : names) {
            UserDAL.instance.create(new User(
                    name + "@jb.co.il",
                    "abc123".hashCode(),
                    UserType.REGULAR
            ));
        }

        UserDAL.instance.create(new User(
                "tav@jb.co.il",
                "admin".hashCode(),
                UserType.ADMIN
            )
        );
    }

    private static void createOrders() {
        float[] prices = { 1.5f, 2500f, 1200f, 430000f };
        String[] productNames = { "Coca cola", "Gaming laptop", "Ticket to Tokyo", "Expensive Fox NFT" };

        for (int i = 0; i < productNames.length; i++) {
            UserDAL.instance.placeOrder(
                    UserDAL.instance.read((long) i + 1).getId(),
                    new Order(prices[i], productNames[i], null)
            );
        }
    }

}