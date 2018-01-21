INSERT INTO city (id, name) VALUES
  (1, 'Moscow'),
  (2, 'Voronezh'),
  (3, 'Milan'),
  (4, 'Berlin'),
  (5, 'Murmansk');

INSERT INTO candidate(id, first_name, surname, email, ctn, anonym, city_id) VALUES
  (1, 'Sergey', 'Sergeev', 'common@gmail.com', '95111122233', false, 3),
  (2, 'Maxim', 'Maximov', 'common@gmail.com', '95111122233', false, 2),
  (3, 'Ivan', 'Popov', 'common@gmail.com', '95111122233', false, 1),
  (4, 'Igor', 'Muhin', 'common@gmail.com', '95111122233', true, 4);

INSERT INTO vacancy(id, company_id, name, description, salary, state, city_id) VALUES
  (1, '123', 'java developer', 'Great position in the great company!', 190000, 'ACTIVE', 3),
  (2, '124F', 'java developer', 'Great position in the great company!', 180000, 'HOLD', 2),
  (3, 'QUQU', 'c# developer', 'Great position in the great company!', 190000, 'ACTIVE', 4),
  (4, '15', 'java developer', 'Great position in the great company!', 200000, 'HOLD', 4),
  (5, 'OHOHO', 'java developer', 'Great position in the great company!', 195000, 'ARCHIEVED', 1),
  (6, '10', 'java developer', 'Great position in the great company!', 190000, 'ACTIVE', 3),
  (7, '10', 'c# developer', 'Great position in the great company!', 185000, 'ACTIVE', 1),
  (8, '10', 'go developer', 'Great position in the great company!', 190000, 'ACTIVE', 3),
  (9, '10', 'js developer', 'Great position in the great company!', 200000, 'HOLD', 1),
  (10, 'OHOHO', 'java developer', 'Great position in the great company!', 190000, 'ACTIVE', 1),
  (11, 'OOO', 'java developer', 'Great position in the great company!', 180000, 'ACTIVE', 5),
  (12, '1243F', 'java developer', 'Great position in the great company!', 180000, 'ACTIVE', 2),
  (14, 'QQ', 'java developer', 'Great position in the great company!', 200000, 'ARCHIEVED', 3),
  (15, '10', 'java developer', 'Great position in the great company!', 180000, 'ACTIVE', 3),
  (16, 'OK', 'java developer', 'Great position in the great company!', 190000, 'ACTIVE', 1);
