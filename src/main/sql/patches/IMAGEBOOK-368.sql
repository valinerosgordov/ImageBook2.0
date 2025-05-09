ALTER TABLE `bill` 
ADD COLUMN `pickup_send_state_date` DATETIME NULL DEFAULT NULL AFTER `ddelivery_pickup_point_address`;

ALTER TABLE `bill` 
ADD COLUMN `notify_pickup` BIT(1) NOT NULL DEFAULT 0 AFTER `pickup_send_state_date`;