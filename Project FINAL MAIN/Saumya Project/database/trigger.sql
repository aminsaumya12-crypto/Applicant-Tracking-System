USE ats;

DROP TRIGGER IF EXISTS trg_set_cooldown_after_rejection;

DELIMITER $$

CREATE TRIGGER trg_set_cooldown_after_rejection
BEFORE UPDATE ON applications
FOR EACH ROW
BEGIN
    IF NEW.status = 'Rejected'
       AND NOT (OLD.status <=> NEW.status) THEN
        SET NEW.cooldown_end = CURRENT_DATE + INTERVAL 3 MONTH;
    END IF;
END$$

DELIMITER ;
