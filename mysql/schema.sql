DROP DATABASE IF EXISTS testdb;
CREATE DATABASE testdb;
USE testdb;

CREATE TABLE role (
                id INT AUTO_INCREMENT NOT NULL,
                name VARCHAR(50) NOT NULL,
                PRIMARY KEY (id)
);


CREATE TABLE user (
                id INT AUTO_INCREMENT NOT NULL,
                email VARCHAR(50) NOT NULL,
                password VARCHAR(500) NOT NULL,
                PRIMARY KEY (id)
);

CREATE TABLE refresh_token (
                id INT AUTO_INCREMENT NOT NULL,
                user_id INT NOT NULL,
                token VARCHAR(255) NOT NULL,
                expiry_date TIMESTAMP NOT NULL,
                PRIMARY KEY (id)
);

CREATE TABLE user_role (
                id INT AUTO_INCREMENT NOT NULL,
                role_id INT NOT NULL,
                user_id INT NOT NULL,
                PRIMARY KEY (id)
);


ALTER TABLE user_role ADD CONSTRAINT role_user_role_fk
FOREIGN KEY (role_id)
REFERENCES role (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE user_role ADD CONSTRAINT user_user_role_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE refresh_token ADD CONSTRAINT user_refresh_token_fk
FOREIGN KEY (user_id)
REFERENCES user (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

INSERT INTO role(name) VALUES
("ROLE_ADMIN"),("ROLE_USER");
