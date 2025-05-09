CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL,
  `user` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK358076B4E86F89` (`user`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `role`
--
ALTER TABLE `role`
  ADD CONSTRAINT `FK358076B4E86F89` FOREIGN KEY (`user`) REFERENCES `user` (`id`);

  
ALTER TABLE  `user` DROP  `role`; 