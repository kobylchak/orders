package ua;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbWorker db = new DbWorker();
        db.initDb();
        while (true) {
            System.out.println("1 : add goods");
            System.out.println("2 : add customer");
            System.out.println("3 : make an order");
            System.out.println("4 : show orders");
            System.out.println("5 : exit");
            System.out.println(" -> ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    db.addGood();
                    break;
                case 2:
                    db.addCustomer();
                    break;
                case 3:
                    try {
                        db.toOrder();
                    } catch (SQLException e) {
                        break;
                    }
                case 4:
                    db.showOrders();
                    break;
                case 5:
                    return;
                default:
                    break;
            }
        }
    }
}
