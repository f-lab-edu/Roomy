CREATE DATABASE IF NOT EXISTS `roomy`;
USE roomy;
CREATE USER IF NOT EXISTS `roomy`@`localhost` IDENTIFIED BY 'backend';
CREATE USER `roomy`@`%` IDENTIFIED BY 'backend';
GRANT all privileges ON `roomy`.* TO `roomy`@`localhost`;
GRANT all privileges ON `roomy`.* TO `roomy`@`%`;