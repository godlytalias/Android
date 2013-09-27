-- phpMyAdmin SQL Dump
-- version 3.3.2deb1ubuntu1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 29, 2013 at 11:15 PM
-- Server version: 5.1.67
-- PHP Version: 5.3.2-1ubuntu4.19

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `db_b100143cs`
--

-- --------------------------------------------------------

--
-- Table structure for table `gtacampus`
--

CREATE TABLE IF NOT EXISTS `gtacampus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender` varchar(50) DEFAULT NULL,
  `message` varchar(50000) DEFAULT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `gtacampus`
--

INSERT INTO `gtacampus` (`id`, `sender`, `message`, `date`) VALUES
(1, 'GTAcampuS Developer', 'Welcome to GTAcampuS. Have fun!.', '2013-01-01 00:00:00');
