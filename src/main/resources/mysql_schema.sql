-- -----------------------------------------------------
-- Schema pricing
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `pricing` DEFAULT CHARACTER SET utf8 ;
USE `product` ;
USE `pricing` ;

-- -----------------------------------------------------
-- Table `pricing`.`product`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pricing`.`product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 74
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `pricing`.`pricing`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `pricing`.`pricing` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `price` INT(11) NOT NULL,
  `timestamp` DATETIME(6) NOT NULL,
  `product_id` BIGINT(20) NOT NULL,
  `timezone` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `product_id_idx` (`product_id` ASC),
  CONSTRAINT `fk_product_id`
    FOREIGN KEY (`product_id`)
    REFERENCES `pricing`.`product` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8;