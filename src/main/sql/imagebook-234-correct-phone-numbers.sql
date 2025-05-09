-- prepare
drop table if exists phones;
create table phones as select id, phone from address;
alter table phones add column updated_phone varchar(255);

-- replace special symbols
update phones
   set phone = REPLACE(REPLACE(REPLACE(REPLACE(TRIM(phone), ' ', ''), '-', ''), '(', ''), ')', '')
;

-- phone.length < 10
update phones 
   set updated_phone = phone
 where CHAR_LENGTH(phone) < 10 
;

-- phone starts with '+'
update phones
   set updated_phone = phone 
 where updated_phone is null
   and LEFT(phone, 1) = '+'
;

-- phone.length = 11 and starts with '8' replacing with '+7'
update phones
   set updated_phone = CONCAT('+7', SUBSTRING(phone, 2))
 where updated_phone is null
   and CHAR_LENGTH(phone) = 11 
   and LEFT(phone, 1) = '8'
;

-- phone.length = 11 and starts with '7' replacing with '+7'
update phones
   set updated_phone = CONCAT('+', phone)
 where updated_phone is null
   and CHAR_LENGTH(phone) = 11 
   and LEFT(phone, 1) = '7'
;

-- phone.length = 10, adding '+7'
update phones
   set updated_phone = CONCAT('+7', phone)
 where updated_phone is null
   and CHAR_LENGTH(phone) = 10
;

-- others add '+'
update phones
   set updated_phone = CONCAT('+', phone)
 where updated_phone is null
;

select count(*) as 'phone numbers to correct'
  from phones
 where updated_phone is null
   and phone is not null
;

-- uncomment to update address table 
-- update address t
--    set t.phone = (select updated_phone from phones where id = t.id) 
-- ;
-- drop table if exists phones;