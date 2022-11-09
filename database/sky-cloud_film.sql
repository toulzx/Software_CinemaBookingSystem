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
-- Table structure for table `film`
--

DROP TABLE IF EXISTS `film`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `film` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `uname` char(20) NOT NULL,
  `info` char(255) DEFAULT NULL,
  `localAddress` char(255) NOT NULL,
  `time` date NOT NULL,
  `price` float DEFAULT '100',
  `discount` float DEFAULT '1',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `uname` (`uname`),
  CONSTRAINT `film_chk_1` CHECK (((`discount` > 0) and (`discount` <= 1))),
  CONSTRAINT `film_chk_2` CHECK ((`price` > 0)),
  CONSTRAINT `film_chk_3` CHECK ((`price` > 0))
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `film`
--

LOCK TABLES `film` WRITE;
/*!40000 ALTER TABLE `film` DISABLE KEYS */;
INSERT INTO `film` VALUES (1,'肖申克的救赎','⭐⭐⭐⭐','src/res/images/Shawshank_Redemption_ver2.jpg','1994-09-10',100,1),(2,'阿甘正传','⭐⭐','src/res/images/Forrestgumppost.jpg','1994-07-23',100,1),(3,'千与千寻','⭐⭐⭐','src/res/images/Spirited_away_poster.jpg','2001-07-20',100,1),(4,'盗梦空间','⭐⭐⭐⭐','src/res/images/Inception_ver3.jpg','2010-07-16',100,1),(5,'星际穿越','⭐⭐⭐⭐⭐','src/res/images/Interstellar_film_poster.jpg','2014-11-07',100,0.99),(6,'机器人总动员','⭐⭐⭐⭐⭐','src/res/images/Wall-E_Poster2.jpg','2008-06-27',100,1);
/*!40000 ALTER TABLE `film` ENABLE KEYS */;
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
