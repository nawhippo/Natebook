CREATE TABLE IF NOT EXISTS app_user (
  id SERIAL PRIMARY KEY,
  firstname VARCHAR(255),
  lastname VARCHAR(255),
  email VARCHAR(255),
  username VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS posts (
  id SERIAL PRIMARY KEY,
  user_id INT,
  title VARCHAR(255),
  description TEXT,
  likes INT,
  dislikes INT,
  FOREIGN KEY (user_id) REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS messages (
  id SERIAL PRIMARY KEY,
  content TEXT,
  sender_id INT,
  FOREIGN KEY (sender_id) REFERENCES app_user (id)
);

CREATE TABLE IF NOT EXISTS message_recipients (
  message_id INT,
  recipient_id INT,
  FOREIGN KEY (message_id) REFERENCES messages (id),
  FOREIGN KEY (recipient_id) REFERENCES app_user (id)
);