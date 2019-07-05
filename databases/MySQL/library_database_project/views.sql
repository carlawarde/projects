--
-- Database: `CS4416_Group13`
--

--
-- Views
--

CREATE VIEW Members_Who_Can_Loan AS
SELECT ID, (SELECT COUNT(MemberID) FROM loans WHERE MemberID = ID) AS NumberOfLoans
FROM members
WHERE ID NOT IN (SELECT MemberID FROM loans HAVING COUNT(MemberID) > 2)
GROUP BY ID;

CREATE VIEW Available_Items AS
SELECT books.ISBN, Title, Genre, Type, (SELECT ItemCount - COUNT(ISBN) FROM loans WHERE books.ISBN = ISBN) AS CurrentStockOfItem
FROM books NATURAL JOIN items
WHERE available = 1
UNION ALL
SELECT movies.ISBN, Title, Genre, Type, (SELECT ItemCount - COUNT(ISBN) FROM loans WHERE movies.ISBN = ISBN) AS CurrentStockOfItem
FROM movies NATURAL JOIN items
WHERE available = 1
ORDER BY ISBN ASC;


CREATE VIEW Status_Of_Loans AS
SELECT ISBN, MemberID, ReturnDate, (CASE WHEN(NOW() > ReturnDate) THEN 'OVERDUE' ELSE 'IN DATE' END) AS LoanStatus
FROM loans
ORDER BY ReturnDate ASC;

