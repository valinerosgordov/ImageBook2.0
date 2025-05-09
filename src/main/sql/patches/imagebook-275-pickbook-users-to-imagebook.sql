drop table if exists pickbook.imagebook_users_to_create
;

create table pickbook.imagebook_users_to_create (
  username varchar(255) not null,
  imagebook_vendor_id int(11) not null,
  pickbook_vendor_id int(11) not null,
  UNIQUE KEY `im_user_uk` (username, imagebook_vendor_id),
  UNIQUE KEY `pk_user_uk` (username, pickbook_vendor_id)
);

insert into pickbook.imagebook_users_to_create
select *
  from (
select distinct u.username
     , (select iv.id
          from pickbook.vendor pv
             , imagebook.vendor iv
         where pv.key_ = iv.key
           and pv.id = u.vendor_id
       ) imagebook_vendor_id
     , u.vendor_id pickbook_vendor_id
  from pickbook.user u
     , pickbook.user_role r
 where r.user_id = u.id
   and u.registered = 1 
   and r.roles_id = (select id from pickbook.role where key_ = 'user')
) t
 where t.imagebook_vendor_id is not null
   and (t.username, t.imagebook_vendor_id) not in (select user_name, vendor_id from imagebook.user)
;  

insert into imagebook.user
( user_name
, password
, name
, last_name
, surname
, locale
, invitation_state
, info
, level
, settings_id
, register_type
, vendor_id
, discountPc
, date
, skipMailing
, urgentOrders
, oldPrice
, advOrders
, photographer
, registered
) 
select u.username
     , null password
     , u.name
     , u.lastname
     , u.fathername surname
     , 'ru' locale
     , 300 invitation_state -- CONFIRMED
     , null info
     , if(u.photographer = 1 and u.wholesaler = 1, 9, 8) level
     , null settings_id  
     , 2 register_type -- OFFICE
     , uc.imagebook_vendor_id
     , 0 discountPc
     , u.date
     , 0 skipMailing
     , 0 urgentOrders
     , 0 oldPrice
     , 0 advOrders
     , if(u.photographer = 1 and u.wholesaler = 0, 1, 0) photographer
     , u.registered
  from pickbook.user u
     , pickbook.imagebook_users_to_create uc
 where u.username = uc.username 
   and u.vendor_id = uc.pickbook_vendor_id
;

insert into imagebook.role_ (type, user) 
select 1 type -- USER
     , (select u.id
	  from imagebook.user u
         where user_name = uc.username
           and vendor_id = uc.imagebook_vendor_id
       ) user
  from pickbook.imagebook_users_to_create uc
;

insert into imagebook.email (active, email, user_id, _index)
select 1 active
     , uc.username email
     , (select u.id
	  from imagebook.user u
         where user_name = uc.username
           and vendor_id = uc.imagebook_vendor_id
       ) user_id
     , 0 _index
  from pickbook.imagebook_users_to_create uc
;

insert into imagebook.user_account (user_name, password_hash, active, user) 
select uc.username
     , '$2a$10$bz06yDoj09zvFR1OuDQ5Qe2CryK1TUkCFT1i36pHPBTMGkF5s.8Ti' password_hash
     , 1 active
     , (select u.id
	  from imagebook.user u
         where user_name = uc.username
           and vendor_id = uc.imagebook_vendor_id
       ) user
  from pickbook.imagebook_users_to_create uc
;

drop table if exists pickbook.imagebook_users_to_create
;