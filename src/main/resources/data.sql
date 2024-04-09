INSERT INTO roles(name) VALUES ('ADMIN'), ('USER');

INSERT INTO users(email, password)
VALUES ('admin@example.com', '$2a$10$OkEqSSQ8sfarbBInvLF7MeEL0u5wLmzZgSVLGtpTdw3UXBg/Y4jfK'),
       ('user@example.com', '$2a$10$YLriM./laB0nhf0DaCkEiOHRkDyjvz4yjFyEnKmm.mCVSiFq8fBya');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1), (1, 2), (2, 2);