package ATS.service;

import ATS.dao.*;
import ATS.model.*;

import java.util.*;

public class TestService {

    private QuestionDAO questionDAO;
    private JobDAO jobDAO;
    private ApplicationDAO applicationDAO;

    // Constructor
    public TestService() {

        questionDAO = new QuestionDAO();
        jobDAO = new JobDAO();
        applicationDAO = new ApplicationDAO();

    }

    // Load questions for the selected job.
    public ArrayList<Question> loadQuestions(int jobId) {

        // Get all questions of the job.
        ArrayList<Question> allQuestions = questionDAO.getQuestionsByJob(jobId);

        // Get job details.
        Job job = jobDAO.getJobById(jobId);

        if (job == null) {

            return new ArrayList<Question>();

        }

        // Number of questions required for this job.
        int totalQuestions = job.getTestQuestions();

        // Select questions according to difficulty.
        return selectQuestionsByDifficulty(allQuestions, totalQuestions);

    }

    // Select questions using 40% Easy, 40% Medium and 20% Hard.
    private ArrayList<Question> selectQuestionsByDifficulty(
            ArrayList<Question> allQuestions,
            int totalQuestions) {

        ArrayList<Question> easyQuestions = new ArrayList<Question>();
        ArrayList<Question> mediumQuestions = new ArrayList<Question>();
        ArrayList<Question> hardQuestions = new ArrayList<Question>();

        // Separate questions according to difficulty.
        for (Question question : allQuestions) {

            if (question.getDifficulty().equalsIgnoreCase("Easy")) {

                easyQuestions.add(question);

            } else if (question.getDifficulty().equalsIgnoreCase("Medium")) {

                mediumQuestions.add(question);

            } else {

                hardQuestions.add(question);

            }

        }

        // Shuffle each difficulty list.
        Collections.shuffle(easyQuestions);
        Collections.shuffle(mediumQuestions);
        Collections.shuffle(hardQuestions);

        int easyCount = (totalQuestions * 40) / 100;
        int mediumCount = (totalQuestions * 40) / 100;
        int hardCount = totalQuestions - easyCount - mediumCount;

        ArrayList<Question> selectedQuestions = new ArrayList<Question>();

        // Add required questions.
        addQuestions(easyQuestions, selectedQuestions, easyCount);
        addQuestions(mediumQuestions, selectedQuestions, mediumCount);
        addQuestions(hardQuestions, selectedQuestions, hardCount);

        // If total questions are still less than required,
        // fill remaining questions from other lists.
        while (selectedQuestions.size() < totalQuestions) {

            if (easyQuestions.size() > 0) {

                selectedQuestions.add(easyQuestions.remove(0));

            } else if (mediumQuestions.size() > 0) {

                selectedQuestions.add(mediumQuestions.remove(0));

            } else if (hardQuestions.size() > 0) {

                selectedQuestions.add(hardQuestions.remove(0));

            } else {

                break;

            }

        }

        // Shuffle again so difficulty is mixed.
        Collections.shuffle(selectedQuestions);

        return selectedQuestions;

    }

    // Add required number of questions from one list to another.
    private void addQuestions(ArrayList<Question> source,
                              ArrayList<Question> destination,
                              int count) {

        int i = 0;

        while (i < count && source.size() > 0) {

            destination.add(source.remove(0));

            i++;

        }

    }

    // Conduct the online aptitude test.
    public double conductTest(ArrayList<Question> questions) {

        Scanner scanner = new Scanner(System.in);

        int correctAnswers = 0;

        System.out.println("\n========== ONLINE TEST ==========\n");

        for (int i = 0; i < questions.size(); i++) {

            Question question = questions.get(i);

            System.out.println("--------------------------------------------");
            System.out.println("Question " + (i + 1));
            System.out.println(question.getQuestionText());

            System.out.println("A. " + question.getOptionA());
            System.out.println("B. " + question.getOptionB());
            System.out.println("C. " + question.getOptionC());
            System.out.println("D. " + question.getOptionD());

            String answer;

            while (true) {

                System.out.print("Enter Answer (A/B/C/D): ");

                answer = scanner.nextLine();

                answer = answer.trim().toUpperCase();

                if (answer.equals("A") ||
                        answer.equals("B") ||
                        answer.equals("C") ||
                        answer.equals("D")) {

                    break;

                }

                System.out.println("Invalid choice. Please enter A, B, C or D.");

            }

            if (answer.equals(question.getCorrectAnswer())) {

                correctAnswers++;

            }

        }

        return calculateTestScore(correctAnswers, questions.size());

    }


    // Calculate percentage score.
    public double calculateTestScore(int correctAnswers,
                                     int totalQuestions) {

        if (totalQuestions == 0) {

            return 0;

        }

        double score;

        score = ((double) correctAnswers * 100) / totalQuestions;

        return score;

    }


    private double validateTestScore(double score) {
        if (score > 100) {
            return 100;
        }

        if (score < 0) {
            return 0;
        }

        return score;
    }

    private double validateFinalScore(double score) {
        if (score > 100) {
            System.out.println("=========================================");
            System.out.println("Final Score exceeded maximum limit.");
            System.out.println("Final Score adjusted to");
            System.out.println("100");
            System.out.println("=========================================");
            return 100;
        }

        if (score < 0) {
            return 0;
        }

        return score;
    }

    // Save test score into applications table.
    public boolean saveTestScore(int applicationId,
                                 double score) {

        double validatedScore = validateTestScore(score);
        return applicationDAO.updateTestScore(applicationId, validatedScore);

    }


    // Complete test process.
    public boolean startTest(int applicationId,
                             int jobId) {

        ArrayList<Question> questions;

        questions = loadQuestions(jobId);

        if (questions.isEmpty()) {

            System.out.println("No questions found for this job.");

            return false;

        }

        double score;

        score = conductTest(questions);
        score = validateTestScore(score);

        System.out.println("Online Test Completed.");

        boolean saved;

        saved = saveTestScore(applicationId, score);
        if (!saved) {
            System.out.println("Error Saving Test Score.");
            return false;
        }

        System.out.println("Test Score Saved.");

        Application application = applicationDAO.getApplicationById(applicationId);
        Job job = jobDAO.getJobById(jobId);

        if (application == null || job == null) {
            System.out.println("Error Calculating Final Score.");
            return false;
        }

        double resumeScore = application.getResumeScore();
        double resumeContribution = resumeScore * 0.40;
        double testContribution = score * 0.60;
        double finalScore = resumeContribution + testContribution;
        finalScore = validateFinalScore(finalScore);

        System.out.println("Final Score Calculated.");
        System.out.println("Final Score : " + finalScore);

        boolean finalScoreSaved = applicationDAO.updateFinalScore(applicationId, finalScore);
        if (!finalScoreSaved) {
            System.out.println("Error Saving Final Score.");
            return false;
        }

        String finalStatus;
        if (finalScore >= job.getPassingPercentage()) {
            finalStatus = "Test Completed";
        } else {
            finalStatus = "Rejected";
        }

        boolean statusUpdated = applicationDAO.updateApplicationStatus(applicationId, finalStatus);
        if (!statusUpdated) {
            System.out.println("Error Updating Application Status.");
            return false;
        }

        System.out.println("Application Status Updated.");
        return true;
    }

}
