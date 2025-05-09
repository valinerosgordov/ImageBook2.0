DELIMITER $$

DROP PROCEDURE IF EXISTS createBillsForPaidOrdersWithoutBillRef;

$$

CREATE PROCEDURE createBillsForPaidOrdersWithoutBillRef()
BEGIN
    DECLARE v_order_id, v_user_id, v_bill_id, v_delivery_type, v_total_weight INT;
    DECLARE v_order_date, v_pay_date DATE;
    DECLARE done INT DEFAULT FALSE;

	DECLARE cur1 CURSOR FOR
	select id, user, date, delivery_type, pay_date, totalWeight from `order`
     where bill_id is null
       and state in (
			   700 -- PAID
			 , 720 -- PDF_ERROR
			 , 721 -- PDF_REGENERATION
			 , 730 -- READY_TO_TRANSFER_PDF
			 , 740 -- PDF_TRANSFER_IN_PROGRESS
			 , 750 -- PRINTING
			 , 800 -- FINISHING
			 , 850 -- PRINTED
			 , 900 -- DELIVERY
			 , 1000 -- SENT	
		   );

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	OPEN cur1;

	the_loop: LOOP
		FETCH cur1 INTO v_order_id, v_user_id, v_order_date, v_delivery_type, v_pay_date, v_total_weight;
		IF done = 1 THEN
			LEAVE the_loop;
		END IF;

        INSERT INTO bill(user, date, state, adv, weight, deliveryType, regLetter, importId, pay_date, discountPc, deliveryCost)
        VALUES (v_user_id, v_order_date, 200, 0, v_total_weight, v_delivery_type, 0, null, ifnull(v_pay_date, v_order_date), 0, 0);

	SELECT last_insert_id() INTO v_bill_id;
	SELECT v_bill_id;

	UPDATE `order` 
           SET bill_id = v_bill_id
         WHERE id = v_order_id;

	END LOOP the_loop;

	CLOSE cur1;

	SELECT 'done';
END

$$

call createBillsForPaidOrdersWithoutBillRef();