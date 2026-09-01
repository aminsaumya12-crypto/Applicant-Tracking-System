package ATS.service;

import ATS.dao.ApplicationDAO;
import ATS.dao.JobSkillDAO;
import ATS.dao.SkillDAO;
import ATS.model.JobSkill;
import ATS.model.Skill;
import ATS.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ResumeService {

    private SkillDAO skillDAO;
    private JobSkillDAO jobSkillDAO;
    private ApplicationDAO applicationDAO;
    private boolean lastResumeReadSuccessful;
    private boolean lastResumeHadContent;

    // Constructor
    public ResumeService() {

        skillDAO = new SkillDAO();
        jobSkillDAO = new JobSkillDAO();
        applicationDAO = new ApplicationDAO();

    }

    // Read resume words from text file.
    public ArrayList<String> readResume(String filePath) {

        ArrayList<String> resumeWords = new ArrayList<String>();
        lastResumeReadSuccessful = false;
        lastResumeHadContent = false;

        System.out.println("==================================================");
        System.out.println("Reading Resume...");
        System.out.println("==================================================");

        try {

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String lowerLine = line.toLowerCase();
                lowerLine = lowerLine.replace(",", " ");
                lowerLine = lowerLine.replace(".", " ");
                lowerLine = lowerLine.replace("(", " ");
                lowerLine = lowerLine.replace(")", " ");
                lowerLine = lowerLine.replace(":", " ");
                lowerLine = lowerLine.replace(";", " ");
                lowerLine = lowerLine.replace("-", " ");
                lowerLine = lowerLine.replace("\"", " ");
                lowerLine = lowerLine.replace("'", " ");
                lowerLine = lowerLine.replace("?", " ");
                lowerLine = lowerLine.replace("!", " ");
                lowerLine = lowerLine.replace("/", " ");
                lowerLine = lowerLine.replace("\\", " ");

                String[] words = lowerLine.split(" ");

                for (int i = 0; i < words.length; i++) {
                    String word = words[i].trim();
                    if (word.length() == 0) {
                        continue;
                    }
                    resumeWords.add(word);
                    System.out.println("Word Found : " + word);
                }
            }

            br.close();
            lastResumeReadSuccessful = true;
            lastResumeHadContent = !resumeWords.isEmpty();

        } catch (IOException e) {

            System.out.println("Error reading resume : " + e.getMessage());

        }

        return resumeWords;

    }

    // Validate the uploaded resume before creating an application record.
    public boolean isResumeFileValid(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        File resumeFile = new File(filePath);
        return resumeFile.exists() && resumeFile.isFile() && resumeFile.canRead();
    }

    public boolean validateResumeIdentity(User user, String filePath) {
        String registeredName = user != null ? user.getFullName() : "";
        String registeredEmail = user != null ? user.getEmail() : "";

        String resumeName = extractResumeName(filePath);
        String resumeEmail = extractResumeEmail(filePath);

        if (isNameMatch(registeredName, resumeName)
                && isEmailMatch(registeredEmail, resumeEmail)) {
            System.out.println("=========================================");
            System.out.println("Resume Validation Successful");
            System.out.println("=========================================");
            System.out.println("Resume identity verified.");
            System.out.println();
            System.out.println("Starting Resume Parsing...");
            System.out.println("=========================================");
            return true;
        }

        System.out.println("=========================================");
        System.out.println("Resume Validation Failed");
        System.out.println("=========================================");
        System.out.println("The uploaded resume does not belong to the logged-in user.");
        System.out.println();
        System.out.println("Registered Name  : " + registeredName);
        System.out.println("Resume Name      : " + resumeName);
        System.out.println();
        System.out.println("Registered Email : " + registeredEmail);
        System.out.println("Resume Email     : " + resumeEmail);
        System.out.println();
        System.out.println("Please upload your own resume.");
        System.out.println("=========================================");
        return false;
    }

    private String extractResumeName(String filePath) {
        return extractResumeField(filePath, "name");
    }

    private String extractResumeEmail(String filePath) {
        return extractResumeField(filePath, "email");
    }

    private String extractResumeField(String filePath, String fieldName) {
        String value = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            ArrayList<String> lines = new ArrayList<String>();
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }

            br.close();

            for (int i = 0; i < lines.size(); i++) {
                String currentLine = lines.get(i);
                String lowerLine = currentLine.toLowerCase();
                lowerLine = lowerLine.replace(",", " ");
                lowerLine = lowerLine.replace(".", " ");
                lowerLine = lowerLine.replace("(", " ");
                lowerLine = lowerLine.replace(")", " ");
                lowerLine = lowerLine.replace(":", " ");
                lowerLine = lowerLine.replace(";", " ");
                lowerLine = lowerLine.replace("-", " ");
                lowerLine = lowerLine.replace("\"", " ");
                lowerLine = lowerLine.replace("'", " ");
                lowerLine = lowerLine.replace("?", " ");
                lowerLine = lowerLine.replace("!", " ");
                lowerLine = lowerLine.replace("/", " ");
                lowerLine = lowerLine.replace("\\", " ");

                if (fieldName.equals("name") && (lowerLine.startsWith("name") || lowerLine.startsWith("candidate name") || lowerLine.startsWith("full name"))) {
                    String candidateValue = currentLine;
                    int separatorIndex = candidateValue.indexOf(':');
                    if (separatorIndex != -1) {
                        candidateValue = candidateValue.substring(separatorIndex + 1).trim();
                    }
                    if (!candidateValue.isEmpty()) {
                        return candidateValue;
                    }
                    for (int j = i + 1; j < lines.size(); j++) {
                        String nextLine = lines.get(j).trim();
                        if (!nextLine.isEmpty()) {
                            return nextLine;
                        }
                    }
                }

                if (fieldName.equals("email") && (lowerLine.startsWith("email") || lowerLine.startsWith("candidate email"))) {
                    String candidateValue = currentLine;
                    int separatorIndex = candidateValue.indexOf(':');
                    if (separatorIndex != -1) {
                        candidateValue = candidateValue.substring(separatorIndex + 1).trim();
                    }
                    if (!candidateValue.isEmpty()) {
                        return candidateValue;
                    }
                    for (int j = i + 1; j < lines.size(); j++) {
                        String nextLine = lines.get(j).trim();
                        if (!nextLine.isEmpty()) {
                            return nextLine;
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading resume : " + e.getMessage());
        }

        return value;
    }

    private boolean isNameMatch(String registeredName, String resumeName) {
        return normalizeName(registeredName).equals(normalizeName(resumeName));
    }

    private boolean isEmailMatch(String registeredEmail, String resumeEmail) {
        if (registeredEmail == null || resumeEmail == null) {
            return false;
        }
        return registeredEmail.trim().equalsIgnoreCase(resumeEmail.trim());
    }

    private String normalizeName(String name) {
        if (name == null) {
            return "";
        }

        String trimmedName = name.trim().toLowerCase();
        StringBuilder normalizedName = new StringBuilder();
        boolean previousSpace = false;

        for (int i = 0; i < trimmedName.length(); i++) {
            char ch = trimmedName.charAt(i);
            if (Character.isWhitespace(ch)) {
                if (!previousSpace && normalizedName.length() > 0) {
                    normalizedName.append(' ');
                    previousSpace = true;
                }
            } else {
                normalizedName.append(ch);
                previousSpace = false;
            }
        }

        return normalizedName.toString();
    }

    // Calculate resume score based on required job skills.
    public double calculateResumeScore(String filePath, int jobId) {

        ArrayList<String> resumeWords = readResume(filePath);

        if (resumeWords == null || resumeWords.isEmpty()) {
            return 0;
        }

        HashSet<String> uniqueSkills = new HashSet<String>();
        ArrayList<String> matchedSkillNames = new ArrayList<String>();

        System.out.println("\nRemoving Duplicate Skills using HashSet...");
        for (int i = 0; i < resumeWords.size(); i++) {
            String word = resumeWords.get(i);
            if (uniqueSkills.contains(word)) {
                continue;
            }
            uniqueSkills.add(word);
            System.out.println(word);
        }

        int totalSkillsRead = resumeWords.size();
        int duplicateSkillsFound = totalSkillsRead - uniqueSkills.size();
        int uniqueSkillsStored = uniqueSkills.size();

        System.out.println("\n=========================================");
        System.out.println("Data Structure Summary");
        System.out.println("=========================================");
        System.out.println("Total Skills Read        : " + totalSkillsRead);
        System.out.println("Duplicate Skills Found   : " + duplicateSkillsFound);
        System.out.println("Unique Skills Stored     : " + uniqueSkillsStored);
        System.out.println("Data Structure Used      : HashSet<String>");
        System.out.println("Purpose                  : Removes duplicate skills before scoring.");
        System.out.println("=========================================");

        System.out.println("\n=========================================");
        System.out.println("Data Structure Used for Scoring");
        System.out.println("=========================================");
        System.out.println("HashMap<String, Integer>");
        System.out.println("Key   -> Skill Name");
        System.out.println("Value -> Skill Points");
        System.out.println("Purpose");
        System.out.println("Fast lookup of skill points from the database.");
        System.out.println("=========================================");

        HashMap<String, Integer> skillMap = new HashMap<String, Integer>();

        System.out.println("\n==================================================");
        System.out.println("Creating Skill HashMap...");
        System.out.println("==================================================");

        ArrayList<JobSkill> requiredSkills = jobSkillDAO.getSkillsByJob(jobId);

        for (int i = 0; i < requiredSkills.size(); i++) {
            JobSkill jobSkill = requiredSkills.get(i);
            Skill skill = skillDAO.getSkillById(jobSkill.getSkillId());
            if (skill != null) {
                String skillName = skill.getSkillName().toLowerCase();
                skillMap.put(skillName, jobSkill.getSkillPoints());
                System.out.println(skill.getSkillName() + " -> " + jobSkill.getSkillPoints());
            }
        }

        double totalScore = 0;
        int matchedSkillCount = 0;

        System.out.println("\n==================================================");
        System.out.println("Comparing Skills...");
        System.out.println("==================================================");

        for (int i = 0; i < resumeWords.size(); i++) {
            String word = resumeWords.get(i);
            if (skillMap.containsKey(word)) {
                int points = skillMap.get(word);
                totalScore = totalScore + points;
                matchedSkillCount = matchedSkillCount + 1;
                matchedSkillNames.add(word);
                System.out.println("Matched : " + word + " (+" + points + ")");
            } else {

                System.out.println("Not Matched : " + word);
            }
        }

        for (String skillName : skillMap.keySet()) {
            if (!matchedSkillNames.contains(skillName)) {
                System.out.println("Not Matched : " + skillName);
            }
        }

        totalScore = validateResumeScore(totalScore);

        System.out.println("\n==================================================");
        System.out.println("Resume Evaluation Completed");
        System.out.println("==================================================");
        System.out.println("Matched Skills : " + matchedSkillCount);
        System.out.println("Resume Score : " + totalScore);

        return totalScore;
    }

    private double validateResumeScore(double score) {
        if (score > 100) {
            System.out.println("=========================================");
            System.out.println("Resume Score exceeded maximum limit.");
            System.out.println("Resume Score adjusted to");
            System.out.println("100");
            System.out.println("=========================================");
            return 100;
        }

        if (score < 0) {
            return 0;
        }

        return score;
    }

    // Save resume score in database.
    public boolean saveResumeScore(int applicationId, double score) {

        double validatedScore = validateResumeScore(score);
        return applicationDAO.updateResumeScore(applicationId, validatedScore);

    }

    // Complete resume processing.
    public boolean processResume(String filePath,
                                 int applicationId,
                                 int jobId) {

        double score = calculateResumeScore(filePath, jobId);

        if (!lastResumeReadSuccessful || !lastResumeHadContent) {
            System.out.println("Error Parsing Resume.");
            return false;
        }

        System.out.println("\nSaving Resume Score...");

        boolean saved = saveResumeScore(applicationId, score);

        if (saved) {
            System.out.println("Resume Score Saved.");
        } else {
            System.out.println("Error Saving Resume Score.");
        }

        return saved;

    }

}
