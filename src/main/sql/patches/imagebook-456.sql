update `order`
   set package_number = substring_index(number, '-', 1)
 where packaged = true
;

alter table `order` drop column packaged;