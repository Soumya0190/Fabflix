CREATE TABLE `employees` (
  `username` varchar(5) NOT NULL,
  `password` varchar(10) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`username`)
);


INSERT INTO `moviedb`.`employees`
(`username`,
`password`,
`role`)
VALUES
('classta@email.edu','classta','admin');
