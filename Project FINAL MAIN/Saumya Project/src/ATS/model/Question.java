package ATS.model;

public class Question {

    private int questionId;
    private int jobId;
    private String difficulty;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;

    // Default constructor.
    // This constructor creates an empty Question object.
    public Question() {
    }

    // Parameterized constructor.
    // This constructor sets all fields when creating the object.
    public Question(int questionId,
                    int jobId,
                    String difficulty,
                    String questionText,
                    String optionA,
                    String optionB,
                    String optionC,
                    String optionD,
                    String correctAnswer) {
        this.questionId = questionId;
        this.jobId = jobId;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    
    public int getQuestionId() {
        return questionId;
    }

    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

   
    public int getJobId() {
        return jobId;
    }

    
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    
    public String getDifficulty() {
        return difficulty;
    }

    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    
    public String getQuestionText() {
        return questionText;
    }

    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    
    public String getOptionA() {
        return optionA;
    }

    
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    
    public String getOptionB() {
        return optionB;
    }

    
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    
    public String getOptionC() {
        return optionC;
    }

    
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    
    public String getOptionD() {
        return optionD;
    }

    
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

  
    public String getCorrectAnswer() {
        return correctAnswer;
    }

   
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", jobId=" + jobId +
                ", difficulty='" + difficulty + '\'' +
                ", questionText='" + questionText + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
