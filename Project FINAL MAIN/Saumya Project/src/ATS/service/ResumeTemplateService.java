package ATS.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResumeTemplateService {

    private static final String RESOURCE_NAME = "resume_format.txt";

    public void downloadResumeTemplate() {

        // Downloads folder of whichever user is running the program on their own PC
        String destinationPath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + RESOURCE_NAME;

        try {
            InputStream inputStream = locateTemplate();

            if (inputStream == null) {
                System.out.println("Resume template file not found: " + RESOURCE_NAME);
                return;
            }

            File destinationFile = new File(destinationPath);

            File downloadsDir = destinationFile.getParentFile();
            if (downloadsDir != null && !downloadsDir.exists()) {
                downloadsDir.mkdirs();
            }

            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(destinationFile))) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                }
            } finally {
                inputStream.close();
            }

            System.out.println("Resume template downloaded successfully.");
            System.out.println("Location:");
            System.out.println(destinationPath);
            System.out.println("Please fill this resume template and upload it while applying.");

        } catch (IOException e) {
            System.out.println("Error downloading resume template: " + e.getMessage());
        }
    }

    // Tries several strategies, in order, so this works regardless of how
    // the project is run (IntelliJ, exported JAR, another machine, etc.):
    //   1. As a classpath resource (works if src/resources is marked Resources Root, or after packaging into a JAR)
    //   2. As a file next to the current working directory (project root, where resume_format.txt currently lives)
    //   3. Walking a few parent directories up from the working directory (covers running from src/ or out/ etc.)
    //   4. A built-in fallback template, generated on the fly, so the download NEVER fails
    private InputStream locateTemplate() throws IOException {

        InputStream fromClasspath = getClass().getClassLoader().getResourceAsStream(RESOURCE_NAME);
        if (fromClasspath != null) {
            return fromClasspath;
        }

        File direct = new File(RESOURCE_NAME);
        if (direct.exists()) {
            return Files.newInputStream(direct.toPath());
        }

        Path current = Paths.get("").toAbsolutePath();
        for (int i = 0; i < 5 && current != null; i++) {
            File candidate = current.resolve(RESOURCE_NAME).toFile();
            if (candidate.exists()) {
                return Files.newInputStream(candidate.toPath());
            }
            File candidateInSrc = current.resolve("src").resolve(RESOURCE_NAME).toFile();
            if (candidateInSrc.exists()) {
                return Files.newInputStream(candidateInSrc.toPath());
            }
            current = current.getParent();
        }

        // Nothing found on disk or in the classpath - fall back to a built-in
        // default template so the candidate always gets something useful.
        return new java.io.ByteArrayInputStream(DEFAULT_TEMPLATE.getBytes());
    }

    private static final String DEFAULT_TEMPLATE =

            "=========================================\r\n" +
                    "SMART APPLICANT TRACKING SYSTEM (ATS)\r\n" +
                    "RESUME TEMPLATE\r\n" +
                    "=========================================\r\n\r\n" +
                    "Name:\r\n" +
                    "Email:\r\n" +
                    "Phone:\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Career Objective\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "Write your career objective here.\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Education\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "College:\r\n" +
                    "Degree:\r\n" +
                    "Passing Year:\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Skills\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "Write ONLY ONE skill per line.\r\n\r\n" +
                    "Example:\r\n\r\n" +
                    "Java\r\n" +
                    "Python\r\n" +
                    "SQL\r\n" +
                    "HTML\r\n" +
                    "CSS\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Projects\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "Project 1:\r\n\r\n" +
                    "Project 2:\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Certifications\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "Certification 1:\r\n\r\n" +
                    "Certification 2:\r\n\r\n" +
                    "-----------------------------------------\r\n" +
                    "Declaration\r\n" +
                    "-----------------------------------------\r\n\r\n" +
                    "I hereby declare that the above information is true.\r\n";
}
