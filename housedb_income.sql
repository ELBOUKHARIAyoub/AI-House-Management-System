-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: housedb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `income`
--

DROP TABLE IF EXISTS `income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `income` (
  `incomeId` int NOT NULL AUTO_INCREMENT,
  `userId` int DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `amount` float DEFAULT NULL,
  `demo` varchar(50) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`incomeId`),
  KEY `FK_get` (`userId`),
  CONSTRAINT `FK_get` FOREIGN KEY (`userId`) REFERENCES `user` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income`
--

LOCK TABLES `income` WRITE;
/*!40000 ALTER TABLE `income` DISABLE KEYS */;
INSERT INTO `income` VALUES (1,1,'FATHER',2000,'nothing','2025-06-03'),(3,1,'FATHER',5000,'nothing','2025-06-02'),(8,1,'FATHER',1400,'good','2025-06-02'),(9,1,'MOM',12000,'dd','2025-06-02'),(10,1,'MOM',5000,'ayoo','2025-06-02'),(13,1,'MOM',10000,'ffff','2025-06-03'),(15,1,'MOM',1200,'GOOD','2025-06-02'),(16,1,'MOM',2000,'SS','2025-06-05'),(17,1,'me',12000,'good job','2025-06-05'),(18,1,'me',140000,'good job','2025-03-31');
/*!40000 ALTER TABLE `income` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-05 15:53:18
