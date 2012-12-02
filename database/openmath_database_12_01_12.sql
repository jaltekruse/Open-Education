SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `openmath` DEFAULT CHARACTER SET latin1 ;
USE `openmath` ;

-- -----------------------------------------------------
-- Table `openmath`.`schools`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`schools` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools` (
  `school_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`school_id`) ,
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`classes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`classes` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`classes` (
  `class_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `subject` VARCHAR(50) NULL DEFAULT NULL ,
  `school_id` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `class_hour` INT(5) NULL DEFAULT NULL ,
  `create_date` DATETIME NULL DEFAULT NULL ,
  `close_date` DATETIME NULL DEFAULT NULL ,
  PRIMARY KEY (`class_id`) ,
  UNIQUE INDEX `class_id_UNIQUE` (`class_id` ASC) ,
  INDEX `fk_classes_schools1` (`school_id` ASC) ,
  CONSTRAINT `fk_classes_schools1`
    FOREIGN KEY (`school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 71
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`user_profiles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`user_profiles` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_profiles` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `id` INT(11) NULL DEFAULT NULL ,
  `first_name` VARCHAR(40) NOT NULL ,
  `last_name` VARCHAR(40) NOT NULL ,
  PRIMARY KEY (`user_id`) ,
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 38
DEFAULT CHARACTER SET = dec8;


-- -----------------------------------------------------
-- Table `openmath`.`documents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`documents` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`documents` (
  `doc_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `upload_date` DATETIME NOT NULL ,
  `owner_user_id` INT(11) NOT NULL ,
  `material_type` INT(11) NULL DEFAULT NULL ,
  `public` TINYINT(1) NULL DEFAULT NULL ,
  `file_path` VARCHAR(300) NULL DEFAULT NULL ,
  `img_preview` BINARY(1) NULL DEFAULT NULL ,
  `description` VARCHAR(300) NULL DEFAULT NULL ,
  `docname` VARCHAR(120) NULL DEFAULT NULL ,
  `size` INT(11) NULL DEFAULT NULL ,
  `doc_type` VARCHAR(6) NULL DEFAULT NULL ,
  `file_mime` VARCHAR(100) NULL ,
  `root_comment_id` INT NOT NULL ,
  `number_of_ratings` INT UNSIGNED NOT NULL ,
  `average_rating` DOUBLE NULL ,
  PRIMARY KEY (`doc_id`) ,
  UNIQUE INDEX `doc_id_UNIQUE` (`doc_id` ASC) ,
  INDEX `fk_documents_users1` (`owner_user_id` ASC) ,
  INDEX `fk_documents_comments1` (`root_comment_id` ASC) ,
  CONSTRAINT `fk_documents_users1`
    FOREIGN KEY (`owner_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_comments1`
    FOREIGN KEY (`root_comment_id` )
    REFERENCES `openmath`.`comments` (`comment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 48
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`comments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`comments` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`comments` (
  `comment_id` INT NOT NULL AUTO_INCREMENT ,
  `title` VARCHAR(100) NULL ,
  `description` VARCHAR(500) NULL ,
  `doc_id` INT(10) UNSIGNED NULL ,
  `post_date` DATETIME NULL ,
  `user_id` INT(11) NOT NULL ,
  PRIMARY KEY (`comment_id`) ,
  INDEX `fk_comments_documents1` (`doc_id` ASC) ,
  INDEX `fk_comments_user_profiles1` (`user_id` ASC) ,
  CONSTRAINT `fk_comments_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_user_profiles1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`assignments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`assignments` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments` (
  `assignment_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `assign_date` DATETIME NULL DEFAULT NULL ,
  `due_date` DATETIME NULL DEFAULT NULL ,
  `total_points` SMALLINT(6) NULL DEFAULT NULL ,
  `class_id` INT(10) UNSIGNED NOT NULL ,
  `teacher_doc_path` VARCHAR(300) NULL DEFAULT NULL ,
  `student_doc_path` VARCHAR(300) NULL DEFAULT NULL ,
  `notes` VARCHAR(300) NULL DEFAULT NULL ,
  `assignment_name` VARCHAR(60) NULL DEFAULT NULL ,
  `assignment_summary_path` VARCHAR(300) NULL DEFAULT NULL ,
  `root_comment_id` INT NOT NULL ,
  PRIMARY KEY (`assignment_id`, `class_id`) ,
  UNIQUE INDEX `assignment_id_UNIQUE` (`assignment_id` ASC) ,
  INDEX `fk_assignments_classes1` (`class_id` ASC) ,
  INDEX `fk_assignments_comments1` (`root_comment_id` ASC) ,
  CONSTRAINT `fk_assignments_classes1`
    FOREIGN KEY (`class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_assignments_comments1`
    FOREIGN KEY (`root_comment_id` )
    REFERENCES `openmath`.`comments` (`comment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 8
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`assignments_and_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`assignments_and_users` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments_and_users` (
  `assignment_id` INT(10) UNSIGNED NOT NULL ,
  `user_id` INT(11) NOT NULL ,
  `auto_graded` TINYINT(1) NOT NULL ,
  `teacher_reviewed` TINYINT(1) NOT NULL ,
  `auto_grade_points` SMALLINT(6) NULL DEFAULT NULL ,
  `teacher_review_points` SMALLINT(6) NULL DEFAULT NULL ,
  `extra_credit` SMALLINT(6) NULL DEFAULT NULL ,
  `comments` VARCHAR(400) NULL DEFAULT NULL ,
  `submit_time` DATETIME NOT NULL ,
  `student_doc_path` VARCHAR(300) NOT NULL ,
  `corrected_doc_path` VARCHAR(300) NOT NULL ,
  PRIMARY KEY (`assignment_id`, `user_id`) ,
  INDEX `fk_assignments_and_users_assignments1` (`assignment_id` ASC) ,
  INDEX `fk_assignments_and_users_users1` (`user_id` ASC) ,
  CONSTRAINT `fk_assignments_and_users_assignments1`
    FOREIGN KEY (`assignment_id` )
    REFERENCES `openmath`.`assignments` (`assignment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_assignments_and_users_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`class_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_doc_sharing` (
  `doc_id` INT(10) UNSIGNED NOT NULL ,
  `class_id` INT(10) UNSIGNED NOT NULL ,
  `request_date` DATETIME NULL ,
  `accepted` TINYINT(1) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`doc_id`, `class_id`) ,
  INDEX `fk_documents_has_classes_classes1` (`class_id` ASC) ,
  INDEX `fk_documents_has_classes_documents1` (`doc_id` ASC) ,
  CONSTRAINT `fk_documents_has_classes_classes1`
    FOREIGN KEY (`class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_classes_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`class_enrollments`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_enrollments` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_enrollments` (
  `class_id` INT(10) UNSIGNED NOT NULL ,
  `start_date` DATE NULL DEFAULT NULL ,
  `end_date` DATE NULL DEFAULT NULL ,
  `salt` CHAR(32) NULL DEFAULT NULL ,
  `password` CHAR(32) NULL DEFAULT NULL ,
  PRIMARY KEY (`class_id`) ,
  INDEX `fk_class_enrollments_classes1` (`class_id` ASC) ,
  CONSTRAINT `fk_class_enrollments_classes1`
    FOREIGN KEY (`class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`class_members`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`class_members` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_members` (
  `class_id` INT(10) UNSIGNED NOT NULL ,
  `user_id` INT(11) NOT NULL ,
  `role` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`class_id`, `user_id`) ,
  INDEX `fk_classes_has_users_users1` (`user_id` ASC) ,
  INDEX `fk_classes_has_users_classes` (`class_id` ASC) ,
  CONSTRAINT `fk_classes_has_users_classes`
    FOREIGN KEY (`class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_classes_has_users_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`tag_radix_nodes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`tag_radix_nodes` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`tag_radix_nodes` (
  `node_id` INT NOT NULL AUTO_INCREMENT ,
  `character` CHAR(1) NULL ,
  `depth` INT UNSIGNED NULL ,
  PRIMARY KEY (`node_id`) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`tags` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`tags` (
  `tag` VARCHAR(50) NULL DEFAULT NULL ,
  `tag_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `use_count` INT UNSIGNED NULL ,
  `node_id` INT NOT NULL ,
  PRIMARY KEY (`tag_id`, `node_id`) ,
  UNIQUE INDEX `tag_id_UNIQUE` (`tag_id` ASC) ,
  UNIQUE INDEX `tag` (`tag` ASC) ,
  INDEX `fk_tags_tag_radix_nodes1` (`node_id` ASC) ,
  CONSTRAINT `fk_tags_tag_radix_nodes1`
    FOREIGN KEY (`node_id` )
    REFERENCES `openmath`.`tag_radix_nodes` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 77
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`document_tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`document_tags` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`document_tags` (
  `tag_id` INT(10) UNSIGNED NOT NULL ,
  `doc_id` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`tag_id`, `doc_id`) ,
  INDEX `fk_tags_has_documents_documents1` (`doc_id` ASC) ,
  INDEX `fk_tags_has_documents_tags1` (`tag_id` ASC) ,
  CONSTRAINT `fk_tags_has_documents_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tags_has_documents_tags1`
    FOREIGN KEY (`tag_id` )
    REFERENCES `openmath`.`tags` (`tag_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`colleagues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`colleagues` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`colleagues` (
  `sender_user_id` INT(11) NOT NULL ,
  `receiver_user_id` INT(11) NOT NULL ,
  `request_date` DATETIME NOT NULL ,
  `accepted` TINYINT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`sender_user_id`, `receiver_user_id`) ,
  INDEX `fk_users_has_users_users4` (`receiver_user_id` ASC) ,
  INDEX `fk_users_has_users_users3` (`sender_user_id` ASC) ,
  CONSTRAINT `fk_users_has_users_users3`
    FOREIGN KEY (`sender_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `pending_colleagues_ibfk_1`
    FOREIGN KEY (`receiver_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` ))
ENGINE = InnoDB
DEFAULT CHARACTER SET = hp8;


-- -----------------------------------------------------
-- Table `openmath`.`school_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`school_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`school_doc_sharing` (
  `doc_id` INT(10) UNSIGNED NOT NULL ,
  `school_id` INT(10) UNSIGNED NOT NULL ,
  `request_date` DATETIME NULL ,
  `accepted` TINYINT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`doc_id`, `school_id`) ,
  INDEX `fk_documents_has_schools_schools1` (`school_id` ASC) ,
  INDEX `fk_documents_has_schools_documents1` (`doc_id` ASC) ,
  CONSTRAINT `fk_documents_has_schools_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_schools_schools1`
    FOREIGN KEY (`school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`schools_and_users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`schools_and_users` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools_and_users` (
  `school_id` INT(10) UNSIGNED NOT NULL ,
  `user_id` INT(11) NOT NULL ,
  `role` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`school_id`, `user_id`) ,
  INDEX `fk_schools_has_users_users1` (`user_id` ASC) ,
  INDEX `fk_schools_has_users_schools1` (`school_id` ASC) ,
  CONSTRAINT `fk_schools_has_users_schools1`
    FOREIGN KEY (`school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_schools_has_users_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`user_doc_sharing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`user_doc_sharing` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_doc_sharing` (
  `user_id` INT(11) NOT NULL ,
  `doc_id` INT(10) UNSIGNED NOT NULL ,
  `request_date` DATETIME NOT NULL ,
  `accepted` TINYINT(1) NULL DEFAULT '0' ,
  PRIMARY KEY (`user_id`, `doc_id`) ,
  INDEX `fk_users_has_documents_documents1` (`doc_id` ASC) ,
  INDEX `fk_users_has_documents_users1` (`user_id` ASC) ,
  CONSTRAINT `fk_users_has_documents_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_documents_users1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = dec8;


-- -----------------------------------------------------
-- Table `openmath`.`doc_revision_edges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`doc_revision_edges` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`doc_revision_edges` (
  `ancestor` INT(10) UNSIGNED NOT NULL ,
  `descendant` INT(10) UNSIGNED NOT NULL ,
  `distance` INT(10) NULL ,
  PRIMARY KEY (`ancestor`, `descendant`) ,
  INDEX `fk_documents_has_documents_documents2` (`descendant` ASC) ,
  INDEX `fk_documents_has_documents_documents1` (`ancestor` ASC) ,
  CONSTRAINT `fk_documents_has_documents_documents1`
    FOREIGN KEY (`ancestor` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_documents_documents2`
    FOREIGN KEY (`descendant` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `openmath`.`comment_edges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`comment_edges` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`comment_edges` (
  `ancestor` INT NOT NULL ,
  `descendant` INT NOT NULL ,
  `distance` INT(11) NULL ,
  PRIMARY KEY (`ancestor`, `descendant`) ,
  INDEX `fk_comments_has_comments_comments2` (`descendant` ASC) ,
  INDEX `fk_comments_has_comments_comments1` (`ancestor` ASC) ,
  CONSTRAINT `fk_comments_has_comments_comments1`
    FOREIGN KEY (`ancestor` )
    REFERENCES `openmath`.`comments` (`comment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_comments_has_comments_comments2`
    FOREIGN KEY (`descendant` )
    REFERENCES `openmath`.`comments` (`comment_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `openmath`.`doc_ratings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`doc_ratings` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`doc_ratings` (
  `doc_id` INT(10) UNSIGNED NOT NULL ,
  `user_id` INT(11) NOT NULL ,
  `rating` INT(4) NULL ,
  `review_title` VARCHAR(100) NULL ,
  `review_body` VARCHAR(1000) NULL ,
  PRIMARY KEY (`doc_id`, `user_id`) ,
  INDEX `fk_documents_has_user_profiles_user_profiles1` (`user_id` ASC) ,
  INDEX `fk_documents_has_user_profiles_documents1` (`doc_id` ASC) ,
  CONSTRAINT `fk_documents_has_user_profiles_documents1`
    FOREIGN KEY (`doc_id` )
    REFERENCES `openmath`.`documents` (`doc_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_documents_has_user_profiles_user_profiles1`
    FOREIGN KEY (`user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


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
AUTO_INCREMENT = 3
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
  `last_login` DATETIME NOT NULL ,
  `created` DATETIME NOT NULL ,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 39
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `openmath`.`tag_radi_edges`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `openmath`.`tag_radi_edges` ;

CREATE  TABLE IF NOT EXISTS `openmath`.`tag_radi_edges` (
  `ancestor` INT NOT NULL ,
  `descendant` INT NOT NULL ,
  `distance` INT UNSIGNED NULL ,
  PRIMARY KEY (`ancestor`, `descendant`) ,
  INDEX `fk_tag_radix_nodes_has_tag_radix_nodes_tag_radix_nodes2` (`descendant` ASC) ,
  INDEX `fk_tag_radix_nodes_has_tag_radix_nodes_tag_radix_nodes1` (`ancestor` ASC) ,
  CONSTRAINT `fk_tag_radix_nodes_has_tag_radix_nodes_tag_radix_nodes1`
    FOREIGN KEY (`ancestor` )
    REFERENCES `openmath`.`tag_radix_nodes` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_radix_nodes_has_tag_radix_nodes_tag_radix_nodes2`
    FOREIGN KEY (`descendant` )
    REFERENCES `openmath`.`tag_radix_nodes` (`node_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
