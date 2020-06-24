CREATE TABLE `tb_message`  (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `message_content` varchar(2048)  NOT NULL,
  `create_time` datetime(0),
  `is_send` tinyint(1),
  message_target INT ,
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB;