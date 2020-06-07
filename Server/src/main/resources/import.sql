INSERT INTO address (city, country, latitude, longitude, number, street) values ('Beograd', 'Srbija',44.8080941, 20.294172799999956, 3, 'Nemanjina');
INSERT INTO address (city, country, latitude, longitude, number, street) values ('Novi Sad', 'Srbija',44.8080941, 20.294172799999956, 10, 'Dunavska');
INSERT INTO building (id, address_id) values (1,1);
INSERT INTO building (id, address_id) values (2,2);

INSERT INTO apartment (number, building_id) values (3, 1);
INSERT INTO apartment (number, building_id) values (8, 2);

INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-09-10 14:45', 'NEW', 'NEW', true, 1);
INSERT INTO task (deadline, state, type_of_apartment, urgent, apartment_id) values ('2020-10-10 14:45', 'NEW', 'NEW', true, 2);

INSERT INTO user (id, email, name, password, phone_number, picture, surname) values (1, 'user@example.com', 'Ana', 'mysecret', '0614427283', 'profile_picture.jpg', 'Mihic');