/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.8.1-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: MU_Gate_Vehicle_Tracker
-- ------------------------------------------------------
-- Server version	11.8.1-MariaDB-2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `Audit_log`
--

DROP TABLE IF EXISTS `Audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Audit_log` (
  `audit_id` int(11) NOT NULL,
  `time_stamp` datetime DEFAULT NULL,
  `ip_address` varchar(20) DEFAULT NULL,
  `action` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`audit_id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Audit_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Audit_log`
--

LOCK TABLES `Audit_log` WRITE;
/*!40000 ALTER TABLE `Audit_log` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `Audit_log` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Entry_log`
--

DROP TABLE IF EXISTS `Entry_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Entry_log` (
  `entry_log_id` int(11) NOT NULL,
  `time_stamp` datetime DEFAULT NULL,
  `audit_log` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`entry_log_id`),
  KEY `audit_log` (`audit_log`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `Entry_log_ibfk_1` FOREIGN KEY (`audit_log`) REFERENCES `Audit_log` (`audit_id`),
  CONSTRAINT `Entry_log_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Entry_log`
--

LOCK TABLES `Entry_log` WRITE;
/*!40000 ALTER TABLE `Entry_log` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `Entry_log` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Permit`
--

DROP TABLE IF EXISTS `Permit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Permit` (
  `permit_id` int(11) NOT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `plate_number` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`permit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Permit`
--

LOCK TABLES `Permit` WRITE;
/*!40000 ALTER TABLE `Permit` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `Permit` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `Vehicle`
--

DROP TABLE IF EXISTS `Vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `Vehicle` (
  `vehicle_id` int(11) NOT NULL,
  `plate_number` varchar(20) DEFAULT NULL,
  `owner_fname` varchar(20) DEFAULT NULL,
  `owner_lname` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `permit_id` int(11) DEFAULT NULL,
  `entry_log_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`),
  KEY `user_id` (`user_id`),
  KEY `permit_id` (`permit_id`),
  KEY `entry_log_id` (`entry_log_id`),
  CONSTRAINT `Vehicle_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `Vehicle_ibfk_2` FOREIGN KEY (`permit_id`) REFERENCES `Permit` (`permit_id`),
  CONSTRAINT `Vehicle_ibfk_3` FOREIGN KEY (`entry_log_id`) REFERENCES `Entry_log` (`entry_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Vehicle`
--

LOCK TABLES `Vehicle` WRITE;
/*!40000 ALTER TABLE `Vehicle` DISABLE KEYS */;
set autocommit=0;
/*!40000 ALTER TABLE `Vehicle` ENABLE KEYS */;
UNLOCK TABLES;
commit;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `F_name` varchar(20) DEFAULT NULL,
  `L_name` varchar(20) DEFAULT NULL,
  `role` varchar(10) DEFAULT NULL,
  `LPC` datetime DEFAULT NULL,
  `FLA` int(11) DEFAULT NULL,
  `Account_locked` tinyint(1) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_uca1400_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
set autocommit=0;
INSERT INTO `user` VALUES
(1,'john','doe','guard','2025-05-11 19:12:26',0,1,'1qazwsxedcrfv'),
(2,'john','doe','guard','2025-05-11 19:12:33',0,1,'1qazwsxedcrfv'),
(3,'john','doe','guard','2025-05-11 19:12:41',0,1,'1qazwsxedcrfv'),
(4,'henrie','mate','admin','2025-05-13 13:21:12',0,1,'wsxedc'),
(5,'john','doe','guard','2025-05-13 15:54:30',0,1,'qazwsxed'),
(6,'john','doe','guard','2025-05-13 15:55:14',0,1,'qazws'),
(7,'john','doe','guard','2025-05-13 15:57:33',0,1,'qazws'),
(8,'john','doe','guard','2025-05-13 15:58:17',0,1,'qazws'),
(9,'john','doe','guard','2025-05-13 15:58:18',0,1,'qazws'),
(10,'john','doe','guard','2025-05-13 15:59:31',0,1,'qazws'),
(11,'Ruth','Filaba','guard','2025-05-13 16:00:07',0,1,'2002fibs'),
(12,'joseph','phili','guard','2025-05-13 16:16:49',0,1,'2002fibs'),
(13,'joseph','phili','guard','2025-05-13 16:16:52',0,1,'2002fibs'),
(14,'joseph','phili','guard','2025-05-13 16:16:54',0,1,'2002fibs'),
(15,'joseph','phili','guard','2025-05-13 16:16:54',0,1,'2002fibs'),
(16,'joseph','phili','guard','2025-05-13 16:16:56',0,1,'2002fibs');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
commit;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-05-13 18:28:31
