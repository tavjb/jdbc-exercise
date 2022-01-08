package answers.util;

import answers.model.Order;
import answers.model.User;
import answers.model.enums.UserType;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectExtractionUtil {
    public static User resultToUser(ResultSet result) throws SQLException {
        return new User(
                result.getLong("id"),
                result.getString("email"),
                result.getInt("hashed_password"),
                UserType.valueOf(result.getString("type"))
        );
    }

    public static Order resultToOrder(ResultSet result) throws SQLException {
        return new Order(
                result.getLong("id"),
                result.getFloat("price"),
                result.getString("product_name"),
                new User(
                        result.getLong("buyer_id"),
                        result.getString("buyer_email"),
                        null,
                        UserType.valueOf(result.getString("buyer_type"))
                ));
    }
}
