-- MySQL dump 10.13  Distrib 9.0.1, for macos14.7 (x86_64)
--
-- Host: localhost    Database: globeDB
-- ------------------------------------------------------
-- Server version	9.0.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Accommodation`
--

DROP TABLE IF EXISTS `Accommodation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Accommodation` (
  `id` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Accommodation`
--

LOCK TABLES `Accommodation` WRITE;
/*!40000 ALTER TABLE `Accommodation` DISABLE KEYS */;
INSERT INTO `Accommodation` VALUES ('00397635-ffee-4c9c-be8f-e8ec736207ca','hotel prova 2','via verdi 2'),('06e99d0e-1897-4936-8969-b1d3b202382a','hotel soare','via verdi 24'),('2dcc0fb7-8798-40e3-8def-c748925fa210','hotel 2','via rossi 2'),('37c6b90c-98af-4636-8f65-551106de6ec7','alloggio CLI','via cli 1'),('3f1c8262-0ee1-4c65-b1fb-1b6225b181c2','prova alloggio cli','via cli 100'),('4290fb20-0650-4ad1-a7da-b13ee85ad39c','alloggio prova','via verdi 33'),('49d058a0-6ca3-4c39-86ae-8b7fe2b39119','afsfsa','asfjknfsaf'),('4bb2bad8-e2dc-4eda-960c-c2b9f7b23d6b','hotel 1','via 1'),('52d57de3-6626-4388-b5e2-5fe3819031ad','ads','sad'),('5767f656-3e88-4987-8db2-ba4213b05ed2','hotel di prova','Via di prova 1'),('5b7533a8-34ac-402a-80f7-359333ec4ccf','hotel california','via rossi 1'),('5ca9f555-3a56-423c-ae8e-74f9b24a14fb','af','vdz'),('5e0ca52d-00d4-4445-b612-523c8e867762','hotel prova 2','via rossi 2'),('630042fc-3ea5-47fb-807f-d7d7593b4990','fsa','fsa'),('6c07c679-4035-4826-a329-cf62fc6784d3','afsfsaf','afsasf'),('70a32aaa-8ba3-4a8a-ae5f-d481495060a1','hotel 1','via verdi 1'),('70e7922b-027a-460f-ada6-924dba6c013f','hotel prova','via prova 1'),('74fa3255-85ab-418a-a3a0-6a4cada0824a','Hotel Hilton','via verdi 23'),('7686fe03-cebf-448c-9180-25c45d59e877','asf','asf'),('7ec4aea3-edc9-415f-a9ff-1af5b827a9df','fasfasfasfsa','asffas'),('84f4fabf-ad7e-4998-b38b-36148cda38fa','Hotel PubLove','via marrano 3'),('8bb018e4-a11e-4ef6-9c7d-409888e7d3c7','prova alloggio da CLI','via cli 11'),('91c3f505-eef1-40da-bfc1-399cf6dffa34','hotel afskjnfasl','via h 1'),('9c5fc8a8-5deb-45da-a1cf-8fd631b03d4c','af','vdz'),('a2ca7bcb-b5cf-40ec-ad29-59e5d1c09b4f','hotel california','via rossi 22'),('a37a3cf6-168a-42fb-b0a3-2c4d136f8436','afssafsfa','saf'),('a4965d4b-d330-44e0-84a5-02980e26fc75','hotel alexandru','via sorburale 21'),('a826ef3a-ed51-47d3-bbb6-dbbbaf8b2fb4','hotel prova 1','via rossi 1'),('ae6cd9e9-5ea0-40da-898e-258a980c306b','hot jljkafc','via verdi 33'),('b4c1c577-b9f5-458f-956e-7a6d21dda2b7','fsa','asf'),('c9ae9c38-a8b9-4efd-a613-ef21e5e85300','hotel prova 1','via verdi 1'),('d48cb026-b3b0-477e-a796-a735868b9a98','prova di alloggio da cli','via verdi 88'),('e17f3cf3-26f3-41cd-b69f-418f462cd6a0','hotel di prova','Via di prova 1'),('f584f8d9-2ee0-4d8c-8b01-17a8f389c237','afs','afs');
/*!40000 ALTER TABLE `Accommodation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Account`
--

DROP TABLE IF EXISTS `Account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Account` (
  `username` varchar(45) NOT NULL,
  `password` varchar(45) DEFAULT NULL,
  `paymentCredential` varchar(45) DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `description` varchar(140) DEFAULT NULL,
  `type` enum('user','agency','guest') DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Account`
--

LOCK TABLES `Account` WRITE;
/*!40000 ALTER TABLE `Account` DISABLE KEYS */;
INSERT INTO `Account` VALUES ('0944c662-545','xxxxxxxx',NULL,0,NULL,'guest'),('0dfb93a6-0df','xxxxxxxx',NULL,0,NULL,'guest'),('0e57f0fc-003','xxxxxxxx',NULL,0,NULL,'guest'),('1036738a-7fd','xxxxxxxx',NULL,0,NULL,'guest'),('123','123',NULL,0,NULL,'user'),('1431ebfb-43d','xxxxxxxx',NULL,0,NULL,'guest'),('159429f6-686','xxxxxxxx',NULL,0,NULL,'guest'),('15e512f1-bd1','xxxxxxxx',NULL,0,NULL,'guest'),('17b338df-59c','xxxxxxxx',NULL,0,NULL,'guest'),('19fec310-2f4','xxxxxxxx',NULL,0,NULL,'guest'),('29a98478-0d6','xxxxxxxx',NULL,0,NULL,'guest'),('31a84d07-480','xxxxxxxx',NULL,0,NULL,'guest'),('3264182a-085','xxxxxxxx',NULL,0,NULL,'guest'),('358e0450-e23','xxxxxxxx',NULL,0,NULL,'guest'),('35f3319b-7c0','xxxxxxxx',NULL,0,NULL,'guest'),('3892193f-389','',NULL,0,NULL,'guest'),('39218384-1d1','xxxxxxxx',NULL,0,NULL,'guest'),('3a239739-656','xxxxxxxx',NULL,0,NULL,'guest'),('3b0864be-261','xxxxxxxx',NULL,0,NULL,'guest'),('3ff6ad81-2b1','xxxxxxxx',NULL,0,NULL,'guest'),('43694d00','b68d2965',NULL,0,NULL,'guest'),('4a6f829c','c646859c',NULL,0,NULL,'guest'),('4f4d9da7-f48','xxxxxxxx',NULL,0,NULL,'guest'),('55eaca59-ecf','xxxxxxxx',NULL,0,NULL,'guest'),('56c816bf-a22','xxxxxxxx',NULL,0,NULL,'guest'),('597d352c-a32','xxxxxxxx',NULL,0,NULL,'guest'),('5ff7b466-3f2','',NULL,0,NULL,'guest'),('628f7435-b7e','xxxxxxxx',NULL,0,NULL,'guest'),('673d53e1-a23','xxxxxxxx',NULL,0,NULL,'guest'),('6d42e8d2-1c4','xxxxxxxx',NULL,0,NULL,'guest'),('75ff27cc-a78','xxxxxxxx',NULL,0,NULL,'guest'),('82b8a8c1-70f','xxxxxxxx',NULL,0,NULL,'guest'),('84a743a1-afb','',NULL,0,NULL,'guest'),('857b43a0-078','xxxxxxxx',NULL,0,NULL,'guest'),('8849f365-69c','xxxxxxxx',NULL,0,NULL,'guest'),('8a485f10-6b5','xxxxxxxx',NULL,0,NULL,'guest'),('939e4c5a-d79','',NULL,0,NULL,'guest'),('955c7d5f-a72','xxxxxxxx',NULL,0,NULL,'guest'),('972ea180-221','',NULL,0,NULL,'guest'),('9ce15b4a-9ca','xxxxxxxx',NULL,0,NULL,'guest'),('9e0838d2-885','xxxxxxxx',NULL,0,NULL,'guest'),('a','aaaaaaaa','1234567890123456',0,NULL,'user'),('a1','a1',NULL,0,'description1','agency'),('a2','a2',NULL,0,'description2','agency'),('a3','a3',NULL,0,'description3','agency'),('aa07fb3f-6ef','xxxxxxxx',NULL,0,NULL,'guest'),('aa6ce8d9-bd5','xxxxxxxx',NULL,0,NULL,'guest'),('ac0c32f3-f07','xxxxxxxx',NULL,0,NULL,'guest'),('af28cc31-bc0','xxxxxxxx',NULL,0,NULL,'guest'),('asd','aaaaaaaa',NULL,0,NULL,'user'),('asdf','12345678','143241414132532',0,'adsfasfasfsafgggaagssga','agency'),('asfnjfsa','saf',NULL,0,NULL,'user'),('be9c0832-37c','xxxxxxxx',NULL,0,NULL,'guest'),('c468dbff-1ee','xxxxxxxx',NULL,0,NULL,'guest'),('d544e672-9f1','xxxxxxxx',NULL,0,NULL,'guest'),('d8f3cc0a-900','xxxxxxxx',NULL,0,NULL,'guest'),('dd143b15-241','xxxxxxxx',NULL,0,NULL,'guest'),('dd34a3db-775','xxxxxxxx',NULL,0,NULL,'guest'),('e1bd7dba-e8d','xxxxxxxx',NULL,0,NULL,'guest'),('e81aaf66-076','xxxxxxxx',NULL,0,NULL,'guest'),('ecf7ea2b-253','',NULL,0,NULL,'guest'),('f19b2793-f89','xxxxxxxx',NULL,0,NULL,'guest'),('f72a13ef-057','xxxxxxxx',NULL,0,NULL,'guest'),('f9ad59f8-4d6','xxxxxxxx',NULL,0,NULL,'guest'),('fratta','12345678','1234567890123456',0,NULL,'user'),('prova','prova',NULL,0,NULL,'user'),('prova2','2',NULL,0,NULL,'user'),('saddas','das',NULL,0,NULL,'user'),('SiVola','12345678','128456087412543',0,'Siamo la migliore agenzia di viaggi mai esistita, gestita da Human Safari','agency'),('t','TeoMio02',NULL,0,NULL,'user'),('teo','12345678','1234567890123456',0,NULL,'user'),('TeoMio','1234',NULL,0,NULL,'user'),('try','try',NULL,0,NULL,'user'),('tt','tt',NULL,0,NULL,'user');
/*!40000 ALTER TABLE `Account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountItinerary`
--

DROP TABLE IF EXISTS `accountItinerary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accountItinerary` (
  `account` varchar(45) NOT NULL,
  `itineraryID` varchar(45) NOT NULL,
  PRIMARY KEY (`account`,`itineraryID`),
  KEY `fk_itinerary_ai_idx` (`itineraryID`),
  CONSTRAINT `fk_account_ai` FOREIGN KEY (`account`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_itinerary_ai` FOREIGN KEY (`itineraryID`) REFERENCES `Itinerary` (`itineraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountItinerary`
--

LOCK TABLES `accountItinerary` WRITE;
/*!40000 ALTER TABLE `accountItinerary` DISABLE KEYS */;
INSERT INTO `accountItinerary` VALUES ('saddas','14c29c6f-e0f6-4fed-9c2b-868b47e3631c'),('SiVola','27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1'),('teo','2845356d-648d-48ca-a09a-55c1a9132940'),('SiVola','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81'),('tt','3a288717-6b41-4472-bbf1-03f6d9a3228a'),('159429f6-686','400b6042-58d4-4723-9519-96df9b7f08fe'),('teo','4aea6411-56a2-44e0-9f4d-16b3aa620daa'),('saddas','4f33164e-6bb5-44fe-9d69-83035096eff4'),('628f7435-b7e','5d143505-ff43-4850-be18-0f5b06b76db8'),('teo','6b9cb6b2-a538-418d-9649-c8ce3542880d'),('teo','6df50656-0d10-4a47-b50b-033631c332a8'),('saddas','79bcde9e-5593-46fe-8e4f-03e2c2327263'),('teo','91034a08-cbe4-4ed0-9d23-f5d2926ae9fe'),('be9c0832-37c','954e3562-f860-40b4-b3f0-a24a6d0202ec'),('t','955f08d3-c694-4a47-a9ea-6c7a7e5b39d7'),('saddas','9cd0de93-9a5a-41a6-a883-b21f1973acf2'),('teo','9e8a2d46-654e-4ff0-a983-49b4d1012757'),('SiVola','a5e7db61-2774-494c-8e54-46d1e46ca7fe'),('saddas','a7ed936f-41a5-40ff-943c-830189c37518'),('t','c4da4260-eebc-4cce-898f-192c49f7cb02'),('teo','cfd5c033-3a0d-40ee-99f5-748ff06c068c'),('teo','d06ccb96-f657-4837-9fd6-5b46921d158f'),('teo','d2a47c7e-ae6d-4604-b90d-d6654bf2fc94'),('teo','e2e21386-5333-4496-bac9-446015e7471e');
/*!40000 ALTER TABLE `accountItinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountProposal`
--

DROP TABLE IF EXISTS `accountProposal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accountProposal` (
  `account` varchar(45) NOT NULL,
  `proposalID` varchar(45) NOT NULL,
  PRIMARY KEY (`account`,`proposalID`),
  KEY `fk_proposal_ap_idx` (`proposalID`),
  CONSTRAINT `fk_account_ap` FOREIGN KEY (`account`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_proposal_ap` FOREIGN KEY (`proposalID`) REFERENCES `Proposal` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountProposal`
--

LOCK TABLES `accountProposal` WRITE;
/*!40000 ALTER TABLE `accountProposal` DISABLE KEYS */;
INSERT INTO `accountProposal` VALUES ('SiVola','78e16cc9-29af-4731-8f90-369e1b56bc77'),('teo','78e16cc9-29af-4731-8f90-369e1b56bc77'),('SiVola','80a9bdc0-2a85-4ffe-be69-64417afa7cc8'),('t','80a9bdc0-2a85-4ffe-be69-64417afa7cc8');
/*!40000 ALTER TABLE `accountProposal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `accountRequest`
--

DROP TABLE IF EXISTS `accountRequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accountRequest` (
  `account` varchar(45) NOT NULL,
  `requestID` varchar(45) NOT NULL,
  PRIMARY KEY (`account`,`requestID`),
  KEY `fk_request_ar_idx` (`requestID`),
  CONSTRAINT `fk_account_ar` FOREIGN KEY (`account`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_request_ar` FOREIGN KEY (`requestID`) REFERENCES `Request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `accountRequest`
--

LOCK TABLES `accountRequest` WRITE;
/*!40000 ALTER TABLE `accountRequest` DISABLE KEYS */;
INSERT INTO `accountRequest` VALUES ('SiVola','1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b'),('t','1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b'),('a3','39421e5c-2dc3-4e24-ab95-4cc3f87250cb'),('t','39421e5c-2dc3-4e24-ab95-4cc3f87250cb'),('a1','7e3ae615-403c-46be-85c3-7d4f7c6a20d4'),('t','7e3ae615-403c-46be-85c3-7d4f7c6a20d4'),('asdf','98aee8fe-e0a9-4543-95f3-b73a06b2118a'),('t','98aee8fe-e0a9-4543-95f3-b73a06b2118a'),('SiVola','cb9d9be1-3bf7-4a58-9225-512fc6d8dff2'),('t','cb9d9be1-3bf7-4a58-9225-512fc6d8dff2'),('SiVola','d42cf56a-3ec8-4293-80bc-c5d394a61ae2'),('t','d42cf56a-3ec8-4293-80bc-c5d394a61ae2'),('SiVola','eb82abf4-c5e7-4260-b137-56919bc9d8d7'),('t','eb82abf4-c5e7-4260-b137-56919bc9d8d7'),('SiVola','fa4aacee-96d2-4069-a288-45e37f6faa0b'),('teo','fa4aacee-96d2-4069-a288-45e37f6faa0b'),('SiVola','ff591483-b3f2-4a72-bdf0-166bd4be85ae'),('t','ff591483-b3f2-4a72-bdf0-166bd4be85ae');
/*!40000 ALTER TABLE `accountRequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agencyType`
--

DROP TABLE IF EXISTS `agencyType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `agencyType` (
  `agency` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`agency`,`type`),
  KEY `fk_type_at_idx` (`type`),
  CONSTRAINT `fk_agency_at` FOREIGN KEY (`agency`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_type_at` FOREIGN KEY (`type`) REFERENCES `Type` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agencyType`
--

LOCK TABLES `agencyType` WRITE;
/*!40000 ALTER TABLE `agencyType` DISABLE KEYS */;
INSERT INTO `agencyType` VALUES ('SiVola','City'),('SiVola','On The Road');
/*!40000 ALTER TABLE `agencyType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Attraction`
--

DROP TABLE IF EXISTS `Attraction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Attraction` (
  `placeID` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`placeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Attraction`
--

LOCK TABLES `Attraction` WRITE;
/*!40000 ALTER TABLE `Attraction` DISABLE KEYS */;
INSERT INTO `Attraction` VALUES ('n1421083055','Fori Imperiali','Roma','Via dei Fori Imperiali',41.893565,12.4863624),('n1704118167','Piazza Grande','Bologna','Via Romita',44.5289065,11.3679298),('n2456061019','Bosco Verticale','Milano','Via Federico Confalonieri',45.4867373,9.1912629),('n9850493699','Duomo','Milano','1_33051',45.4646014,9.1894915),('r11384819','Circo Massimo','Roma','Via dell\'Ara Massima di Ercole',41.886158449999996,12.485308709919227),('r12982355','Torre di Pisa','Pisa','Piazza del Duomo',43.7230159,10.3966322),('r13448560','Fontana di Trevi','Roma','Piazza di Trevi',41.9009778,12.483284842339874),('r1849830','Altare della Patria','Roma','Scala dell\'Arce Capitolina',41.894686699999994,12.48306643636364),('r2529081','Trastevere','Roma','Unknown',41.8911586,12.466845904466918),('r2985896','Villa Borghese','Roma','Unknown',41.914411099999995,12.484843942580198),('r318583','Piazza del Popolo','Roma','Piazza del Popolo',41.910750199999995,12.475843771434324),('r347950','Barcelona','Barcelona','Unknown',41.3828939,2.1774322),('r39513','Palermo','Palermo','Unknown',38.1112268,13.3524434),('r40767','Napoli','Napoli','Unknown',40.8358846,14.2487679),('r62422','Berlin','Berlin','Unknown',52.510885,13.3989367),('w117457119','Stadio Olimpico','Roma','Viale dei Gladiatori',41.933942349999995,12.454770497133222),('w215801333','Colosseo','Roma','Colosseo',41.8909705,12.4922415),('w713360712','Piazza Venezia','Roma','Unknown',41.8961514,12.482407389638281);
/*!40000 ALTER TABLE `Attraction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `City`
--

DROP TABLE IF EXISTS `City`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `City` (
  `placeID` varchar(45) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  PRIMARY KEY (`placeID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `City`
--

LOCK TABLES `City` WRITE;
/*!40000 ALTER TABLE `City` DISABLE KEYS */;
INSERT INTO `City` VALUES ('r347950','Barcelona','España',41.3828939,2.1774322),('r39513','Palermo','Italia',38.1112268,13.3524434),('r40767','Napoli','Italia',40.8358846,14.2487679),('r41485','Roma','Italia',41.8933203,12.4829321),('r42527','Pisa','Italia',43.7159395,10.4018624),('r44915','Milano','Italia',45.4641943,9.1896346),('r62422','Berlin','Deutschland',52.510885,13.3989367);
/*!40000 ALTER TABLE `City` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Day`
--

DROP TABLE IF EXISTS `Day`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Day` (
  `dayNum` int NOT NULL,
  `itineraryID` varchar(45) NOT NULL,
  PRIMARY KEY (`itineraryID`,`dayNum`),
  KEY `fk_itinerary_idx` (`itineraryID`),
  CONSTRAINT `fk_itinerary_d` FOREIGN KEY (`itineraryID`) REFERENCES `Itinerary` (`itineraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Day`
--

LOCK TABLES `Day` WRITE;
/*!40000 ALTER TABLE `Day` DISABLE KEYS */;
INSERT INTO `Day` VALUES (1,'04362ca3-9e62-48bd-8d5c-8013eef45f87'),(2,'04362ca3-9e62-48bd-8d5c-8013eef45f87'),(1,'06a6f784-b865-4ffd-8435-7f11ffba72e7'),(1,'095dd783-03d1-42a9-b6d9-411a6879e515'),(1,'0e89ad89-e8f6-4765-b9ea-6c69eb9da62b'),(2,'0e89ad89-e8f6-4765-b9ea-6c69eb9da62b'),(1,'1101eaaa-8ce3-4f55-9200-128ad81a4b23'),(1,'14c29c6f-e0f6-4fed-9c2b-868b47e3631c'),(1,'16bd9cda-5647-4118-8503-8dc967cfc47c'),(1,'27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1'),(1,'2845356d-648d-48ca-a09a-55c1a9132940'),(2,'2845356d-648d-48ca-a09a-55c1a9132940'),(1,'2d37f5bf-718e-4b23-a4b2-8ecb0a488072'),(1,'2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81'),(2,'2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81'),(1,'334a96c9-c602-41ad-b270-6f482a16ea04'),(1,'3850cf5d-5011-4aac-b9ff-ea635862e626'),(1,'3a288717-6b41-4472-bbf1-03f6d9a3228a'),(1,'400b6042-58d4-4723-9519-96df9b7f08fe'),(1,'4aa33e13-cad9-4a84-b7f0-4c5a761ed848'),(1,'4aea6411-56a2-44e0-9f4d-16b3aa620daa'),(1,'4f33164e-6bb5-44fe-9d69-83035096eff4'),(1,'5d143505-ff43-4850-be18-0f5b06b76db8'),(1,'5f18cfc4-63a5-44f5-a255-54c19f3f8c47'),(1,'6379eb46-48bd-40e3-9b98-942ffc310a9f'),(1,'66e5d2dc-5b69-435e-9298-7d8fe9ab3423'),(2,'66e5d2dc-5b69-435e-9298-7d8fe9ab3423'),(1,'6b9cb6b2-a538-418d-9649-c8ce3542880d'),(1,'6ca5f413-cf96-4416-aa1b-e80b67973222'),(1,'6df50656-0d10-4a47-b50b-033631c332a8'),(1,'79bcde9e-5593-46fe-8e4f-03e2c2327263'),(1,'7f623ffd-bf83-498a-a31b-9eca175bc702'),(2,'7f623ffd-bf83-498a-a31b-9eca175bc702'),(1,'8f79ae84-5ee4-4e50-91b1-2936cc315f8e'),(1,'91034a08-cbe4-4ed0-9d23-f5d2926ae9fe'),(1,'954e3562-f860-40b4-b3f0-a24a6d0202ec'),(1,'955f08d3-c694-4a47-a9ea-6c7a7e5b39d7'),(1,'9b8efdc5-fe9e-4052-a977-402530be784b'),(1,'9cd0de93-9a5a-41a6-a883-b21f1973acf2'),(1,'9e8a2d46-654e-4ff0-a983-49b4d1012757'),(1,'a5d25648-d02d-4c52-bb85-d09cf2c71531'),(1,'a5e7db61-2774-494c-8e54-46d1e46ca7fe'),(2,'a5e7db61-2774-494c-8e54-46d1e46ca7fe'),(1,'a7ed936f-41a5-40ff-943c-830189c37518'),(1,'acc1ff85-6aa4-4db0-ab8d-86f1acb4a268'),(1,'b09f81a7-d0f9-4712-8638-9d315e5aa9a7'),(1,'b3844785-73ee-44a2-b10e-bdf5ed37e1ec'),(1,'c0f1075a-6d8a-42f6-8599-9f5aeb77159d'),(1,'c4da4260-eebc-4cce-898f-192c49f7cb02'),(1,'c9e98b03-5910-4c50-ba1a-4ec376dabca7'),(2,'c9e98b03-5910-4c50-ba1a-4ec376dabca7'),(1,'cfd5c033-3a0d-40ee-99f5-748ff06c068c'),(2,'cfd5c033-3a0d-40ee-99f5-748ff06c068c'),(1,'d06ccb96-f657-4837-9fd6-5b46921d158f'),(1,'d2a47c7e-ae6d-4604-b90d-d6654bf2fc94'),(1,'fff6e821-abca-480f-b655-9587e469ff16');
/*!40000 ALTER TABLE `Day` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dayAttraction`
--

DROP TABLE IF EXISTS `dayAttraction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dayAttraction` (
  `attractionID` varchar(45) NOT NULL,
  `itineraryID` varchar(45) NOT NULL,
  `dayNum` int NOT NULL,
  PRIMARY KEY (`attractionID`,`itineraryID`,`dayNum`),
  KEY `fk_day_idx` (`itineraryID`,`dayNum`),
  CONSTRAINT `fk_attraction` FOREIGN KEY (`attractionID`) REFERENCES `Attraction` (`placeID`),
  CONSTRAINT `fk_day_a` FOREIGN KEY (`itineraryID`, `dayNum`) REFERENCES `Day` (`itineraryID`, `dayNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dayAttraction`
--

LOCK TABLES `dayAttraction` WRITE;
/*!40000 ALTER TABLE `dayAttraction` DISABLE KEYS */;
INSERT INTO `dayAttraction` VALUES ('r1849830','04362ca3-9e62-48bd-8d5c-8013eef45f87',1),('w215801333','04362ca3-9e62-48bd-8d5c-8013eef45f87',1),('n2456061019','04362ca3-9e62-48bd-8d5c-8013eef45f87',2),('w215801333','06a6f784-b865-4ffd-8435-7f11ffba72e7',1),('w215801333','095dd783-03d1-42a9-b6d9-411a6879e515',1),('r1849830','0e89ad89-e8f6-4765-b9ea-6c69eb9da62b',1),('w215801333','0e89ad89-e8f6-4765-b9ea-6c69eb9da62b',1),('r1849830','14c29c6f-e0f6-4fed-9c2b-868b47e3631c',1),('w215801333','27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1',1),('w215801333','2845356d-648d-48ca-a09a-55c1a9132940',1),('n9850493699','2845356d-648d-48ca-a09a-55c1a9132940',2),('r11384819','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',1),('w215801333','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',1),('r1849830','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',2),('w713360712','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',2),('r39513','3a288717-6b41-4472-bbf1-03f6d9a3228a',1),('w215801333','400b6042-58d4-4723-9519-96df9b7f08fe',1),('n1421083055','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r11384819','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r13448560','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r1849830','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r2529081','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r2985896','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r318583','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('w117457119','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('w215801333','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('w713360712','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('w215801333','4aea6411-56a2-44e0-9f4d-16b3aa620daa',1),('w215801333','4f33164e-6bb5-44fe-9d69-83035096eff4',1),('w215801333','5d143505-ff43-4850-be18-0f5b06b76db8',1),('w215801333','5f18cfc4-63a5-44f5-a255-54c19f3f8c47',1),('n9850493699','6379eb46-48bd-40e3-9b98-942ffc310a9f',1),('w215801333','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',1),('w713360712','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',1),('n2456061019','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',2),('n9850493699','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',2),('w215801333','6b9cb6b2-a538-418d-9649-c8ce3542880d',1),('r347950','6ca5f413-cf96-4416-aa1b-e80b67973222',1),('w215801333','6df50656-0d10-4a47-b50b-033631c332a8',1),('w713360712','79bcde9e-5593-46fe-8e4f-03e2c2327263',1),('n1421083055','7f623ffd-bf83-498a-a31b-9eca175bc702',1),('r1849830','7f623ffd-bf83-498a-a31b-9eca175bc702',1),('w215801333','7f623ffd-bf83-498a-a31b-9eca175bc702',1),('n2456061019','7f623ffd-bf83-498a-a31b-9eca175bc702',2),('n9850493699','7f623ffd-bf83-498a-a31b-9eca175bc702',2),('w215801333','8f79ae84-5ee4-4e50-91b1-2936cc315f8e',1),('w215801333','91034a08-cbe4-4ed0-9d23-f5d2926ae9fe',1),('w215801333','954e3562-f860-40b4-b3f0-a24a6d0202ec',1),('r12982355','955f08d3-c694-4a47-a9ea-6c7a7e5b39d7',1),('w215801333','9cd0de93-9a5a-41a6-a883-b21f1973acf2',1),('r40767','9e8a2d46-654e-4ff0-a983-49b4d1012757',1),('r11384819','a5e7db61-2774-494c-8e54-46d1e46ca7fe',1),('w215801333','a5e7db61-2774-494c-8e54-46d1e46ca7fe',1),('r1849830','a5e7db61-2774-494c-8e54-46d1e46ca7fe',2),('w713360712','a5e7db61-2774-494c-8e54-46d1e46ca7fe',2),('w215801333','a7ed936f-41a5-40ff-943c-830189c37518',1),('n1704118167','acc1ff85-6aa4-4db0-ab8d-86f1acb4a268',1),('w215801333','b09f81a7-d0f9-4712-8638-9d315e5aa9a7',1),('w215801333','b3844785-73ee-44a2-b10e-bdf5ed37e1ec',1),('r62422','c0f1075a-6d8a-42f6-8599-9f5aeb77159d',1),('n9850493699','c4da4260-eebc-4cce-898f-192c49f7cb02',1),('r1849830','c9e98b03-5910-4c50-ba1a-4ec376dabca7',1),('w215801333','c9e98b03-5910-4c50-ba1a-4ec376dabca7',1),('n2456061019','c9e98b03-5910-4c50-ba1a-4ec376dabca7',2),('w215801333','cfd5c033-3a0d-40ee-99f5-748ff06c068c',1),('r1849830','cfd5c033-3a0d-40ee-99f5-748ff06c068c',2),('w215801333','d06ccb96-f657-4837-9fd6-5b46921d158f',1),('w215801333','d2a47c7e-ae6d-4604-b90d-d6654bf2fc94',1),('n1704118167','fff6e821-abca-480f-b655-9587e469ff16',1);
/*!40000 ALTER TABLE `dayAttraction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dayCity`
--

DROP TABLE IF EXISTS `dayCity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dayCity` (
  `cityID` varchar(45) NOT NULL,
  `itineraryID` varchar(45) NOT NULL,
  `dayNum` int NOT NULL,
  PRIMARY KEY (`cityID`,`itineraryID`,`dayNum`),
  KEY `fk_day_c_idx` (`itineraryID`,`dayNum`),
  CONSTRAINT `fk_city` FOREIGN KEY (`cityID`) REFERENCES `City` (`placeID`),
  CONSTRAINT `fk_day_c` FOREIGN KEY (`itineraryID`, `dayNum`) REFERENCES `Day` (`itineraryID`, `dayNum`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dayCity`
--

LOCK TABLES `dayCity` WRITE;
/*!40000 ALTER TABLE `dayCity` DISABLE KEYS */;
INSERT INTO `dayCity` VALUES ('r41485','04362ca3-9e62-48bd-8d5c-8013eef45f87',1),('r41485','06a6f784-b865-4ffd-8435-7f11ffba72e7',1),('r41485','095dd783-03d1-42a9-b6d9-411a6879e515',1),('r41485','0e89ad89-e8f6-4765-b9ea-6c69eb9da62b',1),('r41485','14c29c6f-e0f6-4fed-9c2b-868b47e3631c',1),('r41485','27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1',1),('r41485','2845356d-648d-48ca-a09a-55c1a9132940',1),('r44915','2845356d-648d-48ca-a09a-55c1a9132940',2),('r41485','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',1),('r41485','2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81',2),('r39513','3a288717-6b41-4472-bbf1-03f6d9a3228a',1),('r41485','400b6042-58d4-4723-9519-96df9b7f08fe',1),('r41485','4aa33e13-cad9-4a84-b7f0-4c5a761ed848',1),('r41485','4aea6411-56a2-44e0-9f4d-16b3aa620daa',1),('r41485','4f33164e-6bb5-44fe-9d69-83035096eff4',1),('r41485','5d143505-ff43-4850-be18-0f5b06b76db8',1),('r41485','5f18cfc4-63a5-44f5-a255-54c19f3f8c47',1),('r44915','6379eb46-48bd-40e3-9b98-942ffc310a9f',1),('r41485','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',1),('r44915','66e5d2dc-5b69-435e-9298-7d8fe9ab3423',2),('r41485','6b9cb6b2-a538-418d-9649-c8ce3542880d',1),('r347950','6ca5f413-cf96-4416-aa1b-e80b67973222',1),('r41485','6df50656-0d10-4a47-b50b-033631c332a8',1),('r41485','79bcde9e-5593-46fe-8e4f-03e2c2327263',1),('r41485','7f623ffd-bf83-498a-a31b-9eca175bc702',1),('r44915','7f623ffd-bf83-498a-a31b-9eca175bc702',2),('r41485','8f79ae84-5ee4-4e50-91b1-2936cc315f8e',1),('r41485','91034a08-cbe4-4ed0-9d23-f5d2926ae9fe',1),('r41485','954e3562-f860-40b4-b3f0-a24a6d0202ec',1),('r42527','955f08d3-c694-4a47-a9ea-6c7a7e5b39d7',1),('r41485','9cd0de93-9a5a-41a6-a883-b21f1973acf2',1),('r40767','9e8a2d46-654e-4ff0-a983-49b4d1012757',1),('r41485','a5e7db61-2774-494c-8e54-46d1e46ca7fe',1),('r41485','a5e7db61-2774-494c-8e54-46d1e46ca7fe',2),('r41485','a7ed936f-41a5-40ff-943c-830189c37518',1),('r41485','b09f81a7-d0f9-4712-8638-9d315e5aa9a7',1),('r41485','b3844785-73ee-44a2-b10e-bdf5ed37e1ec',1),('r62422','c0f1075a-6d8a-42f6-8599-9f5aeb77159d',1),('r44915','c4da4260-eebc-4cce-898f-192c49f7cb02',1),('r41485','c9e98b03-5910-4c50-ba1a-4ec376dabca7',1),('r41485','cfd5c033-3a0d-40ee-99f5-748ff06c068c',1),('r41485','cfd5c033-3a0d-40ee-99f5-748ff06c068c',2),('r41485','d06ccb96-f657-4837-9fd6-5b46921d158f',1),('r41485','d2a47c7e-ae6d-4604-b90d-d6654bf2fc94',1);
/*!40000 ALTER TABLE `dayCity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Flight`
--

DROP TABLE IF EXISTS `Flight`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Flight` (
  `id` varchar(45) NOT NULL,
  `departureTime` double DEFAULT NULL,
  `arrivalTime` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Flight`
--

LOCK TABLES `Flight` WRITE;
/*!40000 ALTER TABLE `Flight` DISABLE KEYS */;
INSERT INTO `Flight` VALUES ('01c086c2-58a9-4573-a1f0-f108ae4bb846',1.23,4.26),('095f0ecd-eb7c-432a-905b-5e4ea7de29d6',11.45,12),('0ac3bf05-3245-4dfd-9731-fd5360cd1598',11,14.5),('0b022a02-9350-4e4c-8e88-fd6c9ef56de3',12,15.5),('0cb23830-3c65-4862-9cb8-0d90030b9096',13,23),('0e6a78c0-3c38-4656-84a9-500f96d9229c',12,16.4),('0fd26fb9-9cf9-4bf6-9708-0ac3de7c022c',11.3,12.35),('135ee94c-60bb-46d5-8af5-cf4fc942246c',13,15),('165549df-7961-48ab-a1f1-811af8e5efab',13,14),('1a880e66-b74d-4dbf-b2ac-895e986a5dab',12,13),('1f2364e4-4b15-432d-829d-8a9befdd2d35',11,15),('2060028b-6ea0-40cc-9f7b-9cffe68af090',3.3,4.4),('233cfa7c-130b-40f6-9ce6-aa71074fba91',3,4),('28cdbecc-9659-47d3-84d6-d297b5b79aaa',12,13),('2ca8304c-6b62-49eb-9dd9-96ae050ac41c',1,2),('2e6cc3db-4685-49b7-b654-12638fed0709',13,14),('305e2b38-4995-45ab-82d0-d5badaaae5e6',13,14),('381f818f-3966-4119-9e2a-ceac7f390b02',11.2,23.59),('41e74534-82b7-4d70-a46b-76c0e7366662',12,13),('43ad0214-3abf-45a2-b763-0f3646f91c5e',11,12),('473a0cae-f43c-4d69-b439-c59bf5ef976d',14,21),('47a9468e-4263-4f58-93f8-ceaf64eeedc1',12,14),('570858f9-366d-4301-826b-23467a153bee',11,12),('579ed22f-e079-405b-83b6-00732103ff4a',13,14),('5b8c14cc-d601-4776-913b-e1602daa4878',13,15),('60ab0259-2d97-4c12-826d-e25d0dfca79b',11.4,16),('61f1daa2-bdeb-4c35-8f4c-91437d702fc4',11,13.5),('62d589be-e635-4ddd-a6c4-8aec1648aeee',1,2),('67637d43-9e20-40b1-90cb-3ca6be21c7d9',11.3,12.35),('688d8805-cf16-4fba-8a1d-ae36f5365d09',11.23,13.44),('74a904d9-794b-491e-a427-eeabe3b54a97',11,13.7),('75992833-c5b9-45a4-9dc4-0202131aabdd',12.3,15),('766b6d34-3329-4fbc-b37c-6a0fc46b9f30',22,23),('76ef19d7-a6c5-45e6-9eb8-13d59d7fcb65',11,13),('77df67f8-d5d2-4c78-8a9d-a954b86378b5',11.23,23.22),('7daa953a-b2f3-4df5-9b6b-a5bf7031c1ac',11.7,11),('7e859c27-6c12-4d0e-8c36-5e3993a9731b',11,12),('85254418-c205-4442-a9da-a4aad2894645',23,24),('9fd01406-00d0-4e37-aa5c-837b5717f667',1,4.59),('a1821e51-7030-4c9d-b4a4-a3948990bc3f',11,13.6),('ac609806-edde-4c99-a79e-1bbb0042dd71',13,14),('ac6631d9-4971-4d46-a23b-960d282d6513',11.1,13.2),('af4226c2-0a96-4c19-9058-e7e9acd9e89f',14,15),('b13956e6-22de-49fa-9e55-ee11469bcbd1',13,15),('bb21c135-0599-49fa-9a36-f0d2bdb3f680',23,24),('bbad9750-9b17-449d-bc8a-eeaa53b87a8d',22,24),('bbecc9f3-6cea-4a2a-bd5f-511c1c625465',13.4,16),('c3542789-3586-4042-a766-ad2fd0d11aff',11.34,11.22),('cb5e9751-4c53-4e9c-a4e4-33b6ec488782',1.3,2.3),('cd43950e-4b3c-4078-9a56-0e3b9230eccd',11.2,14.5),('d0e9fe29-ed44-4fa1-b86e-b899e990fae9',13,14),('da1d3f97-01b4-4345-bcc8-cbe329737dbe',11,14),('dd29ae20-1df2-4714-b660-50422cca880f',12,13),('e1c1c404-5111-410f-b476-d213077f412f',22,23),('e3992200-25c4-4176-8e78-05d9fb7fbe68',11,14),('e6d4bb69-6284-44b6-adb0-7e10e422e2f7',12,14),('e75df6d8-a4f8-4713-90d7-c0c5552fe047',12,15),('e80852c5-4c6b-48c0-904e-0ef251ca7fad',11.23,11.23),('e871a1c9-a40c-4fe1-bd05-599e3cce30fc',11,12),('ec63346e-fb63-4c7d-88c8-b452b4ef628c',12.3,14.5),('eda07eb9-a201-44bd-b8e7-71b26d718351',11,13),('f42eaf37-b1fc-4b8c-92a8-7994c227380f',12.3,14.5),('fc68dff8-7f1f-42c1-9601-67260caee0a9',12,13),('fe16ff9a-7be8-404e-b42a-eab7fa8ec6a1',13,14),('fe31f6a6-e90c-44b5-931d-a630302b5310',12,13),('fe7759d8-086b-4f62-add5-a46ac61b1eb4',12.22,23.33);
/*!40000 ALTER TABLE `Flight` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Itinerary`
--

DROP TABLE IF EXISTS `Itinerary`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Itinerary` (
  `itineraryID` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `description` varchar(120) NOT NULL,
  `daysNumber` int NOT NULL,
  `inFlight` varchar(45) DEFAULT NULL,
  `outFlight` varchar(45) DEFAULT NULL,
  `photo` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`itineraryID`),
  KEY `fk_inFlight_idx` (`inFlight`),
  KEY `fk_outFlight_idx` (`outFlight`),
  CONSTRAINT `fk_inFlight` FOREIGN KEY (`inFlight`) REFERENCES `Flight` (`id`),
  CONSTRAINT `fk_outFlight` FOREIGN KEY (`outFlight`) REFERENCES `Flight` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Itinerary`
--

LOCK TABLES `Itinerary` WRITE;
/*!40000 ALTER TABLE `Itinerary` DISABLE KEYS */;
INSERT INTO `Itinerary` VALUES ('031504f9-c027-4825-bf07-6a35ccd363af','Giro di Roma','un giro di roma di due giorni, un weekend bello bello alla scoperta della città eterna',2,'dd29ae20-1df2-4714-b660-50422cca880f','bbad9750-9b17-449d-bc8a-eeaa53b87a8d',NULL),('04362ca3-9e62-48bd-8d5c-8013eef45f87','prova','descrizione di prova',2,'095f0ecd-eb7c-432a-905b-5e4ea7de29d6','bbecc9f3-6cea-4a2a-bd5f-511c1c625465',NULL),('06a6f784-b865-4ffd-8435-7f11ffba72e7','afs','afsfas',1,NULL,NULL,NULL),('095dd783-03d1-42a9-b6d9-411a6879e515','prova','aksjfhasbfkhasbkfbaskbasf sakfhb askfh bkashf asi houhfifglsaj lgfalgfahfs alsfjkagf asfg agfksajghf augfsjb ak',1,NULL,NULL,NULL),('0e89ad89-e8f6-4765-b9ea-6c69eb9da62b','prova finale ','descrizione di prova',2,'60ab0259-2d97-4c12-826d-e25d0dfca79b','0b022a02-9350-4e4c-8e88-fd6c9ef56de3',NULL),('1101eaaa-8ce3-4f55-9200-128ad81a4b23','bolobgna 1.0','afsfssaf asf af asf',1,NULL,NULL,NULL),('14c29c6f-e0f6-4fed-9c2b-868b47e3631c','saf','asf',1,NULL,NULL,NULL),('16bd9cda-5647-4118-8503-8dc967cfc47c','bolobgna 1.0','afsfssaf asf af asf',1,NULL,NULL,NULL),('27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1','itinerario proposta finalissima','daionsoasjnvoasjnvasjvasvsasavsvasvavsa',1,'2ca8304c-6b62-49eb-9dd9-96ae050ac41c','233cfa7c-130b-40f6-9ce6-aa71074fba91',NULL),('2845356d-648d-48ca-a09a-55c1a9132940','prova','asfkjnbfsaknj',2,NULL,NULL,NULL),('2d37f5bf-718e-4b23-a4b2-8ecb0a488072','bolobgna 1.0','afsfssaf asf af asf',1,NULL,NULL,NULL),('2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81','weekend a roma','un weekend da sogno nel cuore della città eterna, ROMA CAPUT MUNDI!',2,'41e74534-82b7-4d70-a46b-76c0e7366662','bb21c135-0599-49fa-9a36-f0d2bdb3f680',NULL),('334a96c9-c602-41ad-b270-6f482a16ea04','ttasafa','safsaffsaaffsa',1,NULL,NULL,NULL),('3850cf5d-5011-4aac-b9ff-ea635862e626','bolobgna 1.0','afsfssaf asf af asf',1,NULL,NULL,NULL),('3a288717-6b41-4472-bbf1-03f6d9a3228a','aaa','fasf',1,NULL,NULL,NULL),('400b6042-58d4-4723-9519-96df9b7f08fe','aslfknlasf','asfsfafsaafsafsfsa',1,'76ef19d7-a6c5-45e6-9eb8-13d59d7fcb65','ec63346e-fb63-4c7d-88c8-b452b4ef628c',NULL),('4aa33e13-cad9-4a84-b7f0-4c5a761ed848','as','saf',1,NULL,NULL,NULL),('4aea6411-56a2-44e0-9f4d-16b3aa620daa','prova1','safsafafsfas',1,NULL,NULL,NULL),('4f33164e-6bb5-44fe-9d69-83035096eff4','asf','afs',1,NULL,NULL,NULL),('53876fbc-408a-466e-ad2b-894ddb8da8d5','qwrrwqqrw','rwqqrw',1,NULL,NULL,NULL),('5d143505-ff43-4850-be18-0f5b06b76db8','saffsafas','afsfsaafsfasfafsafasf',1,NULL,NULL,NULL),('5f18cfc4-63a5-44f5-a255-54c19f3f8c47','afsasfasaf','asfsaljfnl asha ashjf ajskj hsaj skjfh aksfhahf ahsfakfj sfha kjhfsakjhf askf',1,NULL,NULL,NULL),('6379eb46-48bd-40e3-9b98-942ffc310a9f','prova prova','proviamo',1,'e871a1c9-a40c-4fe1-bd05-599e3cce30fc','fe16ff9a-7be8-404e-b42a-eab7fa8ec6a1',NULL),('66e5d2dc-5b69-435e-9298-7d8fe9ab3423','prova cli','asfjsakjb',2,'688d8805-cf16-4fba-8a1d-ae36f5365d09','01c086c2-58a9-4573-a1f0-f108ae4bb846',NULL),('6b9cb6b2-a538-418d-9649-c8ce3542880d','provasasv foto','asffsafs',1,NULL,NULL,'/Users/teo.mio/Documents/Immagini sfondo/IMG_5791 2.jpeg'),('6ca5f413-cf96-4416-aa1b-e80b67973222','afsasf','saffsafas',1,NULL,NULL,NULL),('6df50656-0d10-4a47-b50b-033631c332a8','prova foto','asflkasfnljafa',1,NULL,NULL,'/Users/teo.mio/Documents/Immagini sfondo/IMG_5565.jpeg'),('79bcde9e-5593-46fe-8e4f-03e2c2327263','cx','zvx',1,NULL,NULL,NULL),('7f623ffd-bf83-498a-a31b-9eca175bc702','prova nuovo calculate','asfsafsfafsa',2,NULL,NULL,NULL),('8d2e9839-6e72-4bae-b439-3be6df13ab85','ads','asf,msaf ',1,NULL,NULL,NULL),('8f79ae84-5ee4-4e50-91b1-2936cc315f8e','tassfafsafsafsfasfas','afsfsasfasfafsasaf',1,NULL,NULL,NULL),('91034a08-cbe4-4ed0-9d23-f5d2926ae9fe','tafsfsafas','asfouhoasbfjn',1,NULL,NULL,NULL),('954e3562-f860-40b4-b3f0-a24a6d0202ec','afssfafsafs','fsaasffsa',1,NULL,NULL,NULL),('955f08d3-c694-4a47-a9ea-6c7a7e5b39d7','prova da cli','descrizione di prova da cli',1,'e3992200-25c4-4176-8e78-05d9fb7fbe68','e75df6d8-a4f8-4713-90d7-c0c5552fe047',NULL),('9b8efdc5-fe9e-4052-a977-402530be784b','prova finale',' descrizione di prova',2,NULL,NULL,NULL),('9cd0de93-9a5a-41a6-a883-b21f1973acf2','asf','sads',1,NULL,NULL,NULL),('9e8a2d46-654e-4ff0-a983-49b4d1012757','napoli','saffslknfsa',1,NULL,NULL,NULL),('a5d25648-d02d-4c52-bb85-d09cf2c71531','saffasas','fasassaffa',1,NULL,NULL,NULL),('a5e7db61-2774-494c-8e54-46d1e46ca7fe','Weekend a Roma','un weekend spettacolare nel cuore della capitale italiana, la città eterna, un avventura unica',2,'0cb23830-3c65-4862-9cb8-0d90030b9096','473a0cae-f43c-4d69-b439-c59bf5ef976d',NULL),('a7ed936f-41a5-40ff-943c-830189c37518','saf','saf',1,NULL,NULL,NULL),('acc1ff85-6aa4-4db0-ab8d-86f1acb4a268','bologna 2.0','asfkaslkm vak lak aslkals ks',1,NULL,NULL,NULL),('afcf2632-f458-409d-8904-bf95ff35790d','vietnam on the road','kasjbfksabfjlsabfjasbfljasf',1,NULL,NULL,NULL),('b09f81a7-d0f9-4712-8638-9d315e5aa9a7','1111111111111111','227yqudigbskajsbfa2e12rdqfw',1,NULL,NULL,NULL),('b3844785-73ee-44a2-b10e-bdf5ed37e1ec','afs','saf',1,NULL,NULL,NULL),('c0f1075a-6d8a-42f6-8599-9f5aeb77159d','asdafsasf11111111','sfafsafa',1,NULL,NULL,NULL),('c4da4260-eebc-4cce-898f-192c49f7cb02','prova 2','milanosadsdsaf',1,NULL,NULL,NULL),('c5f99fbe-48d3-4c83-b3e5-ca0223f689c4','fsa','asf',1,'fc68dff8-7f1f-42c1-9601-67260caee0a9','85254418-c205-4442-a9da-a4aad2894645',NULL),('c9e98b03-5910-4c50-ba1a-4ec376dabca7','prova ','descrizione di prova',2,'0ac3bf05-3245-4dfd-9731-fd5360cd1598','f42eaf37-b1fc-4b8c-92a8-7994c227380f',NULL),('cfd5c033-3a0d-40ee-99f5-748ff06c068c','ransofk','asfkljnasfkaf',2,NULL,NULL,NULL),('d06ccb96-f657-4837-9fd6-5b46921d158f','knaf s','safafsafs',1,NULL,NULL,NULL),('d2a47c7e-ae6d-4604-b90d-d6654bf2fc94','foto','fas',1,NULL,NULL,'/Users/teo.mio/Documents/Immagini sfondo/IMG_5565.jpeg'),('de2115d5-db79-4657-90fd-77ff1c2f9811','afs','fdsa',1,NULL,NULL,NULL),('e2e21386-5333-4496-bac9-446015e7471e','jkasfn saf','asjflnafsn',1,NULL,NULL,NULL),('fff6e821-abca-480f-b655-9587e469ff16','bologna 2.0','asfkaslkm vak lak aslkals ks',1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `Itinerary` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itineraryAccommodation`
--

DROP TABLE IF EXISTS `itineraryAccommodation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itineraryAccommodation` (
  `itineraryID` varchar(45) NOT NULL,
  `accommodationID` varchar(45) NOT NULL,
  PRIMARY KEY (`itineraryID`,`accommodationID`),
  KEY `fk_accommodation_ia_idx` (`accommodationID`),
  CONSTRAINT `fk_accommodation_ia` FOREIGN KEY (`accommodationID`) REFERENCES `Accommodation` (`id`),
  CONSTRAINT `fk_itinerary_ia` FOREIGN KEY (`itineraryID`) REFERENCES `Itinerary` (`itineraryID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itineraryAccommodation`
--

LOCK TABLES `itineraryAccommodation` WRITE;
/*!40000 ALTER TABLE `itineraryAccommodation` DISABLE KEYS */;
INSERT INTO `itineraryAccommodation` VALUES ('04362ca3-9e62-48bd-8d5c-8013eef45f87','00397635-ffee-4c9c-be8f-e8ec736207ca'),('0e89ad89-e8f6-4765-b9ea-6c69eb9da62b','2dcc0fb7-8798-40e3-8def-c748925fa210'),('66e5d2dc-5b69-435e-9298-7d8fe9ab3423','3f1c8262-0ee1-4c65-b1fb-1b6225b181c2'),('c5f99fbe-48d3-4c83-b3e5-ca0223f689c4','52d57de3-6626-4388-b5e2-5fe3819031ad'),('c9e98b03-5910-4c50-ba1a-4ec376dabca7','5e0ca52d-00d4-4445-b612-523c8e867762'),('0e89ad89-e8f6-4765-b9ea-6c69eb9da62b','70a32aaa-8ba3-4a8a-ae5f-d481495060a1'),('2e0e19a8-9726-4c38-86d0-7cc6c5ab1f81','74fa3255-85ab-418a-a3a0-6a4cada0824a'),('6379eb46-48bd-40e3-9b98-942ffc310a9f','7686fe03-cebf-448c-9180-25c45d59e877'),('a5e7db61-2774-494c-8e54-46d1e46ca7fe','84f4fabf-ad7e-4998-b38b-36148cda38fa'),('955f08d3-c694-4a47-a9ea-6c7a7e5b39d7','a2ca7bcb-b5cf-40ec-ad29-59e5d1c09b4f'),('400b6042-58d4-4723-9519-96df9b7f08fe','a4965d4b-d330-44e0-84a5-02980e26fc75'),('04362ca3-9e62-48bd-8d5c-8013eef45f87','a826ef3a-ed51-47d3-bbb6-dbbbaf8b2fb4'),('91034a08-cbe4-4ed0-9d23-f5d2926ae9fe','ae6cd9e9-5ea0-40da-898e-258a980c306b'),('c9e98b03-5910-4c50-ba1a-4ec376dabca7','c9ae9c38-a8b9-4efd-a613-ef21e5e85300');
/*!40000 ALTER TABLE `itineraryAccommodation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ItineraryType`
--

DROP TABLE IF EXISTS `ItineraryType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ItineraryType` (
  `itineraryID` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  KEY `fk_itinerary_idx` (`itineraryID`),
  KEY `fk_type_idx` (`type`),
  CONSTRAINT `fk_itinerary_it` FOREIGN KEY (`itineraryID`) REFERENCES `Itinerary` (`itineraryID`),
  CONSTRAINT `fk_type` FOREIGN KEY (`type`) REFERENCES `Type` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItineraryType`
--

LOCK TABLES `ItineraryType` WRITE;
/*!40000 ALTER TABLE `ItineraryType` DISABLE KEYS */;
INSERT INTO `ItineraryType` VALUES ('5f18cfc4-63a5-44f5-a255-54c19f3f8c47','On The Road'),('5d143505-ff43-4850-be18-0f5b06b76db8','On The Road'),('5d143505-ff43-4850-be18-0f5b06b76db8','Culture'),('5d143505-ff43-4850-be18-0f5b06b76db8','Relax'),('954e3562-f860-40b4-b3f0-a24a6d0202ec','On The Road'),('954e3562-f860-40b4-b3f0-a24a6d0202ec','Nature'),('400b6042-58d4-4723-9519-96df9b7f08fe','On The Road'),('400b6042-58d4-4723-9519-96df9b7f08fe','Nature'),('400b6042-58d4-4723-9519-96df9b7f08fe','Culture'),('d06ccb96-f657-4837-9fd6-5b46921d158f','On The Road'),('cfd5c033-3a0d-40ee-99f5-748ff06c068c','Culture'),('e2e21386-5333-4496-bac9-446015e7471e','On The Road'),('4aea6411-56a2-44e0-9f4d-16b3aa620daa','On The Road'),('2845356d-648d-48ca-a09a-55c1a9132940','On The Road'),('2845356d-648d-48ca-a09a-55c1a9132940','Culture'),('9e8a2d46-654e-4ff0-a983-49b4d1012757','City'),('a5e7db61-2774-494c-8e54-46d1e46ca7fe','City'),('955f08d3-c694-4a47-a9ea-6c7a7e5b39d7','On The Road'),('955f08d3-c694-4a47-a9ea-6c7a7e5b39d7','Nature'),('c4da4260-eebc-4cce-898f-192c49f7cb02','Nature'),('27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1','On The Road'),('91034a08-cbe4-4ed0-9d23-f5d2926ae9fe','On The Road'),('d2a47c7e-ae6d-4604-b90d-d6654bf2fc94','On The Road'),('6df50656-0d10-4a47-b50b-033631c332a8','On The Road'),('6b9cb6b2-a538-418d-9649-c8ce3542880d','Nature');
/*!40000 ALTER TABLE `ItineraryType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Proposal`
--

DROP TABLE IF EXISTS `Proposal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Proposal` (
  `id` varchar(45) NOT NULL,
  `itineraryID` varchar(45) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `description` varchar(120) DEFAULT NULL,
  `user` varchar(45) DEFAULT NULL,
  `agency` varchar(45) DEFAULT NULL,
  `accepted` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_itinerary_pro_idx` (`itineraryID`),
  KEY `fk_user_pro_idx` (`user`),
  KEY `fk_agency_pro_idx` (`agency`),
  CONSTRAINT `fk_agency_pro` FOREIGN KEY (`agency`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_itinerary_pro` FOREIGN KEY (`itineraryID`) REFERENCES `Itinerary` (`itineraryID`),
  CONSTRAINT `fk_user_pro` FOREIGN KEY (`user`) REFERENCES `Account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Proposal`
--

LOCK TABLES `Proposal` WRITE;
/*!40000 ALTER TABLE `Proposal` DISABLE KEYS */;
INSERT INTO `Proposal` VALUES ('78e16cc9-29af-4731-8f90-369e1b56bc77','27d8e8dd-f0d1-4a4a-a6d1-f4719851ebf1',149,'descrizione prova finalissima','teo','SiVola','rejected'),('80a9bdc0-2a85-4ffe-be69-64417afa7cc8','a5e7db61-2774-494c-8e54-46d1e46ca7fe',111.2,'preventivo per viaggio a roma di due giorni, per ulteriori informazioni contattaci al +39 356 3572988','t','SiVola','accepted');
/*!40000 ALTER TABLE `Proposal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Request`
--

DROP TABLE IF EXISTS `Request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Request` (
  `id` varchar(45) NOT NULL,
  `user` varchar(45) DEFAULT NULL,
  `agency` varchar(45) DEFAULT NULL,
  `accepted` varchar(45) DEFAULT NULL,
  `description` varchar(120) DEFAULT NULL,
  `days` int DEFAULT NULL,
  `flight` tinyint DEFAULT NULL,
  `accommodation` tinyint DEFAULT NULL,
  `trekkingDistance` double DEFAULT NULL,
  `trekkingDifficulty` varchar(45) DEFAULT NULL,
  `travelMode` varchar(45) DEFAULT NULL,
  `dayDrivingHours` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_req_idx` (`user`),
  KEY `fk_agency_req_idx` (`agency`),
  CONSTRAINT `fk_agency_req` FOREIGN KEY (`agency`) REFERENCES `Account` (`username`),
  CONSTRAINT `fk_user_req` FOREIGN KEY (`user`) REFERENCES `Account` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Request`
--

LOCK TABLES `Request` WRITE;
/*!40000 ALTER TABLE `Request` DISABLE KEYS */;
INSERT INTO `Request` VALUES ('1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b','t','SiVola','pending','saf.ksa nfklòsaf',1,NULL,NULL,NULL,NULL,NULL,NULL),('39421e5c-2dc3-4e24-ab95-4cc3f87250cb','t','a3','pending','description',3,NULL,NULL,NULL,NULL,NULL,NULL),('7e3ae615-403c-46be-85c3-7d4f7c6a20d4','t','a1','pending','description',3,NULL,NULL,NULL,NULL,NULL,NULL),('98aee8fe-e0a9-4543-95f3-b73a06b2118a','t','asdf','pending','description',3,NULL,NULL,NULL,NULL,NULL,NULL),('cb9d9be1-3bf7-4a58-9225-512fc6d8dff2','t','SiVola','accepted','description',3,NULL,NULL,NULL,NULL,NULL,NULL),('d42cf56a-3ec8-4293-80bc-c5d394a61ae2','t','SiVola','pending','aklfnsajbfcnsa',1,NULL,NULL,NULL,NULL,NULL,NULL),('eb82abf4-c5e7-4260-b137-56919bc9d8d7',NULL,NULL,'pending','prova finale',1,1,1,NULL,NULL,'Morning Mode',3.4),('fa4aacee-96d2-4069-a288-45e37f6faa0b',NULL,NULL,'pending','prova finalissima',1,1,0,NULL,NULL,'Morning Mode',2),('ff591483-b3f2-4a72-bdf0-166bd4be85ae','t','SiVola','pending','safaslkfnsaknfa',1,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `Request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestAttraction`
--

DROP TABLE IF EXISTS `requestAttraction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requestAttraction` (
  `requestID` varchar(45) NOT NULL,
  `attractionID` varchar(45) NOT NULL,
  PRIMARY KEY (`requestID`,`attractionID`),
  KEY `fk_attraction_ra_idx` (`attractionID`),
  CONSTRAINT `fk_attraction_ra` FOREIGN KEY (`attractionID`) REFERENCES `Attraction` (`placeID`),
  CONSTRAINT `fk_request_ra` FOREIGN KEY (`requestID`) REFERENCES `Request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestAttraction`
--

LOCK TABLES `requestAttraction` WRITE;
/*!40000 ALTER TABLE `requestAttraction` DISABLE KEYS */;
INSERT INTO `requestAttraction` VALUES ('39421e5c-2dc3-4e24-ab95-4cc3f87250cb','r1849830'),('7e3ae615-403c-46be-85c3-7d4f7c6a20d4','r1849830'),('98aee8fe-e0a9-4543-95f3-b73a06b2118a','r1849830'),('cb9d9be1-3bf7-4a58-9225-512fc6d8dff2','r1849830'),('1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b','w215801333'),('39421e5c-2dc3-4e24-ab95-4cc3f87250cb','w215801333'),('7e3ae615-403c-46be-85c3-7d4f7c6a20d4','w215801333'),('98aee8fe-e0a9-4543-95f3-b73a06b2118a','w215801333'),('cb9d9be1-3bf7-4a58-9225-512fc6d8dff2','w215801333'),('d42cf56a-3ec8-4293-80bc-c5d394a61ae2','w215801333'),('eb82abf4-c5e7-4260-b137-56919bc9d8d7','w215801333'),('fa4aacee-96d2-4069-a288-45e37f6faa0b','w215801333'),('ff591483-b3f2-4a72-bdf0-166bd4be85ae','w215801333');
/*!40000 ALTER TABLE `requestAttraction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestCity`
--

DROP TABLE IF EXISTS `requestCity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requestCity` (
  `requestID` varchar(45) NOT NULL,
  `cityID` varchar(45) NOT NULL,
  PRIMARY KEY (`requestID`,`cityID`),
  KEY `fk_city_rc_idx` (`cityID`),
  CONSTRAINT `fk_city_rc` FOREIGN KEY (`cityID`) REFERENCES `City` (`placeID`),
  CONSTRAINT `fk_request_rc` FOREIGN KEY (`requestID`) REFERENCES `Request` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestCity`
--

LOCK TABLES `requestCity` WRITE;
/*!40000 ALTER TABLE `requestCity` DISABLE KEYS */;
INSERT INTO `requestCity` VALUES ('1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b','r41485'),('39421e5c-2dc3-4e24-ab95-4cc3f87250cb','r41485'),('7e3ae615-403c-46be-85c3-7d4f7c6a20d4','r41485'),('98aee8fe-e0a9-4543-95f3-b73a06b2118a','r41485'),('cb9d9be1-3bf7-4a58-9225-512fc6d8dff2','r41485'),('d42cf56a-3ec8-4293-80bc-c5d394a61ae2','r41485'),('eb82abf4-c5e7-4260-b137-56919bc9d8d7','r41485'),('fa4aacee-96d2-4069-a288-45e37f6faa0b','r41485'),('ff591483-b3f2-4a72-bdf0-166bd4be85ae','r41485');
/*!40000 ALTER TABLE `requestCity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requestType`
--

DROP TABLE IF EXISTS `requestType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `requestType` (
  `requestID` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  PRIMARY KEY (`requestID`,`type`),
  KEY `fk_type_rt_idx` (`type`),
  CONSTRAINT `fk_request_rt` FOREIGN KEY (`requestID`) REFERENCES `Request` (`id`),
  CONSTRAINT `fk_type_rt` FOREIGN KEY (`type`) REFERENCES `Type` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requestType`
--

LOCK TABLES `requestType` WRITE;
/*!40000 ALTER TABLE `requestType` DISABLE KEYS */;
INSERT INTO `requestType` VALUES ('cb9d9be1-3bf7-4a58-9225-512fc6d8dff2','City'),('d42cf56a-3ec8-4293-80bc-c5d394a61ae2','City'),('1f2cdcaa-e852-43c8-a0c4-b766ff6eb10b','On The Road'),('eb82abf4-c5e7-4260-b137-56919bc9d8d7','On The Road'),('fa4aacee-96d2-4069-a288-45e37f6faa0b','On The Road'),('ff591483-b3f2-4a72-bdf0-166bd4be85ae','On The Road');
/*!40000 ALTER TABLE `requestType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Type`
--

DROP TABLE IF EXISTS `Type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Type` (
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Type`
--

LOCK TABLES `Type` WRITE;
/*!40000 ALTER TABLE `Type` DISABLE KEYS */;
INSERT INTO `Type` VALUES ('City'),('Culture'),('Nature'),('On The Road'),('Relax');
/*!40000 ALTER TABLE `Type` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-21 14:36:17
