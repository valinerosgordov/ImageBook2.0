-- products
insert into product
SELECT null id,
    11 `type`,
    3 `number`,
    'планшет буклет 18х18' `name_ru`,
    `product`.`name_en`,
    `product`.`availability`,
    `product`.`block_format`,
    `product`.`binding`,
    `product`.`cover`,
    `product`.`paper`,
    `product`.`multiplicity`,
    `product`.`min_page_count`,
    `product`.`max_page_count`,
    `product`.`min_quantity`,
    `product`.`width`,
    `product`.`height`,
    `product`.`jpeg_folder`,
    `product`.`block_width`,
    `product`.`block_height`,
    `product`.`upper_safe_area`,
    `product`.`bottom_safe_area`,
    `product`.`inner_safe_area`,
    `product`.`outer_safe_area`,
    `product`.`addressPrinted`,
    `product`.`nonEditor`,
    `product`.`trialAlbum`,
    `product`.`hasSpecialOffer`,
    `product`.`minAlbumsCountForDiscount`,
    `product`.`imagebookDiscount`,
    `product`.`phDiscount`,
    `product`.`trialDelivery`,
    `product`.`approx_prod_time`,
    `product`.`calc_comment`
FROM `product`
where  type = 5 and number = 3;

insert into product
SELECT null id,
    11 `type`,
    4 `number`,
    'планшет буклет 20х20' `name_ru`,
    `product`.`name_en`,
    `product`.`availability`,
    `product`.`block_format`,
    `product`.`binding`,
    `product`.`cover`,
    `product`.`paper`,
    `product`.`multiplicity`,
    `product`.`min_page_count`,
    `product`.`max_page_count`,
    `product`.`min_quantity`,
    `product`.`width`,
    `product`.`height`,
    `product`.`jpeg_folder`,
    `product`.`block_width`,
    `product`.`block_height`,
    `product`.`upper_safe_area`,
    `product`.`bottom_safe_area`,
    `product`.`inner_safe_area`,
    `product`.`outer_safe_area`,
    `product`.`addressPrinted`,
    `product`.`nonEditor`,
    `product`.`trialAlbum`,
    `product`.`hasSpecialOffer`,
    `product`.`minAlbumsCountForDiscount`,
    `product`.`imagebookDiscount`,
    `product`.`phDiscount`,
    `product`.`trialDelivery`,
    `product`.`approx_prod_time`,
    `product`.`calc_comment`
FROM `product`
where  type = 5 and number = 3;

insert into product
SELECT null id,
    11 `type`,
    8 `number`,
    'планшет буклет 14х14' `name_ru`,
    `product`.`name_en`,
    `product`.`availability`,
    `product`.`block_format`,
    `product`.`binding`,
    `product`.`cover`,
    `product`.`paper`,
    `product`.`multiplicity`,
    `product`.`min_page_count`,
    `product`.`max_page_count`,
    `product`.`min_quantity`,
    `product`.`width`,
    `product`.`height`,
    `product`.`jpeg_folder`,
    `product`.`block_width`,
    `product`.`block_height`,
    `product`.`upper_safe_area`,
    `product`.`bottom_safe_area`,
    `product`.`inner_safe_area`,
    `product`.`outer_safe_area`,
    `product`.`addressPrinted`,
    `product`.`nonEditor`,
    `product`.`trialAlbum`,
    `product`.`hasSpecialOffer`,
    `product`.`minAlbumsCountForDiscount`,
    `product`.`imagebookDiscount`,
    `product`.`phDiscount`,
    `product`.`trialDelivery`,
    `product`.`approx_prod_time`,
    `product`.`calc_comment`
FROM `product`
where  type = 5 and number = 3;

-- color ranges
insert into color_range select id, 0, 0 from product where type = 11 and number = 3;
insert into color_range select id, 0, 0 from product where type = 11 and number = 4;
insert into color_range select id, 0, 0 from product where type = 11 and number = 8;

-- albums
insert into album 
SELECT (select id from product where type = 11 and number = 3) id,
    `album`.`size`,
    `album`.`cover_size`,
    `album`.`pdf_cover_width`,
    `album`.`pdf_cover_height`,
    `album`.`hardcover`,
    `album`.`inner_crop`,
    `album`.`jpeg_cover_folder`,
    `album`.`front_upper_crop`,
    `album`.`front_bottom_crop`,
    `album`.`front_left_crop`,
    `album`.`front_right_crop`,
    `album`.`back_upper_crop`,
    `album`.`back_bottom_crop`,
    `album`.`back_left_crop`,
    `album`.`back_right_crop`,
    `album`.`upper_cover_safe_area`,
    `album`.`bottom_cover_safe_area`,
    `album`.`left_cover_safe_area`,
    `album`.`right_cover_safe_area`,
    `album`.`coverName`,
    `album`.`lastPageTemplate`,
    `album`.`basePrice`,
    `album`.`pagePrice`,
    `album`.`phBasePrice`,
    `album`.`phPagePrice`,
    `album`.`phCoverLaminationPrice`,
    `album`.`coverLaminationPrice`,
    `album`.`cover_width`,
    `album`.`cover_height`,
    `album`.`barcode_x`,
    `album`.`barcode_y`,
    `album`.`mphoto_barcode_x`,
    `album`.`mphoto_barcode_y`,
    `album`.`page_lam_price`,
    `album`.`ph_page_lam_price`
FROM `album`
where id = (select id from product where type = 5 and number = 3);

insert into album
SELECT (select id from product where type = 11 and number = 4) id,
    `album`.`size`,
    `album`.`cover_size`,
    `album`.`pdf_cover_width`,
    `album`.`pdf_cover_height`,
    `album`.`hardcover`,
    `album`.`inner_crop`,
    `album`.`jpeg_cover_folder`,
    `album`.`front_upper_crop`,
    `album`.`front_bottom_crop`,
    `album`.`front_left_crop`,
    `album`.`front_right_crop`,
    `album`.`back_upper_crop`,
    `album`.`back_bottom_crop`,
    `album`.`back_left_crop`,
    `album`.`back_right_crop`,
    `album`.`upper_cover_safe_area`,
    `album`.`bottom_cover_safe_area`,
    `album`.`left_cover_safe_area`,
    `album`.`right_cover_safe_area`,
    `album`.`coverName`,
    `album`.`lastPageTemplate`,
    `album`.`basePrice`,
    `album`.`pagePrice`,
    `album`.`phBasePrice`,
    `album`.`phPagePrice`,
    `album`.`phCoverLaminationPrice`,
    `album`.`coverLaminationPrice`,
    `album`.`cover_width`,
    `album`.`cover_height`,
    `album`.`barcode_x`,
    `album`.`barcode_y`,
    `album`.`mphoto_barcode_x`,
    `album`.`mphoto_barcode_y`,
    `album`.`page_lam_price`,
    `album`.`ph_page_lam_price`
FROM `album`
where id = (select id from product where type = 5 and number = 3);

insert into album
SELECT (select id from product where type = 11 and number = 8) id,
    `album`.`size`,
    `album`.`cover_size`,
    `album`.`pdf_cover_width`,
    `album`.`pdf_cover_height`,
    `album`.`hardcover`,
    `album`.`inner_crop`,
    `album`.`jpeg_cover_folder`,
    `album`.`front_upper_crop`,
    `album`.`front_bottom_crop`,
    `album`.`front_left_crop`,
    `album`.`front_right_crop`,
    `album`.`back_upper_crop`,
    `album`.`back_bottom_crop`,
    `album`.`back_left_crop`,
    `album`.`back_right_crop`,
    `album`.`upper_cover_safe_area`,
    `album`.`bottom_cover_safe_area`,
    `album`.`left_cover_safe_area`,
    `album`.`right_cover_safe_area`,
    `album`.`coverName`,
    `album`.`lastPageTemplate`,
    `album`.`basePrice`,
    `album`.`pagePrice`,
    `album`.`phBasePrice`,
    `album`.`phPagePrice`,
    `album`.`phCoverLaminationPrice`,
    `album`.`coverLaminationPrice`,
    `album`.`cover_width`,
    `album`.`cover_height`,
    `album`.`barcode_x`,
    `album`.`barcode_y`,
    `album`.`mphoto_barcode_x`,
    `album`.`mphoto_barcode_y`,
    `album`.`page_lam_price`,
    `album`.`ph_page_lam_price`
FROM `album`
where id = (select id from product where type = 5 and number = 3);

-- cover_lam ranges

insert into cover_lam_range
select (select id from product where type = 11 and number = 3) product_id
     , number
     , _index
  from cover_lam_range where product_id = (select id from product where type = 5 and number = 3);

insert into cover_lam_range
select (select id from product where type = 11 and number = 4) product_id
     , number
     , _index
  from cover_lam_range where product_id = (select id from product where type = 5 and number = 3);

insert into cover_lam_range
select (select id from product where type = 11 and number = 8) product_id
     , number
     , _index
  from cover_lam_range where product_id = (select id from product where type = 5 and number = 3);

-- page_lam ranges

insert into page_lam_range
select (select id from product where type = 11 and number = 3) product_id
     , number
     , _index
  from page_lam_range where product_id = (select id from product where type = 5 and number = 3);

insert into page_lam_range
select (select id from product where type = 11 and number = 4) product_id
     , number
     , _index
  from page_lam_range where product_id = (select id from product where type = 5 and number = 3);

insert into page_lam_range
select (select id from product where type = 11 and number = 8) product_id
     , number
     , _index
  from page_lam_range where product_id = (select id from product where type = 5 and number = 3);
