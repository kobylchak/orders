package ua;

import java.sql.*;
import java.util.Scanner;

public class DbWorker {
    private DbProperties props = new DbProperties();
    private Connection connection;

    public DbWorker() {
        try {
            connection = DriverManager.getConnection(props.getConnection(), props.getUser(), props.getPassword());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void initDb() {
        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Goods;");
            st.execute("CREATE TABLE Goods(name VARCHAR(128) NOT NULL PRIMARY KEY, price INT NOT NULL);");
            st.execute("DROP TABLE IF EXISTS Customers;");
            st.execute("CREATE TABLE Customers(login VARCHAR(128) NOT NULL PRIMARY KEY, address VARCHAR(128) NOT NULL);");
            st.execute("DROP TABLE IF EXISTS Orders;");
            st.execute("CREATE TABLE Orders(customer VARCHAR(128) NOT NULL, goods VARCHAR(128) NOT NULL, quantity INT NOT NULL);");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void addGood() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name : ");
        String name = scanner.nextLine();
        System.out.println("Enter price : ");
        String sPrice = scanner.next();
        int price = Integer.parseInt(sPrice);
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Goods (name, price) VALUES (?,?);")) {
            ps.setString(1, name);
            ps.setInt(2, price);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void addCustomer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter login : ");
        String login = scanner.nextLine();
        System.out.println("Enter address : ");
        String address = scanner.nextLine();
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Customers (login, address) VALUES (?,?);")) {
            ps.setString(1, login);
            ps.setString(2, address);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void toOrder() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter login of customer : ");
        String login = scanner.nextLine();
        System.out.println("Enter name of goods : ");
        String goodsName = scanner.nextLine();
        System.out.println("Enter quantity of goods : ");
        String sQuantity = scanner.nextLine();
        int quantity = Integer.parseInt(sQuantity);
        try (Statement st = connection.createStatement()) {
            connection.setAutoCommit(false);
            st.execute("INSERT INTO Orders (customer, goods, quantity) VALUES ((SELECT login FROM Customers WHERE login='" + login + "'), (SELECT name FROM Goods WHERE name='" + goodsName + "'), " + quantity + ");");
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
        }
    }

    protected void showOrders() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Orders;")) {
            ResultSetMetaData md = rs.getMetaData();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                System.out.print(md.getColumnName(i) + "\t\t");
            }
            System.out.println();
            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t\t");
                }
                System.out.println();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
