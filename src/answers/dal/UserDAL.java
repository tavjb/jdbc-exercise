package answers.dal;

import answers.model.Order;
import answers.model.User;
import answers.util.JDBCUtil;
import answers.util.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAL implements CrudDAL<Long, User>{
    public static final UserDAL instance = new UserDAL();

    private UserDAL() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish connection with database");
        }
    }

    private final Connection connection;

    public Long create(final User user) {
        try {
            String sqlStatement = "INSERT INTO user (email, hashed_password, type) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setInt(2, user.getHashedPassword());
            preparedStatement.setString(3, user.getUserType().toString());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve auto-incremented id");
            }

            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new user");
        }
    }

    public User read(final Long id) {
        try {
            String sqlStatement = "SELECT * FROM user WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) { return null; }

            return ObjectExtractionUtil.resultToUser(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve user with id: " + id);
        }
    }


    public void update(final User user) {
        try {
            String sqlStatement = "UPDATE user SET email = ?, hashed_password = ?, type = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setInt(2, user.getHashedPassword());
            preparedStatement.setString(3, user.getUserType().toString());
            preparedStatement.setLong(4, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the user");
        }
    }

    public void delete(final Long id) {
        try {
            String sqlStatement = "DELETE * FROM user WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user with id: " + id);
        }
    }

    @Override
    public List<User> readAll() {
        try {
            String sqlStatement = "SELECT * FROM user";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();

            while (result.next()) {
                users.add(ObjectExtractionUtil.resultToUser(result));
            }

            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new book");
        }
    }


    public Order findUserOrder(Long id) {
        try {
            String sqlStatement = "SELECT `order`.id, price, product_name, `user`.id AS buyer_id, email AS buyer_email, type AS buyer_type FROM `order` JOIN `user` ON purchased_by=`user`.id WHERE purchased_by=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) { return null; }

            return ObjectExtractionUtil.resultToOrder(result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve order with id: " + id);
        }
    }

    public Long placeOrder(User user, Order order) {
        try {
            String query = "INSERT INTO exercise_db_1.order (price, product_name, purchased_by) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, order.getPrice());
            preparedStatement.setString(2, order.getProductName());
            preparedStatement.setLong(3, user.getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeysResult = preparedStatement.getGeneratedKeys();

            if (!generatedKeysResult.next()) {
                throw new RuntimeException("Failed to retrieve auto-incremented id");
            }

            return generatedKeysResult.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new order");
        }
    }
}
