-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema task02
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `task02` ;

-- -----------------------------------------------------
-- Schema task02
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `task02` DEFAULT CHARACTER SET utf8 ;
USE `task02` ;

-- -----------------------------------------------------
-- Table `task02`.`countries`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`countries` (
  `id_country` INT(11) NOT NULL AUTO_INCREMENT,
`country` VARCHAR(100) NOT NULL,
PRIMARY KEY (`id_country`),
UNIQUE INDEX `id_country_UNIQUE` (`id_country` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task02`.`cities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`cities` (
  `id_city` INT(11) NOT NULL AUTO_INCREMENT,
`city` VARCHAR(100) NOT NULL,
`fid_country` INT(11) NOT NULL,
PRIMARY KEY (`id_city`),
INDEX `fk_cities_countries_idx` (`fid_country` ASC),
CONSTRAINT `fk_cities_countries`
FOREIGN KEY (`fid_country`)
REFERENCES `task02`.`countries` (`id_country`)
  ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task02`.`addresses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`addresses` (
  `id_address` INT(11) NOT NULL AUTO_INCREMENT,
`address` VARCHAR(200) NOT NULL,
`fid_city` INT(11) NOT NULL,
PRIMARY KEY (`id_address`),
INDEX `fk_addresses_cities1_idx` (`fid_city` ASC),
CONSTRAINT `fk_addresses_cities`
FOREIGN KEY (`fid_city`)
REFERENCES `task02`.`cities` (`id_city`)
  ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task02`.`companies`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`companies` (
  `id_company` INT(11) NOT NULL AUTO_INCREMENT,
`name` VARCHAR(100) NOT NULL,
PRIMARY KEY (`id_company`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task02`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`employees` (
  `id_employee` INT(11) NOT NULL AUTO_INCREMENT,
`first_name` VARCHAR(100) NOT NULL,
`last_name` VARCHAR(100) NOT NULL,
`fid_address` INT(11) NOT NULL,
PRIMARY KEY (`id_employee`),
INDEX `fk_employees_addresses1_idx` (`fid_address` ASC),
CONSTRAINT `fk_employees_addresses`
FOREIGN KEY (`fid_address`)
REFERENCES `task02`.`addresses` (`id_address`)
  ON DELETE CASCADE
  ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `task02`.`employee_register`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `task02`.`employee_register` (
  `id_register_entry` INT(11) NOT NULL AUTO_INCREMENT,
`fid_employee` INT(11) NOT NULL,
`fid_address` INT(11) NOT NULL,
`fid_company` INT(11) NOT NULL,
`job_position` VARCHAR(100) NOT NULL,
PRIMARY KEY (`id_register_entry`, `fid_employee`, `fid_address`, `fid_company`, `job_position`),
INDEX `fk_employee_register_employees` (`fid_employee` ASC),
INDEX `fk_employee_register_addresses1_idx` (`fid_address` ASC),
INDEX `fk_employee_register_companies1_idx` (`fid_company` ASC),
CONSTRAINT `fk_employee_register_addresses`
FOREIGN KEY (`fid_address`)
REFERENCES `task02`.`addresses` (`id_address`)
  ON DELETE RESTRICT
  ON UPDATE CASCADE,
CONSTRAINT `fk_employee_register_companies`
FOREIGN KEY (`fid_company`)
REFERENCES `task02`.`companies` (`id_company`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
CONSTRAINT `fk_employee_register_employees`
FOREIGN KEY (`fid_employee`)
REFERENCES `task02`.`employees` (`id_employee`)
  ON DELETE CASCADE
  ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
