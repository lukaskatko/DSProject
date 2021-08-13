create schema if not exists cs6650;
use cs6650;
DROP TABLE IF EXISTS `account`;

CREATE TABLE IF NOT EXISTS `account` (
  `account_id` int NOT NULL AUTO_INCREMENT,
  `balance` decimal(12,2) NOT NULL, 
  `account_number` varchar(25) NOT NULL,
  first_name varchar(50) NOT NULL,
  last_name varchar(50) NOT NULL,
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (account_number),
  PRIMARY KEY (`account_id`)  
);

SELECT * FROM account
