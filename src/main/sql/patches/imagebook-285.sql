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
-- Table structure for table `ddelivery_module_cache`, used by PHP SDK
--
DROP TABLE IF EXISTS `ddelivery_module_cache`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ddelivery_module_cache` (
  `id` int(11) NOT NULL,
  `data_container` mediumtext,
  `expired` datetime DEFAULT NULL,
  `filter_company` text,
  PRIMARY KEY (`id`),
  KEY `dd_cache` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `ddelivery_module_orders`, used by PHP SDK
--
DROP TABLE IF EXISTS `ddelivery_module_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ddelivery_module_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_variant` varchar(255) DEFAULT NULL,
  `shop_refnum` varchar(255) DEFAULT NULL,
  `local_status` varchar(255) DEFAULT NULL,
  `dd_status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `to_city` int(11) DEFAULT NULL,
  `point_id` int(11) DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  `ddeliveryorder_id` int(11) DEFAULT NULL,
  `delivery_company` int(11) DEFAULT NULL,
  `order_info` text,
  `cache` text,
  `point` text,
  `add_field1` varchar(255) DEFAULT NULL,
  `add_field2` varchar(255) DEFAULT NULL,
  `add_field3` varchar(255) DEFAULT NULL,
  `cart` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ddelivery_module_order`, used as bridge between java and PHP
--
DROP TABLE IF EXISTS `ddelivery_module_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ddelivery_module_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `assessedPrice` decimal(19,2) DEFAULT NULL,
  `description` text,
  `heightCm` int(11) DEFAULT NULL,
  `imagebookBillId` int(11) DEFAULT NULL,
  `imagebookOrderId` int(11) DEFAULT NULL,
  `lengthCm` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `weightKg` decimal(19,3) DEFAULT NULL,
  `widthCm` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;