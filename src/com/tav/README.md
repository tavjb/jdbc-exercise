# JDBC Advanced exercise

Create the following database:
## schema:
- db_exercise_1
## tables:
- user
    - id (bigint)
    - email (varchar)
    - hashed_password (int)
    - type (ADMIN, REGULAR)
- order
    - id (bigint)
    - price (float)
    - product_name (varchar)
    - purchased_by (id of the user)
    
Write data classes (model) for both entities:

## User
```
Long id;
String email;
Integer hashedPassword;
UserType userType;
```

## Order
```
Long id;
float price;
String productName;
User buyer;
```

Also, define a UserDAL with full crud functionality (create, read, update, delete)
and also add two additional methods to UserDAL:
```
Order findUserOrder(Long id)
Long placeOrder(long id, Order order)
```

** findUserOrder will return a full User inside it with the password as null (You will need to use a JOIN statement for this method)

** After defining all the classes, you can use the DatabaseTester that is written in the answers (You might have to make small adjustments)

---------------------------
# Dependencies:

- mysql-connector-java
- lombok

