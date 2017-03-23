-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 22-03-2017 a las 17:49:17
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

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `coolpoints_recalculation`()
    NO SQL
BEGIN

	DECLARE m_GroupID BIGINT;
	DECLARE m_UserID BIGINT;
    DECLARE m_Molopuntos BIGINT;
    
    DECLARE pointsToDecrease BIGINT;
    
	-- Variable para controlar el fin del bucle
  	DECLARE fin INTEGER DEFAULT 0;
    
    -- El SELECT que vamos a ejecutar
  	DECLARE groupuser_cursor CURSOR FOR 
    	SELECT GroupID, UserID, Molopuntos FROM groupuser;
        
    -- Condición de salida
  	DECLARE CONTINUE HANDLER FOR NOT FOUND SET fin=1;
        
    OPEN groupuser_cursor;
  	get_groupuser: LOOP
    
    	
    	FETCH groupuser_cursor INTO m_GroupID, m_UserID, m_Molopuntos;
        
        IF fin = 1 THEN
       		LEAVE get_groupuser;
    	END IF;
        
        IF m_Molopuntos <> 0 THEN
        
        	SET pointsToDecrease = ((m_Molopuntos * 3)/100);
            
            IF pointsToDecrease < 1 THEN
            	SET pointsToDecrease = 1;
            END IF;
        
        	UPDATE groupuser SET Molopuntos = Molopuntos - pointsToDecrease WHERE GroupID = m_GroupID AND UserID = m_UserID;
        
        END IF;
    
  	END LOOP get_groupuser;
  	CLOSE groupuser_cursor;

END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `comments`
--

CREATE TABLE IF NOT EXISTS `comments` (
  `CommentID` int(11) NOT NULL AUTO_INCREMENT,
  `GroupID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `DestinationUserID` int(11) NOT NULL,
  `AssociatedCommentID` int(11) DEFAULT NULL,
  `hasAnswers` tinyint(1) NOT NULL DEFAULT '0',
  `Text` varchar(400) CHARACTER SET utf8 COLLATE utf8_spanish_ci DEFAULT NULL,
  `Image` blob,
  `Created` datetime NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `Deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CommentID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Estructura de tabla para la tabla `groups`
--

CREATE TABLE IF NOT EXISTS `groups` (
  `GroupID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(150) COLLATE utf8_spanish_ci NOT NULL,
  `Image` blob,
  `CreatedBy` int(11) NOT NULL,
  `Created` datetime NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `Deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`GroupID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `groupuser`
--

CREATE TABLE IF NOT EXISTS `groupuser` (
  `GroupID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Molopuntos` int(11) DEFAULT '0',
  `isAdmin` tinyint(1) DEFAULT '0',
  `Created` datetime NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `Deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`GroupID`,`UserID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Estructura de tabla para la tabla `likes`
--

CREATE TABLE IF NOT EXISTS `likes` (
  `CommentID` int(11) NOT NULL,
  `UserID` int(11) NOT NULL,
  `Created` datetime NOT NULL,
  `Deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`CommentID`,`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(75) COLLATE utf8_spanish_ci NOT NULL,
  `Phone` varchar(25) COLLATE utf8_spanish_ci NOT NULL,
  `State` varchar(200) COLLATE utf8_spanish_ci DEFAULT NULL,
  `Image` blob,
  `FirebaseToken` varchar(250) COLLATE utf8_spanish_ci DEFAULT NULL,
  `Created` datetime NOT NULL,
  `LastUpdate` datetime NOT NULL,
  `Deleted` tinyint(1) NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;


DELIMITER $$
--
-- Eventos
--
CREATE DEFINER=`root`@`localhost` EVENT `daily_coolpoints_recalculation` ON SCHEDULE EVERY 1 DAY STARTS '2017-03-22 13:00:00' ON COMPLETION PRESERVE ENABLE DO CALL `coolpoints_recalculation`()$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
