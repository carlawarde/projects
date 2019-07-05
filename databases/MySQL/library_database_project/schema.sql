-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Nov 19, 2018 at 01:39 PM
-- Server version: 10.1.20-MariaDB
-- PHP Version: 5.6.29

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `CS4416_Group_13`
--

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `ISBN` double NOT NULL,
  `Title` char(128) DEFAULT NULL,
  `AuthorFirstName` char(32) DEFAULT NULL,
  `AuthorLastName` char(32) DEFAULT NULL,
  `Genre` char(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`ISBN`, `Title`, `AuthorFirstName`, `AuthorLastName`, `Genre`) VALUES
(1234567890120, 'Harry Potter and the Philosopher\'s Stone', 'JK', 'Rowling', 'Fantasy'),
(1234567890121, 'Harry Potter and the Chamber of Secrets', 'JK', 'Rowling', 'Fantasy'),
(1234567890122, 'Harry Potter and the Prisoner of Askaban', 'JK', 'Rowling', 'Fantasy'),
(1234567890127, 'The Book Thief', 'Benjamin', 'Franklin', 'Adult'),
(1234567890128, 'Percy Jackson and the Lightning Thief', 'Rick', 'Riordan', 'Action'),
(1234567890129, 'Alex Rider', 'Anthony', 'Horowitz', 'Young Adult');

-- --------------------------------------------------------

--
-- Table structure for table `movies`
--

CREATE TABLE `movies` (
  `ISBN` double NOT NULL,
  `Genre` char(32) DEFAULT NULL,
  `Title` char(128) DEFAULT NULL,
  `DirectorFirstName` char(32) DEFAULT NULL,
  `DirectorLastName` char(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `movies`
--

INSERT INTO `movies` (`ISBN`, `Genre`, `Title`, `DirectorFirstName`, `DirectorLastName`) VALUES
(2000000, 'Fantasy', 'LOTR', 'Peter', 'Jackson'),
(2000001, 'Drama', 'The Titanic', 'James', 'Cameron'),
(2000002, 'Animated', 'Frozen', 'Jennifer', 'Lee'),
(2000004, 'Fantasy', 'Harry Potter', 'Chris', 'Columbus'),
(2000005, 'Drama', 'Pulp Fiction', 'Quentin', 'Tarantino');

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `ISBN` double NOT NULL,
  `ItemCount` int(4) DEFAULT NULL,
  `Type` char(8) DEFAULT NULL,
  `Available` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`ISBN`, `ItemCount`, `Type`, `Available`) VALUES
(2000000, 1, 'Movie', 0),
(2000001, 2, 'Movie', 1),
(2000002, 5, 'Movie', 1),
(2000004, 2, 'Movie', 1),
(2000005, 2, 'Movie', 1),
(1234567890120, 3, 'Book', 1),
(1234567890121, 3, 'Book', 1),
(1234567890122, 3, 'Book', 1),
(1234567890127, 2, 'Book', 1),
(1234567890128, 4, 'Book', 1),
(1234567890129, 1, 'Book', 0);

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `MemberID` int(16) NOT NULL,
  `ISBN` double NOT NULL,
  `CheckoutDate` datetime DEFAULT CURRENT_TIMESTAMP,
  `ReturnDate` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`MemberID`, `ISBN`, `CheckoutDate`, `ReturnDate`) VALUES
(100002, 2000000, '2018-11-02 14:00:49', '2018-11-16 14:00:49'),
(100001, 1234567890120, '2018-11-12 09:03:00', '2018-11-26 09:03:00'),
(100001, 1234567890121, '2018-11-12 09:03:00', '2018-11-26 09:03:00'),
(100001, 1234567890122, '2018-11-12 00:03:00', '2018-11-26 00:03:00'),
(100003, 2000002, '2018-11-14 14:28:48', '2018-11-28 14:28:48'),
(100003, 1234567890129, '2018-11-14 14:27:44', '2018-11-28 14:27:44'),
(100004, 1234567890127, '2018-11-22 16:06:01', '2018-12-06 16:06:01');

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE `members` (
  `ID` int(16) NOT NULL,
  `FirstName` char(32) DEFAULT NULL,
  `LastName` char(32) DEFAULT NULL,
  `EmailAddress` char(128) DEFAULT NULL,
  `Address` char(128) DEFAULT NULL,
  `PhoneNumber` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `members`
--

INSERT INTO `members` (`ID`, `FirstName`, `LastName`, `EmailAddress`, `Address`, `PhoneNumber`) VALUES
(100000, 'Cole', 'Wallin', NULL, 'Kilmurry', 6514888888),
(100001, 'Carla', 'Warde', 'cwarde@gmail.com', 'Mayo, Ireland', 0851234567),
(100002, 'Vincent', 'Kiely', 'vk@hotmail.com', 'Cork, Ireland', 0892034337),
(100003, 'John', 'JacobJingleHeimerSchmidt', 'Schmitty@facebook.com', 'Canada', NULL),
(100004, 'Becca', 'Levine', NULL, NULL, NULL);

-- --------------------------------------------------------

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`ISBN`);

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`ISBN`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`MemberID`,`ISBN`);

--
-- Indexes for table `members`
--
ALTER TABLE `members`
  ADD PRIMARY KEY (`ID`);

--
-- Indexes for table `movies`
--
ALTER TABLE `movies`
  ADD PRIMARY KEY (`ISBN`);
  
--
-- AUTO_INCREMENT for table `books`
--
ALTER TABLE `books`
  MODIFY `ISBN` double NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2147483647;
--
-- AUTO_INCREMENT for table `members`
--
ALTER TABLE `members`
  MODIFY `ID` int(16) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=100005;
--
-- AUTO_INCREMENT for table `movies`
--
ALTER TABLE `movies`
  MODIFY `ISBN` double NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2000006;
  
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;