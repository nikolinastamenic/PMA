INSERT INTO user (id, name,surname, email, password, phone_number, picture) VALUES (1, 'Mika', 'Mikic', 'user@yahoo.com','123', '0292929', 'profile_picture.jpg');
INSERT INTO user (id, name,surname, email, password, phone_number, picture) VALUES (2, 'Mika', 'Mikic', 'user1@yahoo.com','123', '0292929', 'profile_picture.jpg');
INSERT INTO user (id, email, name, password, phone_number, picture, surname) values (3, 'user@example.com', 'Ana', 'mysecret', '0614427283', 'profile_picture.jpg', 'Mihic');

INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.254606, 19.829638, 31, 'Novosadskog Sajma');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.252786, 19.834369, 10, 'Ćirpanova');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.254848, 19.838038, 16, 'Gajeva');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.244266, 19.830978, 36, 'Miše Dimitrijevića');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.238608, 19.805466, 150, 'Ćirila i Metodija');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',45.245490, 19.797130, 40, 'Futoški put');

INSERT INTO building (id, address_id) values (1,1);
INSERT INTO building (id, address_id) values (2,2);
INSERT INTO building (id, address_id) values (3,3);
INSERT INTO building (id, address_id) values (4,4);
INSERT INTO building (id, address_id) values (5,5);
INSERT INTO building (id, address_id) values (6,6);

INSERT INTO apartment (number, building_id) values (3, 1);
INSERT INTO apartment (number, building_id) values (8, 2);
INSERT INTO apartment (number, building_id) values (6, 3);
INSERT INTO apartment (number, building_id) values (19, 4);
INSERT INTO apartment (number, building_id) values (9, 5);
INSERT INTO apartment (number, building_id) values (11, 6);
INSERT INTO apartment (number, building_id) values (12, 1);
INSERT INTO apartment (number, building_id) values (13, 2);
INSERT INTO apartment (number, building_id) values (14, 3);
INSERT INTO apartment (number, building_id) values (15, 4);
INSERT INTO apartment (number, building_id) values (16, 5);
INSERT INTO apartment (number, building_id) values (17, 6);

INSERT INTO report_item (details, fault_name, picture) values ('Tiles in kitchen are broken', 'Broken tiles', 'broken_tiles.jpg');
INSERT INTO report_item (details, fault_name, picture) values ('Window is broken', 'Broken window', 'broken_window.jpg');

INSERT INTO report (id, date) values (1, '2020-09-10 14:45');
INSERT INTO report_item_list (report_id, item_list_id) values (1,1);
INSERT INTO report_item_list (report_id, item_list_id) values (1,2);

INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-09-10 14:45', 'NEW', 'NEW', false, 1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-09-11 14:45', 'NEW', 'NEW', true, 2);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-07-06 14:45', 'NEW', 'NEW', false, 7);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-07-22 14:45', 'NEW', 'NEW', true, 8);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-07-27 14:45', 'NEW', 'NEW', false, 9);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-08-01 14:45', 'NEW', 'NEW', true, 10);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-08-11 14:45', 'NEW', 'NEW', true, 11);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-08-14 14:45', 'NEW', 'NEW', false, 12);

INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-10-02 14:45', 'IN_PROCESS', 'NEW', false, 3,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-10-05 14:45', 'IN_PROCESS', 'NEW', true, 4,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id, report_id) values ('2020-06-10 14:45', 'FINISHED', 'NEW', true, 5,1,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-10-10 14:45', 'IN_PROCESS', 'NEW', true, 6,2);