SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `openmath` DEFAULT CHARACTER SET latin1 ;
USE `openmath` ;

-- -----------------------------------------------------
-- Table `openmath`.`user_profiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`user_profiles` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_profiles` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `id` INT(11) NULL ,
  `first_name` VARCHAR(40) NOT NULL ,
  `last_name` VARCHAR(40) NOT NULL ,
  PRIMARY KEY (`user_id`) ,
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = dec8;


-- -----------------------------------------------------
-- Table `openmath`.`schools`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`schools` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools` (
  `school_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`school_id`) ,
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`classes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`classes` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`classes` (
  `class_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `subject` VARCHAR(50) NULL ,
  `schools_school_id` INT UNSIGNED NOT NULL ,
  PRIMARY KEY (`class_id`, `schools_school_id`) ,
  UNIQUE INDEX `class_id_UNIQUE` (`class_id` ASC) ,
  INDEX `fk_classes_schools1` (`schools_school_id` ASC) ,
  CONSTRAINT `fk_classes_schools1`
    FOREIGN KEY (`schools_school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`class_members`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_members` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_members` (
  `classes_class_id` INT UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `role` INT UNSIGNED NOT NULL ,
  INDEX `fk_classes_has_users_users1` (`users_user_id` ASC) ,
  INDEX `fk_classes_has_users_classes` (`classes_class_id` ASC) ,
  PRIMARY KEY (`classes_class_id`, `users_user_id`) ,
  CONSTRAINT `fk_classes_has_users_classes`
    FOREIGN KEY (`classes_class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_has_users_users1`
    FOREIGN KEY (`users_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`documents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`documents` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`documents` (
  `doc_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `upload_date` DATETIME NOT NULL ,
  `owner_user_id` INT(11) NOT NULL ,
  `material_type` INT NULL ,
  `doc_type` INT NULL ,
  `public` TINYINT(1) NULL ,
  `file` MEDIUMBLOB NULL ,
  `img_preview` BINARY NULL ,
  `description` VARCHAR(300) NULL ,
  `size` INT UNSIGNED NULL ,
  `docname` VARCHAR(60) NULL ,
  PRIMARY KEY (`doc_id`, `owner_user_id`) ,
  UNIQUE INDEX `doc_id_UNIQUE` (`doc_id` ASC) ,
  INDEX `fk_documents_users1` (`owner_user_id` ASC) ,
  CONSTRAINT `fk_documents_users1`
    FOREIGN KEY (`owner_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`assignments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`assignments` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments` (
  `assignment_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  `documents_doc_id` INT UNSIGNED NOT NULL ,
  `assign_date` DATETIME NULL ,
  `due_date` DATETIME NULL ,
  `total_points` SMALLINT NULL ,
  `classes_class_id` INT UNSIGNED NOT NULL ,
  `student_doc` MEDIUMBLOB NULL ,
  `teacher_doc` MEDIUMBLOB NULL ,
  `notes` VARCHAR(300) NULL ,
  `assignment_name` VARCHAR(60) NULL ,
  PRIMARY KEY (`assignment_id`, `classes_class_id`, `documents_doc_id`) ,
  UNIQUE INDEX `assignment_id_UNIQUE` (`assignment_id` ASC) ,
  INDEX `fk_assignments_documents1` (`documents_doc_id` ASC) ,
  INDEX `fk_assignments_classes1` (`classes_class_id` ASC) ,
  CONSTRAINT `fk_assignments_documents1`
    FOREIGN KEY (`documents_doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_assignments_classes1`
    FOREIGN KEY (`classes_class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`assignments_and_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`assignments_and_users` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments_and_users` (
  `assignments_assignment_id` INT UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `auto_graded` TINYINT(1) NOT NULL ,
  `teacher_reviewed` TINYINT(1) NOT NULL ,
  `auto_grade_points` SMALLINT NULL ,
  `teacher_review_points` SMALLINT NULL ,
  `extra_credit` SMALLINT NULL ,
  `comments` VARCHAR(400) NULL ,
  `submit_time` DATETIME NOT NULL ,
  `student_doc` MEDIUMBLOB NOT NULL ,
  `corrected_doc` MEDIUMBLOB NOT NULL ,
  INDEX `fk_assignments_and_users_assignments1` (`assignments_assignment_id` ASC) ,
  INDEX `fk_assignments_and_users_users1` (`users_user_id` ASC) ,
  PRIMARY KEY (`assignments_assignment_id`, `users_user_id`) ,
  CONSTRAINT `fk_assignments_and_users_assignments1`
    FOREIGN KEY (`assignments_assignment_id` )
    REFERENCES `openmath`.`assignments` (`assignment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_assignments_and_users_users1`
    FOREIGN KEY (`users_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`class_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_doc_sharing` (
  `documents_doc_id` INT UNSIGNED NOT NULL ,
  `classes_class_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_documents_has_classes_classes1` (`classes_class_id` ASC) ,
  INDEX `fk_documents_has_classes_documents1` (`documents_doc_id` ASC) ,
  PRIMARY KEY (`documents_doc_id`, `classes_class_id`) ,
  CONSTRAINT `fk_documents_has_classes_documents1`
    FOREIGN KEY (`documents_doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_classes_classes1`
    FOREIGN KEY (`classes_class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`user_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`user_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_doc_sharing` (
  `users_user_id` INT(11) NOT NULL ,
  `documents_doc_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_users_has_documents_documents1` (`documents_doc_id` ASC) ,
  INDEX `fk_users_has_documents_users1` (`users_user_id` ASC) ,
  PRIMARY KEY (`users_user_id`, `documents_doc_id`) ,
  CONSTRAINT `fk_users_has_documents_users1`
    FOREIGN KEY (`users_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_documents_documents1`
    FOREIGN KEY (`documents_doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = dec8;


-- -----------------------------------------------------
-- Table `openmath`.`school_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`school_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`school_doc_sharing` (
  `documents_doc_id` INT UNSIGNED NOT NULL ,
  `schools_school_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_documents_has_schools_schools1` (`schools_school_id` ASC) ,
  INDEX `fk_documents_has_schools_documents1` (`documents_doc_id` ASC) ,
  PRIMARY KEY (`documents_doc_id`, `schools_school_id`) ,
  CONSTRAINT `fk_documents_has_schools_documents1`
    FOREIGN KEY (`documents_doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_schools_schools1`
    FOREIGN KEY (`schools_school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`peers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`peers` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`peers` (
  `users_user_id1` INT(11) NOT NULL ,
  `users_user_id2` INT(11) NOT NULL ,
  INDEX `fk_users_has_users_users2` (`users_user_id2` ASC) ,
  INDEX `fk_users_has_users_users1` (`users_user_id1` ASC) ,
  PRIMARY KEY (`users_user_id1`, `users_user_id2`) ,
  CONSTRAINT `fk_users_has_users_users1`
    FOREIGN KEY (`users_user_id1` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_users_users2`
    FOREIGN KEY (`users_user_id2` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = dec8;


-- -----------------------------------------------------
-- Table `openmath`.`pending_peers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`pending_peers` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`pending_peers` (
  `sender_user_id` INT(11) NOT NULL ,
  `reciever_user_id1` INT(11) NOT NULL ,
  INDEX `fk_users_has_users_users4` (`reciever_user_id1` ASC) ,
  INDEX `fk_users_has_users_users3` (`sender_user_id` ASC) ,
  PRIMARY KEY (`sender_user_id`, `reciever_user_id1`) ,
  CONSTRAINT `fk_users_has_users_users3`
    FOREIGN KEY (`sender_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_users_users4`
    FOREIGN KEY (`reciever_user_id1` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = hp8;


-- -----------------------------------------------------
-- Table `openmath`.`tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`tags` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`tags` (
  `tag` CHAR(50) NOT NULL ,
  `tag_id` INT UNSIGNED NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`tag_id`) ,
  UNIQUE INDEX `tag_id_UNIQUE` (`tag_id` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`schools_and_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`schools_and_users` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools_and_users` (
  `schools_school_id` INT UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `role` INT UNSIGNED NOT NULL ,
  INDEX `fk_schools_has_users_users1` (`users_user_id` ASC) ,
  INDEX `fk_schools_has_users_schools1` (`schools_school_id` ASC) ,
  PRIMARY KEY (`schools_school_id`, `users_user_id`) ,
  CONSTRAINT `fk_schools_has_users_schools1`
    FOREIGN KEY (`schools_school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schools_has_users_users1`
    FOREIGN KEY (`users_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`document_tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`document_tags` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`document_tags` (
  `tags_tag_id` INT UNSIGNED NOT NULL ,
  `documents_doc_id` INT UNSIGNED NOT NULL ,
  INDEX `fk_tags_has_documents_documents1` (`documents_doc_id` ASC) ,
  INDEX `fk_tags_has_documents_tags1` (`tags_tag_id` ASC) ,
  PRIMARY KEY (`tags_tag_id`, `documents_doc_id`) ,
  CONSTRAINT `fk_tags_has_documents_tags1`
    FOREIGN KEY (`tags_tag_id` )
    REFERENCES `openmath`.`tags` (`tag_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tags_has_documents_documents1`
    FOREIGN KEY (`documents_doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`class_enrollments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_enrollments` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_enrollments` (
  `classes_class_id` INT UNSIGNED NOT NULL ,
  `start_date` DATE NULL ,
  `end_date` DATE NULL ,
  `salt` CHAR(32) NOT NULL ,
  `password` CHAR(32) NOT NULL ,
  INDEX `fk_class_enrollments_classes1` (`classes_class_id` ASC) ,
  PRIMARY KEY (`classes_class_id`) ,
  CONSTRAINT `fk_class_enrollments_classes1`
    FOREIGN KEY (`classes_class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`ci_sessions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`ci_sessions` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`ci_sessions` (
  `session_id` VARCHAR(40) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL DEFAULT '0' ,
  `ip_address` VARCHAR(16) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL DEFAULT '0' ,
  `user_agent` VARCHAR(150) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `last_activity` INT(10) UNSIGNED NOT NULL DEFAULT '0' ,
  `user_data` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  PRIMARY KEY (`session_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `openmath`.`login_attempts`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`login_attempts` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`login_attempts` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `ip_address` VARCHAR(40) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `login` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `openmath`.`user_autologin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`user_autologin` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_autologin` (
  `key_id` CHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `user_id` INT(11) NOT NULL DEFAULT '0' ,
  `user_agent` VARCHAR(150) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `last_ip` VARCHAR(40) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `last_login` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`key_id`, `user_id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `openmath`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`users` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `password` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `email` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `activated` TINYINT(1) NOT NULL DEFAULT '1' ,
  `banned` TINYINT(1) NOT NULL DEFAULT '0' ,
  `ban_reason` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `new_password_key` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `new_password_requested` DATETIME NULL DEFAULT NULL ,
  `new_email` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `new_email_key` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `last_ip` VARCHAR(40) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `last_login` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `created` DATETIME NOT NULL DEFAULT '0000-00-00 00:00:00' ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
