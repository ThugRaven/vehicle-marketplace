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
  `image` VARCHAR(100) NULL,
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
INSERT INTO `vehicle_marketplace_db`.`user` (`id_user`, `login`, `password`, `email`, `id_user_role`, `name`, `surname`, `phone_number`, `create_time`, `archived`) VALUES (2, 'user', '7c0d04d7:a6593a7bd6ad399df60521d09f98f54f9cbc885210cc0160a728d552b2820b16', 'user@gmail.com', 1, 'Name', 'Surname', '48123123123', '2022-01-06 16:04:51', 0);
INSERT INTO `vehicle_marketplace_db`.`user` (`id_user`, `login`, `password`, `email`, `id_user_role`, `name`, `surname`, `phone_number`, `create_time`, `archived`) VALUES (3, 'kamil', '9890a311:2cb3a8f9c9fe56eb6126718be21c87585da9568eb828f745e3b4bb5cd6fc3ac4', 'kamil@gmail.com', 2, 'Kamil', 'Wesołowski', '48987654321', '2022-01-06 16:11:10', 0);

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


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`body_style`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (1, 'Auta małe');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (2, 'Auta miejskie');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (3, 'Coupe');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (4, 'Kabriolet');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (5, 'Kombi');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (6, 'Kompakt');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (7, 'Minivan');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (8, 'Sedan');
INSERT INTO `vehicle_marketplace_db`.`body_style` (`id_body_style`, `name`) VALUES (9, 'SUV');

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`offer`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`offer` (`id_offer`, `id_brand`, `id_model`, `id_generation`, `id_user`, `title`, `image`, `price`, `city`, `production_year`, `mileage`, `displacement`, `power`, `fuel`, `transmission`, `drive`, `id_body_style`, `doors`, `seats`, `color`, `color_type`, `description`, `license_plate`, `vin`, `first_registration`, `is_new`, `is_damaged`, `is_accident_free`, `is_first_owner`, `is_registered`, `is_right_hand_drive`, `archived`, `create_time`, `update_time`) VALUES (1, 1, 1, 4, 1, 'Seat Ibiza III 1.2 64KM', 'DSC_3777-8336529144170014815.jpg', 4500, 'Sosnowiec', 2002, 179000, 1198, 64, 'Benzyna', 'Manualna', 'FWD', 2, 3, 5, 'Srebrny', 'Metalik', 'Witam, do sprzedania mam Seat\'a Ibizę z 2002 roku. Wszystko działa, serdecznie polecam!', 'SO1892S', 'VSSZZZ6LZ4R191497', '2002-01-01', 0, 0, 0, 0, 1, 0, 0, '2022-01-06 00:21:15', '2022-01-06 00:21:15');

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`equipment`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (1, 'ABS');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (2, 'Alarm');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (3, 'Alufelgi');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (4, 'ASR (kontrola trakcji)');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (5, 'Asystent parkowania');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (6, 'Asystent pasa ruchu');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (7, 'Bluetooth');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (8, 'CD');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (9, 'Centralny zamek');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (10, 'Czujnik deszczu');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (11, 'Czujniki parkowania przednie');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (12, 'Czujniki parkowania tylne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (13, 'Czujniki martwego pola');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (14, 'Czujnik zmierzchu');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (15, 'Dach panoramiczny');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (16, 'Elektrochromatyczne lusterka boczne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (17, 'Elektrochromatyczne lusterko wsteczne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (18, 'Elektryczne szyby przednie');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (19, 'Elektryczne szyby tylne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (20, 'Elektrycznie ustawiane fotele');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (21, 'Elektrycznie ustawiane lusterka');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (22, 'ESP (stabilizacja toru jazdy)');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (23, 'Gniazdo AUX');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (24, 'Gniazdo SD');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (25, 'Gniazdo USB');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (26, 'Hak');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (27, 'HUD (wyświetlacz przezierny)');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (28, 'Immobilizer');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (29, 'Isofix');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (30, 'Kamera cofania');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (31, 'Klimatyzacja automatyczna');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (32, 'Klimatyzacja czterostrefowa');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (33, 'Klimatyzacja dwustrefowa');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (34, 'Klimatyzacja manualna');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (35, 'Komputer pokładowy');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (36, 'Kurtyny powietrzne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (37, 'MP3');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (38, 'Nawigacja GPS');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (39, 'Odtwarzacz DVD');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (40, 'Ogranicznik prędkości');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (41, 'Ogrzewanie postojowe');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (42, 'Podgrzewana przednia szyba');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (43, 'Podgrzewane lusterka boczne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (44, 'Podgrzewane przednie siedzenia');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (45, 'Podgrzewane tylne siedzenia');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (46, 'Poduszka powietrzna chroniąca kolana');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (47, 'Poduszka powietrzna kierowcy');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (48, 'Poduszka powietrzna pasażera');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (49, 'Poduszki boczne przednie');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (50, 'Poduszki boczne tylne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (51, 'Przyciemniane szyby');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (52, 'Radio fabryczne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (53, 'Radio niefabryczne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (54, 'Regulowane zawieszenie');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (55, 'Relingi dachowe');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (56, 'System Start-Stop');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (57, 'Szyberdach');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (58, 'Tapicerka skórzana');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (59, 'Tapicerka welurowa');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (60, 'Tempomat');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (61, 'Tempomat aktywny');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (62, 'Tuner TV');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (63, 'Wielofunkcyjna kierownica');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (64, 'Wspomaganie kierownicy');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (65, 'Zmieniarka CD');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (66, 'Łopatki zmiany biegów');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (67, 'Światła do jazdy dziennej');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (68, 'Światła LED');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (69, 'Światła przeciwmgielne');
INSERT INTO `vehicle_marketplace_db`.`equipment` (`id_equipment`, `name`) VALUES (70, 'Światła Xenonowe');

COMMIT;


-- -----------------------------------------------------
-- Data for table `vehicle_marketplace_db`.`offer_equipment`
-- -----------------------------------------------------
START TRANSACTION;
USE `vehicle_marketplace_db`;
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 1);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 2);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 3);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 9);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 18);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 28);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 34);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 47);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 48);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 53);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 64);
INSERT INTO `vehicle_marketplace_db`.`offer_equipment` (`id_offer`, `id_equipment`) VALUES (1, 69);

COMMIT;

