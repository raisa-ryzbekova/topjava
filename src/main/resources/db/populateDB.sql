DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime, description, calories, user_id) VALUES
  ('2018-01-01 08:00:00', 'Завтрак0', 500, 100000),
  ('2018-01-01 12:00:00', 'Обед0', 1000, 100000),
  ('2018-01-01 17:00:00', 'Ужин0', 510, 100000),
  ('2018-01-02 08:00:00', 'Завтрак0', 510, 100000),
  ('2018-01-01 08:00:00', 'Завтрак1', 500, 100001),
  ('2018-01-01 12:00:00', 'Обед1', 500, 100001),
  ('2018-01-01 17:00:00', 'Ужин1', 500, 100001);