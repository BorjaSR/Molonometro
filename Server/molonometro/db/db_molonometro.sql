-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-02-2017 a las 13:53:26
-- Versión del servidor: 5.6.17
-- Versión de PHP: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de datos: `db_molonometro`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `GroupID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(150) COLLATE utf8_spanish_ci NOT NULL,
  `Image` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `Created` date NOT NULL,
  `LastUpdate` date NOT NULL,
  `Deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`GroupID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(75) COLLATE utf8_spanish_ci NOT NULL,
  `Phone` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `State` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `Image` longblob,
  `Created` date NOT NULL,
  `LastUpdate` date NOT NULL,
  `Deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci AUTO_INCREMENT=16 ;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
