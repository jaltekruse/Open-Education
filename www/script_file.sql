-- MySQL dump 10.13  Distrib 5.5.24, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: openmath
-- ------------------------------------------------------
-- Server version	5.5.24-0ubuntu0.12.04.1

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
-- Table structure for table `assignments`
--

DROP TABLE IF EXISTS `assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignments` (
  `assignment_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `assign_date` datetime DEFAULT NULL,
  `due_date` datetime DEFAULT NULL,
  `total_points` smallint(6) DEFAULT NULL,
  `classes_class_id` int(10) unsigned NOT NULL,
  `teacher_doc` mediumblob,
  `student_doc` mediumblob,
  `notes` varchar(300) DEFAULT NULL,
  `assignment_name` varchar(60) DEFAULT NULL,
  `documents_doc_id` int(10) unsigned DEFAULT NULL,
  `assignment_summary` mediumblob,
  PRIMARY KEY (`assignment_id`,`classes_class_id`),
  UNIQUE KEY `assignment_id_UNIQUE` (`assignment_id`),
  KEY `fk_assignments_classes1` (`classes_class_id`),
  CONSTRAINT `fk_assignments_classes1` FOREIGN KEY (`classes_class_id`) REFERENCES `classes` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `assignments_and_users`
--

DROP TABLE IF EXISTS `assignments_and_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assignments_and_users` (
  `assignments_assignment_id` int(10) unsigned NOT NULL,
  `users_user_id` int(11) NOT NULL,
  `auto_graded` tinyint(1) NOT NULL,
  `teacher_reviewed` tinyint(1) NOT NULL,
  `auto_grade_points` smallint(6) DEFAULT NULL,
  `teacher_review_points` smallint(6) DEFAULT NULL,
  `extra_credit` smallint(6) DEFAULT NULL,
  `comments` varchar(400) DEFAULT NULL,
  `submit_time` datetime NOT NULL,
  `student_doc` mediumblob NOT NULL,
  `corrected_doc` mediumblob NOT NULL,
  PRIMARY KEY (`assignments_assignment_id`,`users_user_id`),
  KEY `fk_assignments_and_users_assignments1` (`assignments_assignment_id`),
  KEY `fk_assignments_and_users_users1` (`users_user_id`),
  CONSTRAINT `fk_assignments_and_users_assignments1` FOREIGN KEY (`assignments_assignment_id`) REFERENCES `assignments` (`assignment_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_assignments_and_users_users1` FOREIGN KEY (`users_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ci_sessions`
--

DROP TABLE IF EXISTS `ci_sessions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ci_sessions` (
  `session_id` varchar(40) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `ip_address` varchar(16) COLLATE utf8_bin NOT NULL DEFAULT '0',
  `user_agent` varchar(150) COLLATE utf8_bin NOT NULL,
  `last_activity` int(10) unsigned NOT NULL DEFAULT '0',
  `user_data` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_doc_sharing`
--

DROP TABLE IF EXISTS `class_doc_sharing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class_doc_sharing` (
  `documents_doc_id` int(10) unsigned NOT NULL,
  `classes_class_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`documents_doc_id`,`classes_class_id`),
  KEY `fk_documents_has_classes_classes1` (`classes_class_id`),
  KEY `fk_documents_has_classes_documents1` (`documents_doc_id`),
  CONSTRAINT `fk_documents_has_classes_classes1` FOREIGN KEY (`classes_class_id`) REFERENCES `classes` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_classes_documents1` FOREIGN KEY (`documents_doc_id`) REFERENCES `documents` (`doc_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_enrollments`
--

DROP TABLE IF EXISTS `class_enrollments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class_enrollments` (
  `classes_class_id` int(10) unsigned NOT NULL,
  `start_date` date DEFAULT NULL,
  `end_date` date DEFAULT NULL,
  `salt` char(32) DEFAULT NULL,
  `password` char(32) DEFAULT NULL,
  PRIMARY KEY (`classes_class_id`),
  KEY `fk_class_enrollments_classes1` (`classes_class_id`),
  CONSTRAINT `fk_class_enrollments_classes1` FOREIGN KEY (`classes_class_id`) REFERENCES `classes` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_members`
--

DROP TABLE IF EXISTS `class_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `class_members` (
  `classes_class_id` int(10) unsigned NOT NULL,
  `users_user_id` int(11) NOT NULL,
  `role` int(10) unsigned NOT NULL,
  PRIMARY KEY (`classes_class_id`,`users_user_id`),
  KEY `fk_classes_has_users_users1` (`users_user_id`),
  KEY `fk_classes_has_users_classes` (`classes_class_id`),
  CONSTRAINT `fk_classes_has_users_classes` FOREIGN KEY (`classes_class_id`) REFERENCES `classes` (`class_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_has_users_users1` FOREIGN KEY (`users_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classes` (
  `class_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(50) DEFAULT NULL,
  `schools_school_id` int(10) unsigned DEFAULT NULL,
  `class_hour` int(5) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `close_date` datetime DEFAULT NULL,
  PRIMARY KEY (`class_id`),
  UNIQUE KEY `class_id_UNIQUE` (`class_id`),
  KEY `fk_classes_schools1` (`schools_school_id`),
  CONSTRAINT `fk_classes_schools1` FOREIGN KEY (`schools_school_id`) REFERENCES `schools` (`school_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `colleagues`
--

DROP TABLE IF EXISTS `colleagues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `colleagues` (
  `users_user_id1` int(11) NOT NULL,
  `users_user_id2` int(11) NOT NULL,
  PRIMARY KEY (`users_user_id1`,`users_user_id2`),
  KEY `fk_users_has_users_users2` (`users_user_id2`),
  KEY `fk_users_has_users_users1` (`users_user_id1`),
  CONSTRAINT `fk_users_has_users_users1` FOREIGN KEY (`users_user_id1`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_users_users2` FOREIGN KEY (`users_user_id2`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=dec8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `document_tags`
--

DROP TABLE IF EXISTS `document_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document_tags` (
  `tags_tag_id` int(10) unsigned NOT NULL,
  `documents_doc_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`tags_tag_id`,`documents_doc_id`),
  KEY `fk_tags_has_documents_documents1` (`documents_doc_id`),
  KEY `fk_tags_has_documents_tags1` (`tags_tag_id`),
  CONSTRAINT `fk_tags_has_documents_documents1` FOREIGN KEY (`documents_doc_id`) REFERENCES `documents` (`doc_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_tags_has_documents_tags1` FOREIGN KEY (`tags_tag_id`) REFERENCES `tags` (`tag_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documents`
--

DROP TABLE IF EXISTS `documents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `documents` (
  `doc_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `upload_date` datetime NOT NULL,
  `owner_user_id` int(11) NOT NULL,
  `material_type` int(11) DEFAULT NULL,
  `public` tinyint(1) DEFAULT NULL,
  `file` mediumblob,
  `img_preview` binary(1) DEFAULT NULL,
  `description` varchar(300) DEFAULT NULL,
  `docname` varchar(120) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `doc_type` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`doc_id`,`owner_user_id`),
  UNIQUE KEY `doc_id_UNIQUE` (`doc_id`),
  KEY `fk_documents_users1` (`owner_user_id`),
  CONSTRAINT `fk_documents_users1` FOREIGN KEY (`owner_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `login_attempts`
--

DROP TABLE IF EXISTS `login_attempts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `login_attempts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip_address` varchar(40) COLLATE utf8_bin NOT NULL,
  `login` varchar(50) COLLATE utf8_bin NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pending_colleagues`
--

DROP TABLE IF EXISTS `pending_colleagues`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pending_colleagues` (
  `sender_user_id` int(11) NOT NULL,
  `receiver_user_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`sender_user_id`,`receiver_user_id`),
  KEY `fk_users_has_users_users4` (`receiver_user_id`),
  KEY `fk_users_has_users_users3` (`sender_user_id`),
  CONSTRAINT `fk_users_has_users_users3` FOREIGN KEY (`sender_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `pending_colleagues_ibfk_1` FOREIGN KEY (`receiver_user_id`) REFERENCES `user_profiles` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=hp8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `school_doc_sharing`
--

DROP TABLE IF EXISTS `school_doc_sharing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `school_doc_sharing` (
  `documents_doc_id` int(10) unsigned NOT NULL,
  `schools_school_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`documents_doc_id`,`schools_school_id`),
  KEY `fk_documents_has_schools_schools1` (`schools_school_id`),
  KEY `fk_documents_has_schools_documents1` (`documents_doc_id`),
  CONSTRAINT `fk_documents_has_schools_documents1` FOREIGN KEY (`documents_doc_id`) REFERENCES `documents` (`doc_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_schools_schools1` FOREIGN KEY (`schools_school_id`) REFERENCES `schools` (`school_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schools`
--

DROP TABLE IF EXISTS `schools`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schools` (
  `school_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`school_id`),
  UNIQUE KEY `school_id_UNIQUE` (`school_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `schools_and_users`
--

DROP TABLE IF EXISTS `schools_and_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schools_and_users` (
  `schools_school_id` int(10) unsigned NOT NULL,
  `users_user_id` int(11) NOT NULL,
  `role` int(10) unsigned NOT NULL,
  PRIMARY KEY (`schools_school_id`,`users_user_id`),
  KEY `fk_schools_has_users_users1` (`users_user_id`),
  KEY `fk_schools_has_users_schools1` (`schools_school_id`),
  CONSTRAINT `fk_schools_has_users_schools1` FOREIGN KEY (`schools_school_id`) REFERENCES `schools` (`school_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_schools_has_users_users1` FOREIGN KEY (`users_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `tag` varchar(50) DEFAULT NULL,
  `tag_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_id_UNIQUE` (`tag_id`),
  UNIQUE KEY `tag` (`tag`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_autologin`
--

DROP TABLE IF EXISTS `user_autologin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_autologin` (
  `key_id` char(32) COLLATE utf8_bin NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `user_agent` varchar(150) COLLATE utf8_bin NOT NULL,
  `last_ip` varchar(40) COLLATE utf8_bin NOT NULL,
  `last_login` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`key_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_doc_sharing`
--

DROP TABLE IF EXISTS `user_doc_sharing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_doc_sharing` (
  `users_user_id` int(11) NOT NULL,
  `documents_doc_id` int(10) unsigned NOT NULL,
  PRIMARY KEY (`users_user_id`,`documents_doc_id`),
  KEY `fk_users_has_documents_documents1` (`documents_doc_id`),
  KEY `fk_users_has_documents_users1` (`users_user_id`),
  CONSTRAINT `fk_users_has_documents_documents1` FOREIGN KEY (`documents_doc_id`) REFERENCES `documents` (`doc_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_documents_users1` FOREIGN KEY (`users_user_id`) REFERENCES `user_profiles` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=dec8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_profiles`
--

DROP TABLE IF EXISTS `user_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_profiles` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) DEFAULT NULL,
  `first_name` varchar(40) NOT NULL,
  `last_name` varchar(40) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=dec8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_bin NOT NULL,
  `password` varchar(255) COLLATE utf8_bin NOT NULL,
  `email` varchar(100) COLLATE utf8_bin NOT NULL,
  `activated` tinyint(1) NOT NULL DEFAULT '1',
  `banned` tinyint(1) NOT NULL DEFAULT '0',
  `ban_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `new_password_key` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `new_password_requested` datetime DEFAULT NULL,
  `new_email` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `new_email_key` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `last_ip` varchar(40) COLLATE utf8_bin NOT NULL,
  `last_login` datetime NOT NULL,
  `created` datetime NOT NULL,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-16 13:19:30
