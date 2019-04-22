insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (1,'user','$2a$10$cDuj4VfCuQYp87vArrpDruckpl.trMfrbChWJPoF.JsghXMbUzKNO','user1@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (2,'user2','$2a$10$cDuj4VfCuQYp87vArrpDruckpl.trMfrbChWJPoF.JsghXMbUzKNO','user2@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (3,'admin','$2a$10$IEC3HGpttX.96BzJGaG7P.Zqv/cVh.2dkuCGMcaZ0kfbZ8KR7thv.','admin@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (4,'tst1','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst1@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (5,'tst2','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst2@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (6,'tst3','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst3@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (7,'tst4','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst4@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (8,'tst5','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst5@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (9,'tst6','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst6@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (10,'tst7','$2a$10$Wot9ngIUk26WqwRM77l1H.CuoFBDqqQVS033JNPtTDQf3bwtMiPm.','tst7@wizclass.com', true, false, 'avatar.png');
insert into user (id, username, password, email, enabled, newsletter_Activa, picture) values (11,'a','$2a$10$1pnQGZ/7hAGPBm7YNyqrwuZ2Ha2jeaERMAufqRbT18dOXrTIwIfe6','adminA@wizclass.com', true, false, 'avatar.png');

insert into role (id, role) values (1, 'USER');
insert into role (id, role) values (2, 'ADMIN');

insert into user_role (user_id, role_id) values (1,1);
insert into user_role (user_id, role_id) values (2,1);
insert into user_role (user_id, role_id) values (3,1);
insert into user_role (user_id, role_id) values (3,2);
insert into user_role (user_id, role_id) values (4,1);
insert into user_role (user_id, role_id) values (5,1);
insert into user_role (user_id, role_id) values (6,1);
insert into user_role (user_id, role_id) values (7,1);
insert into user_role (user_id, role_id) values (8,1);
insert into user_role (user_id, role_id) values (9,1);
insert into user_role (user_id, role_id) values (10,1);
insert into user_role (user_id, role_id) values (11,1);
insert into user_role (user_id, role_id) values (11,2);

insert into ensenanza (id, ensenanza) values (1, 'EDUCACION INFANTIL');
insert into ensenanza (id, ensenanza) values (2, 'EDUCACION PRIMARIA');
insert into ensenanza (id, ensenanza) values (3, 'EDUCACION SECUNDARIA');
insert into ensenanza (id, ensenanza) values (4, 'BACHILLERATO');
insert into ensenanza (id, ensenanza) values (5, 'FORMACION PROFESIONAL');

insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (1, 'negro', '#2E2E2E', 'elegant-color' , '#212121', 'elegant-color-dark');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (2, 'azulIndigo', '#3f51b5', 'indigo', '#283593', 'indigo darken-3');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (3, 'rojo', '#c62828', 'red darken-3', '#b71c1c', 'red darken-4');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (4, 'verde', '#2e7d32', 'green darken-3', '#1b5e20', 'green darken-4');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (5, 'naranja', '#ef6c00', 'orange darken-3', '#e65100', 'orange darken-4');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (6, 'morado', '#512da8', 'deep-purple darken-2', '#4527a0', 'deep-purple darken-3');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (7, 'amarillo', '#ffa000', 'amber darken-2', '#ff8f00', 'amber darken-3');
insert into paleta (id, nombre, color_base_hex, color_base_css, color_secundario_hex, color_secundario_css) values (8, 'teal', '#009688', 'teal', '#00897b', 'teal darken-1');

insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (1, 'IMPORT SQL-1', 'Import SQL-1', 'Alcala', '50', 'Madrid', '28047', 'raul@gmail.com', '681422657', '16/02/2019', 99.90, false, false, 3, 6, 'WizclassLogo.png');
insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (2, 'IMPORT SQL-2', 'Import SQL-2', 'Los Yebenes', '112', 'Madrid', '28047', 'raul@gmail.com', '681422657', '20/02/2019', 99.90, true, false, 3, 5, 'WizclassLogo.png');
insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (3, 'IMPORT SQL-3', 'Import SQL-3', 'Plaça de l''Ajuntament', '1', 'Alacant', '03002', 'raul@gmail.com', '681422657', '17/10/2018', 99.90, false, false, 3, 4, 'WizclassLogo.png');
insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (4, 'IMPORT SQL-4', 'Import SQL-4', 'Plaza Nueva', '1', 'Sevilla', '41001', 'wizclass.app@gmail.com', '681422657', '22/12/2018', 99.90, false, true, 3, 3, 'WizclassLogo.png');
insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (5, 'IMPORT SQL-5', 'Import SQL-5', 'Plaza Nueva', '1', 'Sevilla', '41001', 'wizclass.app@gmail.com', '681422657', '22/12/2018', 99.90, true, false, 11, 2, 'WizclassLogo.png');
insert into pagina (id, nombre, titulo, calle, numero, localidad, codigo_Postal, email_Contacto, telefono_Contacto, fecha_Modificacion, precio, en_Carrito, comprado, user_id, paleta_id, picture) values (6, 'IMPORT SQL-6', 'Import SQL-6', 'Plaza Nueva', '1', 'Sevilla', '41001', 'wizclass.app@gmail.com', '681422657', '22/12/2018', 99.90, false, true, 1, 1, 'WizclassLogo.png');

insert into pagina_ensenanza (pagina_id, ensenanza_id) values (1,2);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (1,3);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (2,1);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (2,4);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (3,4);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (3,5);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (4,1);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (4,2);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (4,3);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (5,1);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (5,2);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (5,3);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (6,1);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (6,2);
insert into pagina_ensenanza (pagina_id, ensenanza_id) values (6,3);

insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (1,'Noticia de ejemplo 1', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '22/12/2018', 5);
insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (2,'Noticia de ejemplo 2', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '15/1/2018', 5);
insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (3,'Noticia de ejemplo 3', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '7/2/2018', 1);
insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (4,'Noticia de ejemplo 4', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '30/3/2018', 1);
insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (5,'Noticia de ejemplo 5', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '15/1/2018', 2);
insert into noticia (id, titulo, cuerpo, imagen, fecha_publicacion, pagina_id) values (6,'Noticia de ejemplo 6', 'Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo Cuerpo de ejemplo', 'defaultNews.jpg', '30/3/2018', 2);
