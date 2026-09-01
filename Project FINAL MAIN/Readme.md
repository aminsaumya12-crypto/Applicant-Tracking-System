

````markdown
# 🚀 Applicant Tracking System (ATS)

### A Java + MySQL based recruitment management system for resume screening, candidate evaluation, interviews, and job offers.

---

## 📌 About the Project

The **Applicant Tracking System (ATS)** is a console-based recruitment management application developed using **Java, JDBC, and MySQL**.

The system manages the recruitment process from **candidate registration and job application to resume scoring, aptitude testing, candidate ranking, interview scheduling, and job offer management**.

The project demonstrates how Java application logic can be integrated with database features such as **Stored Procedures, Triggers, Transactions, COMMIT, and ROLLBACK**.

---

## ✨ Key Features

### 👤 Candidate

- Candidate registration and login
- Gmail and Yahoo email validation
- Password validation
- View available jobs
- Apply for jobs
- Resume upload and screening
- Resume score
- Job-specific aptitude test
- Test score and final score
- View application status
- Interview details
- Offer notification
- Accept or decline job offer
- Reapplication cooldown after rejection

### 🏢 HR

- Add, update, delete, and view jobs
- Manage job vacancies
- Add, update, and delete skills
- Dynamically manage skill points
- View candidate applications
- Candidate ranking using `PriorityQueue`
- Schedule interviews
- Complete interviews
- Send job offers
- Prevent duplicate offers
- Manage candidate rejection
- Track application status

---

## 🔄 Recruitment Workflow

```text
Candidate Registration
        ↓
Login
        ↓
Apply for Job
        ↓
Resume Screening
        ↓
Resume Score
        ↓
Aptitude Test
        ↓
Test Score
        ↓
Final Score
        ↓
Candidate Ranking
        ↓
Interview Scheduling
        ↓
Interview Completed
        ↓
Offer Sent
        ↓
 ┌──────────────────┐
 ↓                  ↓
Accept            Decline
 ↓                  ↓
Selected       Offer Declined
 ↓
Vacancy - 1
````

---

## 📊 Candidate Ranking

Candidates are ranked using Java's **PriorityQueue** based on their final score.

The final score combines the candidate's:

```text
Resume Score + Aptitude Test Score
                ↓
          Final Score
                ↓
       Candidate Ranking
```

This allows HR to process higher-scoring candidates first.

---

## 🗄️ Database Features

The project uses **MySQL** with JDBC.

Database concepts implemented:

* Primary Keys
* Foreign Keys
* Constraints
* Relationships
* Joins
* ENUM status management
* Stored Procedures
* Triggers
* Transactions
* COMMIT
* ROLLBACK

### Stored Procedure

Interview scheduling uses a database stored procedure:

```sql
CALL ScheduleInterview(...);
```

### Triggers

Database triggers are used for automatic database-side operations, including candidate cooldown/notification-related events.

---

## 💼 Offer Management

The offer workflow is divided between Java business logic and database operations.

```text
Interview Completed
        ↓
Application = Completed
        ↓
HR Sends Offer
        ↓
Application = Offer Sent
        ↓
Candidate Notification
        ↓
Accept / Decline
```

When a candidate accepts an offer, the system validates the available vacancy and performs the related updates using a transaction.

If an operation fails:

```text
ROLLBACK
```

If all operations succeed:

```text
COMMIT
```

This helps maintain database consistency.

---

## 🛡️ Validation & Business Rules

The system includes validations such as:

* Duplicate email prevention
* Gmail/Yahoo email validation
* Password validation
* Duplicate application prevention
* Application status validation
* Reapplication cooldown
* Interview date validation
* Interview completion validation
* Offer eligibility validation
* Duplicate offer prevention
* Vacancy availability validation
* Accept/Decline confirmation
* Transaction rollback on failure

---

## 🛠️ Technology Stack

| Technology          | Usage                            |
| ------------------- | -------------------------------- |
| ☕ Java              | Application development          |
| 🐬 MySQL            | Database                         |
| 🔌 JDBC             | Java–MySQL connectivity          |
| 💻 IntelliJ IDEA    | Development                      |
| 📂 File Handling    | Resume processing                |
| 📚 Java Collections | Candidate management and ranking |

---

## ☕ Java Concepts Used

The project demonstrates:

* Object-Oriented Programming
* Encapsulation
* Inheritance
* Polymorphism
* Abstraction
* Interfaces
* Constructors
* Methods
* Exception Handling
* ArrayList
* LinkedList
* HashMap
* HashSet
* PriorityQueue
* File Handling
* Character Streams
* Date & Time API
* Multithreading
* JDBC

---

## 📁 Project Structure

```text
ATS Project/
│
├── src/
│   └── ATS/
│       ├── dao/
│       ├── database/
│       ├── model/
│       ├── service/
│       └── main/
│
├── database/
│   ├── P.sql
│   ├── procedure.sql
│   └── trigger.sql
│
├── resources/
│   └── resume_format.txt
│
├── .gitignore
└── README.md
```

---

## ▶️ How to Run

### 1. Clone the repository

```bash
git clone YOUR_REPOSITORY_URL
```

### 2. Open the project

Open the project in **IntelliJ IDEA**.

### 3. Configure MySQL

Create the required MySQL database and execute the SQL files:

```text
1. P.sql
2. procedure.sql
3. trigger.sql
```

### 4. Configure JDBC

Update your local MySQL connection details in:

```text
DBConnection.java
```

Use your own database username and password.

### 5. Run the Application

Run the main Java class from IntelliJ IDEA.

---

## 🔮 Future Improvements

Possible future enhancements:

* Web-based interface
* AI-powered semantic resume matching
* Email notifications
* HR dashboard
* Advanced candidate analytics
* Cloud database integration
* Automated candidate recommendations

---

## 👨‍💻 Author

**Saumya**

**Applicant Tracking System (ATS)**

Built with **Java + MySQL + JDBC**

---

