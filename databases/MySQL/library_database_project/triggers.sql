--
-- Database: `CS4416_Group13`
--

DELIMITER $$
--
-- Procedures
--
$$

CREATE PROCEDURE `makeAvailable` (`itemISBN` DOUBLE)  MODIFIES SQL DATA
BEGIN
	UPDATE items SET available = 1 WHERE ISBN = itemISBN;
END$$

CREATE PROCEDURE `checkoutItem` (`itemISBN` DOUBLE, `mID` INT(16))  MODIFIES SQL DATA
BEGIN
	IF (SELECT loanCount(mID)) < 3 THEN
    	INSERT into loans (MemberID, ISBN) values (mID, itemISBN);
    END IF;
END$$

CREATE PROCEDURE `returnItem` (`itemISBN` DOUBLE, `mID` INT(16))  MODIFIES SQL DATA
BEGIN
	DELETE from loans where MemberID = mID and ISBN = itemISBN;
END$$

--
-- Functions
--
CREATE FUNCTION `isAvailable` (`itemISBN` DOUBLE) RETURNS INT(11) BEGIN 
	DECLARE a INTEGER;
   	SET a = (SELECT Available FROM items WHERE ISBN = itemISBN);
   	RETURN a;
END$$

CREATE FUNCTION `loanCount` (`mID` INT(16)) RETURNS INT(11) BEGIN
    DECLARE numLoans INTEGER;
    SET numLoans = (SELECT COUNT(*) FROM loans WHERE MemberID = mID);
    Return numLoans;
End$$

DELIMITER ;

--
-- Triggers
--

DELIMITER $$
CREATE TRIGGER `dateinsert` BEFORE INSERT ON `loans` FOR EACH ROW SET NEW.ReturnDate =  DATE_ADD(CURRENT_TIMESTAMP(),INTERVAL 14 DAY)
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `itemReturned` AFTER DELETE ON `loans` FOR EACH ROW BEGIN
	IF NOT (SELECT isAvailable(OLD.ISBN)) THEN
		call makeAvailable(OLD.ISBN);
	END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER `updateAvailability` AFTER INSERT ON `loans` FOR EACH ROW BEGIN
	DECLARE itemsOnLoan, totalAvailable INTEGER;
	SET itemsOnLoan = (SELECT count(*) FROM loans WHERE NEW.ISBN = ISBN);
	Set totalAvailable = (SELECT ItemCount FROM items WHERE NEW.ISBN = ISBN);
	IF (itemsOnLoan = totalAvailable) THEN
		UPDATE items SET available = 0 WHERE ISBN = NEW.ISBN;
	END IF;
END $$
DELIMITER ;