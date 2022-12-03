CREATE DATABASE  IF NOT EXISTS `cars_db` /*!40100 DEFAULT CHARACTER SET utf8mb4  */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cars_db`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cars_db
-- ------------------------------------------------------
-- Server version	8.0.20

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
-- Table structure for table `brands`
--

DROP TABLE IF EXISTS `brands`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `brands` (
  `name` varchar(40) NOT NULL,
  `brand_id` bigint NOT NULL AUTO_INCREMENT,
  `logo` text,
  PRIMARY KEY (`brand_id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brands`
--

LOCK TABLES `brands` WRITE;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` VALUES ('SUZUKI',1,'https://imgs.search.brave.com/H6imnvKe0KpSdRjVDB4H3wlbosS8FBrPy_HdIoElZ-4/rs:fit:474:225:1/g:ce/aHR0cHM6Ly90c2U0/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5R/aWpuTmNvRWY5R0Ra/NUdkSFVTLVpRSGFI/YSZwaWQ9QXBp'),('VOLKSWAGEN',2,'https://imgs.search.brave.com/HTf3axcovBtAZcADKxKmlB5nK64KJGpsxOV-AMR05bE/rs:fit:632:225:1/g:ce/aHR0cHM6Ly90c2U0/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5h/V280bXRzemtpNlQt/eEY3LWZkNWhRSGFG/aiZwaWQ9QXBp'),('HYUNDAI',3,'https://imgs.search.brave.com/6hADfyizs3TfGb9etca2egV1lCcvVPEhHURXeeAxW9Y/rs:fit:844:225:1/g:ce/aHR0cHM6Ly90c2Ux/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC55/bVdfOFR0Z1BOa0M1/cDBKMTl0UTNRSGFF/SyZwaWQ9QXBp'),('SKODA',6,'https://imgs.search.brave.com/LsIHRS_ViBaMHuA94u96i5m6csGlWgJJVRjT7qn2zK4/rs:fit:474:225:1/g:ce/aHR0cHM6Ly90c2Uz/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC4y/X0c1QWJEQnZDbFVL/dGctOWRjRHh3SGFI/YSZwaWQ9QXBp');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_model`
--

DROP TABLE IF EXISTS `car_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_model` (
  `model_id` bigint NOT NULL AUTO_INCREMENT,
  `model_name` varchar(40) NOT NULL,
  `brand_id` bigint NOT NULL,
  PRIMARY KEY (`model_id`),
  KEY `brand_id_idx` (`brand_id`),
  CONSTRAINT `brand_id` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`brand_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_model`
--

LOCK TABLES `car_model` WRITE;
/*!40000 ALTER TABLE `car_model` DISABLE KEYS */;
INSERT INTO `car_model` VALUES (4,'IGNIS',1),(5,'SWIFT',1),(6,'S-CROSS',1),(7,'i-10',3),(8,'i-30',3),(9,'KONA',3),(10,'POLO',2),(11,'GOLF',2),(12,'T-CROSS',2),(13,'FABIA',6),(14,'OCTAVIA',6),(15,'KAROQ',6);
/*!40000 ALTER TABLE `car_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL,
  `address` varchar(50) NOT NULL,
  `dept` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'penelope','patision 60, Athens',0),(2,'jonathan','address 45, city2',0),(3,'John','papagou 78, Thessaloniki',0),(4,'paul','lincoln 43, Seattle',0),(5,'lola','St. gerand 84, Madrid',0),(6,'Lewis','Cherchill 65, West London',0);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `engine`
--

DROP TABLE IF EXISTS `engine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `engine` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `power` varchar(5) NOT NULL,
  `type` enum('DIESEL','PETROL','HYBRID','PLUG_IN_HYBRID','LPG','ELECTRIC','HYDROGEN') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `engine`
--

LOCK TABLES `engine` WRITE;
/*!40000 ALTER TABLE `engine` DISABLE KEYS */;
INSERT INTO `engine` VALUES (1,'1.2L ΒΕΝΖΙΝΗ DUALJET + HYBRID 12V','83','HYBRID'),(3,'ΚΙΝΗΤΗΡΑΣ 1.4L HYBRID 48V','129','HYBRID'),(4,'Βενζινοκινητήρας 1.0L Mpi','67','PETROL'),(5,'Βενζινοκινητήρας 1.2L','84','PETROL'),(6,'Κινητήρας 1.0 T-GDi','100','PETROL'),(7,'Βενζινοκινητήρας 1.5 DPi','110','PETROL'),(8,'Βενζινοκινητήρας 1.0 T-GDi','120','PETROL'),(9,'Βενζινοκινητήρας 1.5 T-GDi 48V','159','HYBRID'),(10,'Βενζινοκινητήρας 1.6 T-GDi','198','PETROL'),(12,'1.0 TSI','95','PETROL'),(13,'1.5 TSI ACT','130','PETROL'),(14,'2.0 TDI SCR','150','DIESEL'),(15,'1.0 eTSI Mild Hybrid','110','HYBRID'),(16,'1.5 eTSI ACT Mild Hybrid','150','HYBRID'),(17,'Βενζινοκινητήρας 1.0 T-GDi 48V','120','HYBRID'),(18,'1.4 eHybrid Plug-In-Hybrid','150','PLUG_IN_HYBRID'),(19,'2.0 TSI','245','PETROL'),(20,'2.0 TDI','200','DIESEL'),(21,'1.0 TSI','110','PETROL'),(22,'1.5 TSI','150','PETROL'),(38,'1.0 MPI','80','PETROL'),(42,'1.0 TSI','110','PETROL');
/*!40000 ALTER TABLE `engine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `engine_provided_with_gearbox`
--

DROP TABLE IF EXISTS `engine_provided_with_gearbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `engine_provided_with_gearbox` (
  `engine_id` bigint NOT NULL,
  `gearbox_id` bigint NOT NULL,
  `model_version_id` bigint NOT NULL,
  `price` double NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `gearbox_id_idx` (`gearbox_id`),
  KEY `engine_id_idx` (`engine_id`),
  CONSTRAINT `engine_id` FOREIGN KEY (`engine_id`) REFERENCES `engine` (`id`),
  CONSTRAINT `gearbox_id` FOREIGN KEY (`gearbox_id`) REFERENCES `gearbox` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `engine_provided_with_gearbox`
--

LOCK TABLES `engine_provided_with_gearbox` WRITE;
/*!40000 ALTER TABLE `engine_provided_with_gearbox` DISABLE KEYS */;
INSERT INTO `engine_provided_with_gearbox` VALUES (1,1,1,14680,1),(1,1,2,16180,2),(1,1,3,17680,3),(1,2,1,14680,4),(1,2,3,17680,5),(1,2,2,16180,6),(1,3,2,16180,7),(1,3,3,17680,8),(1,3,1,14680,9),(1,1,4,17380,10),(1,1,5,15880,11),(1,1,6,14380,12),(1,2,6,14380,13),(1,2,5,15880,14),(1,2,4,17380,15),(1,3,4,17380,16),(1,3,5,15880,17),(1,3,6,14380,18),(3,4,7,21730,19),(3,4,8,23930,20),(3,4,9,26680,21),(3,5,9,26680,22),(3,5,8,23930,23),(3,5,7,21730,24),(4,7,10,13874,25),(4,6,10,13874,26),(5,6,10,14351,27),(5,7,10,14351,28),(5,7,11,16423,29),(6,7,11,16868,30),(7,8,12,16366,31),(8,8,12,17252,32),(17,9,12,17252,33),(17,10,12,17252,34),(9,9,12,18411,35),(9,10,12,18411,36),(9,9,13,22451,37),(9,10,13,22451,38),(8,8,14,20232,39),(8,10,14,20232,40),(17,9,14,20232,41),(10,10,14,23452,42),(10,11,14,24356,43),(10,10,15,22848,44),(10,11,15,25123,45),(12,12,16,21113,46),(12,13,16,22713,47),(12,12,17,18909,48),(12,13,17,20327,49),(18,15,18,43276,50),(13,14,18,25693,51),(16,13,18,28604,52),(18,15,19,45735,53),(19,13,20,35459,54),(20,13,21,35051,55),(12,12,22,20072,56),(21,14,22,20609,57),(21,13,22,22117,58),(21,14,23,22117,59),(21,13,23,23518,60),(22,13,23,24980,61),(38,19,26,14990,64),(38,19,27,16600,65),(42,22,27,16600,68),(42,23,27,17254,69),(42,22,28,26150,72);
/*!40000 ALTER TABLE `engine_provided_with_gearbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gearbox`
--

DROP TABLE IF EXISTS `gearbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gearbox` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `gears` int NOT NULL,
  `type` enum('MANUAL','AUTOMATIC') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gearbox`
--

LOCK TABLES `gearbox` WRITE;
/*!40000 ALTER TABLE `gearbox` DISABLE KEYS */;
INSERT INTO `gearbox` VALUES (1,'SUZUKI CVT',6,'AUTOMATIC'),(2,'SUZUKI M',5,'MANUAL'),(3,'SUZUKI M ALLGRIP AUTO',5,'MANUAL'),(4,'SUZUKI 2WD',6,'MANUAL'),(5,'SUZUKI 4WD',6,'MANUAL'),(6,'HYUNDAI AMT',5,'AUTOMATIC'),(7,'HYUNDAI M',5,'MANUAL'),(8,'HYUNDAI M6',6,'MANUAL'),(9,'HYUNDAI M iMT',6,'MANUAL'),(10,'HYUNDAI DCT',7,'AUTOMATIC'),(11,'HYUNDAI DCT 4WD',7,'AUTOMATIC'),(12,'VW M ',5,'MANUAL'),(13,'VW DSG',7,'AUTOMATIC'),(14,'VW M6',6,'MANUAL'),(15,'VW DSG 6',6,'AUTOMATIC'),(19,'SKODA M5',5,'MANUAL'),(22,'SKODA M6',6,'MANUAL'),(23,'SKODA DSG',7,'AUTOMATIC');
/*!40000 ALTER TABLE `gearbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `model_version`
--

DROP TABLE IF EXISTS `model_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `model_version` (
  `version_id` bigint NOT NULL AUTO_INCREMENT,
  `version_name` varchar(40) NOT NULL,
  `model_id` bigint NOT NULL,
  `fiveDoorsAvail` tinyint DEFAULT '1',
  `threeDoorsAvail` tinyint DEFAULT '0',
  `image` text,
  `colors` json DEFAULT NULL,
  PRIMARY KEY (`version_id`),
  KEY `model_id_idx` (`model_id`),
  CONSTRAINT `model_id` FOREIGN KEY (`model_id`) REFERENCES `car_model` (`model_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `model_version`
--

LOCK TABLES `model_version` WRITE;
/*!40000 ALTER TABLE `model_version` DISABLE KEYS */;
INSERT INTO `model_version` VALUES (1,'GL',4,1,0,'https://auto.suzuki.gr/photo/master/202102/v62ulg1613048529420.png','[\"Flame Orange Pearl Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\", \"Speedy Blue Metallic\", \"Tough khaki Pearl Metallic\"]'),(2,'GL+',4,1,0,'https://auto.suzuki.gr/photo/master/202102/hhe5b41613048548256.png','[\"Flame Orange Pearl Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\", \"Speedy Blue Metallic\", \"Tough khaki Pearl Metallic\"]'),(3,'GLX',4,1,0,'https://auto.suzuki.gr/photo/master/202102/ozwyfb1613048585119.png','[\"Flame Orange Pearl Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\", \"Speedy Blue Metallic\", \"Tough khaki Pearl Metallic\"]'),(4,'GLX',5,1,0,'https://auto.suzuki.gr/photo/master/202102/two5u1613059614616.png','[\"Burning Red Pearl Metallic\", \"Speedy Blue Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\"]'),(5,'GL+',5,1,0,'https://auto.suzuki.gr/photo/master/202102/567r1613060696817.png','[\"Burning Red Pearl Metallic\", \"Speedy Blue Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\"]'),(6,'GL',5,1,0,'https://auto.suzuki.gr/photo/master/202102/y2gc4n1613059589892.png','[\"Burning Red Pearl Metallic\", \"Speedy Blue Metallic\", \"Pure White Pearl\", \"Premium Silver Metallic\", \"Super Black Pearl\", \"Fervent Red\"]'),(7,'GL',6,1,0,'https://auto.suzuki.gr/photo/master/202111/bva4tc1638189653059.png','[\"Solid White\", \"Cool White Pearl\", \"Prime Cosmic Black\", \"Prime Canyon Brown\", \"Pearl Sphere Blue\", \"Energetic Red Pearl\"]'),(8,'GL+',6,1,0,'https://auto.suzuki.gr/photo/master/202111/q2enb71638189671957.png','[\"Solid White\", \"Cool White Pearl\", \"Prime Cosmic Black\", \"Prime Canyon Brown\", \"Pearl Sphere Blue\", \"Energetic Red Pearl\"]'),(9,'GLX',6,1,0,'https://auto.suzuki.gr/photo/master/202111/mjejo61638189687596.png','[\"Solid White\", \"Cool White Pearl\", \"Prime Cosmic Black\", \"Prime Canyon Brown\", \"Pearl Sphere Blue\", \"Energetic Red Pearl\"]'),(10,'i10',7,1,0,'https://www.hyundai.gr/wp-content/uploads/2022/02/M3_01_AC3_top_trim_3I4front_ds_Aqua_Turquoise_U3H_rgb_4x3.jpeg','[\"Aqua Turquoise Metallic\", \"Intense Blue Pearl\", \"Sleek Silver Metallic\", \"Phantom Black Pearl\", \"Mangrove Green Pearl\", \"Aurora Gray\", \"Dragon Red Pearl\", \"Elemental Brass Metallic\"]'),(11,'i10 N Line',7,1,0,'https://www.hyundai.gr/wp-content/uploads/2022/02/M7_00_AC3_NLine_top_trim_3I4frontps_lights-on_layered_YMO_4x3.jpeg','[\"Aqua Turquoise Metallic\", \"Intense Blue Pearl\", \"Sleek Silver Metallic\", \"Phantom Black Pearl\", \"Mangrove Green Pearl\", \"Aurora Gray\", \"Dragon Red Pearl\", \"Elemental Brass Metallic\"]'),(12,'i30',8,1,0,'https://www.hyundai.gr/wp-content/uploads/2021/07/pdpe_hb_toptrim_dg03-01_ext_3-4-front_rgb_v01_w4b-cl.jpeg','[\"Silky Bronze Metallic\", \"Engine Red\", \"Phantom Black Pearl\", \"Atlas White\", \"Shimmering Silver Metallic\", \"Sunset Red Pearl\", \"Dark Knight Grey Pearl\"]'),(13,'i30 N Line',8,1,0,'https://www.hyundai.gr/wp-content/uploads/2021/07/pdpe_hb_n-line_dg03-04_ext_3-4-front_rgb_v01_w2b.jpeg','[\"Silky Bronze Metallic\", \"Engine Red\", \"Phantom Black Pearl\", \"Atlas White\", \"Shimmering Silver Metallic\", \"Sunset Red Pearl\", \"Dark Knight Grey Pearl\"]'),(14,'KONA',9,1,0,'https://www.hyundai.gr/wp-content/uploads/2021/08/hyundai_kona_14_beauty_3-4-front_4x3.jpeg','[\"Dive in Jeju\", \"Surfy Blue Metallic\", \"Ignite Red\", \"Pulse Red Pearl\", \"Atlas White\", \"Cyber Grey Metallic\"]'),(15,'KONA N Line',9,1,0,NULL,'[\"Dive in Jeju\", \"Surfy Blue Metallic\", \"Ignite Red\", \"Pulse Red Pearl\", \"Atlas White\", \"Cyber Grey Metallic\"]'),(16,'POLO Style',10,1,0,'https://r-media.volkswagen.com/v1/VW/2G0/2023/MP1/20220715/el-GR-gr/2T2T/FJ/00A,0A2,0B1,0EG,0FB,0K0,0NB,0P0,0TD,1A7,1D0,1G1,1KV,1SA,1T2,1TB,1X0,1ZE,2FE,2H0,2JH,31K,3A2,3C7,3FA,3GD,3H0,3L3,3NZ,3Q6,3QH,3U1,4A0,4AP,4BI,4I2,4KC,4L2,4N1,4P0,4R4,4UF,51A,5MJ,5RQ,5SL,5XJ,5ZF,6C2,6E3,6FF,6I2,6K4,6NA,6PC,6Q2,6YD,7J1,7L6,7M5,7P4,7UJ,7W0,7X2,7Y0,8AR,8G4,8IV,8N8,8Q3,8RL,8S6,8T8,8VP,8WH,8Z4,8ZQ,9AK,9JB,9P4,9T0,9ZX,A8F,AV1,B0A,B19,DG8,E0A,EF0,EL5,ER1,G1C,GP1,K8G,L0L,N2M,NZ4,Q4H,QH0,QJ3,QK1,QN4,QQ3,QV3,TC5,U9C,UD3,V21,VF0/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/17116a7a596cce7e4bd0a177c0109c1e6cbd876993eeae2d2571f9da90bf72b6.png','[\"Ascot Grey\", \"Pure White\", \"Reflex Silver\", \"Smoky Grey\", \"Kings Red\", \"Reef Blue\", \"Deep Black\"]'),(17,'POLO Life',10,1,0,'https://r-media.volkswagen.com/v1/VW/2G0/2023/MP1/20220715/el-GR-gr/2T2T/FJ/00A,0A2,0B1,0EG,0FB,0K0,0NB,0P0,0TD,1A7,1D0,1G1,1KV,1SA,1T2,1TB,1X0,1ZE,2FE,2H0,2JH,31K,3A2,3C7,3FA,3GD,3H0,3L3,3NZ,3Q6,3QH,3U1,4A0,4AP,4BI,4I2,4KC,4L2,4N1,4P0,4R4,4UF,51A,5MJ,5RQ,5SL,5XJ,5ZF,6C2,6E3,6FF,6I2,6K4,6NA,6PC,6Q2,6YD,7J1,7L6,7M5,7P4,7UJ,7W0,7X2,7Y0,8AR,8G4,8IV,8N8,8Q3,8RL,8S6,8T8,8VP,8WH,8Z4,8ZQ,9AK,9JB,9P4,9T0,9ZX,A8F,AV1,B0A,B19,DG8,E0A,EF0,EL5,ER1,G1C,GP1,K8G,L0L,N2M,NZ4,Q4H,QH0,QJ3,QK1,QN4,QQ3,QV3,TC5,U9C,UD3,V21,VF0/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/17116a7a596cce7e4bd0a177c0109c1e6cbd876993eeae2d2571f9da90bf72b6.png','[\"Ascot Grey\", \"Pure White\", \"Reflex Silver\", \"Smoky Grey\", \"Kings Red\", \"Reef Blue\", \"Deep Black\"]'),(18,'GOLF Style',11,1,0,'https://r-media.volkswagen.com/v1/VW/5H0/2023/MP1/20220923/el-GR-gr/C1C1/BD/0B1,0ED,0FA,0K0,0N4,0NB,0P0,0TD,1A5,1D0,1E5,1G9,1J2,1KE,1R0,1X0,1ZE,2CM,2F0,2FD,2H0,2JD,31K,3A2,3CA,3FA,3H0,3LT,3NU,3QH,3T2,4A0,4BI,4D0,4E0,4GF,4I7,4KC,4L6,4P0,4UF,4ZN,5C0,5F1,5JA,5MJ,5XJ,5ZF,6C2,6E3,6FF,6I2,6Q2,6T2,6XR,6Y8,7B2,7J2,7UJ,7X2,7Y0,8AR,8G0,8I2,8IY,8J3,8N6,8Q3,8RT,8T8,8TL,8VG,8WP,8Z4,8ZQ,9JA,9M0,9P4,9Z0,9ZV,A8F,AV1,B0A,C3Z,DS9,E0A,EL5,ER1,G0K,GW2,K8G,KA0,KH7,KS0,L0L,N0R,NZ4,Q4H,QJ1,QK1,QQ9,QV3,TJ7,U9E,UD2,UH2,VF0/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/bdb753386bf3f66af9c5a576c53d30695177ad94db963c8834eec08cf5ed2984.png','[\"Deep Black\", \"Urano Grey\", \"Pure White\", \"Reflex Silver\", \"Dolphin Grey\", \"Lime Yellow\", \"Kings Red\", \"Atlantic Blue\", \"Deep Black\"]'),(19,'GOLF GTE',11,1,0,'https://r-media.volkswagen.com/v1/VW/5H0/2023/MP1/20220923/el-GR-gr/H7H7/UP/0B1,0FA,0IC,0K3,0N4,0P0,0QT,0TD,1A5,1D0,1E5,1G8,1J2,1KJ,1LG,1X0,2CM,2F0,2H5,2JW,2PE,31K,3A2,3CA,3FA,3H0,3L3,3LT,3NU,3QH,3T2,4A0,4BI,4D0,4E0,4GF,4I7,4KF,4L6,4P0,4UF,4X3,4ZQ,5C0,5F1,5JA,5MR,5XJ,6E3,6FF,6I6,6Q6,6T2,6XR,6Y8,7B2,7J2,7P4,7UG,7X2,7Y0,8AR,8IU,8J3,8N6,8Q5,8RT,8T8,8TL,8VM,8WP,8Z4,8ZQ,9JA,9M0,9P4,9Z0,9ZV,A8K,AV1,B0A,C4N,E0A,EL5,ER1,G1A,GW2,K8G,KA0,KH7,KS1,L0L,ML9,N2T,NZ4,Q4P,QJ1,QK1,QQ4,QV3,TH8,U9E,UD2,UH2,VF1/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/3cd3a31a220175f95f308caf8c2fd5ee1c77289304e0071e7d2256cf6c1f6cc3.png','[\"Moonstone Grey\", \"Urano Grey\", \"Pure White\", \"Dolphin Grey\", \"Lime Yellow\", \"Kings Red\", \"Atlantic Blue\"]'),(20,'GOLF GTI',11,1,0,'https://r-media.volkswagen.com/v1/VW/5H0/2023/MP1/20220923/el-GR-gr/H7H7/UG/0B1,0FA,0IJ,0K0,0N4,0NH,0P0,0TD,1A5,1D0,1E5,1G9,1J2,1KJ,1LG,1X0,2CM,2F0,2H5,2JG,2PE,31K,3A2,3CA,3FA,3H0,3L3,3LT,3NU,3QT,3T2,4A0,4BI,4D0,4E0,4GF,4I7,4KF,4L6,4P0,4UF,4X3,4ZQ,5C0,5F1,5JA,5MR,5XJ,6E3,6FF,6I6,6Q6,6T2,6XR,6Y8,7B2,7J2,7P4,7UT,7X2,7Y0,8AR,8G0,8IY,8J3,8N6,8Q3,8RT,8T8,8TL,8VG,8WP,8Z4,8ZQ,9JA,9M0,9P4,9Z0,9ZV,A8H,AV1,B0A,C4N,DQ4,E0A,EL5,ER1,G1C,GW2,K8G,KA0,KH7,KS0,L0L,N2T,NZ4,Q4P,QJ1,QK1,QQ4,QV3,T3J,U9E,UD2,UH2,VF1/D6Turntable11/b19d7b4a-df80-487c-908a-47785dbb35fc/7256e36af7f0a5d0e43f898350c984b0d5a9d9e66a4d4b1ab53bb143cdc793ba.png?width=500','[\"Moonstone Grey\", \"Urano Grey\", \"Pure White\", \"Dolphin Grey\", \"Lime Yellow\", \"Kings Red\", \"Atlantic Blue\", \"Deep Black\"]'),(21,'GOLF GTD',11,1,0,'https://r-media.volkswagen.com/v1/VW/5H0/2023/MP1/20220923/el-GR-gr/0Q0Q/UN/0B1,0F5,0FA,0IJ,0K0,0N4,0P0,0TD,1D0,1E9,1G9,1J2,1X0,1ZB,2CM,2EC,2F0,2H5,2JQ,2PE,2RK,31K,3A2,3CA,3FA,3H0,3L3,3LT,3NU,3QT,3T2,4A0,4BI,4D0,4E0,4GF,4I7,4KF,4L6,4P0,4UF,4X3,4ZQ,5C0,5F1,5JA,5MR,5XJ,6E3,6FF,6I6,6Q6,6T2,6XR,6Y8,7B2,7J2,7P4,7UT,7X2,7Y0,8AR,8G0,8IY,8J3,8N6,8Q3,8RT,8T8,8TL,8VG,8WP,8Z4,8ZQ,9JA,9M0,9P4,9Z0,9ZV,A8G,AV1,B0A,C4N,E0A,EL5,ER1,G1C,GW2,K8G,KA0,KH7,KS0,L0L,MC3,N2T,NZ4,Q4P,QJ1,QK1,QQ4,QV3,T6F,U9E,UD2,UH2,VF1/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/bf41c7ee3f88dfa28eb90c11f3ca3209186c217a1b875a72158860d7240c89a9.png','[\"Moonstone Grey\", \"Urano Grey\", \"Pure White\", \"Dolphin Grey\", \"Lime Yellow\", \"Kings Red\", \"Atlantic Blue\", \"Deep Black\", \"Oryx White\"]'),(22,'T-CROSS Life',12,1,0,'https://r-media.volkswagen.com/v1/VW/2GC/2023/MP1/20220722/el-GR-gr/6U6U/UL/00A,0A2,0B1,0EM,0FB,0K0,0N6,0NB,0P0,1A7,1D0,1KA,1T2,1X0,1ZS,2CC,2FD,2H0,2JG,31K,3A2,3C7,3FA,3GD,3H2,3L3,3NQ,3Q6,3QT,3S2,3U1,3ZU,40L,4A0,4AP,4BI,4I2,4KC,4L2,4P0,4R4,4UF,4Z5,4ZE,51A,5MB,5RQ,5SL,5XS,5ZF,6E3,6FF,6I1,6K2,6NA,6PC,6Q2,6YD,7J1,7M5,7UJ,7W0,7X0,7Y0,8AR,8ID,8K3,8N8,8Q1,8RL,8S3,8T9,8TL,8VG,8WH,8X0,8Z4,8ZQ,9AB,9E1,9JD,9P4,9ZV,A8T,AV1,B19,DI6,E0A,EF0,EL5,ER1,G0C,K8G,KA1,L0L,N2H,NZ4,Q1A,QH0,QJ0,QK1,QN2,QQ0,QV3,TC5,U5A,U9C,VF0/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/3e631aa5a4dde0f20e8c5cdf28d1a96ea99abbf6334c647ceec1729539d7f56e.png','[\"Ascot Grey\", \"Pure White\", \"Smoky Grey\", \"Energetic Orange\", \"Reef Blue\", \"Deep Black\"]'),(23,'T-CROSS Style',12,1,0,'https://r-media.volkswagen.com/v1/VW/2GC/2023/MP1/20220722/el-GR-gr/6U6U/FL/00A,0A2,0B1,0EC,0FB,0K0,0N6,0NB,0P0,0TD,1A7,1D0,1KV,1T2,1X0,1ZS,2CC,2FD,2H0,2JH,31K,3A2,3C7,3FA,3GD,3H2,3L3,3NQ,3Q6,3QT,3S1,3U1,3ZU,41B,4A0,4AP,4BI,4I2,4KC,4L2,4P0,4R4,4UF,4Z5,4ZB,51A,5MY,5RQ,5SL,5XS,5ZF,6E3,6FF,6I1,6K4,6NA,6PC,6Q2,6T1,6YD,7J1,7M5,7UJ,7W0,7X0,7Y0,8AR,8IY,8K3,8N8,8Q3,8RL,8S6,8T7,8TL,8VH,8WH,8X0,8Z4,8ZQ,9AK,9E1,9JD,9P4,9ZV,A9T,AV1,B19,E0A,EF0,EL5,ER1,G0K,K8G,KA1,L0L,N2M,NZ4,Q4H,QH0,QJ1,QK1,QN2,QQ3,QV3,TC5,U5A,U9C,VF0/D6Explore1/b19d7b4a-df80-487c-908a-47785dbb35fc/7b999003f9118412a38826eed69feac1e14888ca4a09e3841f0051edae6a6f42.png','[\"Ascot Grey\", \"Pure White\", \"Smoky Grey\", \"Energetic Orange\", \"Reef Blue\", \"Deep Black\"]'),(26,'ACTIVE',13,1,0,'https://imgs.search.brave.com/thyNjRggU9WzKOwkBbNCk0X8mpja3oLdVyG1cH-cH6w/rs:fit:964:225:1/g:ce/aHR0cHM6Ly90c2Ux/Lm1tLmJpbmcubmV0/L3RoP2lkPU9JUC5V/dExDa3UtUU12ZWRW/c0NTdjlBaUZBSGFE/cCZwaWQ9QXBp','[\"Energy-Blue\", \"Velvet red metallic\", \"Brilliant Silver Metallic\", \"Black Magic Pearlescent\", \"Race Blue Metallic\", \"Candy White\"]'),(27,'AMBITION',13,1,0,'FABIA AMBITION','[\"Phoenix orange metallic\", \"Brilliant Silver Metallic\", \"Black Magic Pearlescent\", \"Race Blue Metallic\", \"Graphite grey metallic\"]'),(28,'AMBITION',15,1,0,'none','[\"Phoenix orange metallic\", \"Energy Blue\", \"Steel Grey\", \"Graphite grey metallic\", \"Velvet red\"]');
/*!40000 ALTER TABLE `model_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cost` double NOT NULL,
  `brand_id` bigint NOT NULL,
  `model_id` bigint NOT NULL,
  `version_id` bigint NOT NULL,
  `engine_id` bigint NOT NULL,
  `type` enum('DIESEL','PETROL','HYBRID','ELECTRIC','LPG','HIDROGEN') NOT NULL,
  `gearbox_id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `color` varchar(50) NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cust_id_idx` (`customer_id`),
  CONSTRAINT `cust_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 ;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,20232,3,9,14,8,'PETROL',10,2,'Surfy Blue Metallic','2022-09-23'),(2,20072,2,12,22,12,'PETROL',12,4,'Ascot Grey','2022-09-24'),(3,17380,1,5,4,1,'HYBRID',1,2,'Super Black Pearl','2022-09-25'),(4,16366,3,8,12,7,'PETROL',8,1,'Shimmering Silver Metallic','2022-09-28'),(5,14380,1,5,6,1,'HYBRID',2,4,'Premium Silver Metallic','2022-09-28'),(6,22117,2,12,22,21,'PETROL',13,3,'Pure White','2022-09-28'),(7,35051,2,11,21,20,'DIESEL',13,2,'Pure White','2022-09-28');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` varchar(30) NOT NULL,
  `name` varchar(40) NOT NULL,
  `role` enum('ADMIN','USER') NOT NULL DEFAULT 'USER',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin','admin','chris','ADMIN'),(2,'joe','123','joe','ADMIN'),(3,'user','user','user','USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-12-03 16:55:09
