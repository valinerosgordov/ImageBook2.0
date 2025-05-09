ALTER TABLE `bonus_action` 
ADD COLUMN `discountPCenter` INT NULL DEFAULT NULL AFTER `discountSum`;

ALTER TABLE `order` 
ADD COLUMN `discountPCenter` INT(11) NULL DEFAULT NULL AFTER `pay_date`;