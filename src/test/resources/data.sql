ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE roles_role_id_seq RESTART WITH 1;
ALTER SEQUENCE features_feature_id_seq RESTART WITH 1;
ALTER SEQUENCE tags_tag_id_seq RESTART WITH 1;
ALTER SEQUENCE banners_banner_id_seq RESTART WITH 1;

INSERT INTO roles(name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users(email, password)
VALUES ('admin@example.com', '$2a$10$OkEqSSQ8sfarbBInvLF7MeEL0u5wLmzZgSVLGtpTdw3UXBg/Y4jfK'),
       ('user@example.com', '$2a$10$YLriM./laB0nhf0DaCkEiOHRkDyjvz4yjFyEnKmm.mCVSiFq8fBya');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2);

INSERT INTO features DEFAULT
VALUES;
INSERT INTO features DEFAULT
VALUES;
INSERT INTO features DEFAULT
VALUES;

INSERT INTO tags DEFAULT
VALUES;
INSERT INTO tags DEFAULT
VALUES;
INSERT INTO tags DEFAULT
VALUES;
INSERT INTO tags DEFAULT
VALUES;

INSERT INTO banners(content, is_active, created_at, updated_at, feature_id)
VALUES ('{
  "name": "John",
  "age": 30,
  "city": "New York",
  "hobbies": [
    "reading",
    "traveling"
  ]
}'::jsonb, true, make_timestamp(2020, 8, 8, 2, 30, 20), make_timestamp(2020, 8, 8, 2, 30, 20), 1),
       ('{
         "product": "Laptop",
         "brand": "Apple",
         "specs": {
           "RAM": "8GB",
           "storage": "256GB SSD"
         }
       }'::jsonb, false, make_timestamp(2020, 8, 8, 2, 30, 20), make_timestamp(2020, 8, 8, 2, 30, 20), 2);

INSERT INTO banners_tags (banner_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (2, 3),
       (2, 4);