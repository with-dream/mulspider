CREATE DATABASE IF NOT EXISTS `wallpaper` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use wallpaper;

CREATE TABLE IF NOT EXISTS `wallpaper` (
   `id` int NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `imgWrapUrl`  varchar(500) default '',
    `imgUrl`  varchar(500) NOT NULL default '',
    `tags` varchar(500) default '',
    `imgW` INT,
    `imgH` INT,
    `size` varchar(30),
    `fav` INT,
    `views` INT,
    `insertTime` BIGINT
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;