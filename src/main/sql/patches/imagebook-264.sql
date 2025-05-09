update mailing t
   inner join vendor v on t.vendor = v.id
   set t.name_from = v.name
     , t.email_from = v.email
;