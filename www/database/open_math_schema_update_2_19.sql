SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

ALTER TABLE `openmath`.`user_profiles` CHARACTER SET = utf8 , COLLATE = latin1_swedish_ci , DROP COLUMN `website` , DROP COLUMN `country` , ADD COLUMN `first_name` VARCHAR(40) NOT NULL  AFTER `id` , ADD COLUMN `last_name` VARCHAR(40) NOT NULL  AFTER `first_name` , CHANGE COLUMN `user_id` `user_id` INT(11) NOT NULL AUTO_INCREMENT  FIRST , CHANGE COLUMN `id` `id` INT(11) NULL DEFAULT NULL  
, DROP PRIMARY KEY 
, ADD PRIMARY KEY (`user_id`) 
, ADD UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) ;

CREATE  TABLE IF NOT EXISTS `openmath`.`classes` (
  `class_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `subject` VARCHAR(50) NULL DEFAULT NULL ,
  `schools_school_id` INT(10) UNSIGNED NOT NULL ,
  PRIMARY KEY (`class_id`, `schools_school_id`) ,
  UNIQUE INDEX `class_id_UNIQUE` (`class_id` ASC) ,
  INDEX `fk_classes_schools1` (`schools_school_id` ASC) ,
  CONSTRAINT `fk_classes_schools1`
    FOREIGN KEY (`schools_school_id` )
    REFERENCES `openmath`.`schools` (`school_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_members` (
  `classes_class_id` INT(10) UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `role` INT(10) UNSIGNED NOT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools` (
  `school_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(50) NOT NULL ,
  PRIMARY KEY (`school_id`) ,
  UNIQUE INDEX `school_id_UNIQUE` (`school_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`documents` (
  `doc_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `upload_date` DATETIME NOT NULL ,
  `owner_user_id` INT(11) NOT NULL ,
  `material_type` INT(11) NULL DEFAULT NULL ,
  `doc_type` INT(11) NULL DEFAULT NULL ,
  `public` TINYINT(1) NULL DEFAULT NULL ,
  `file` MEDIUMBLOB NULL DEFAULT NULL ,
  `img_preview` BINARY NULL DEFAULT NULL ,
  `description` VARCHAR(300) NULL DEFAULT NULL ,
  `size` INT(10) UNSIGNED NULL DEFAULT NULL ,
  `docname` VARCHAR(60) NULL DEFAULT NULL ,
  PRIMARY KEY (`doc_id`, `owner_user_id`) ,
  UNIQUE INDEX `doc_id_UNIQUE` (`doc_id` ASC) ,
  INDEX `fk_documents_users1` (`owner_user_id` ASC) ,
  CONSTRAINT `fk_documents_users1`
    FOREIGN KEY (`owner_user_id` )
    REFERENCES `openmath`.`user_profiles` (`user_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments` (
  `assignment_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `documents_doc_id` INT(10) UNSIGNED NOT NULL ,
  `assign_date` DATETIME NULL DEFAULT NULL ,
  `due_date` DATETIME NULL DEFAULT NULL ,
  `total_points` SMALLINT(6) NULL DEFAULT NULL ,
  `classes_class_id` INT(10) UNSIGNED NOT NULL ,
  `student_doc` MEDIUMBLOB NULL DEFAULT NULL ,
  `teacher_doc` MEDIUMBLOB NULL DEFAULT NULL ,
  `notes` VARCHAR(300) NULL DEFAULT NULL ,
  `assignment_name` VARCHAR(60) NULL DEFAULT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`assignments_and_users` (
  `assignments_assignment_id` INT(10) UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `auto_graded` TINYINT(1) NOT NULL ,
  `teacher_reviewed` TINYINT(1) NOT NULL ,
  `auto_grade_points` SMALLINT(6) NULL DEFAULT NULL ,
  `teacher_review_points` SMALLINT(6) NULL DEFAULT NULL ,
  `extra_credit` SMALLINT(6) NULL DEFAULT NULL ,
  `comments` VARCHAR(400) NULL DEFAULT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_doc_sharing` (
  `documents_doc_id` INT(10) UNSIGNED NOT NULL ,
  `classes_class_id` INT(10) UNSIGNED NOT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`user_doc_sharing` (
  `users_user_id` INT(11) NOT NULL ,
  `documents_doc_id` INT(10) UNSIGNED NOT NULL ,
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
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`school_doc_sharing` (
  `documents_doc_id` INT(10) UNSIGNED NOT NULL ,
  `schools_school_id` INT(10) UNSIGNED NOT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

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
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

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
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`tags` (
  `tag` CHAR(50) NOT NULL ,
  `tag_id` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`tag_id`) ,
  UNIQUE INDEX `tag_id_UNIQUE` (`tag_id` ASC) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`schools_and_users` (
  `schools_school_id` INT(10) UNSIGNED NOT NULL ,
  `users_user_id` INT(11) NOT NULL ,
  `role` INT(10) UNSIGNED NOT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`document_tags` (
  `tags_tag_id` INT(10) UNSIGNED NOT NULL ,
  `documents_doc_id` INT(10) UNSIGNED NOT NULL ,
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
ENGINE = InnoDB
DEFAULT CHARACTER SET =utf8
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `openmath`.`class_enrollments` (
  `classes_class_id` INT(10) UNSIGNED NOT NULL ,
  `start_date` DATE NULL DEFAULT NULL ,
  `end_date` DATE NULL DEFAULT NULL ,
  `salt` CHAR(32) NOT NULL ,
  `password` CHAR(32) NOT NULL ,
  INDEX `fk_class_enrollments_classes1` (`classes_class_id` ASC) ,
  PRIMARY KEY (`classes_class_id`) ,
  CONSTRAINT `fk_class_enrollments_classes1`
    FOREIGN KEY (`classes_class_id` )
    REFERENCES `openmath`.`classes` (`class_id` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET =utf8
COLLATE = latin1_swedish_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
