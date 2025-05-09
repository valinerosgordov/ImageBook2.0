DROP PROCEDURE IF EXISTS createBillsForOrdersWithStateGreaterThan600;

$$

CREATE PROCEDURE createBillsForOrdersWithStateGreaterThan600()
BEGIN
    DECLARE v_order_id, v_user_id, v_bill_id INT;
    DECLARE v_order_date DATE;
    DECLARE done INT DEFAULT FALSE;

	DECLARE cur1 CURSOR FOR
	select id, user, date from `order`
	 where bill_id is null
	   and state >= 600;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

	OPEN cur1;

	the_loop: LOOP
		FETCH cur1 INTO v_order_id, v_user_id, v_order_date;
		IF done = 1 THEN
			LEAVE the_loop;
		END IF;

        INSERT INTO bill (user, date, state, adv, regLetter)
        VALUES (v_user_id, v_order_date, 100, 0, 0);

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

call createBillsForOrdersWithStateGreaterThan600();