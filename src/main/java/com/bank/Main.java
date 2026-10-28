
package com.bank;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO dao = new AccountDAO();

        while (true) {
            System.out.println("\n===== BANK MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Transaction History");
            System.out.println("6. Exit");
            System.out.print("Choose option: ");

            String choice = sc.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Customer name: ");
                        String name = sc.nextLine().trim();
                        if (name.isBlank()) {
                            System.out.println("Name cannot be empty.");
                            break;
                        }

                        System.out.print("Account type (SAVINGS/CURRENT): ");
                        String type = sc.nextLine().trim().toUpperCase();
                        if (!type.equals("SAVINGS")
                                && !type.equals("CURRENT")) {
                            System.out.println("Invalid account type.");
                            break;
                        }

                        System.out.print("Initial deposit: ");
                        double initial =
                                Double.parseDouble(sc.nextLine().trim());
                        if (!Double.isFinite(initial) || initial < 0) {
                            System.out.println("Invalid amount.");
                            break;
                        }

                        long number = dao.createAccount(
                                new Account(name, type, initial));
                        System.out.println("Account created successfully!");
                        System.out.println("Account number: " + number);
                    }

                    case "2" -> {
                        System.out.print("Account number: ");
                        long number =
                                Long.parseLong(sc.nextLine().trim());
                        System.out.print("Deposit amount: ");
                        double amount =
                                Double.parseDouble(sc.nextLine().trim());
                        dao.deposit(number, amount);
                        System.out.println("Deposit successful!");
                    }

                    case "3" -> {
                        System.out.print("Account number: ");
                        long number =
                                Long.parseLong(sc.nextLine().trim());
                        System.out.print("Withdrawal amount: ");
                        double amount =
                                Double.parseDouble(sc.nextLine().trim());
                        dao.withdraw(number, amount);
                        System.out.println("Withdrawal successful!");
                    }

                    case "4" -> {
                        System.out.print("Account number: ");
                        long number =
                                Long.parseLong(sc.nextLine().trim());
                        BigDecimal balance = dao.getBalance(number);
                        System.out.println(
                                "Current balance: ₹" + balance.toPlainString());
                    }

                    case "5" -> {
                        System.out.print("Account number: ");
                        long number =
                                Long.parseLong(sc.nextLine().trim());
                        dao.showTransactionHistory(number);
                    }

                    case "6" -> {
                        System.out.println("Thank you for using our system.");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("Invalid option.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter valid numeric values.");
            } catch (IllegalArgumentException | SQLException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }
}
