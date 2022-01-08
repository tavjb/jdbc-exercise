package answers.dal;

import answers.model.Order;
import answers.util.JDBCUtil;
import answers.util.ObjectExtractionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAL implements CrudDAL<Long, Order>{
    public static final OrderDAL instance = new OrderDAL();

    private OrderDAL() {
        try {
            connection = JDBCUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to establish connection with database");
        }
    }

    private final Connection connection;

    public Long create(final Order order) {
        try {
            String sqlStatement = "INSERT INTO order (price, productName, purchased_by) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, order.getProductName());
            preparedStatement.setFloat(2, order.getPrice());
            preparedStatement.setFloat(3, order.getBuyer().getId());
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

    public Order read(final Long id) {
        try {
            String sqlStatement = "SELECT * FROM order WHERE id = ?";
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

    public void update(final Order order) {
        try {
            String sqlStatement = "UPDATE order SET price = ?, productName = ?, type = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, order.getProductName());
            preparedStatement.setFloat(2, order.getPrice());
            preparedStatement.setLong(3, order.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update the order");
        }
    }

    public void delete(final Long id) {
        try {
            String sqlStatement = "DELETE * FROM order WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to delete order with id: " + id);
        }
    }

    @Override
    public List<Order> readAll() {
        try {
            String sqlStatement = "SELECT * FROM order";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();

            while (result.next()) {
                orders.add(ObjectExtractionUtil.resultToOrder(result));
            }

            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new book");
        }
    }
}
