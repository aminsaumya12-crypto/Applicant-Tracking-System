-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 31, 2026 at 12:43 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ats`
--

-- --------------------------------------------------------

--
-- Table structure for table `applications`
--

CREATE TABLE `applications` (
  `application_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `resume_path` varchar(255) NOT NULL,
  `resume_score` decimal(5,2) NOT NULL DEFAULT 0.00,
  `test_score` decimal(5,2) NOT NULL DEFAULT 0.00,
  `final_score` decimal(5,2) NOT NULL DEFAULT 0.00,
  `status` enum('Applied','Resume Screened','Test Pending','Test Completed','Interview Scheduled','Offer Sent','Offer Accepted','Joined','Rejected') NOT NULL DEFAULT 'Applied',
  `applied_date` datetime NOT NULL,
  `cooldown_end` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `applications`
--

INSERT INTO `applications` (`application_id`, `user_id`, `job_id`, `resume_path`, `resume_score`, `test_score`, `final_score`, `status`, `applied_date`, `cooldown_end`) VALUES
(1, 2, 1, 'rahul_resume.txt', 85.00, 78.00, 81.50, 'Rejected', '2026-07-26 11:32:34', '2026-11-05'),
(42, 42, 3, 'D://resume.txt', 50.00, 100.00, 80.00, 'Test Completed', '2026-08-07 14:29:20', NULL);

--
-- Triggers `applications`
--
DELIMITER $$
CREATE TRIGGER `trg_set_cooldown_after_rejection` BEFORE UPDATE ON `applications` FOR EACH ROW BEGIN
    IF NEW.status = 'Rejected'
       AND OLD.status <> 'Rejected' THEN
        SET NEW.cooldown_end = DATE_ADD(CURDATE(), INTERVAL 3 MONTH);
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `hr_notes`
--

CREATE TABLE `hr_notes` (
  `note_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `handled_by` int(11) NOT NULL,
  `remarks` text NOT NULL,
  `rejection_reason` varchar(255) NOT NULL DEFAULT '',
  `rejection_stage` varchar(100) NOT NULL DEFAULT '',
  `created_at` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `hr_notes`
--

INSERT INTO `hr_notes` (`note_id`, `application_id`, `handled_by`, `remarks`, `rejection_reason`, `rejection_stage`, `created_at`) VALUES
(2, 2, 5, 'Average technical skills', '', '', '2026-07-26'),
(3, 3, 1, 'Excellent candidate', '', '', '2026-07-26'),
(29, 1, 1, '', 'leak', 'HR', '2026-08-05'),
(30, 33, 1, '', 'not good at work', 'HR', '2026-08-05'),
(31, 34, 1, '', 'not good', 'HR', '2026-08-06');

-- --------------------------------------------------------

--
-- Table structure for table `interviews`
--

CREATE TABLE `interviews` (
  `interview_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `interview_date` date NOT NULL,
  `interview_time` time NOT NULL,
  `location` varchar(255) NOT NULL,
  `status` enum('Scheduled','Completed','Cancelled') NOT NULL DEFAULT 'Scheduled'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `interviews`
--

INSERT INTO `interviews` (`interview_id`, `application_id`, `interview_date`, `interview_time`, `location`, `status`) VALUES
(1, 1, '2026-08-01', '10:00:00', 'Ahmedabad', 'Scheduled');

-- --------------------------------------------------------

--
-- Table structure for table `jobs`
--

CREATE TABLE `jobs` (
  `job_id` int(11) NOT NULL,
  `job_title` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `vacancies` int(11) NOT NULL,
  `status` enum('Open','Closed') NOT NULL DEFAULT 'Open',
  `cooldown_months` int(11) NOT NULL DEFAULT 3,
  `test_questions` int(11) NOT NULL,
  `duration_minutes` int(11) NOT NULL,
  `passing_percentage` int(11) NOT NULL,
  `minimum_resume_score` decimal(5,2) NOT NULL DEFAULT 60.00,
  `difficulty` enum('Easy','Medium','Hard') NOT NULL,
  `created_by` int(11) NOT NULL,
  `created_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jobs`
--

INSERT INTO `jobs` (`job_id`, `job_title`, `description`, `vacancies`, `status`, `cooldown_months`, `test_questions`, `duration_minutes`, `passing_percentage`, `minimum_resume_score`, `difficulty`, `created_by`, `created_at`) VALUES
(1, 'bye', 'nice', 1, 'Closed', 3, 0, 0, 60, 30.00, 'Hard', 5, '2026-07-26 09:47:07'),
(2, 'Backend Developer', 'Develop REST APIs and server-side applications using Java and SQL.', 2, 'Open', 3, 20, 30, 85, 60.00, 'Easy', 1, '2026-07-26 09:49:11'),
(3, 'Frontend Developer', 'Build responsive user interfaces using HTML, CSS and JavaScript.', 3, 'Open', 3, 20, 30, 65, 60.00, 'Hard', 5, '2026-07-26 09:49:11'),
(4, 'Data Scientist', 'Analyze datasets and build predictive models using Python.', 2, 'Open', 6, 30, 45, 65, 60.00, 'Hard', 5, '2026-07-26 09:51:21'),
(5, 'Java Developer', 'Develop Java desktop and enterprise applications.', 3, 'Open', 3, 20, 30, 70, 60.00, 'Medium', 1, '2026-07-26 09:52:26'),
(6, 'Python Developer', 'Develop automation scripts and backend applications using Python.', 2, 'Open', 3, 20, 30, 70, 60.00, 'Medium', 5, '2026-07-26 09:52:26'),
(7, 'Machine Learning Engineer', 'Design and train ML models for intelligent systems.', 2, 'Open', 6, 30, 45, 85, 60.00, 'Easy', 1, '2026-07-26 09:54:37');

-- --------------------------------------------------------

--
-- Table structure for table `job_skills`
--

CREATE TABLE `job_skills` (
  `job_skill_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `skill_id` int(11) NOT NULL,
  `skill_points` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='This is our bridge table.';

--
-- Dumping data for table `job_skills`
--

INSERT INTO `job_skills` (`job_skill_id`, `job_id`, `skill_id`, `skill_points`) VALUES
(1, 1, 2, 20),
(2, 1, 4, 20),
(3, 1, 6, 15),
(5, 2, 3, 15),
(6, 3, 9, 15),
(7, 3, 10, 15),
(8, 3, 11, 20),
(9, 4, 2, 20),
(10, 4, 8, 15),
(12, 6, 2, 20),
(13, 7, 5, 20),
(16, 6, 8, 15);

-- --------------------------------------------------------

--
-- Table structure for table `offers`
--

CREATE TABLE `offers` (
  `offer_id` int(11) NOT NULL,
  `application_id` int(11) NOT NULL,
  `offer_date` date NOT NULL,
  `offer_status` enum('Pending','Accepted','Rejected') NOT NULL DEFAULT 'Pending',
  `accepted_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `offers`
--

INSERT INTO `offers` (`offer_id`, `application_id`, `offer_date`, `offer_status`, `accepted_date`) VALUES
(1, 1, '2026-08-10', 'Pending', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `question_id` int(11) NOT NULL,
  `job_id` int(11) NOT NULL,
  `difficulty` enum('Easy','Medium','Hard') NOT NULL,
  `question_text` text NOT NULL,
  `option_a` varchar(255) NOT NULL,
  `option_b` varchar(255) NOT NULL,
  `option_c` varchar(255) NOT NULL,
  `option_d` varchar(255) NOT NULL,
  `correct_answer` char(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`question_id`, `job_id`, `difficulty`, `question_text`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_answer`) VALUES
(1, 1, 'Easy', 'What is Machine Learning?', 'A', 'D', 'O', 'C', 'A'),
(2, 1, 'Medium', 'Which library is used for Deep Learning?', 'T', 'H', 'C', 'B', 'A'),
(3, 2, 'Hard', 'Which language is mainly used for Java backend?', 'J', 'P', 'P', 'S', 'A'),
(4, 2, 'Medium', 'JDBC is used for?', 'N', 'D', 'G', 'S', 'B'),
(5, 3, 'Easy', 'HTML stands for?', 'H', 'H', 'H', 'N', 'C'),
(6, 3, 'Medium', 'CSS is used for?', 'P', 'S', 'D', 'T', 'B'),
(7, 4, 'Easy', 'Which library is used for data analysis?', 'R', 'F', 'S', 'P', 'D'),
(8, 5, 'Medium', 'JVM stands for?', 'J', 'J', 'J', 'N', 'A'),
(9, 6, 'Hard', 'Python is a?', 'D', 'B', 'F', 'P', 'D'),
(10, 7, 'Hard', 'Neural Networks belong to?', 'M', 'N', 'C', 'C', 'A');

-- --------------------------------------------------------

--
-- Table structure for table `skills`
--

CREATE TABLE `skills` (
  `skill_id` int(11) NOT NULL,
  `skill_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Instead of storing skill names repeatedly.';

--
-- Dumping data for table `skills`
--

INSERT INTO `skills` (`skill_id`, `skill_name`) VALUES
(14, 'C++'),
(10, 'CSS'),
(15, 'Data Structures'),
(5, 'Deep Learning'),
(13, 'Git'),
(16, 'hi'),
(9, 'HTML'),
(11, 'JavaScript'),
(4, 'Machine Learning'),
(7, 'NumPy'),
(8, 'Pandas'),
(2, 'Python'),
(12, 'Spring'),
(3, 'SQL'),
(6, 'TensorFlow');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL COMMENT 'Unique ID',
  `full_name` varchar(100) NOT NULL COMMENT 'Name',
  `email` varchar(100) NOT NULL COMMENT 'Login email',
  `password` varchar(15) NOT NULL COMMENT 'Password',
  `role` enum('HR','Candidate') NOT NULL COMMENT 'User role',
  `created_at` datetime NOT NULL COMMENT 'Registration date',
  `account_status` enum('Active','Inactive') NOT NULL DEFAULT 'Active'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='This table stores both HRs and Candidates.';

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `full_name`, `email`, `password`, `role`, `created_at`, `account_status`) VALUES
(1, 'Neha Sharma', 'neha@company.com', 'neha123', 'HR', '2026-07-26 09:30:05', 'Active'),
(2, 'Rahul Patel', 'rahul@gmail.com', 'rahul1237', 'Candidate', '2026-07-26 09:31:34', 'Active'),
(3, 'Priya Shah', 'priya@gmail.com', 'priya123', 'Candidate', '2026-07-26 09:32:44', 'Active'),
(5, 'Mohit Shah', 'mohit@company.com', 'mohit$$5', 'HR', '2026-07-26 09:33:57', 'Active'),
(42, 'Aryan', 'Aryan1@gmail.com', 'Aryan@123', 'Candidate', '2026-08-07 14:28:39', 'Active');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `applications`
--
ALTER TABLE `applications`
  ADD PRIMARY KEY (`application_id`),
  ADD UNIQUE KEY `user_id` (`user_id`,`job_id`),
  ADD KEY `applications_ibfk_1` (`user_id`),
  ADD KEY `applications_ibfk_2` (`job_id`);

--
-- Indexes for table `hr_notes`
--
ALTER TABLE `hr_notes`
  ADD PRIMARY KEY (`note_id`),
  ADD KEY `hr_notes_ibfk_1` (`handled_by`);

--
-- Indexes for table `interviews`
--
ALTER TABLE `interviews`
  ADD PRIMARY KEY (`interview_id`),
  ADD UNIQUE KEY `application_id` (`application_id`);

--
-- Indexes for table `jobs`
--
ALTER TABLE `jobs`
  ADD PRIMARY KEY (`job_id`),
  ADD KEY `jobs_ibfk_1` (`created_by`);

--
-- Indexes for table `job_skills`
--
ALTER TABLE `job_skills`
  ADD PRIMARY KEY (`job_skill_id`),
  ADD KEY `job_skills_ibfk_1` (`job_id`),
  ADD KEY `job_skills_ibfk_2` (`skill_id`);

--
-- Indexes for table `offers`
--
ALTER TABLE `offers`
  ADD PRIMARY KEY (`offer_id`),
  ADD UNIQUE KEY `application_id` (`application_id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`question_id`),
  ADD KEY `questions_ibfk_1` (`job_id`);

--
-- Indexes for table `skills`
--
ALTER TABLE `skills`
  ADD PRIMARY KEY (`skill_id`),
  ADD UNIQUE KEY `skill_name` (`skill_name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `applications`
--
ALTER TABLE `applications`
  MODIFY `application_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `hr_notes`
--
ALTER TABLE `hr_notes`
  MODIFY `note_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;

--
-- AUTO_INCREMENT for table `interviews`
--
ALTER TABLE `interviews`
  MODIFY `interview_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `jobs`
--
ALTER TABLE `jobs`
  MODIFY `job_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `job_skills`
--
ALTER TABLE `job_skills`
  MODIFY `job_skill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `offers`
--
ALTER TABLE `offers`
  MODIFY `offer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `question_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `skills`
--
ALTER TABLE `skills`
  MODIFY `skill_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Unique ID', AUTO_INCREMENT=43;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `applications`
--
ALTER TABLE `applications`
  ADD CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`) ON UPDATE CASCADE;

--
-- Constraints for table `hr_notes`
--
ALTER TABLE `hr_notes`
  ADD CONSTRAINT `hr_notes_ibfk_1` FOREIGN KEY (`handled_by`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `interviews`
--
ALTER TABLE `interviews`
  ADD CONSTRAINT `interviews_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `applications` (`application_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `jobs`
--
ALTER TABLE `jobs`
  ADD CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON UPDATE CASCADE;

--
-- Constraints for table `job_skills`
--
ALTER TABLE `job_skills`
  ADD CONSTRAINT `job_skills_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `job_skills_ibfk_2` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`skill_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `offers`
--
ALTER TABLE `offers`
  ADD CONSTRAINT `offers_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `applications` (`application_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
