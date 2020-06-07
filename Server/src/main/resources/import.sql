INSERT INTO user (name,surname, email, password, phone_number) VALUES ('Mika', 'Mikic', 'user@yahoo.com','$2a$10$d2bYEem94Do7dck2CP14M.p4u3r2CPb7Di9uyrkxdDF0ibSbU5Bpy', '0292929');

INSERT INTO address (city, country, latitude, longitude, number, street) values ('Beograd', 'Srbija',44.8080941, 20.294172799999956, 3, 'Nemanjina');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 10, 'Dunavska');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 5, 'Cirpanova');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Beograd', 'Srbija',44.8080941, 20.294172799999956, 17, 'Vojvode Stepe');

INSERT INTO building (id, address_id) values (1,1);
INSERT INTO building (id, address_id) values (2,2);
INSERT INTO building (id, address_id) values (3,3);
INSERT INTO building (id, address_id) values (4,4);


INSERT INTO apartment (number, building_id) values (3, 1);
INSERT INTO apartment (number, building_id) values (8, 2);
INSERT INTO apartment (number, building_id) values (6, 3);
INSERT INTO apartment (number, building_id) values (19, 4);


INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-09-10 14:45', 'NEW', 'NEW', true, 1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 2);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-09-10 14:45', 'IN_PROCESS', 'NEW', true, 3,1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id, user_id) values ('2020-08-10 14:45', 'IN_PROCESS', 'NEW', true, 4,1);