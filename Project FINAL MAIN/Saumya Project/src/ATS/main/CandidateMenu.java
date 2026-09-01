package ATS.main;

import ATS.model.Application;
import ATS.model.Job;
import ATS.model.User;
import ATS.service.ApplicationService;
import ATS.service.ApplicationTransactionService;
import ATS.service.JobService;
import ATS.service.ResumeService;
import ATS.service.ResumeTemplateService;
import ATS.service.TestService;
import ATS.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class CandidateMenu {

    private static UserService userService = new UserService();
    private static JobService jobService = new JobService();
    private static ApplicationService applicationService = new ApplicationService();
    private static ResumeService resumeService = new ResumeService();
    private static ResumeTemplateService resumeTemplateService = new ResumeTemplateService();
    private static TestService testService = new TestService();
    private static ApplicationTransactionService applicationTransactionService =
            new ApplicationTransactionService();

    private static String readValidEmail(Scanner scanner) {
        while (true) {
            System.out.print("Enter email: ");
            String email = scanner.nextLine().trim();

            if (email.isEmpty()) {
                System.out.println("Email cannot be blank. Please try again.");
                continue;
            }

            if (isValidEmail(email)) {
                return email;
            }

            System.out.println("Invalid email. Use a gmail.com or yahoo.com address without spaces.");
        }
    }

    private static boolean isValidEmail(String email) {
        if (email.contains(" ")) {
            return false;
        }

        int atIndex = email.indexOf('@');
        if (atIndex <= 0) {
            return false;
        }

        if (email.indexOf('@', atIndex + 1) != -1) {
            return false;
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);

        if (username.length() == 0) {
            return false;
        }

        return domain.equalsIgnoreCase("gmail.com") || domain.equalsIgnoreCase("yahoo.com");
    }

    private static String readValidPassword(Scanner scanner) {
        while (true) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (password == null || password.trim().isEmpty()) {
                System.out.println("Password cannot be blank. Please try again.");
                continue;
            }

            if (isValidPassword(password)) {
                return password;
            }

            System.out.println("Password must contain");
            System.out.println("- Minimum 8 characters");
            System.out.println("- One uppercase letter");
            System.out.println("- One lowercase letter");
            System.out.println("- One digit");
            System.out.println("- One special character");
            System.out.println("- No spaces");
        }
    }

    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        boolean hasSpace = false;

        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);

            if (ch == ' ') {
                hasSpace = true;
            } else if (ch >= 'A' && ch <= 'Z') {
                hasUpper = true;
            } else if (ch >= 'a' && ch <= 'z') {
                hasLower = true;
            } else if (ch >= '0' && ch <= '9') {
                hasDigit = true;
            } else if (ch == '@' || ch == '#' || ch == '$' || ch == '%' || ch == '&' || ch == '!' || ch == '*' || ch == '?') {
                hasSpecial = true;
            }
        }

        return !hasSpace && hasUpper && hasLower && hasDigit && hasSpecial;
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static void showMenu(User loggedInUser) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("========== Candidate Menu ==========");
            System.out.println("1. View Jobs");
            System.out.println("2. Download Resume Template");
            System.out.println("3. Apply for Job");
            System.out.println("4. View Application Status");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewJobs();
                    break;
                case "2":
                    downloadResumeTemplate();
                    break;
                case "3":
                    applyForJob(scanner, loggedInUser);
                    break;
                case "4":
                    viewAppliedJobs(scanner, loggedInUser);
                    break;
                case "5":
                    System.out.println("Logging out of candidate menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    public static void registerCandidate(Scanner scanner) {
        System.out.println("=================================");
        System.out.println("      Candidate Registration");
        System.out.println("=================================");

        String fullName;
        while (true) {
            System.out.print("Enter full name: ");
            fullName = scanner.nextLine().trim();
            if (isBlank(fullName)) {
                System.out.println("Name cannot be blank. Please try again.");
            } else {
                break;
            }
        }

        String email = readValidEmail(scanner);

        if (userService.getUserByEmail(email) != null) {
            System.out.println("Email already registered.");
            System.out.println("Registration cancelled.");
            return;
        }

        String password = readValidPassword(scanner);

        while (true) {
            System.out.print("Confirm Password: ");
            String confirmPassword = scanner.nextLine();

            if (confirmPassword == null) {
                confirmPassword = "";
            }

            if (password.equals(confirmPassword)) {
                break;
            }

            System.out.println("Passwords do not match. Enter again.");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("Candidate");

        boolean success = userService.registerCandidate(user);

        if (success) {
            System.out.println("\nRegistration Successful!");
            System.out.println("\nWelcome " + fullName + ".");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    public static User loginCandidate(Scanner scanner) {
        System.out.println("=================================");
        System.out.println("        Candidate Login");
        System.out.println("=================================");

        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine().trim();
            if (isBlank(email)) {
                System.out.println("Email cannot be blank. Please try again.");
            } else {
                break;
            }
        }

        User existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            System.out.println("Email not found. Returning to Main Menu...");
            return null;
        }

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (password == null || password.trim().isEmpty()) {
                System.out.println("Password cannot be blank. Please try again.");
                attempt--;
                continue;
            }

            User user = userService.loginUser(email, password);
            if (user != null) {
                System.out.println("Login successful. Welcome " + user.getFullName() + "!");
                return user;
            }

            if (attempt < 3) {
                System.out.println("Incorrect Password.");
            } else {
                System.out.println("Too many failed attempts.");
                System.out.println("Returning to Main Menu...");
            }
        }

        return null;
    }

    private static void viewJobs() {
        System.out.println("\n--- Available Jobs ---");
        ArrayList<Job> jobs = (ArrayList<Job>) jobService.getAllJobs();

        if (jobs == null || jobs.isEmpty()) {
            System.out.println("No jobs found.");
            return;
        }

        for (Job job : jobs) {
            System.out.println("Job ID: " + job.getJobId());
            System.out.println("Title: " + job.getJobTitle());
            System.out.println("Description: " + job.getDescription());
            System.out.println("Passing Percentage: " + job.getPassingPercentage());
            System.out.println("Minimum Resume Score: " + job.getMinimumResumeScore());
            System.out.println("Status: " + job.getStatus());
            System.out.println("----------------------------------------");
        }
    }

    private static void applyForJob(Scanner scanner, User user) {
        if (user == null) {
            System.out.println("Please login first to apply for a job.");
            return;
        }

        System.out.println("\n--- Apply for Job ---");

        ArrayList<Application> previousApplications = applicationService.getApplicationsByUserId(user.getUserId());

        for (Application application : previousApplications) {
            if ("Rejected".equalsIgnoreCase(application.getStatus())) {
                LocalDate today = LocalDate.now();
                LocalDate cooldownEnd = application.getCooldownEnd();

                if (cooldownEnd != null && today.isBefore(cooldownEnd)) {
                    System.out.println("=========================================");
                    System.out.println("Application Blocked");
                    System.out.println("=========================================");
                    System.out.println();
                    System.out.println("You cannot apply for any job yet.");
                    System.out.println();
                    System.out.println("Previous Application Status : Rejected");
                    System.out.println("Cooldown Ends On : " + cooldownEnd);
                    System.out.println();
                    System.out.println("Please wait until the cooldown period ends.");
                    System.out.println("=========================================");
                    return;
                }
            }
        }

        System.out.print("Enter Job ID: ");
        int jobId = readInt(scanner);
        System.out.print("Enter Resume File Path: ");
        String resumePath = scanner.nextLine().trim();

        Application existingApplication = applicationService.getApplication(user.getUserId(), jobId);

        if (existingApplication != null) {
            String status = existingApplication.getStatus();

            if (isDuplicateStatus(status)) {
                System.out.println("=========================================");
                System.out.println("You have already applied for this job.");
                System.out.println("Current Status : " + status);
                System.out.println("Duplicate applications are not allowed.");
                System.out.println("=========================================");
                return;
            }

            if ("Rejected".equalsIgnoreCase(status)) {
                LocalDate today = LocalDate.now();
                LocalDate cooldownEnd = existingApplication.getCooldownEnd();

                if (cooldownEnd != null && today.isBefore(cooldownEnd)) {
                    System.out.println("=========================================");
                    System.out.println("Application Blocked");
                    System.out.println("=========================================");
                    System.out.println();
                    System.out.println("You cannot apply for this job yet.");
                    System.out.println();
                    System.out.println("Previous Application Status : Rejected");
                    System.out.println("Cooldown Ends On : " + cooldownEnd);
                    System.out.println();
                    System.out.println("Please wait until cooldown ends.");
                    System.out.println("=========================================");
                    return;
                }

                System.out.println("=========================================");
                System.out.println("Previous cooldown has expired.");
                System.out.println("You may apply again.");
                System.out.println("=========================================");
            }
        }

        applicationTransactionService.processApplication(user, jobId, resumePath);
    }

    private static void downloadResumeTemplate() {
        resumeTemplateService.downloadResumeTemplate();
    }

//    private static void uploadResume(Scanner scanner, User user) {
//        if (user == null) {
//            System.out.println("Please login first to upload a resume.");
//            return;
//        }
//
//        System.out.println("\n--- Upload Resume ---");
//        System.out.print("Enter Application ID: ");
//        int applicationId = readInt(scanner);
//        System.out.print("Enter Job ID: ");
//        int jobId = readInt(scanner);
//        System.out.print("Enter file path for resume: ");
//        String filePath = scanner.nextLine().trim();
//
//        resumeService.processResume(filePath, applicationId, jobId);
//    }

//    private static void takeTest(Scanner scanner, User user) {
//        if (user == null) {
//            System.out.println("Please login first to take a test.");
//            return;
//        }
//
//        System.out.println("\n--- Take Test ---");
//        System.out.print("Enter Application ID: ");
//        int applicationId = readInt(scanner);
//        System.out.print("Enter Job ID: ");
//        int jobId = readInt(scanner);
//
//        testService.startTest(applicationId, jobId);
//    }

    private static void viewAppliedJobs(Scanner scanner, User user) {
        if (user == null) {
            System.out.println("Please login first to view applied jobs.");
            return;
        }

        System.out.println("\n--- View Applied Jobs ---");
        System.out.print("Enter Job ID for application details: ");
        int jobId = readInt(scanner);

        Application application = applicationService.getApplication(user.getUserId(), jobId);

        if (application == null) {
            System.out.println("No application found for this job.");
            return;
        }

        System.out.println(application.toString());
    }

//    private static void updateOfferStatus(Scanner scanner, User user, String status) {
//        if (user == null) {
//            System.out.println("Please login first to update offer status.");
//            return;
//        }
//
//        System.out.println("\n--- " + status + " Offer ---");
//        System.out.print("Enter Application ID: ");
//        int applicationId = readInt(scanner);
//
//        System.out.print("Enter Offer ID: ");
//        int offerId = readInt(scanner);
//
//        boolean success = false;
//
//        try {
//            ATS.dao.OfferDAO offerDAO = new ATS.dao.OfferDAO();
//            success = offerDAO.updateOfferStatus(offerId, status);
//        } catch (Exception ex) {
//            System.out.println("Error updating offer status: " + ex.getMessage());
//        }
//
//        if (success) {
//            System.out.println("Offer status updated to " + status + ".");
//            applicationService.updateApplicationStatus(applicationId, status.equals("Accepted") ? "Offer Accepted" : "Rejected");
//        } else {
//            System.out.println("Unable to update offer status. Please check the offer ID.");
//        }
//    }

    private static boolean isDuplicateStatus(String status) {
        if (status == null) {
            return false;
        }

        String normalizedStatus = status.trim();

        return "Awaiting HR Review".equalsIgnoreCase(normalizedStatus)
                || "Interview Scheduled".equalsIgnoreCase(normalizedStatus)
                || "Completed".equalsIgnoreCase(normalizedStatus)
                || "Offer Sent".equalsIgnoreCase(normalizedStatus)
                || "OfferAccepted".equalsIgnoreCase(normalizedStatus);
    }

    private static int readInt(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter a valid integer: ");
            }
        }
    }
}
