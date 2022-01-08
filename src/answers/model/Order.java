package answers.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Order {
    public Order(float price, String productName, User buyer) {
        this.id = null;
        this.price = price;
        this.productName = productName;
        this.buyer = buyer;
    }

    public Order(float price, String productName) {
        this.id = null;
        this.price = price;
        this.productName = productName;
    }

    private final Long id;
    private float price;
    private String productName;
    private User buyer;
}
