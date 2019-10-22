DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (user_id, role)
VALUES (100000, 'ROLE_USER'),
       (100001, 'ROLE_ADMIN');

INSERT INTO meals (datetime, description, calories, user_id)
VALUES ('2015/05/30 10:00', 'Завтрак', 500, 100000),
       ('2015/05/30 13:00', 'Обед', 1000, 100000),
       ('2015/05/30 20:00', 'Ужин', 500, 100000),
       ('2019/10/23 9:00', 'Завтрак', 500, 100000),
       ('2019/10/23 12:00', 'Обед', 1500, 100000),
       ('2019/10/24 09:00', 'Завтрак', 1500, 100000),
       ('2015/05/31 10:00', 'Завтрак', 1000, 100001),
       ('2015/05/31 13:00', 'Обед', 500, 100001),
       ('2015/05/31 20:00', 'Ужин', 510, 100001);
