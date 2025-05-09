drop table if exists `feedback_answer`;
drop table if exists `feedback`;
drop table if exists `feedback_user`;
drop table if exists `feedback_anonymous_user`;
drop table if exists `property`;
drop table if exists `recommendation`;

CREATE TABLE `property` (
  `property_key` varchar(255) NOT NULL,
  `value` text,
  PRIMARY KEY (`property_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `feedback` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` mediumtext,
  `create_date` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `feedback_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_user_id` (`feedback_user_id`),
  KEY `id` (`id`),
  KEY `create_date` (`create_date`),
  CONSTRAINT `feedback_ibfk_1` FOREIGN KEY (`feedback_user_id`) REFERENCES `feedback_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `feedback_anonymous_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(13) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `feedback_answer` (
  `feedback_id` int(11) NOT NULL,
  `answer_date` datetime DEFAULT NULL,
  `answer` mediumtext,
  PRIMARY KEY (`feedback_id`),
  KEY `feedback_id` (`feedback_id`),
  CONSTRAINT `feedback_answer_ibfk_1` FOREIGN KEY (`feedback_id`) REFERENCES `feedback` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `feedback_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office` varchar(255) DEFAULT NULL,
  `profession` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `feedback_anonymous_user_id` int(11) DEFAULT NULL,
  `feedback_internal_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `feedback_anonymous_user_id` (`feedback_anonymous_user_id`),
  KEY `feedback_internal_user_id` (`feedback_internal_user_id`),
  KEY `id` (`id`),
  CONSTRAINT `feedback_user_ibfk_1` FOREIGN KEY (`feedback_anonymous_user_id`) REFERENCES `feedback_anonymous_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `feedback_user_ibfk_2` FOREIGN KEY (`feedback_internal_user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `recommendation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;