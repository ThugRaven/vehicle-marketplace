-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema vehicle_marketplace_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `vehicle_marketplace_db` ;

-- -----------------------------------------------------
-- Schema vehicle_marketplace_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `vehicle_marketplace_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci ;
USE `vehicle_marketplace_db` ;

-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`user_role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`user_role` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`user_role` (
  `id_user_role` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_user_role`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`user` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` CHAR(73) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `id_user_role` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(15) NOT NULL,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `archived` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_user`),
  CONSTRAINT `fk_user_user_role`
    FOREIGN KEY (`id_user_role`)
    REFERENCES `vehicle_marketplace_db`.`user_role` (`id_user_role`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_user_user_role_idx` ON `vehicle_marketplace_db`.`user` (`id_user_role` ASC);

CREATE UNIQUE INDEX `login_UNIQUE` ON `vehicle_marketplace_db`.`user` (`login` ASC);


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`brand`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`brand` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`brand` (
  `id_brand` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_brand`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`model`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`model` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`model` (
  `id_model` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `id_brand` INT NOT NULL,
  PRIMARY KEY (`id_model`),
  CONSTRAINT `fk_model_brand1`
    FOREIGN KEY (`id_brand`)
    REFERENCES `vehicle_marketplace_db`.`brand` (`id_brand`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_model_brand1_idx` ON `vehicle_marketplace_db`.`model` (`id_brand` ASC);


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`generation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`generation` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`generation` (
  `id_generation` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `production_start` SMALLINT NOT NULL,
  `production_end` SMALLINT NULL,
  `id_model` INT NOT NULL,
  PRIMARY KEY (`id_generation`),
  CONSTRAINT `fk_generation_model1`
    FOREIGN KEY (`id_model`)
    REFERENCES `vehicle_marketplace_db`.`model` (`id_model`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_generation_model1_idx` ON `vehicle_marketplace_db`.`generation` (`id_model` ASC);


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`body_style`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`body_style` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`body_style` (
  `id_body_style` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_body_style`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`offer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`offer` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`offer` (
  `id_offer` INT NOT NULL AUTO_INCREMENT,
  `id_brand` INT NOT NULL,
  `id_model` INT NOT NULL,
  `id_generation` INT NOT NULL,
  `id_user` INT NOT NULL,
  `title` VARCHAR(70) NULL,
  `image` BLOB NULL,
  `price` INT NOT NULL,
  `city` VARCHAR(255) NOT NULL,
  `production_year` YEAR NOT NULL,
  `mileage` INT NOT NULL,
  `displacement` FLOAT NOT NULL,
  `power` SMALLINT NOT NULL,
  `fuel` VARCHAR(20) NOT NULL,
  `transmission` VARCHAR(15) NOT NULL,
  `drive` VARCHAR(3) NOT NULL,
  `id_body_style` INT NOT NULL,
  `doors` TINYINT NOT NULL,
  `seats` TINYINT NOT NULL,
  `color` VARCHAR(20) NOT NULL,
  `color_type` VARCHAR(20) NULL,
  `description` TEXT NULL,
  `license_plate` VARCHAR(15) NULL,
  `vin` CHAR(17) NOT NULL,
  `first_registration` DATE NULL,
  `is_new` TINYINT(1) NOT NULL,
  `is_damaged` TINYINT(1) NULL,
  `is_accident_free` TINYINT(1) NULL,
  `is_first_owner` TINYINT(1) NULL,
  `is_registered` TINYINT(1) NULL,
  `is_right_hand_drive` TINYINT(1) NULL,
  `archived` TINYINT(1) NOT NULL DEFAULT 0,
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_offer`),
  CONSTRAINT `fk_offer_generation1`
    FOREIGN KEY (`id_generation`)
    REFERENCES `vehicle_marketplace_db`.`generation` (`id_generation`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_body_style1`
    FOREIGN KEY (`id_body_style`)
    REFERENCES `vehicle_marketplace_db`.`body_style` (`id_body_style`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_user1`
    FOREIGN KEY (`id_user`)
    REFERENCES `vehicle_marketplace_db`.`user` (`id_user`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_model1`
    FOREIGN KEY (`id_model`)
    REFERENCES `vehicle_marketplace_db`.`model` (`id_model`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_brand1`
    FOREIGN KEY (`id_brand`)
    REFERENCES `vehicle_marketplace_db`.`brand` (`id_brand`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_offer_generation1_idx` ON `vehicle_marketplace_db`.`offer` (`id_generation` ASC);

CREATE INDEX `fk_offer_body_style1_idx` ON `vehicle_marketplace_db`.`offer` (`id_body_style` ASC);

CREATE INDEX `fk_offer_user1_idx` ON `vehicle_marketplace_db`.`offer` (`id_user` ASC);

CREATE INDEX `fk_offer_model1_idx` ON `vehicle_marketplace_db`.`offer` (`id_model` ASC);

CREATE INDEX `fk_offer_brand1_idx` ON `vehicle_marketplace_db`.`offer` (`id_brand` ASC);


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`equipment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`equipment` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`equipment` (
  `id_equipment` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_equipment`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `vehicle_marketplace_db`.`offer_equipment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `vehicle_marketplace_db`.`offer_equipment` ;

CREATE TABLE IF NOT EXISTS `vehicle_marketplace_db`.`offer_equipment` (
  `id_offer` INT NOT NULL,
  `id_equipment` INT NOT NULL,
  CONSTRAINT `fk_offer_equipment_offer1`
    FOREIGN KEY (`id_offer`)
    REFERENCES `vehicle_marketplace_db`.`offer` (`id_offer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_offer_equipment_equipment1`
    FOREIGN KEY (`id_equipment`)
    REFERENCES `vehicle_marketplace_db`.`equipment` (`id_equipment`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_offer_equipment_offer1_idx` ON `vehicle_marketplace_db`.`offer_equipment` (`id_offer` ASC);

CREATE INDEX `fk_offer_equipment_equipment1_idx` ON `vehicle_marketplace_db`.`offer_equipment` (`id_equipment` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`user_role`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`user_role` (`id_user_role`, `role_name`) VALUES (1, 'user');
INSERT INTO `vehicle_marketplace_db`.`user_role` (`id_user_role`, `role_name`) VALUES (2, 'admin');

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`user` (`id_user`, `login`, `password`, `email`, `id_user_role`, `name`, `surname`, `phone_number`, `create_time`, `archived`) VALUES (1, 'admin', 'ef4cf0fd:4f230caf9dd674891e057a810500f5cc2ce550d45f36d62a6a72cbc8788d26e7', 'admin@gmail.com', 2, 'Kamil', 'Wesołowski', '48123456789', '2021-11-18 21:05:00', 0);

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`brand`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`brand` (`id_brand`, `name`) VALUES (1, 'Seat');

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`model`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`model` (`id_model`, `name`, `id_brand`) VALUES (1, 'Ibiza', 1);
INSERT INTO `vehicle_marketplace_db`.`model` (`id_model`, `name`, `id_brand`) VALUES (2, 'Leon', 1);

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`generation`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (1, 'I', 1985, 1993, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (2, 'II', 1993, 1999, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (3, 'II FL', 1999, 2002, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (4, 'III', 2002, 2008, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (5, 'IV', 2008, 2017, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (6, 'V', 2017, NULL, 1);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (7, 'I', 1999, 2005, 2);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (8, 'II', 2005, 2012, 2);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (9, 'III', 2012, 2020, 2);
INSERT INTO `vehicle_marketplace_db`.`generation` (`id_generation`, `name`, `production_start`, `production_end`, `id_model`) VALUES (10, 'IV', 2020, NULL, 2);

COMMIT;

