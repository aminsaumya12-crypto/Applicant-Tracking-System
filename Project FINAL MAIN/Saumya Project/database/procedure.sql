USE ats;

DROP PROCEDURE IF EXISTS ScheduleInterview;

DELIMITER $$

CREATE PROCEDURE ScheduleInterview(
    IN p_application_id INT,
    IN p_interview_date DATE,
    IN p_interview_time TIME
)
BEGIN
    IF EXISTS (
        SELECT 1
        FROM interviews
        WHERE application_id = p_application_id
    ) THEN
        UPDATE interviews
        SET interview_date = p_interview_date,
            interview_time = p_interview_time,
            status = 'Scheduled'
        WHERE application_id = p_application_id;
    ELSE
        INSERT INTO interviews(
            application_id,
            interview_date,
            interview_time,
            location,
            status
        ) VALUES (
            p_application_id,
            p_interview_date,
            p_interview_time,
            '',
            'Scheduled'
        );
    END IF;

    UPDATE applications
    SET status = 'Interview Scheduled'
    WHERE application_id = p_application_id;
END$$

DELIMITER ;
