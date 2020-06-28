INSERT INTO user (id, name,surname, email, password, phone_number) VALUES (1, 'Mika', 'Mikic', 'user@yahoo.com','123', '0292929');
INSERT INTO user (id, name,surname, email, password, phone_number) VALUES (2, 'Mika', 'Mikic', 'user1@yahoo.com','123', '0292929');
INSERT INTO user (id, email, name, password, phone_number, picture, surname) values (3, 'user@example.com', 'Ana', 'mysecret', '0614427283', 'profile_picture.jpg', 'Mihic');

INSERT INTO address (city, country, latitude, longitude, number, street) values ('Beograd', 'Srbija',44.8080941, 20.294172799999956, 3, 'Nemanjina');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 10, 'Dunavska');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 5, 'Cirpanova');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Beograd', 'Srbija',44.8080941, 20.294172799999956, 17, 'Vojvode Stepe');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 3, 'Bulevar Oslobodjenja');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 3, 'Puskinova');

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

INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-09-10 14:45', 'NEW', 'NEW', true, 1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 2);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 7);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 8);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 9);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 10);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 11);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 12);

INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-09-10 14:45', 'IN_PROCESS', 'NEW', true, 3,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-08-10 14:45', 'IN_PROCESS', 'NEW', true, 4,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id, report_id) values ('2020-03-10 14:45', 'FINISHED', 'NEW', true, 5,1,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-03-10 14:45', 'IN_PROCESS', 'NEW', true, 6,2);