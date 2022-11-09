-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: sky-cloud
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `filmscreen`
--

DROP TABLE IF EXISTS `filmscreen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `filmscreen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `filmId` int NOT NULL,
  `startDay` date NOT NULL,
  `time` char(5) NOT NULL,
  `theaterId` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `screen-info` (`filmId`,`startDay`,`time`),
  KEY `theaterId` (`theaterId`),
  CONSTRAINT `filmscreen_ibfk_1` FOREIGN KEY (`filmId`) REFERENCES `film` (`uid`),
  CONSTRAINT `filmscreen_ibfk_2` FOREIGN KEY (`theaterId`) REFERENCES `theater` (`id`),
  CONSTRAINT `filmscreen_chk_1` CHECK ((str_to_date(`time`,_utf8mb4'%H:%i') <> NULL))
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filmscreen`
--

LOCK TABLES `filmscreen` WRITE;
/*!40000 ALTER TABLE `filmscreen` DISABLE KEYS */;
INSERT INTO `filmscreen` VALUES (1,1,'2022-11-11','15:02',1),(2,6,'2022-11-11','16:19',2),(5,2,'2022-11-12','09:50',1),(6,2,'2022-11-12','10:20',2),(7,4,'2022-11-09','10:40',3),(8,3,'2022-11-10','10:50',1),(9,5,'2022-11-09','11:20',2),(10,3,'2022-11-08','11:40',3),(11,3,'2022-11-07','21:50',4),(12,4,'2022-11-15','16:40',1),(14,1,'2022-11-06','13:22',2),(15,1,'2022-11-11','12:59',5),(16,5,'2022-11-10','15:10',2),(17,5,'2022-11-08','15:02',2),(18,5,'2022-10-30','15:02',2),(21,3,'2022-11-07','12:59',1),(27,1,'2022-11-09','21:00',13),(28,6,'2022-11-13','23:59',13),(29,6,'2022-11-06','01:00',13),(31,6,'2022-09-16','11:22',13),(32,6,'2022-11-06','18:00',13),(33,5,'2022-11-13','11:11',3);
/*!40000 ALTER TABLE `filmscreen` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-09 18:25:16
