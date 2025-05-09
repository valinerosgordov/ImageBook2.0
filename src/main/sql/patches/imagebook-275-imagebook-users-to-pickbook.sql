drop table if exists imagebook.pickbook_users_to_create
;

create table imagebook.pickbook_users_to_create (
  username varchar(255) not null,
  imagebook_vendor_id int(11) not null,
  pickbook_vendor_id int(11) not null,
  UNIQUE KEY `im_user_uk` (username, imagebook_vendor_id),
  UNIQUE KEY `pk_user_uk` (username, pickbook_vendor_id)
);

insert into imagebook.pickbook_users_to_create
select *
  from (
select distinct u.user_name
     , u.vendor_id imagebook_vendor_id
     , (select pv.id
          from pickbook.vendor pv
             , imagebook.vendor iv
         where pv.key_ = iv.key
           and iv.id = u.vendor_id
       ) pickbook_vendor_id
  from imagebook.user u
     , imagebook.role_ r
 where r.user = u.id
   and u.registered = 1 
   and r.type = 1 -- USER
) t
 where t.pickbook_vendor_id is not null
   and (t.user_name, t.pickbook_vendor_id) not in (select username, vendor_id from pickbook.user)
;  

insert into pickbook.user
( version_
, active
, lastname
, name
, passwordHash
, salt
, username
, currentAlbum_id
, vendor_id
, date
, referrer
, firstPage
, code_id
, papVisitorId
, papAffiliateId
, papValidTill
, photographer
, fathername
, bcryptPasswordHash
, wholesaler
, registered
)
select 1 version_
     , 1 active
     , u.last_name
     , u.name
     , null passwordHash
     , null salt
     , u.user_name
     , null currentAlbum_id
     , uc.pickbook_vendor_id
     , u.date
     , null referrer
     , null firstPage
     , null code_id
     , null papVisitorId
     , null papAffiliateId
     , null papValidTill
     , u.photographer
     , u.surname fathername
     , null bcryptPasswordHash
     , IF(u.photographer = 1 AND u.level = 9, 1, 0) wholesaler
     , u.registered
  from imagebook.user u
     , imagebook.pickbook_users_to_create uc
 where u.user_name = uc.username
   and u.vendor_id = uc.imagebook_vendor_id
;

insert into pickbook.user_role (user_id, roles_id) 
select (select u.id
          from pickbook.user u
         where u.username = uc.username
           and u.vendor_id = uc.pickbook_vendor_id
       ) user_id
     , (select id
 	  from pickbook.role
         where key_ = 'user'
       )
  from imagebook.pickbook_users_to_create uc
;

drop table imagebook.pickbook_users_to_create
;
