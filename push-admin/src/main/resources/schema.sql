/*存储 admin端投递的消息*/
CREATE TABLE `tb_message`  (
  message_id VARCHAR (128) NOT NULL,
  message_content TEXT NOT NULL,
  create_time datetime,
  is_send TINYINT(1) NOT NULL,
  message_target VARCHAR(128),
  PRIMARY KEY (message_id)
) ENGINE = InnoDB;