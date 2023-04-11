CREATE SCHEMA `personel` DEFAULT CHARACTER SET utf8mb4;

CREATE TABLE `personel`.`ludzie` (
  `P_ID` INT NOT NULL AUTO_INCREMENT,
  `PESEL` CHAR(11) NOT NULL,
  `imie` VARCHAR(30) NULL,
  `nazwisko` VARCHAR(30) NULL,
  `data_urodzenia` DATE NULL,
  `plec` ENUM('K', 'M') NULL,
  CONSTRAINT sprawdzeniePESEL CHECK (PESEL regexp '^[0-9]{11}$'), 
  PRIMARY KEY (`P_ID`));
  
  CREATE TABLE `personel`.`zawody` (
  `zawod_id` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(50) NULL,
  `pensja_min` FLOAT,
  `pensja_max` FLOAT,
  constraint pensjaMin CHECK (`pensja_min` >= 0),  
  constraint pensjaMax CHECK (`pensja_max` >= 0), 
  constraint relacjePensji CHECK (`pensja_max` > `pensja_min`),  
  PRIMARY KEY (`zawod_id`));
  
  CREATE TABLE `personel`.`pracownicy` (
  `P_ID` INT NOT NULL,
  `zawod_id` INT NOT NULL,
  `pensja` FLOAT NULL,
  PRIMARY KEY (`P_ID`));