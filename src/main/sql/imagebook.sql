-- MySQL dump 10.13  Distrib 5.1.59, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: imagebook_test
-- ------------------------------------------------------
-- Server version	5.1.59-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) NOT NULL,
  `index` varchar(255) NOT NULL,
  `region` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `street` varchar(255) DEFAULT NULL,
  `home` varchar(255) DEFAULT NULL,
  `building` varchar(255) DEFAULT NULL,
  `office` varchar(255) DEFAULT NULL,
  `comment` longtext,
  `user_id` int(11) DEFAULT NULL,
  `_index` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `surname` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBB979BF4E37A8A4A` (`user_id`),
  CONSTRAINT `FKBB979BF4E37A8A4A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
INSERT INTO `address` VALUES (4,'z','z','z','z','z','z','z','z','z',NULL,NULL,'z','z','z','z'),(6,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(7,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(8,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(9,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(10,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(11,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(13,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(14,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(15,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(16,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(17,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934'),(18,'РФ','2','3','4','5','6','7','8','9',NULL,NULL,'Андрей','Сорокин','1','89261078934');
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `admin_settings`
--

DROP TABLE IF EXISTS `admin_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin_settings` (
  `id` int(11) NOT NULL,
  `order_filter_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_filter_id` (`order_filter_id`),
  KEY `FK6B1F3313649AB8A4` (`order_filter_id`),
  KEY `FK6B1F33139A3A2FB1` (`id`),
  CONSTRAINT `FK6B1F3313649AB8A4` FOREIGN KEY (`order_filter_id`) REFERENCES `order_filter` (`id`),
  CONSTRAINT `FK6B1F33139A3A2FB1` FOREIGN KEY (`id`) REFERENCES `settings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_settings`
--

LOCK TABLES `admin_settings` WRITE;
/*!40000 ALTER TABLE `admin_settings` DISABLE KEYS */;
INSERT INTO `admin_settings` VALUES (1,1);
/*!40000 ALTER TABLE `admin_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `id` int(11) NOT NULL,
  `size` varchar(255) DEFAULT NULL,
  `cover_size` varchar(255) DEFAULT NULL,
  `cover_width` int(11) DEFAULT NULL,
  `cover_height` int(11) DEFAULT NULL,
  `hardcover` bit(1) DEFAULT NULL,
  `inner_crop` int(11) DEFAULT NULL,
  `jpeg_cover_folder` varchar(255) DEFAULT NULL,
  `front_upper_crop` int(11) DEFAULT NULL,
  `front_bottom_crop` int(11) DEFAULT NULL,
  `front_left_crop` int(11) DEFAULT NULL,
  `front_right_crop` int(11) DEFAULT NULL,
  `back_upper_crop` int(11) DEFAULT NULL,
  `back_bottom_crop` int(11) DEFAULT NULL,
  `back_left_crop` int(11) DEFAULT NULL,
  `back_right_crop` int(11) DEFAULT NULL,
  `upper_cover_safe_area` float DEFAULT NULL,
  `bottom_cover_safe_area` float DEFAULT NULL,
  `left_cover_safe_area` float DEFAULT NULL,
  `right_cover_safe_area` float DEFAULT NULL,
  `coverName` varchar(255) DEFAULT NULL,
  `lastPageTemplate` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5897E6F1C8754D7` (`id`),
  CONSTRAINT `FK5897E6F1C8754D7` FOREIGN KEY (`id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES (9,'290 х 200','407 х 237',407,237,'',70,'АльбомГоризБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(10,'290 x 290','407 x 320',407,320,'',70,'Элит_белая_M_',150,150,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(11,'290 х 200','654 х 237',654,237,'',70,'Альбом панорамный Обл_M_',185,185,3455,185,185,185,185,3455,8,8,8,8,'Плоттерная',NULL),(12,'290 x 290','654 x 330',654,330,'',70,'Элит-индивидуальная обложка_Cover_Auto_M_',200,200,3455,185,200,200,185,3455,10,10,8,8,'Плоттерная',NULL),(13,'215 x 140','322 x 177',322,177,'',150,'МиниГоризБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(14,'150 x 205','257 x 242',257,242,'',150,'МиниВертБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(15,'210 x 200','317 x 237',317,237,'',150,'БуклетБелыйОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(16,'260 x 200','367 x 237',367,237,'',150,'КнигаГоризБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(17,'210 x 250','317 x 287',317,287,'',150,'КнигаВертБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(18,'300 x 200','407 x 237',407,237,'',150,'АльбомГоризБелаяОбл_M_',185,185,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(19,'210 x 290','317 x 320',317,320,'',150,'АльбомВертБелаяОбл_M_',150,150,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(20,'300 x 290','407 x 320',407,320,'',150,'Элит_белая_M_',150,150,585,585,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(21,'416 x 280','483 x 317',483,317,'',150,'СуперГоризБелаяобл_M_',185,185,535,235,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Xerox',NULL),(22,NULL,NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(23,'215 x 140','483 x 177',474,177,'',150,'Миги гризПлоттер_M_',185,185,2595,185,185,185,185,2595,8,8,8,8,'Xerox',NULL),(24,'150 x 205','353 x 242',344,242,'',150,'МиниВертПлоттер_M_',185,185,1945,185,185,185,185,1945,8,8,8,8,'Xerox',NULL),(25,'210 x 200','473 x 237',464,237,'',150,'БуклетПлоттер_M_',185,185,2545,185,185,185,185,2545,8,8,8,8,'Xerox',NULL),(26,'260 x 200','573 x 237',564,237,'',150,'Книга ГоризПлоттер_M_',185,185,3045,185,185,185,185,3045,8,8,8,8,'Плоттерная',NULL),(27,'210 x 250','473 x 287',464,287,'',150,'КнигаВертПлоттер_M_',185,185,2545,185,185,185,185,2545,8,8,8,8,'Xerox',NULL),(28,'300 x 200','653 x 237',644,237,'',150,'АльбомГоризПлоттерОбл_M_',185,185,3445,185,185,185,185,3445,8,8,8,8,'Плоттерная',NULL),(29,'210 x 290','473 x 320',464,327,'',150,'АльбВертПлоттОбл_M_',185,185,2545,185,185,185,185,2545,8,8,8,8,'Xerox',NULL),(30,'300 x 290','644 x 330',644,330,'',150,'Элит-индивидуальная обложка_Cover_Auto_M_',200,200,3455,185,200,200,185,3455,10,10,8,8,'Плоттерная',NULL),(31,'416 x 280','885 x 317',876,317,'',150,'СуперАльбГоризПлоттер_M_',185,185,4605,185,185,185,185,4605,8,8,8,8,'Плоттерная',NULL),(32,'290 x 406','639 х 443 мм',624,443,'',150,'СуперАльбомВертикальныйПлоттер_M_',185,185,3345,185,185,185,185,3345,8,8,8,8,'Плоттерная',NULL),(33,'205 x 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(34,'140 x 205',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(35,'200 x 200',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(36,'250 x 200',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(37,'200 x 250',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(38,'290 x 200',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(39,'200 x 290',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(40,'290 x 290',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(41,'406 x 280',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(42,'280 х 406',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'Полупрозрачный пластик',NULL),(43,'205 x 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,'140 x 205',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(45,'200 x 200',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(46,'200 x 250',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(47,'200 x 290',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL),(48,'205 x 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'trial.jpg'),(49,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1-back.jpg'),(50,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2-back.jpg'),(51,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'3-back.jpg'),(52,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4-back.jpg'),(53,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'christmas_1.jpg'),(54,'215 х 95','483 х 132',474,132,'',150,'Евро_плоттер_обл_M_',185,185,2595,185,185,185,185,2595,8,8,8,8,'Xerox',NULL),(55,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'st_val_01.jpg'),(56,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'st_val_02.jpg'),(57,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_01.jpg'),(58,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_02.jpg'),(59,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_03.jpg'),(60,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_04.jpg'),(61,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_05.jpg'),(62,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'8_mart_06.jpg'),(63,'205 х 140 мм',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'trial_book2look.jpg'),(64,'290 x 420 мм','654 x 457',654,457,'',70,'СуперПанорамныйОбл_M_',185,185,3455,185,185,185,185,3455,8,8,8,8,'Плоттерная',NULL),(65,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'new_year_1.jpg'),(66,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'new_year_2.jpg'),(67,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine1-back.jpg'),(68,'205 х140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine2-back.jpg'),(69,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine3-back.jpg'),(70,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine4-back.jpg'),(71,'205 ч 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1-back.jpg'),(72,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2-back.jpg'),(73,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'3-back.jpg'),(74,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'4-back.jpg'),(75,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1-back.jpg'),(76,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine1-back.jpg'),(77,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine2-back.jpg'),(78,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'iloveyou3_back.jpg'),(79,'205 х 140',NULL,NULL,NULL,'\0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'valentine4-back.jpg');
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `album_order`
--

DROP TABLE IF EXISTS `album_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album_order` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE434997E57155E76` (`id`),
  CONSTRAINT `FKE434997E57155E76` FOREIGN KEY (`id`) REFERENCES `order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album_order`
--

LOCK TABLES `album_order` WRITE;
/*!40000 ALTER TABLE `album_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `album_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `state` int(11) NOT NULL,
  `adv` bit(1) NOT NULL,
  `weight` int(11) DEFAULT NULL,
  `deliveryType` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2E2407EC768806` (`user`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bonus_action`
--

DROP TABLE IF EXISTS `bonus_action`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bonus_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `level1` int(11) NOT NULL,
  `level2` int(11) NOT NULL,
  `discount1` int(11) NOT NULL,
  `discount2` int(11) NOT NULL,
  `permanentLevel` int(11) NOT NULL,
  `dateStart` datetime NOT NULL,
  `dateEnd` datetime DEFAULT NULL,
  `repeatal` bit(1) NOT NULL,
  `discountSum` int(11) DEFAULT NULL,
  `discountPCenter` INT(11)  DEFAULT NULL,
  `vendor` int(11) DEFAULT NULL,
  `code_length` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32C6CA568F564369` (`vendor`),
  CONSTRAINT `FK32C6CA568F564369` FOREIGN KEY (`vendor`) REFERENCES `vendor` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bonus_action`
--

LOCK TABLES `bonus_action` WRITE;
/*!40000 ALTER TABLE `bonus_action` DISABLE KEYS */;
/*!40000 ALTER TABLE `bonus_action` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bonus_code`
--

DROP TABLE IF EXISTS `bonus_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bonus_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action` int(11) NOT NULL,
  `code` varchar(255) NOT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`),
  KEY `FK676A820DCADB81B8` (`action`),
  CONSTRAINT `FK676A820DCADB81B8` FOREIGN KEY (`action`) REFERENCES `bonus_action` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bonus_code`
--

LOCK TABLES `bonus_code` WRITE;
/*!40000 ALTER TABLE `bonus_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `bonus_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendar`
--

DROP TABLE IF EXISTS `calendar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF55EFB3E1C8754D7` (`id`),
  CONSTRAINT `FKF55EFB3E1C8754D7` FOREIGN KEY (`id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendar`
--

LOCK TABLES `calendar` WRITE;
/*!40000 ALTER TABLE `calendar` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calendar_order`
--

DROP TABLE IF EXISTS `calendar_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `calendar_order` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2B26FF0D57155E76` (`id`),
  CONSTRAINT `FK2B26FF0D57155E76` FOREIGN KEY (`id`) REFERENCES `order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calendar_order`
--

LOCK TABLES `calendar_order` WRITE;
/*!40000 ALTER TABLE `calendar_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `calendar_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `color`
--

DROP TABLE IF EXISTS `color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `color` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  `name_ru` longtext NOT NULL,
  `name_en` longtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `color`
--

LOCK TABLES `color` WRITE;
/*!40000 ALTER TABLE `color` DISABLE KEYS */;
INSERT INTO `color` VALUES (1,0,'Нет','None'),(2,11,'Кожа коричневая','Brown leather'),(3,12,'Кожа синяя','Blue leather'),(4,51,'Пластик полупрозрачный','Semitransparent plastic'),(6,52,'Пластик синий','Pl_Bl'),(7,61,'Папка синяя','Fol_bl');
/*!40000 ALTER TABLE `color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `color_range`
--

DROP TABLE IF EXISTS `color_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `color_range` (
  `product_id` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `_index` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`_index`),
  KEY `FK4EE1756185041227` (`product_id`),
  CONSTRAINT `FK4EE1756185041227` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `color_range`
--

LOCK TABLES `color_range` WRITE;
/*!40000 ALTER TABLE `color_range` DISABLE KEYS */;
INSERT INTO `color_range` VALUES (9,0,0),(10,0,0),(11,0,0),(12,0,0),(13,0,0),(14,0,0),(15,0,0),(16,0,0),(17,0,0),(18,0,0),(19,0,0),(20,0,0),(21,0,0),(22,0,0),(23,0,0),(24,0,0),(25,0,0),(26,0,0),(27,0,0),(28,0,0),(29,0,0),(30,0,0),(31,0,0),(32,0,0),(33,51,0),(34,51,0),(35,51,0),(36,51,0),(37,51,0),(38,51,0),(39,51,0),(40,51,0),(41,51,0),(42,51,0),(43,0,0),(44,0,0),(45,0,0),(46,0,0),(47,0,0),(48,0,0),(49,0,0);
/*!40000 ALTER TABLE `color_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `country`
--

DROP TABLE IF EXISTS `country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `isDefault` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `country`
--

LOCK TABLES `country` WRITE;
/*!40000 ALTER TABLE `country` DISABLE KEYS */;
/*!40000 ALTER TABLE `country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cover_lam_range`
--

DROP TABLE IF EXISTS `cover_lam_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cover_lam_range` (
  `product_id` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `_index` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`_index`),
  KEY `FKB561014E85041227` (`product_id`),
  CONSTRAINT `FKB561014E85041227` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cover_lam_range`
--

LOCK TABLES `cover_lam_range` WRITE;
/*!40000 ALTER TABLE `cover_lam_range` DISABLE KEYS */;
INSERT INTO `cover_lam_range` VALUES (9,1,0),(9,2,1),(10,1,0),(10,2,1),(11,1,0),(11,2,1),(12,1,0),(12,2,1),(13,1,0),(13,2,1),(14,1,0),(14,2,1),(15,1,0),(16,1,0),(16,2,1),(17,1,0),(17,2,1),(18,1,0),(18,2,1),(19,1,0),(19,2,1),(20,1,0),(20,2,1),(21,1,0),(21,2,1),(22,1,0),(22,2,1),(23,1,0),(23,2,1),(24,1,0),(24,2,1),(25,1,0),(25,2,1),(26,1,0),(26,2,1),(27,1,0),(27,2,1),(28,1,0),(28,2,1),(29,1,0),(29,2,1),(30,1,0),(30,2,1),(31,1,0),(31,2,1),(32,1,0),(32,2,1),(33,0,0),(34,0,0),(35,0,0),(36,0,0),(37,0,0),(38,0,0),(39,0,0),(40,0,0),(41,0,0),(42,0,0),(43,0,0),(43,1,1),(43,2,2),(44,0,0),(44,1,1),(44,2,2),(45,0,0),(45,1,1),(45,2,2),(46,0,0),(46,1,1),(46,2,2),(47,0,0),(47,1,1),(47,2,2),(48,0,0),(49,0,0);
/*!40000 ALTER TABLE `cover_lam_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_barcode`
--

DROP TABLE IF EXISTS `editor_barcode`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_barcode` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEC1DE88011FF683C` (`id`),
  CONSTRAINT `FKEC1DE88011FF683C` FOREIGN KEY (`id`) REFERENCES `editor_rectangle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_barcode`
--

LOCK TABLES `editor_barcode` WRITE;
/*!40000 ALTER TABLE `editor_barcode` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_barcode` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_component`
--

DROP TABLE IF EXISTS `editor_component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_component` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `page_id` int(11) DEFAULT NULL,
  `index` int(11) NOT NULL,
  `blocked` bit(1) NOT NULL,
  `component_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKAC8F1CFDC93B1428` (`page_id`),
  CONSTRAINT `FKAC8F1CFDC93B1428` FOREIGN KEY (`page_id`) REFERENCES `editor_page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_component`
--

LOCK TABLES `editor_component` WRITE;
/*!40000 ALTER TABLE `editor_component` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_counter`
--

DROP TABLE IF EXISTS `editor_counter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_counter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_counter`
--

LOCK TABLES `editor_counter` WRITE;
/*!40000 ALTER TABLE `editor_counter` DISABLE KEYS */;
INSERT INTO `editor_counter` VALUES (1,17000130);
/*!40000 ALTER TABLE `editor_counter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_image`
--

DROP TABLE IF EXISTS `editor_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_image` (
  `id` int(11) NOT NULL,
  `layout_type` int(11) NOT NULL,
  `clipLeft` float NOT NULL,
  `clipTop` float NOT NULL,
  `clipWidth` float NOT NULL,
  `clipHeight` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5FAA95B1B3A60CA` (`id`),
  KEY `FK5FAA95B11FF683C` (`id`),
  CONSTRAINT `FK5FAA95B11FF683C` FOREIGN KEY (`id`) REFERENCES `editor_rectangle` (`id`),
  CONSTRAINT `FK5FAA95B1B3A60CA` FOREIGN KEY (`id`) REFERENCES `editor_component` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_image`
--

LOCK TABLES `editor_image` WRITE;
/*!40000 ALTER TABLE `editor_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_page`
--

DROP TABLE IF EXISTS `editor_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `width` float NOT NULL,
  `height` float NOT NULL,
  `x_margin` float NOT NULL,
  `y_margin` float NOT NULL,
  `layout_id` int(11) DEFAULT NULL,
  `number` int(11) DEFAULT NULL,
  `blocked` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK34628FD9658EC8` (`layout_id`),
  CONSTRAINT `FK34628FD9658EC8` FOREIGN KEY (`layout_id`) REFERENCES `layout` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_page`
--

LOCK TABLES `editor_page` WRITE;
/*!40000 ALTER TABLE `editor_page` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_position`
--

DROP TABLE IF EXISTS `editor_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_position` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2C92992911FF683C` (`id`),
  CONSTRAINT `FK2C92992911FF683C` FOREIGN KEY (`id`) REFERENCES `editor_rectangle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_position`
--

LOCK TABLES `editor_position` WRITE;
/*!40000 ALTER TABLE `editor_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_rectangle`
--

DROP TABLE IF EXISTS `editor_rectangle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_rectangle` (
  `id` int(11) NOT NULL,
  `left` float NOT NULL,
  `top` float NOT NULL,
  `width` float NOT NULL,
  `height` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK42D5AD6F1B3A60CA` (`id`),
  CONSTRAINT `FK42D5AD6F1B3A60CA` FOREIGN KEY (`id`) REFERENCES `editor_component` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_rectangle`
--

LOCK TABLES `editor_rectangle` WRITE;
/*!40000 ALTER TABLE `editor_rectangle` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_rectangle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `editor_safe_area`
--

DROP TABLE IF EXISTS `editor_safe_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `editor_safe_area` (
  `id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD74571AD11FF683C` (`id`),
  CONSTRAINT `FKD74571AD11FF683C` FOREIGN KEY (`id`) REFERENCES `editor_rectangle` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `editor_safe_area`
--

LOCK TABLES `editor_safe_area` WRITE;
/*!40000 ALTER TABLE `editor_safe_area` DISABLE KEYS */;
/*!40000 ALTER TABLE `editor_safe_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email`
--

DROP TABLE IF EXISTS `email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` bit(1) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `_index` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5C24B9CE37A8A4A` (`user_id`),
  CONSTRAINT `FK5C24B9CE37A8A4A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email`
--

LOCK TABLES `email` WRITE;
/*!40000 ALTER TABLE `email` DISABLE KEYS */;
INSERT INTO `email` VALUES (28,'','minogin@imagebook.ru',1,0);
/*!40000 ALTER TABLE `email` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `layout`
--

DROP TABLE IF EXISTS `layout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `layout` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `layout`
--

LOCK TABLES `layout` WRITE;
/*!40000 ALTER TABLE `layout` DISABLE KEYS */;
/*!40000 ALTER TABLE `layout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mailing`
--

DROP TABLE IF EXISTS `mailing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mailing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `content` longtext NOT NULL,
  `total` int(11) NOT NULL,
  `sent` int(11) NOT NULL,
  `report` longtext,
  `date` datetime NOT NULL,
  `state` int(11) NOT NULL,
  `type` int(11) NOT NULL,
  `vendor` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mailing_ibfk_1` (`vendor`),
  CONSTRAINT `mailing_ibfk_1` FOREIGN KEY (`vendor`) REFERENCES `vendor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mailing`
--

LOCK TABLES `mailing` WRITE;
/*!40000 ALTER TABLE `mailing` DISABLE KEYS */;
INSERT INTO `mailing` VALUES (17,'1','2','3',1,1,'','2011-12-19 23:02:25',300,2,1),(18,'Рассылка pfb','По поводу альбомов Printfotobook','По поводу альбомов Printfotobook\nтекст\nтекст\nтекст\nтекст\nтекст',2,2,'','2012-05-16 13:04:42',100,0,2);
/*!40000 ALTER TABLE `mailing` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(255) NOT NULL,
  `user` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `product` int(11) NOT NULL,
  `state` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `flash` varchar(255) DEFAULT NULL,
  `stored_price` int(11) DEFAULT NULL,
  `stored_cost` int(11) DEFAULT NULL,
  `reject_comment` varchar(255) DEFAULT NULL,
  `page_count` int(11) NOT NULL,
  `color` int(11) NOT NULL,
  `cover_lamination` int(11) NOT NULL,
  `page_lamination` int(11) NOT NULL,
  `bill_id` int(11) DEFAULT NULL,
  `trial` bit(1) NOT NULL DEFAULT b'0',
  `address_id` int(11) DEFAULT NULL,
  `in_request_basket` bit(1) NOT NULL DEFAULT b'0',
  `request_id` int(11) DEFAULT NULL,
  `bonus_code` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `delivery_type` int(11) DEFAULT NULL,
  `delivery_code` varchar(255) DEFAULT NULL,
  `comment` longtext,
  `publish_flash` bit(1) NOT NULL DEFAULT b'0',
  `type` int(11) NOT NULL DEFAULT '100',
  `layout_id` int(11) DEFAULT NULL,
  `print_date` datetime DEFAULT NULL,
  `web_flash` bit(1) NOT NULL,
  `storageState` int(11) NOT NULL,
  `sentDate` datetime DEFAULT NULL,
  `discountPc` int(11) NOT NULL,
  `adv` bit(1) NOT NULL,
  `deliveryComment` longtext,
  `discountSum` int(11) DEFAULT NULL,
  `itemWeight` int(11) DEFAULT NULL,
  `totalWeight` int(11) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `bonusDiscount` int(11) DEFAULT NULL,
  `bonusCodeText` varchar(255) DEFAULT NULL,
  `discountPCenter` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `number` (`number`),
  UNIQUE KEY `address_id` (`address_id`),
  UNIQUE KEY `layout_id` (`layout_id`),
  KEY `FK651874E5C1228D3` (`color`),
  KEY `FK651874EA1514AB` (`product`),
  KEY `FK651874EA16660AD` (`bill_id`),
  KEY `FK651874EEC768806` (`user`),
  KEY `FK651874E92B2B6CA` (`address_id`),
  KEY `FK651874E727EAE27` (`request_id`),
  KEY `FK651874E13CE9846` (`bonus_code`),
  CONSTRAINT `FK651874E5C1228D3` FOREIGN KEY (`color`) REFERENCES `color` (`id`),
  CONSTRAINT `FK651874E91DEFC67` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
  CONSTRAINT `FK651874EA1514AB` FOREIGN KEY (`product`) REFERENCES `product` (`id`),
  CONSTRAINT `FK651874EA16660AD` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`),
  CONSTRAINT `FK651874EC9DDB287` FOREIGN KEY (`request_id`) REFERENCES `request` (`id`),
  CONSTRAINT `FK651874ED9658EC1` FOREIGN KEY (`layout_id`) REFERENCES `layout` (`id`),
  CONSTRAINT `FK651874ED9658EC8` FOREIGN KEY (`layout_id`) REFERENCES `layout` (`id`),
  CONSTRAINT `FK651874EEC768806` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FK651874EF7E04A6` FOREIGN KEY (`bonus_code`) REFERENCES `bonus_code` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=199 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_filter`
--

DROP TABLE IF EXISTS `order_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bonusCode` varchar(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_filter`
--

LOCK TABLES `order_filter` WRITE;
/*!40000 ALTER TABLE `order_filter` DISABLE KEYS */;
INSERT INTO `order_filter` VALUES (1);
/*!40000 ALTER TABLE `order_filter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_filter_state`
--

DROP TABLE IF EXISTS `order_filter_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_filter_state` (
  `filter_id` int(11) NOT NULL,
  `state` int(11) DEFAULT NULL,
  KEY `FK626C2A5BB0568055` (`filter_id`),
  CONSTRAINT `FK626C2A5BB0568055` FOREIGN KEY (`filter_id`) REFERENCES `order_filter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_filter_state`
--

LOCK TABLES `order_filter_state` WRITE;
/*!40000 ALTER TABLE `order_filter_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_filter_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_lam_range`
--

DROP TABLE IF EXISTS `page_lam_range`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page_lam_range` (
  `product_id` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `_index` int(11) NOT NULL,
  PRIMARY KEY (`product_id`,`_index`),
  KEY `FKC66B622685041227` (`product_id`),
  CONSTRAINT `FKC66B622685041227` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_lam_range`
--

LOCK TABLES `page_lam_range` WRITE;
/*!40000 ALTER TABLE `page_lam_range` DISABLE KEYS */;
INSERT INTO `page_lam_range` VALUES (9,0,0),(10,0,0),(11,0,0),(12,0,0),(13,0,0),(13,1,1),(13,2,2),(14,0,0),(14,1,1),(14,2,2),(15,0,0),(15,1,1),(16,0,0),(16,1,1),(16,2,2),(17,0,0),(17,1,1),(17,2,2),(18,0,0),(18,1,1),(18,2,2),(19,0,0),(19,1,1),(19,2,2),(20,0,0),(20,1,1),(20,2,2),(21,0,0),(21,1,1),(21,2,2),(22,0,0),(22,1,1),(22,2,2),(23,0,0),(23,1,1),(23,2,2),(24,0,0),(24,1,1),(24,2,2),(25,0,0),(25,1,1),(25,2,2),(26,0,0),(26,1,1),(26,2,2),(27,0,0),(27,1,1),(27,2,2),(28,0,0),(28,1,1),(28,2,2),(29,0,0),(29,1,1),(29,2,2),(30,0,0),(30,1,1),(30,2,2),(31,0,0),(31,1,1),(31,2,2),(32,0,0),(32,1,1),(32,2,2),(33,0,0),(33,1,1),(33,2,2),(34,0,0),(34,1,1),(34,2,2),(35,0,0),(35,1,1),(35,2,2),(36,0,0),(36,1,1),(36,2,2),(37,0,0),(37,1,1),(37,2,2),(38,0,0),(38,1,1),(38,2,2),(39,0,0),(39,1,1),(39,2,2),(40,0,0),(40,1,1),(40,2,2),(41,0,0),(41,1,1),(41,2,2),(42,0,0),(42,1,1),(42,2,2),(43,0,0),(44,0,0),(45,0,0),(46,0,0),(47,0,0),(48,0,0),(49,0,0);
/*!40000 ALTER TABLE `page_lam_range` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `phone`
--

DROP TABLE IF EXISTS `phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `_index` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK65B3D6EE37A8A4A` (`user_id`),
  CONSTRAINT `FK65B3D6EE37A8A4A` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phone`
--

LOCK TABLES `phone` WRITE;
/*!40000 ALTER TABLE `phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `phone` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `name_ru` longtext NOT NULL,
  `name_en` longtext NOT NULL,
  `availability` int(11) NOT NULL,
  `block_format` varchar(255) NOT NULL,
  `binding` int(11) NOT NULL,
  `cover` int(11) NOT NULL,
  `paper` int(11) NOT NULL,
  `multiplicity` int(11) NOT NULL,
  `min_page_count` int(11) NOT NULL,
  `max_page_count` int(11) NOT NULL,
  `min_quantity` int(11) NOT NULL,
  `width` int(11) DEFAULT NULL,
  `height` int(11) DEFAULT NULL,
  `jpeg_folder` varchar(255) DEFAULT NULL,
  `block_width` int(11) DEFAULT NULL,
  `block_height` int(11) DEFAULT NULL,
  `upper_safe_area` float DEFAULT NULL,
  `bottom_safe_area` float DEFAULT NULL,
  `inner_safe_area` float DEFAULT NULL,
  `outer_safe_area` float DEFAULT NULL,
  `addressPrinted` bit(1) NOT NULL,
  `nonEditor` bit(1) NOT NULL,
  `trialAlbum` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (9,1,6,'Альбом панорамный - твердая обложка с белыми полями (01-01-06)','A4A Everflat white',2,'290 x 200 мм',1,1,1,4,20,100,1,302,210,'Альбом панорамный_M_',290,200,5,5,1,5,'\0','\0','\0'),(10,1,8,'Элит панорамный — твердая обложка с белыми полями (01-01-08)','Elit Everflat white',2,'290 x 290 мм',1,1,1,2,20,100,1,302,300,'Элит панорамный_M_',290,290,5,5,1,5,'\0','\0','\0'),(11,2,6,'Альбом панорамный - твердая обложка с полной запечаткой (01-02-06)','A4A Everflat',2,'290 х 200 мм',1,2,1,2,20,100,1,302,210,'Альбом панорамный_M_',290,200,5,5,1,5,'\0','\0','\0'),(12,2,8,'Элит панорамный — твердая обложка с полной запечаткой (01-02-08)','Elit Everflat',2,'290 x 290 мм',1,2,1,2,20,100,1,302,300,'Элит панорамный_M_',290,290,5,5,1,5,'\0','\0','\0'),(13,4,1,'МиниАльбом горизонтальный — твердая обложка с белыми полями (01-04-01)','Mini Land Whight Hard Cover',2,'205 x 140 мм',1,1,2,8,24,100,1,225,150,'Мини тверд гориз_M_',205,140,5,5,8,5,'\0','\0','\0'),(14,4,2,'МиниАльбом вертикальный — твердая обложка с белыми полями (01-04-02)','Mini Port White Hard Cover',2,'140 x 205 мм',1,1,2,8,24,100,1,160,215,'Мини твердый_M_',140,205,5,5,8,5,'\0','\0','\0'),(15,4,3,'Буклет — твердая обложка с белыми полями (01-04-03)','Buklet White Hard Cover ',2,'200 x 200 мм',1,1,2,4,20,100,1,220,210,'Буклет_M_',200,200,5,5,8,5,'\0','\0','\0'),(16,4,4,'Книга горизонтальная — твердая обложка с белыми полями (01-04-04)','Book Land White Hard Cover',2,'250 х 200 мм',1,1,2,4,20,100,1,270,210,'КнигаГориз_M_',250,200,5,5,8,5,'\0','\0','\0'),(17,4,5,'Книга вертикальная — твердая обложка с белыми полями (01-04-05)','Book Port White Hard Cover',2,'200 х 250 мм',1,1,2,4,20,100,1,220,260,'КнигаВертик_M_',200,250,5,5,8,5,'\0','\0','\0'),(18,4,6,'Альбом горизонтальный — твердая обложка с белыми полями (01-04-06)','Album Land White Hard Cover',2,'290 х 200 мм',1,1,2,4,20,100,1,310,210,'АльбомГориз_M_',290,200,5,5,8,5,'\0','\0','\0'),(19,4,7,'Альбом вертикальный — твердая обложка с белыми полями (01-04-07)','Album Port White Hard Cover',2,'200 x 290 мм',1,1,2,4,20,100,1,220,300,'A4_Album_Cover_M_',200,290,5,5,8,5,'\0','\0','\0'),(20,4,8,'Элит — твердая обложка с белыми полями (01-04-08)','Elit White Hard Cover',2,'290 х 290 мм',1,1,2,2,20,100,1,310,300,'Элит_M_',290,290,5,5,8,5,'\0','\0','\0'),(21,4,9,'СуперАльбом горизонтальный — твердая обложка с белыми полями (01-04-09)','SuperAlbum White Hard Cover',2,'406 х 280 мм',1,1,2,2,20,100,1,426,290,'СуперАльбом Гориз_M_',406,280,5,5,8,5,'\0','\0','\0'),(22,4,10,'СуперАльбом вертикальный — твердая обложка с белыми полями (01-04-10)','SuperAlbum Port White Hard Cover',0,'280 х 406 мм',1,1,2,2,20,100,1,NULL,NULL,NULL,280,406,NULL,NULL,NULL,NULL,'\0','\0','\0'),(23,5,1,'МиниАльбом горизонтальный — твердая обложка с полной запечаткой (01-05-01)','Mini Land Hard Cover',2,'205 х 140 мм',1,2,2,8,24,100,1,225,150,'Мини тверд гориз_M_',205,140,5,5,8,5,'\0','\0','\0'),(24,5,2,'МиниАльбом вертикальный — твердая обложка с полной запечаткой (01-05-02)','Mini Port Hard Cover',2,'140 х 205 мм',1,2,2,8,24,100,1,160,215,'Мини твердый_M_',140,205,5,5,8,5,'\0','\0','\0'),(25,5,3,'Буклет — твердая обложка с полной запечаткой (01-05-03)','Buklet Hard Cover',2,'200 х 200 мм',1,2,2,4,20,100,1,220,210,'Буклет_M_',200,200,5,5,8,5,'\0','\0','\0'),(26,5,4,'Книга горизонтальная — твердая обложка с полной запечаткой (01-05-04)','Book Land Hard Cover',2,'250 х 200 мм',1,2,2,4,20,100,1,270,210,'КнигаГориз_M_',250,200,5,5,8,5,'\0','\0','\0'),(27,5,5,'Книга вертикальная — твердая обложка с полной запечаткой (01-05-05)','Book Port Hard Cover',2,'200 х 250 мм',1,2,2,4,20,100,1,220,260,'КнигаВертик_M_',200,250,5,5,8,5,'\0','\0','\0'),(28,5,6,'Альбом горизонтальный — твердая обложка с полной запечаткой (01-05-06)','Album Land Hard Cover',2,'290 х 200 мм',1,2,2,4,20,100,1,310,210,'АльбомГориз_M_',290,200,5,5,8,5,'\0','\0','\0'),(29,5,7,'Альбом вертикальный — твердая обложка с полной запечаткой (01-05-07)','Album Port Hard Cover',2,'200 х 290 мм',1,2,2,4,20,100,1,220,300,'A4_Album_Cover_M_',200,290,5,5,8,5,'\0','\0','\0'),(30,5,8,'Элит — твердая обложка с полной запечаткой (01-05-08)','Elit Hard Cover',2,'290 х 290 мм',1,2,2,2,20,100,1,310,300,'Элит_M_',290,290,5,5,8,5,'\0','\0','\0'),(31,5,9,'СуперАльбом горизонтальный — твердая обложка с полной запечаткой (01-05-09)','SuperAlbum Land Hard Cover',2,'406 х 280 мм',1,2,2,2,20,100,1,426,290,'СуперАльбом Гориз_M_',406,280,5,5,8,5,'\0','\0','\0'),(32,5,10,'СуперАльбом вертикальный — твердая обложка с полной запечаткой (01-05-10)','SuperAlbum Port Hard Cover',2,'280 х 406 мм',1,2,2,2,20,100,1,300,416,'СуперАльбомВертикальный_M_',280,406,5,5,8,5,'\0','\0','\0'),(33,7,1,'МиниАльбом горизонтальный — переплет на пружине (01-07-01)','Mini Land Spiral',2,'205 х 140 мм',2,4,3,8,24,80,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,10,5,'\0','\0','\0'),(34,7,2,'МиниАльбом вертикальный — переплет на пружине (01-07-02)','Mini Port Spiral',2,'140 х 205 мм',2,4,3,8,24,80,1,150,215,'МиниМягкийГоризонтальный_M_',140,205,5,5,10,5,'\0','\0','\0'),(35,7,3,'Буклет — переплет на пружине (01-07-03)','Buklet Spiral',2,'200 х 200 мм',2,4,3,4,20,80,1,210,210,'БуклетПруСкрепка_M_',200,200,5,5,10,5,'\0','\0','\0'),(36,7,4,'Книга горизонтальная — переплет на пружине (01-07-04)','Book Land Spiral',2,'250 х 200 мм',2,4,3,4,20,80,1,260,210,'КнигаВертикПру_M_',250,200,5,5,10,5,'\0','\0','\0'),(37,7,5,'Книга вертикальная — переплет на пружине (01-07-05)','Book Port Spiral',2,'200 х 250 мм',2,4,3,4,20,80,1,210,260,'КнигаВертикПру_M_',200,250,5,5,10,5,'\0','\0','\0'),(38,7,6,'Альбом горизонтальный — переплет на пружине (01-07-06)','Album Land Spiral',2,'290 х 200 мм',2,4,3,4,20,80,1,300,210,'АльбомВертикПружина_M_',290,200,5,5,10,5,'\0','\0','\0'),(39,7,7,'Альбом вертикальный — переплет на пружине (01-07-07)','Album Port Spiral',2,'200 х 290 мм',2,4,3,4,20,80,1,210,300,'АльбомВертикПружина_M_',200,290,5,5,10,5,'\0','\0','\0'),(40,7,8,'Элит — переплет на пружине (01-07-08)','Elit Spiral',2,'290 х 290 мм',2,4,3,2,20,80,1,300,300,'ЭлитПружина_M_',290,290,5,5,10,5,'\0','\0','\0'),(41,7,9,'СуперАльбом горизонтальный — переплет на пружине (01-07-09)','SuperAlbum Land Spiral',2,'406 х 280 мм',2,4,3,2,20,80,1,416,290,'СуперАльбомПру_M_',406,280,5,5,10,5,'\0','\0','\0'),(42,7,10,'СуперАльбом вертикальный — переплет на пружине (01-07-10)','SuperAlbum Port Spiral',2,'280 х 406 мм',2,4,3,2,20,80,1,290,416,'СуперАльбомПру_M_',280,406,5,5,10,5,'\0','\0','\0'),(43,8,1,'МиниАльбом горизонтальный — переплет на скрепке (01-08-01)','Mini Land Notebook',2,'205 х 140 мм',3,0,3,8,8,24,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'\0','\0','\0'),(44,8,2,'МиниАльбом вертикальный — переплет на скрепке (01-08-02)','Mini Port Notebook',2,'140 х 205 мм',3,0,3,8,8,24,1,150,215,'МиниМягкийГоризонтальный_M_',140,205,5,5,1,5,'\0','\0','\0'),(45,8,3,'Буклет — переплет на скрепке (01-08-03)','Buklet Notebook',2,'200 х 200 мм',3,0,3,4,8,20,1,210,210,'БуклетПруСкрепка_M_',200,200,5,5,1,5,'\0','\0','\0'),(46,8,5,'Книга вертикальная — переплет на скрепке (01-08-05)','Book Port Notebook',2,'200 х 250 мм',3,0,3,4,8,20,1,210,260,'КнигаВертикПру_M_',200,250,5,5,1,5,'\0','\0','\0'),(47,8,7,'Альбом вертикальный — переплет на скрепке (01-08-07)','Album Port Notebook',2,'200 х 290 мм',3,0,3,4,8,20,1,210,300,'АльбомВертикПружина_M_',200,290,5,5,1,5,'\0','\0','\0'),(48,9,99,'Пробный альбом (01-09-99)','Specimen Copy',2,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','\0',''),(49,10,1,'23 февраля 1 (01-10-01)','new_year_1.jpg - New Year 1 (01-10-01)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(50,10,2,'23 февраля 2 (01-10-02)','new_year_2.jpg - New Year 2 (01-10-02)',1,'205 Х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(51,10,3,'23 февраля 3 (01-10-03)','new_year_3.jpg - New Year 3 (01-10-03)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(52,10,4,'23 февраля 4 (01-10-04)','new_year_4.jpg - New Year 4 (01-10-04)',1,'204 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(53,10,5,'Рождественский 5 (01-10-05)','Christmas 5 (01-10-05)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(54,5,0,'Евростандарт - твердая обложка с полной запечаткой (01-05-00)','Evrostandart Full cover',2,'205 x 95 мм',1,2,2,12,24,96,1,225,105,'Евро_плоттер_M_',205,95,5,5,8,5,'\0','\0','\0'),(55,10,6,'Св-Валентин 1 (01-10-06)','St-Valentin 1',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(56,10,7,'Св-Валентин 2 (01-10-07)','StValentin 2',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(57,10,8,'8 Марта 1 (01-10-08)','8 Mar 1 (01-10-08)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(58,10,9,'8 Марта 2 (01-10-09)','8 Mar 2 (01-10-09)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(59,10,10,'8 Марта 3 (01-10-10)','8 Mar 3 (01-10-10)',1,'205 х 140',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(60,10,11,'8 Марта 4 (01-10-11)','8 Mar 4 (01-10-11)',1,'205 x 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(61,10,12,'8 Марта 5 (01-10-12)','8 Mar 5 (01-10-12)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(62,10,14,'8 Марта 6 (01-10-14)','8 Mar 6 (01-10-14)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(63,9,98,'Пробный альбом (01-09-98)','Test album (01-09-98)',0,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(64,2,10,'СуперАльбом панорамный - твердая обложка с полной запечаткой (01-02-10)','SuperAlbumEverflat (01-02-10)',2,'290 x 420 мм',1,2,1,2,20,100,1,302,430,'СуперПанорамный_M_',290,420,5,5,1,5,'\0','\0','\0'),(65,10,21,'Новогодний 1 (01-10-21)','New Year 1 (01-10-21)',0,'205 х140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(66,10,22,'Новогодний 2 (01-10-22)','New Year 2 (01-10-22)',0,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(67,10,24,'Валентинка 1 (01-10-24)','Val 1',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(68,10,25,'Валентинка 2 (01-10-25)','Val 2',1,'205 x 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(69,10,26,'Валентинка 3 (01-10-26)','Val 3',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(70,10,27,'Валентинка 4 (01-10-27)','Val 4',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(71,10,28,'23 февраля 1 (01-10-28)','23Febr ',1,'205 x 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(72,10,29,'23 февраля 2 (01-10-29)','23Febr',1,'205 x 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(73,10,30,'23 февраля 3 (01-10-30)','23Febr3',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(74,10,31,'23 февраля 4 (01-10-31)','23Febr4',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(75,10,15,'23 февраля 1 (01-10-15)','23 Febr 1 (01-10-15)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(76,10,32,'Я люблю тебя 1 (01-10-32)','I Love You 1 (01-10-32)',1,'205 x 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(77,10,33,'Я люблю тебя 2 (01-10-33)','I Lpve You 2 (01-10-33)',1,'205 х 140',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(78,10,34,'Я люблю тебя 3 (01-10-34)','P Love You (01-10-34)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0'),(79,10,35,'Я люблю тебя 4 (01-10-35)','I Love You 4 (01-10-35)',1,'205 х 140 мм',3,0,3,8,8,8,1,215,150,'МиниМягкийГоризонтальный_M_',205,140,5,5,1,5,'','','\0');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `answer` longtext,
  `date` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `publ` bit(1) NOT NULL,
  `question` longtext NOT NULL,
  `category` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKBA823BE65391519B` (`category`),
  CONSTRAINT `FKBA823BE65391519B` FOREIGN KEY (`category`) REFERENCES `questioncategory` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'Ответ\n1\n2\n3','2011-12-27 16:41:00','andreys82@list.ru','Андрей','','Есть вопрос\n1\n2\n3',1),(2,NULL,'2011-12-27 16:44:41','andreys82@list.ru','1','\0','3',NULL),(3,'Ответ','2012-04-16 14:16:00','minogin@gmail.com','Андрей','','Вопрос по поводу фотокниг',1),(4,NULL,'2012-05-16 13:16:43','minogin@gmail.com','Вова','\0','У меня есть вопрос!!!\nУ меня есть вопрос!!!\nУ меня есть вопрос!!!',2);
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questioncategory`
--

DROP TABLE IF EXISTS `questioncategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `questioncategory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questioncategory`
--

LOCK TABLES `questioncategory` WRITE;
/*!40000 ALTER TABLE `questioncategory` DISABLE KEYS */;
INSERT INTO `questioncategory` VALUES (1,'Категория 1',100),(2,'Категория 2',200);
/*!40000 ALTER TABLE `questioncategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `country_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC84826F42E786441` (`country_id`),
  CONSTRAINT `FKC84826F42E786441` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` int(11) NOT NULL,
  `date` datetime DEFAULT NULL,
  `state` int(11) NOT NULL,
  `paid` tinyint(1) NOT NULL,
  `total` int(11) NOT NULL DEFAULT '0',
  `bill_number` varchar(255) DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL,
  `total2` int(11) NOT NULL DEFAULT '0',
  `confirmed` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK358076B4E86F89` (`user`),
  CONSTRAINT `FK358076B4E86F89` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (3,4,1),(74,5,1),(78,3,1);
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_datastore`
--

DROP TABLE IF EXISTS `s_datastore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_datastore` (
  `id` varchar(255) NOT NULL,
  `object` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_datastore`
--

LOCK TABLES `s_datastore` WRITE;
/*!40000 ALTER TABLE `s_datastore` DISABLE KEYS */;
INSERT INTO `s_datastore` VALUES ('pricingData','{\"Кформпру_3\":\"q:2.0\",\"Кформпру_4\":\"q:2.85\",\"Кформпру_1\":\"q:1.4\",\"Кформпру_2\":\"q:1.7\",\"КИ21\":\"q:2.0\",\"КИ22\":\"q:1.7\",\"Кформпру_5\":\"q:4.0\",\"Кцоблпру_12\":\"q:2.0\",\"Кцоблпру_11\":\"q:2.0\",\"Кцоблкож_52\":\"q:1.0\",\"Кламобмп_1\":\"q:1.1\",\"Кцоблкож_51\":\"q:1.0\",\"Кцоблкож_12\":\"q:1.0\",\"Кцоблкож_11\":\"q:1.0\",\"Кламстр_1\":\"q:1.0\",\"Кламстр_2\":\"q:1.1\",\"КИ12\":\"q:2.0\",\"КИ11\":\"q:1.6\",\"Кцоблпру_51\":\"q:1.0\",\"Кцоблпру_52\":\"q:1.0\",\"class\":\"imagebook.PricingData\",\"ПоздравительныйСебестоимость\":\"q:34.0\",\"Кцоблкож_61\":\"q:1.0\",\"Цплотт_1\":\"q:200.0\",\"Цфальц_1\":\"q:10.0\",\"TAPPH\":\"q:34.0\",\"КИ1\":\"q:3.0\",\"IB_1\":\"q:0.0\",\"Цламоблм_1\":\"q:30.0\",\"КИ4\":\"q:3.0\",\"IB_4\":\"q:30.0\",\"IB_5\":\"q:33.0\",\"КИ2\":\"q:1.5\",\"IB_2\":\"q:18.0\",\"КИ3\":\"q:1.1\",\"IB_3\":\"q:24.0\",\"Кцоблпру_61\":\"q:1.0\",\"Цстоб_1\":\"q:100.0\",\"Кформ_1\":\"q:1.4\",\"Кформплотт_1\":\"q:1.4\",\"Кформплотт_2\":\"q:1.7\",\"Кформ_2\":\"q:1.7\",\"Кформплотт_3\":\"q:2.0\",\"Кформплотт_4\":\"q:2.85\",\"Кформ_5\":\"q:4.0\",\"Кформ_3\":\"q:2.0\",\"Кформ_4\":\"q:2.85\",\"IB_9\":\"q:50.0\",\"IB_8\":\"q:39.0\",\"IB_7\":\"q:37.0\",\"IB_6\":\"q:35.0\",\"Кформплотт_5\":\"q:4.0\",\"IB_EVERFLAT_1\":\"q:0.0\",\"ПоздравительныйСтоимость\":\"q:350.0\",\"Цпру_1\":\"q:20.0\",\"Кформстоб_3\":\"q:2.0\",\"Кформстоб_4\":\"q:2.85\",\"Кформстоб_5\":\"q:4.0\",\"IB_EVERFLAT_9\":\"q:50.0\",\"Ца3_2\":\"q:55.0\",\"IB_EVERFLAT_8\":\"q:39.0\",\"Ца3_3\":\"q:24.0\",\"Кформмп_1\":\"q:1.0\",\"IB_EVERFLAT_7\":\"q:37.0\",\"Кформмп_2\":\"q:1.0\",\"Кламоб_1\":\"q:1.1\",\"Кформстоб_1\":\"q:1.4\",\"IB_EVERFLAT_6\":\"q:35.0\",\"TAPC\":\"q:50.0\",\"Кформмп_3\":\"q:1.0\",\"Кформстоб_2\":\"q:1.7\",\"IB_EVERFLAT_5\":\"q:33.0\",\"IB_EVERFLAT_4\":\"q:30.0\",\"Цкож_1\":\"q:300.0\",\"IB_EVERFLAT_3\":\"q:24.0\",\"IB_EVERFLAT_2\":\"q:18.0\",\"Ца3_1\":\"q:45.0\"}');
/*!40000 ALTER TABLE `s_datastore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `settings`
--

DROP TABLE IF EXISTS `settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `settings`
--

LOCK TABLES `settings` WRITE;
/*!40000 ALTER TABLE `settings` DISABLE KEYS */;
INSERT INTO `settings` VALUES (1);
/*!40000 ALTER TABLE `settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_banner`
--

DROP TABLE IF EXISTS `site_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` longtext,
  `url` varchar(255) DEFAULT NULL,
  `target_blank` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_banner`
--

LOCK TABLES `site_banner` WRITE;
/*!40000 ALTER TABLE `site_banner` DISABLE KEYS */;
INSERT INTO `site_banner` VALUES (1,'Баннер 1','Баннер 1 заг.','<p>\n	Скачайте программу</p>\n','/download','\0'),(3,'Баннер 2','Баннер 2 заг.','<p>\n	Альбом</p>\n','http://google.com','');
/*!40000 ALTER TABLE `site_banner` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_dir_section_1`
--

DROP TABLE IF EXISTS `site_dir_section_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_dir_section_1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_dir_section_1`
--

LOCK TABLES `site_dir_section_1` WRITE;
/*!40000 ALTER TABLE `site_dir_section_1` DISABLE KEYS */;
INSERT INTO `site_dir_section_1` VALUES (1,100,'cover','Фотокниги по виду обложки'),(2,200,'format','Фотокниги по форматам');
/*!40000 ALTER TABLE `site_dir_section_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_dir_section_2`
--

DROP TABLE IF EXISTS `site_dir_section_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_dir_section_2` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `index` int(11) DEFAULT NULL,
  `key` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `preview` longtext,
  `section_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`),
  KEY `FKFDDB412ECE21415A` (`section_id`),
  CONSTRAINT `FKFDDB412ECE21415A` FOREIGN KEY (`section_id`) REFERENCES `site_dir_section_1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_dir_section_2`
--

LOCK TABLES `site_dir_section_2` WRITE;
/*!40000 ALTER TABLE `site_dir_section_2` DISABLE KEYS */;
INSERT INTO `site_dir_section_2` VALUES (1,100,'hard_white','Твердая обложка (с белыми полями)','Твердая обложка (с белыми полями) текст',1),(2,200,'hard_full','Твердая обложка (с полной запечаткой)','<p>Твердая обложка (с полной запечаткой) текст<br></p>',1),(3,100,'mini_hor','МиниАльбом вертикальный (14х20,5)','<p>МиниАльбом вертикальный (14х20,5) текст<br></p>',2);
/*!40000 ALTER TABLE `site_dir_section_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_dir_section_2__album`
--

DROP TABLE IF EXISTS `site_dir_section_2__album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_dir_section_2__album` (
  `dir_section_2_id` int(11) NOT NULL,
  `album_id` int(11) NOT NULL,
  PRIMARY KEY (`dir_section_2_id`,`album_id`),
  KEY `FKE5B58DA1EA44547A` (`dir_section_2_id`),
  KEY `FKE5B58DA1BC609607` (`album_id`),
  CONSTRAINT `FKE5B58DA1BC609607` FOREIGN KEY (`album_id`) REFERENCES `album` (`id`),
  CONSTRAINT `FKE5B58DA1EA44547A` FOREIGN KEY (`dir_section_2_id`) REFERENCES `site_dir_section_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_dir_section_2__album`
--

LOCK TABLES `site_dir_section_2__album` WRITE;
/*!40000 ALTER TABLE `site_dir_section_2__album` DISABLE KEYS */;
INSERT INTO `site_dir_section_2__album` VALUES (1,10),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,48),(2,18),(2,19),(3,23),(3,33);
/*!40000 ALTER TABLE `site_dir_section_2__album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_document`
--

DROP TABLE IF EXISTS `site_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_document` (
  `id` int(11) NOT NULL,
  `folder_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5E156313B94596DE` (`id`),
  KEY `FK5E1563139557B06E` (`folder_id`),
  CONSTRAINT `FK5E1563139557B06E` FOREIGN KEY (`folder_id`) REFERENCES `site_folder` (`id`),
  CONSTRAINT `FK5E156313B94596DE` FOREIGN KEY (`id`) REFERENCES `site_page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_document`
--

LOCK TABLES `site_document` WRITE;
/*!40000 ALTER TABLE `site_document` DISABLE KEYS */;
INSERT INTO `site_document` VALUES (26,4),(28,5),(29,6);
/*!40000 ALTER TABLE `site_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_folder`
--

DROP TABLE IF EXISTS `site_folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_folder` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_folder`
--

LOCK TABLES `site_folder` WRITE;
/*!40000 ALTER TABLE `site_folder` DISABLE KEYS */;
INSERT INTO `site_folder` VALUES (4,'Каталог'),(5,'Флэш для альбомов'),(6,'Системные');
/*!40000 ALTER TABLE `site_folder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_page`
--

DROP TABLE IF EXISTS `site_page`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_page` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `h1` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `content` longtext,
  `wide` bit(1) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `target_blank` bit(1) NOT NULL,
  `footer` longtext,
  `tag_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`),
  KEY `FKE034B087FE8A2AAC` (`tag_id`),
  CONSTRAINT `FKE034B087FE8A2AAC` FOREIGN KEY (`tag_id`) REFERENCES `site_tag` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_page`
--

LOCK TABLES `site_page` WRITE;
/*!40000 ALTER TABLE `site_page` DISABLE KEYS */;
INSERT INTO `site_page` VALUES (1,'index','Главная',NULL,NULL,NULL,NULL,'<p>\n	Главная текст</p>\n<p>\n	{twitter}</p>\n','\0',NULL,'\0',NULL,NULL),(2,'down','Скачать программу',NULL,NULL,NULL,NULL,'<p><img src=\"/var/image/aaaaaaaaaa/bunny.jpg\" _fcksavedurl=\"/var/image/aaaaaaaaaa/bunny.jpg\" alt=\"\" height=\"600\" width=\"800\"></p>','',NULL,'\0',NULL,NULL),(3,NULL,'Калькулятор цены',NULL,NULL,NULL,NULL,'<p>fffffffffffffff<br></p>','\0','http://calc.imagebook.ru','',NULL,NULL),(4,NULL,'Главная',NULL,NULL,NULL,NULL,'','\0','/','\0',NULL,NULL),(5,'about','О проекте','О проекте1',NULL,NULL,NULL,'','\0',NULL,'\0',NULL,3),(10,'doc1','Документ 111111111111',NULL,NULL,NULL,NULL,'<p>111111111111111111111111111111111111<br></p>','\0',NULL,'\0',NULL,NULL),(11,'key1','Раздел 1','Раздел 1',NULL,NULL,NULL,'<p>\n	#{flash(\'VAMV89WB\', \'\', \'\', true)}</p>\n','\0',NULL,'\0',NULL,2),(12,'2','Раздел 2','1','3',NULL,NULL,'<p>1</p>\n<p>2</p>\n<p>3</p>\n<p>4</p>','\0',NULL,'\0','4',2),(13,'key11','Раздел 1.1','Раздел 1.1',NULL,NULL,NULL,'<p>Раздел 1.1<br></p>','\0',NULL,'\0',NULL,NULL),(15,'key12','Раздел 1.2','Раздел 1.2',NULL,NULL,NULL,'<p>Раздел 1.2<br></p>','\0',NULL,'\0',NULL,NULL),(21,'books','Фотокниги',NULL,NULL,NULL,NULL,'','\0',NULL,'\0',NULL,3),(22,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'\0',NULL,'\0',NULL,NULL),(26,'cover','Виды обложек','Виды обложек',NULL,NULL,NULL,'<p>Виды обложек</p>','\0',NULL,'\0',NULL,NULL),(28,'dir_flash_10','Элит панорамный — твердая «белая» обложка (01-01-02)',NULL,NULL,NULL,NULL,'<p>\n	44444</p>\n<p>\n	#{flash(&#39;VAMV89WB&#39;, &#39;&#39;, &#39;&#39;, true)}</p>\n','\0',NULL,'\0',NULL,NULL),(29,'404','404','404',NULL,NULL,NULL,'<p>\n	404</p>\n','\0',NULL,'\0',NULL,NULL);
/*!40000 ALTER TABLE `site_page` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_phrase`
--

DROP TABLE IF EXISTS `site_phrase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_phrase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(255) DEFAULT NULL,
  `value` longtext,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_phrase`
--

LOCK TABLES `site_phrase` WRITE;
/*!40000 ALTER TABLE `site_phrase` DISABLE KEYS */;
INSERT INTO `site_phrase` VALUES (1,'телефон','<p>514-54-36</p>','1');
/*!40000 ALTER TABLE `site_phrase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_section`
--

DROP TABLE IF EXISTS `site_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_section` (
  `id` int(11) NOT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `level` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  `hidden` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB8E0CDED5106856A` (`id`),
  KEY `FKB8E0CDEDB39B9681` (`parent_id`),
  CONSTRAINT `FKB8E0CDED5106856A` FOREIGN KEY (`id`) REFERENCES `site_page` (`id`),
  CONSTRAINT `FKB8E0CDEDB39B9681` FOREIGN KEY (`parent_id`) REFERENCES `site_section` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_section`
--

LOCK TABLES `site_section` WRITE;
/*!40000 ALTER TABLE `site_section` DISABLE KEYS */;
INSERT INTO `site_section` VALUES (1,NULL,0,100,'\0'),(4,1,1,100,'\0'),(5,1,1,200,'\0'),(11,5,2,100,'\0'),(12,5,2,200,'\0'),(13,11,3,100,'\0'),(15,11,3,200,'\0'),(21,1,1,300,'\0');
/*!40000 ALTER TABLE `site_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_tag`
--

DROP TABLE IF EXISTS `site_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_tag`
--

LOCK TABLES `site_tag` WRITE;
/*!40000 ALTER TABLE `site_tag` DISABLE KEYS */;
INSERT INTO `site_tag` VALUES (1,'Тег 1'),(2,'Тег 2'),(3,'Тег 3');
/*!40000 ALTER TABLE `site_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_top_section`
--

DROP TABLE IF EXISTS `site_top_section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_top_section` (
  `id` int(11) NOT NULL,
  `number` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC00B4D035106856A` (`id`),
  CONSTRAINT `FKC00B4D035106856A` FOREIGN KEY (`id`) REFERENCES `site_page` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_top_section`
--

LOCK TABLES `site_top_section` WRITE;
/*!40000 ALTER TABLE `site_top_section` DISABLE KEYS */;
INSERT INTO `site_top_section` VALUES (2,100),(3,200);
/*!40000 ALTER TABLE `site_top_section` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `status_request`
--

DROP TABLE IF EXISTS `status_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `status_request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user` int(11) NOT NULL,
  `request` longtext,
  `state` int(11) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKFA1E5742B4E86F89` (`user`),
  CONSTRAINT `FKFA1E5742B4E86F89` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `status_request`
--

LOCK TABLES `status_request` WRITE;
/*!40000 ALTER TABLE `status_request` DISABLE KEYS */;
/*!40000 ALTER TABLE `status_request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `locale` varchar(255) NOT NULL,
  `invitation_state` int(11) DEFAULT NULL,
  `info` longtext,
  `level` int(11) DEFAULT NULL,
  `settings_id` int(11) DEFAULT NULL,
  `register_type` int(11) NOT NULL,
  `vendor_id` int(11) NOT NULL,
  `discountPc` int(11) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`,`vendor_id`),
  UNIQUE KEY `settings_id` (`settings_id`),
  KEY `FK36EBCB92FF414D` (`settings_id`),
  KEY `FK36EBCB37F13E07` (`vendor_id`),
  CONSTRAINT `FK36EBCB92FF414D` FOREIGN KEY (`settings_id`) REFERENCES `settings` (`id`),
  CONSTRAINT `user_ibfk_1` FOREIGN KEY (`vendor_id`) REFERENCES `vendor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','','Администратор','','','',300,NULL,2,1,1,1,10,'2010-11-28 14:37:27');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `active` bit(1) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK14C321B9EC768806` (`user`),
  CONSTRAINT `FK14C321B9EC768806` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (2,'admin','$2a$10$NjfRGAiFJw7JuAUx5smj/.xXoRYboZllMd1o6v.EF5eZ9wrS1BYs2','',1),(55,'common','$2a$10$NjfRGAiFJw7JuAUx5smj/.xXoRYboZllMd1o6v.EF5eZ9wrS1BYs2','',1);
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor`
--

DROP TABLE IF EXISTS `vendor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `customerId` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `companyName` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `site` varchar(255) NOT NULL,
  `color` varchar(255) NOT NULL,
  `adminEmail` varchar(255) NOT NULL,
  `receiver` varchar(255) NOT NULL,
  `inn` varchar(255) NOT NULL,
  `kpp` varchar(255) DEFAULT NULL,
  `account` varchar(255) NOT NULL,
  `corrAccount` varchar(255) NOT NULL,
  `bank` varchar(255) NOT NULL,
  `bik` varchar(255) NOT NULL,
  `key` varchar(255) NOT NULL,
  `roboLogin` varchar(255) DEFAULT NULL,
  `roboPassword1` varchar(255) DEFAULT NULL,
  `smsFrom` varchar(255) DEFAULT NULL,
  `roboPassword2` varchar(255) DEFAULT NULL,
  `printer` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendor`
--

LOCK TABLES `vendor` WRITE;
/*!40000 ALTER TABLE `vendor` DISABLE KEYS */;
INSERT INTO `vendor` VALUES (1,'Imagebook',NULL,1,'ООО «ИМИДЖБУК»','imagebook@mailinator.com','(+7 495) 111-22-33','test.imagebook.ru','0000ff','admin-imagebook@mailinator.com','ООО «ИМИДЖБУК»','0123456789','012345678','40702810100001111111','30101810400000000111','Московский филиал ЗАО «Райффайзенбанк» г.Москва','01234567','imagebook','imagebook','passwd1','Imagebook','passwd2',0),(2,'Book2Look','100',2,'ЗАО \"БукТуЛук\"','book2look@mailinator.com','111-22-33','book2look.ru','ff0000','admin-book2look@mailnator.com','получатель','инн','кпп','счет','коррсчет','банк','бик','book2look','логин','пароль1','Book2Look','пароль2',0),(3,'Типография АПР','101',2,'ООО \"АПР\"','apr@mailnator.com','+74951112233','apr.ru','00ff00','admin-apr@mailnator.com','1','2','3','4','5','6','7','apr','8','9','11','10',1);
/*!40000 ALTER TABLE `vendor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weight_param_bb`
--

DROP TABLE IF EXISTS `weight_param_bb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weight_param_bb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bb` int(11) DEFAULT NULL,
  `kvpru` double DEFAULT NULL,
  `kvtobl` double DEFAULT NULL,
  `kpanoramn` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weight_param_bb`
--

LOCK TABLES `weight_param_bb` WRITE;
/*!40000 ALTER TABLE `weight_param_bb` DISABLE KEYS */;
INSERT INTO `weight_param_bb` VALUES (1,1,0,1,1.13),(2,2,0,1,1.13),(3,4,0,1,1),(4,5,0,1,1),(5,7,1,0,1),(6,8,0,0,1),(7,9,0,0,1),(8,10,0,0,1),(9,0,0,0,0),(11,0,0,0,0),(12,0,0,0,0),(13,0,0,0,0);
/*!40000 ALTER TABLE `weight_param_bb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weight_param_cc`
--

DROP TABLE IF EXISTS `weight_param_cc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weight_param_cc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cc` int(11) DEFAULT NULL,
  `vpru` double DEFAULT NULL,
  `vstr1_200` double DEFAULT NULL,
  `vstr2_250` double DEFAULT NULL,
  `vtobl` double DEFAULT NULL,
  `vupak` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weight_param_cc`
--

LOCK TABLES `weight_param_cc` WRITE;
/*!40000 ALTER TABLE `weight_param_cc` DISABLE KEYS */;
INSERT INTO `weight_param_cc` VALUES (1,0,0,2.2,0,75,50),(2,1,25,3.3,4.13,100,75),(3,2,25,3.3,4.13,100,75),(4,3,37.5,4.17,5.3,150,100),(5,4,43.75,5.45,6.62,175,110),(6,5,43.75,5.45,6.62,175,110),(7,6,50,6.27,7.68,200,125),(8,7,50,6.27,7.68,200,125),(9,8,75,8.79,11,300,150),(10,9,100,11.72,14.5,400,200),(11,10,100,11.72,14.5,400,200),(13,99,25,3.3,4.13,100,75),(15,0,0,0,0,0,0),(16,0,0,0,0,0,0),(17,10,3,4,50,2,6);
/*!40000 ALTER TABLE `weight_param_cc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weight_param_g`
--

DROP TABLE IF EXISTS `weight_param_g`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weight_param_g` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `g` int(11) DEFAULT NULL,
  `kvlam` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weight_param_g`
--

LOCK TABLES `weight_param_g` WRITE;
/*!40000 ALTER TABLE `weight_param_g` DISABLE KEYS */;
INSERT INTO `weight_param_g` VALUES (1,0,1),(2,1,1.42),(3,2,1.35),(4,0,0),(5,0,0),(6,15,3000);
/*!40000 ALTER TABLE `weight_param_g` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zone`
--

DROP TABLE IF EXISTS `zone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zone` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `district` longtext,
  `region` varchar(255) DEFAULT NULL,
  `zip` longtext NOT NULL,
  `zone` varchar(255) DEFAULT NULL,
  `country_id` int(11) NOT NULL,
  `region_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3923AC2E786441` (`country_id`),
  KEY `FK3923ACBDCF9613` (`region_id`),
  CONSTRAINT `FK3923AC2E786441` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`),
  CONSTRAINT `FK3923ACBDCF9613` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zone`
--

LOCK TABLES `zone` WRITE;
/*!40000 ALTER TABLE `zone` DISABLE KEYS */;
/*!40000 ALTER TABLE `zone` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-06-27 21:54:11
