package ATS.main;

import ATS.model.*;
import ATS.service.ApplicationService;
import ATS.service.InterviewService;
import ATS.service.JobService;
import ATS.service.JobSkillService;
import ATS.service.OfferService;
import ATS.service.RejectionService;
import ATS.service.SkillService;
import ATS.service.UserService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HRMenu {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static JobService jobService = new JobService();
    private static SkillService skillService = new SkillService();
    private static JobSkillService jobSkillService = new JobSkillService();
    private static ApplicationService applicationService = new ApplicationService();
    private static InterviewService interviewService = new InterviewService();
    private static RejectionService rejectionService = new RejectionService();
    private static UserService userService = new UserService();
    private static OfferService offerService = new OfferService();

    public static void showMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("============ HR Menu ============");
            System.out.println("1. Manage Jobs");
            System.out.println("2. Manage Skills");
            System.out.println("3. View Applications");
            System.out.println("4. Schedule Interview");
            System.out.println("5. Complete Interview");
            System.out.println("6. Send Offer");
            System.out.println("7. Logout");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manageJobs(scanner);
                    break;
                case "2":
                    manageSkills(scanner);
                    break;
                case "3":
                    viewApplication(scanner);
                    break;
                case "4":
                    manageCandidateSelection(scanner);
                    break;
                case "5":
                    completeInterview(scanner);
                    break;
                case "6":
                    sendOffer(scanner);
                    break;
                case "7":
                    System.out.println("Logging out of HR menu.");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }

    private static void manageJobs(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Manage Jobs ---");
            System.out.println("1. Add Job");
            System.out.println("2. Update Job");
            System.out.println("3. Delete Job");
            System.out.println("4. View All Jobs");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("5")) {
                break;
            }

            if (choice.equals("1")) {
                Job job = new Job();
                System.out.print("Enter job title: ");
                job.setJobTitle(readJobTitle(scanner));
                System.out.print("Enter description: ");
                job.setDescription(scanner.nextLine().trim());
                System.out.print("Enter vacancies: ");
                job.setVacancies(readPositiveVacancies(scanner));
                System.out.print("Enter status: ");
                job.setStatus(readJobStatus(scanner));
                job.setCooldownMonths(3);
                job.setTestQuestions(0);
                job.setDurationMinutes(0);
                job.setPassingPercentage(readPassingPercentage(scanner));
                System.out.print("Enter minimum resume score: ");
                job.setMinimumResumeScore(Double.parseDouble(scanner.nextLine().trim()));
                System.out.print("Enter difficulty: ");
                job.setDifficulty(readJobDifficulty(scanner));
                System.out.print("Enter created by user ID: ");
                job.setCreatedBy(readInt(scanner));

                boolean added = jobService.addJob(job);
                if (added) {
                    System.out.println("Job added successfully.");
                } else {
                    System.out.println("Unable to add job.");
                }
            } else if (choice.equals("2")) {
                Job job = new Job();
                System.out.print("Enter job ID: ");
                job.setJobId(readInt(scanner));
                System.out.print("Enter job title: ");
                job.setJobTitle(readJobTitle(scanner));
                System.out.print("Enter description: ");
                job.setDescription(scanner.nextLine().trim());
                System.out.print("Enter vacancies: ");
                job.setVacancies(readPositiveVacancies(scanner));
                System.out.print("Enter status: ");
                job.setStatus(readJobStatus(scanner));
                job.setCooldownMonths(3);
                job.setTestQuestions(0);
                job.setDurationMinutes(0);
                job.setPassingPercentage(readPassingPercentage(scanner));
                System.out.print("Enter minimum resume score: ");
                job.setMinimumResumeScore(Double.parseDouble(scanner.nextLine().trim()));
                System.out.print("Enter difficulty: ");
                job.setDifficulty(readJobDifficulty(scanner));
                System.out.print("Enter created by user ID: ");
                job.setCreatedBy(readInt(scanner));

                boolean updated = jobService.updateJob(job);
                if (updated) {
                    System.out.println("Job updated successfully.");
                } else {
                    System.out.println("Unable to update job.");
                }
            } else if (choice.equals("3")) {
                System.out.print("Enter job ID: ");
                int jobId = readInt(scanner);
                boolean deleted = jobService.deleteJob(jobId);
                if (deleted) {
                    System.out.println("Job deleted successfully.");
                } else {
                    System.out.println("Unable to delete job.");
                }
            } else if (choice.equals("4")) {
                ArrayList<Job> jobs = (ArrayList<Job>) jobService.getAllJobs();

                if (jobs == null || jobs.isEmpty()) {
                    System.out.println("No jobs found.");
                    continue;
                }

                for (Job currentJob : jobs) {
                    System.out.println(currentJob.toString());
                    System.out.println("----------------------------------------");
                }
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private static void manageSkills(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Manage Skills ---");
            System.out.println("1. Add Skill");
            System.out.println("2. Update Skill");
            System.out.println("3. Delete Skill");
            System.out.println("4. Assign Skill To Job");
            System.out.println("5. View All Skills");
            System.out.println("6. Back");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("6")) {
                break;
            }

            if (choice.equals("1")) {
                Skill skill = new Skill();
                System.out.print("Enter skill name: ");
                skill.setSkillName(scanner.nextLine().trim());
                System.out.print("Enter skill points: ");
                skill.setSkillPoints(readSkillPoints(scanner));

                boolean added = skillService.addSkill(skill);
                if (added) {
                    System.out.println("Skill added successfully.");
                } else {
                    System.out.println("Unable to add skill.");
                }
            } else if (choice.equals("2")) {
                Skill skill = new Skill();
                System.out.print("Enter skill ID: ");
                skill.setSkillId(readInt(scanner));
                System.out.print("Enter skill name: ");
                skill.setSkillName(scanner.nextLine().trim());
                System.out.print("Enter skill points: ");
                skill.setSkillPoints(readSkillPoints(scanner));

                boolean updated = skillService.updateSkill(skill);
                if (updated) {
                    System.out.println("Skill updated successfully.");
                } else {
                    System.out.println("Unable to update skill.");
                }
            } else if (choice.equals("3")) {
                System.out.print("Enter skill ID: ");
                int skillId = readInt(scanner);
                boolean deleted = skillService.deleteSkill(skillId);
                if (deleted) {
                    System.out.println("Skill deleted successfully.");
                } else {
                    System.out.println("Unable to delete skill.");
                }
            } else if (choice.equals("4")) {
                JobSkill jobSkill = new JobSkill();
                System.out.print("Enter job ID: ");
                jobSkill.setJobId(readInt(scanner));
                System.out.print("Enter skill ID: ");
                jobSkill.setSkillId(readInt(scanner));
                System.out.print("Enter skill points: ");
                jobSkill.setSkillPoints(readInt(scanner));

                boolean assigned = jobSkillService.assignSkillToJob(jobSkill);
                if (assigned) {
                    System.out.println("Skill assigned to job successfully.");
                } else {
                    System.out.println("Unable to assign skill to job.");
                }
            } else if (choice.equals("5")) {
                ArrayList<Skill> skills = skillService.getAllSkills();

                if (skills == null || skills.isEmpty()) {
                    System.out.println("No skills found.");
                    continue;
                }

                for (Skill skill : skills) {
                    System.out.println(skill.toString());
                }
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void viewApplication(Scanner scanner) {
        System.out.println("\n--- View Applications ---");

        ArrayList<ATS.model.Application> applications = applicationService.getAllApplications();
        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        PriorityQueue<ATS.model.Application> queue = new PriorityQueue<ATS.model.Application>(new Comparator<ATS.model.Application>() {
            public int compare(ATS.model.Application a1, ATS.model.Application a2) {
                if (a1.getFinalScore() > a2.getFinalScore()) {
                    return -1;
                } else if (a1.getFinalScore() < a2.getFinalScore()) {
                    return 1;
                }
                return 0;
            }
        });

        for (int i = 0; i < applications.size(); i++) {
            queue.add(applications.get(i));
        }

        ArrayList<ATS.model.Application> rankedApplications = new ArrayList<ATS.model.Application>();
        while (!queue.isEmpty()) {
            rankedApplications.add(queue.poll());
        }

        for (int i = 0; i < rankedApplications.size(); i++) {
            ATS.model.Application application = rankedApplications.get(i);
            ATS.model.User user = userService.getUserById(application.getUserId());
            ATS.model.Job job = jobService.getJobById(application.getJobId());
            String candidateName = user != null ? user.getFullName() : "Unknown";
            String jobTitle = job != null ? job.getJobTitle() : "Unknown";
            System.out.println((i + 1) + " " + candidateName);
            System.out.println("Applied Job : " + jobTitle);
            System.out.println("Current Status : " + application.getStatus());
            System.out.println("Final Score : " + String.format("%.2f", application.getFinalScore()));
            System.out.println("-----------------------------------------");
        }

        System.out.print("Choose Candidate Number: ");
        int choice = readInt(scanner);

        if (choice < 1 || choice > rankedApplications.size()) {
            System.out.println("Invalid candidate number.");
            return;
        }

        ATS.model.Application selectedApplication = rankedApplications.get(choice - 1);
        ATS.model.User selectedUser = userService.getUserById(selectedApplication.getUserId());
        ATS.model.Job selectedJob = jobService.getJobById(selectedApplication.getJobId());
        String selectedName = selectedUser != null ? selectedUser.getFullName() : "Unknown";
        String selectedJobTitle = selectedJob != null ? selectedJob.getJobTitle() : "Unknown";

        System.out.println("\nSelected Candidate");
        System.out.println("Name : " + selectedName);
        System.out.println("Applied Job : " + selectedJobTitle);
        System.out.println("Resume Score : " + selectedApplication.getResumeScore());
        System.out.println("Test Score : " + selectedApplication.getTestScore());
        System.out.println("Final Score : " + String.format("%.2f", selectedApplication.getFinalScore()));
        System.out.println("Status : " + selectedApplication.getStatus());
    }

    private static void manageCandidateSelection(Scanner scanner) {
        System.out.println("\nFetching Applications...");
        System.out.println("Creating Priority Queue...");
        System.out.println("Ranking Candidates...");

        ArrayList<ATS.model.Application> applications = applicationService.getAllApplications();

        if (applications == null || applications.isEmpty()) {
            System.out.println("No applications found.");
            return;
        }

        PriorityQueue<ATS.model.Application> queue = new PriorityQueue<ATS.model.Application>(new Comparator<ATS.model.Application>() {
            public int compare(ATS.model.Application a1, ATS.model.Application a2) {
                if (a1.getFinalScore() > a2.getFinalScore()) {
                    return -1;
                } else if (a1.getFinalScore() < a2.getFinalScore()) {
                    return 1;
                }
                return 0;
            }
        });

        for (int i = 0; i < applications.size(); i++) {
            ATS.model.Application application = applications.get(i);
            String status = application.getStatus();
            if ("Rejected".equalsIgnoreCase(status)) {
                continue;
            }
            queue.add(application);
        }

        ArrayList<ATS.model.Application> rankedApplications = new ArrayList<ATS.model.Application>();
        while (!queue.isEmpty()) {
            rankedApplications.add(queue.poll());
        }

        System.out.println("=====================================");
        System.out.println("Candidate Ranking");
        System.out.println("=====================================");

        for (int i = 0; i < rankedApplications.size(); i++) {
            ATS.model.Application application = rankedApplications.get(i);
            ATS.model.User user = userService.getUserById(application.getUserId());
            ATS.model.Job job = jobService.getJobById(application.getJobId());
            String candidateName = user != null ? user.getFullName() : "Unknown";
            String jobTitle = job != null ? job.getJobTitle() : "Unknown";
            String appliedDate = application.getAppliedDate() != null ? application.getAppliedDate().toLocalDate().toString() : "N/A";

            System.out.println((i + 1) + " " + candidateName);
            System.out.println("Applied Job : " + jobTitle);
            System.out.println("Resume Score : " + application.getResumeScore());
            System.out.println("Test Score : " + application.getTestScore());
            System.out.println("Final Score : " + String.format("%.2f", application.getFinalScore()));
            System.out.println("Applied Date : " + appliedDate);
            System.out.println("Current Status : " + application.getStatus());
            System.out.println("-----------------------------------------");
        }

        System.out.print("Choose Candidate Number: ");
        int choice = readInt(scanner);

        if (choice < 1 || choice > rankedApplications.size()) {
            System.out.println("Invalid candidate number.");
            return;
        }

        ATS.model.Application selectedApplication = rankedApplications.get(choice - 1);
        showCandidateActions(scanner, selectedApplication);
    }

    private static void showCandidateActions(Scanner scanner, ATS.model.Application application) {
        ATS.model.User user = userService.getUserById(application.getUserId());
        ATS.model.Job job = jobService.getJobById(application.getJobId());
        String candidateName = user != null ? user.getFullName() : "Unknown";
        String jobTitle = job != null ? job.getJobTitle() : "Unknown";
        String appliedDate = application.getAppliedDate() != null ? application.getAppliedDate().toLocalDate().toString() : "N/A";

        System.out.println("\n=========================================");
        System.out.println("Selected Candidate");
        System.out.println("=========================================");
        System.out.println("Name : " + candidateName);
        System.out.println("Applied Job : " + jobTitle);
        System.out.println("Resume Score : " + application.getResumeScore());
        System.out.println("Test Score : " + application.getTestScore());
        System.out.println("Final Score : " + String.format("%.2f", application.getFinalScore()));
        System.out.println("Applied Date : " + appliedDate);
        System.out.println("Current Status : " + application.getStatus());
        System.out.println("-----------------------------------------");
        System.out.println("1 Schedule Interview");
        System.out.println("2 Reject Candidate");
        System.out.println("3 Back");
        System.out.print("Enter your choice: ");

        String hrChoice = scanner.nextLine().trim();

        if (hrChoice.equals("1")) {
            scheduleInterviewForCandidate(scanner, application, candidateName, jobTitle);
        } else if (hrChoice.equals("2")) {
            rejectCandidate(
                    scanner,
                    application,
                    candidateName,
                    jobTitle
            );
        } else if (hrChoice.equals("3")) {
            return;
        } else {
            System.out.println("Invalid choice. Returning to HR menu.");
        }
    }

    private static void completeInterview(Scanner scanner) {
        System.out.println("\n=========================================");
        System.out.println("Candidates With Scheduled Interview");
        System.out.println("=========================================");

        ArrayList<Application> applications = applicationService.getAllApplications();
        ArrayList<Application> eligibleApplications = new ArrayList<Application>();

        for (int i = 0; i < applications.size(); i++) {
            Application application = applications.get(i);
            if (!"Interview Scheduled".equals(application.getStatus())) {
                continue;
            }

            ATS.model.Interview interview = new ATS.dao.InterviewDAO()
                    .getInterviewByApplication(application.getApplicationId());
            if (interview != null && "Scheduled".equals(interview.getStatus())) {
                eligibleApplications.add(application);
            }
        }

        if (eligibleApplications.isEmpty()) {
            System.out.println("=========================================");
            System.out.println("No scheduled interviews found.");
            System.out.println("=========================================");
            return;
        }

        for (int i = 0; i < eligibleApplications.size(); i++) {
            Application application = eligibleApplications.get(i);
            User user = userService.getUserById(application.getUserId());
            Job job = jobService.getJobById(application.getJobId());
            ATS.model.Interview interview = new ATS.dao.InterviewDAO()
                    .getInterviewByApplication(application.getApplicationId());
            String candidateName = user != null ? user.getFullName() : "Unknown";
            String jobTitle = job != null ? job.getJobTitle() : "Unknown";
            String interviewDate = interview != null ? interview.getInterviewDate() : "N/A";
            String interviewTime = interview != null ? interview.getInterviewTime() : "N/A";

            System.out.println((i + 1) + " " + candidateName);
            System.out.println();
            System.out.println("Applied Job : " + jobTitle);
            System.out.println();
            System.out.println("Interview Date : " + interviewDate);
            System.out.println();
            System.out.println("Interview Time : " + interviewTime);
            System.out.println();
            System.out.println("Interview Status : Scheduled");
            System.out.println("-----------------------------------------");
        }

        System.out.print("Choose Candidate Number: ");
        int choice = readInt(scanner);

        if (choice < 1 || choice > eligibleApplications.size()) {
            System.out.println("Invalid candidate number.");
            return;
        }

        Application selectedApplication = eligibleApplications.get(choice - 1);
        boolean success = interviewService.completeInterview(selectedApplication.getApplicationId());

        if (success) {
            System.out.println("=========================================");
            System.out.println("Interview Completed Successfully");
            System.out.println("=========================================");
            System.out.println();
//            System.out.println("Candidate is now eligible for Offer.");
//            System.out.println("=========================================");
        } else {
            System.out.println("Unable to complete interview.");
        }
    }

    private static void scheduleInterviewForCandidate(Scanner scanner, ATS.model.Application application, String candidateName, String jobTitle) {
        if (application != null && "Rejected".equalsIgnoreCase(application.getStatus())) {
            System.out.println("=========================================");
            System.out.println("Interview Cannot Be Scheduled");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("This candidate has been rejected.");
            System.out.println("Current Status : Rejected");
            System.out.println("Cooldown Ends On : " + application.getCooldownEnd());
            System.out.println("=========================================");
            return;
        }

        String interviewDate;
        while (true) {
            System.out.print("Enter interview date (yyyy-mm-dd): ");
            interviewDate = scanner.nextLine().trim();
            try {
                LocalDate date = LocalDate.parse(interviewDate);
                if (!date.isBefore(LocalDate.now())) {
                    break;
                }
                System.out.println("=========================================");
                System.out.println("Invalid Interview Date.");
                System.out.println("Interview date cannot be in the past.");
                System.out.println("Please enter today's date or a future date.");
                System.out.println("=========================================");
            } catch (Exception e) {
                System.out.println("=========================================");
                System.out.println("Invalid Interview Date.");
                System.out.println("Interview date cannot be in the past.");
                System.out.println("Please enter today's date or a future date.");
                System.out.println("=========================================");
            }
        }

        String interviewTime;
        while (true) {
            System.out.print("Enter interview time (HH:mm): ");
            interviewTime = scanner.nextLine().trim();
            try {
                LocalTime.parse(interviewTime, TIME_FORMATTER);
                break;
            } catch (Exception e) {
                System.out.println("=========================================");
                System.out.println("Invalid Time Format.");
                System.out.println("Please enter time in HH:mm format.");
                System.out.println("Example");
                System.out.println("09:30");
                System.out.println("14:45");
                System.out.println("=========================================");
            }
        }

        ATS.model.Application currentApplication =
                applicationService.getApplicationById(application.getApplicationId());
        ATS.model.Interview existingInterview =
                new ATS.dao.InterviewDAO().getInterviewByApplication(application.getApplicationId());

        boolean alreadyValidated = existingInterview != null;
        if (!alreadyValidated && currentApplication != null) {
            String currentStatus = currentApplication.getStatus();
            alreadyValidated = "Rejected".equalsIgnoreCase(currentStatus)

                    || "Offer Sent".equalsIgnoreCase(currentStatus)
                    || "Offer Declined".equalsIgnoreCase(currentStatus);
        }//|| "Interview Scheduled".equalsIgnoreCase(currentStatus)

        boolean success = interviewService.scheduleInterview(application.getApplicationId(), interviewDate, interviewTime);
        if (!success && !alreadyValidated) {
            System.out.println("Unable to schedule interview. Please try again.");
        }
    }

    private static void rejectCandidate(
            Scanner scanner,
            Application application,
            String candidateName,
            String jobTitle){
        System.out.print("Enter rejection reason: ");
        String rejectionReason = scanner.nextLine().trim();

        boolean success = rejectionService.rejectApplication(application.getApplicationId(),1,"", rejectionReason, "HR");
        if (success) {
            ATS.model.Application rejectedApplication =
                    applicationService.getApplicationById(application.getApplicationId());
            System.out.println("=========================================");
            System.out.println("Candidate : " + candidateName);
            System.out.println("Applied Job : " + jobTitle);
            System.out.println("Reason : " + rejectionReason);
            if (rejectedApplication != null) {
                System.out.println("Cooldown Ends On : " + rejectedApplication.getCooldownEnd());
            }
            System.out.println("=========================================");
        } else {
            System.out.println("Unable to reject candidate. Please try again.");
        }
    }

    private static void sendOffer(Scanner scanner) {
        System.out.println("\n=========================================");
        System.out.println("Candidates Eligible for Offer");
        System.out.println("=========================================");

        ArrayList<Application> applications = applicationService.getAllApplications();
        ArrayList<Application> eligibleApplications = new ArrayList<Application>();

        for (int i = 0; i < applications.size(); i++) {
            Application application = applications.get(i);
            String status = application.getStatus();

            if ("Rejected".equalsIgnoreCase(status)
                    || "Offer Sent".equalsIgnoreCase(status)
                    || "Offer Declined".equalsIgnoreCase(status)) {
                continue;
            }

            if (!"Completed".equalsIgnoreCase(status)) {
                continue;
            }

            ATS.model.Interview interview = new ATS.dao.InterviewDAO()
                    .getInterviewByApplication(application.getApplicationId());
            if (interview != null && "Completed".equalsIgnoreCase(interview.getStatus())) {
                eligibleApplications.add(application);
            }
        }

        if (eligibleApplications.isEmpty()) {
            System.out.println("No candidates eligible for offer.");
            return;
        }

        for (int i = 0; i < eligibleApplications.size(); i++) {
            Application application = eligibleApplications.get(i);
            User user = userService.getUserById(application.getUserId());
            Job job = jobService.getJobById(application.getJobId());
            String candidateName = user != null ? user.getFullName() : "Unknown";
            String jobTitle = job != null ? job.getJobTitle() : "Unknown";
            System.out.println((i + 1) + " " + candidateName);
            System.out.println();
            System.out.println("Applied Job : " + jobTitle);
            System.out.println();
            System.out.println("Interview Status : offer");
            System.out.println("-----------------------------------------");
        }

        System.out.print("Choose Candidate Number: ");
        int choice = readInt(scanner);

        if (choice < 1 || choice > eligibleApplications.size()) {
            System.out.println("Invalid candidate number.");
            return;
        }

        ATS.model.Application selectedApplication = eligibleApplications.get(choice - 1);

        // Reuse OfferService, which checks the application status again
        // right before creating the offer (and updates the status to
        // "Offer Sent" once it succeeds), so HR can never send a duplicate
        // offer through this menu either.
        boolean success = offerService.sendOfferForApplication(
                selectedApplication.getApplicationId(), LocalDate.now(), "Pending");

        if (success) {
            System.out.println("=========================================");
            System.out.println("Offer Sent Successfully");
            System.out.println("=========================================");
        }
    }

    private static String readJobTitle(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }

            System.out.println("=========================================");
            System.out.println("Invalid Job Title.");
            System.out.println("Job Title cannot be empty.");
            System.out.println("Please enter again.");
            System.out.println("=========================================");
            System.out.print("Enter job title: ");
        }
    }

    private static int readSkillPoints(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= 0 && value <= 100) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // continue asking
            }

            System.out.println("=========================================");
            System.out.println("Invalid Skill Points.");
            System.out.println("Skill Points must be between 0 and 100.");
            System.out.println("Please enter again.");
            System.out.println("=========================================");
            System.out.print("Enter skill points: ");
        }
    }

    private static String readJobStatus(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Open")) {
                return "Open";
            }
            if (input.equalsIgnoreCase("Closed")) {
                return "Closed";
            }

            System.out.println("=========================================");
            System.out.println("Invalid Status.");
            System.out.println("Status must be either 'Open' or 'Closed'.");
            System.out.println("=========================================");
            System.out.print("Enter status: ");
        }
    }

    private static String readJobDifficulty(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Easy")) {
                return "Easy";
            }
            if (input.equalsIgnoreCase("Medium")) {
                return "Medium";
            }
            if (input.equalsIgnoreCase("Hard")) {
                return "Hard";
            }

            System.out.println("=========================================");
            System.out.println("Invalid Difficulty.");
            System.out.println("Difficulty must be 'Easy', 'Medium', or 'Hard'.");
            System.out.println("=========================================");
            System.out.print("Enter difficulty: ");
        }
    }

    private static int readPositiveVacancies(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // continue asking
            }

            System.out.println("=========================================");
            System.out.println("Invalid Number of Vacancies.");
            System.out.println("Vacancies must be greater than zero.");
            System.out.println("Please enter again.");
            System.out.println("=========================================");
            System.out.print("Enter vacancies: ");
        }
    }

    private static int readPassingPercentage(Scanner scanner) {
        while (true) {
            System.out.print("Enter passing percentage: ");
            String input = scanner.nextLine().trim();

            try {
                int value = Integer.parseInt(input);
                if (value >= 0 && value <= 100) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // continue asking
            }

            System.out.println("=========================================");
            System.out.println("Passing Percentage must be between");
            System.out.println("0 and 100.");
            System.out.println("Please enter again.");
            System.out.println("=========================================");
        }
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
