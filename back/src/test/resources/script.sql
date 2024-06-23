
DELETE FROM PARTICIPATE;
DELETE FROM SESSIONS;
DELETE FROM TEACHERS;
DELETE FROM USERS;

ALTER TABLE PARTICIPATE AUTO_INCREMENT = 1;
ALTER TABLE SESSIONS AUTO_INCREMENT = 1;
ALTER TABLE TEACHERS AUTO_INCREMENT = 1;
ALTER TABLE USERS AUTO_INCREMENT = 1;


INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO SESSIONS (name,description,`date`,teacher_id,created_at,updated_at)
VALUES ('Yoga','First session','2023-04-05 10:13:16',1,'2023-04-04 10:13:16','2023-04-04 10:13:39'),
       ('Zoumba','Second session','2023-04-06 10:13:16',2,'2023-04-04 10:14:08','2023-04-04 10:14:08');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$iubE9N0INEpjueHwqfKJq.d/Dr2QpWc3l91Z.v7nH1uBMcDdH4X4.'),
       ('lasalle', 'degym', true, 'gym@studio.com', '$2a$10$WWUaHpk6yi9PKDmAv/BekejHy14u.ahqw8HjHmlm7NgKy9xOXs9p.');


INSERT INTO PARTICIPATE (user_id,session_id) VALUES (1,1);