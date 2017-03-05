CREATE TABLE `users` (
  `id` BIGINT AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `user_credentials` (
  `user_id` BIGINT NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `salt` VARCHAR(255) NOT NULL
);

CREATE INDEX `user_id_idx` ON `user_credentials` (`user_id`);
