package ATS.main;

import ATS.model.User;
import ATS.service.NotificationService;

import java.util.Scanner;

public class MainMenu {

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("======================================");
            System.out.println(" Welcome to ATS Console Application");
            System.out.println("======================================");
            System.out.println("1. Candidate");
            System.out.println("2. HR");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    showCandidatePortal(scanner);
                    break;
                case "2":
                    if (authenticateHR(scanner)) {
                        HRMenu.showMenu();
                    }
                    break;
                case "3":
                    System.out.println("Thank you for using ATS. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }

    private static final String HR_PASSWORD = "7015";

    private static boolean authenticateHR(Scanner scanner) {
        while (true) {
            System.out.print("Enter HR password: ");
            String enteredPassword = scanner.nextLine().trim();

            if (HR_PASSWORD.equals(enteredPassword)) {
                return true;
            }

            System.out.println("Incorrect password.");
            System.out.print("Try again? (y/n): ");
            String retry = scanner.nextLine().trim();
            if (!retry.equalsIgnoreCase("y")) {
                System.out.println("Returning to main menu.");
                return false;
            }
        }
    }

    private static void showCandidatePortal(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("========== Candidate Portal ==========");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    CandidateMenu.registerCandidate(scanner);
                    break;
                case "2":
                    User loggedInUser = CandidateMenu.loginCandidate(scanner);
                    if (loggedInUser != null) {
                        NotificationService notificationService = new NotificationService();
                        notificationService.showNotifications(loggedInUser);
                        CandidateMenu.showMenu(loggedInUser);
                    }
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2 or 3.");
            }
        }
    }
}
