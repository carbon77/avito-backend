INSERT INTO roles(name) VALUES ('ADMIN'), ('USER');

INSERT INTO users(email, password)
VALUES ('admin@example.com', '$2a$10$OkEqSSQ8sfarbBInvLF7MeEL0u5wLmzZgSVLGtpTdw3UXBg/Y4jfK'),
       ('user@example.com', '$2a$10$YLriM./laB0nhf0DaCkEiOHRkDyjvz4yjFyEnKmm.mCVSiFq8fBya');

INSERT INTO users_roles(user_id, role_id)
VALUES (1, 1), (1, 2), (2, 2);

INSERT INTO features DEFAULT VALUES;
INSERT INTO features DEFAULT VALUES;
INSERT INTO features DEFAULT VALUES;

INSERT INTO tags DEFAULT VALUES;
INSERT INTO tags DEFAULT VALUES;
INSERT INTO tags DEFAULT VALUES;
INSERT INTO tags DEFAULT VALUES;

INSERT INTO banners(content, is_active, created_at, updated_at, feature_id)
VALUES ('{"name": "John", "age": 30, "city": "New York", "hobbies": ["reading", "traveling"]}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('{"product": "Laptop", "brand": "Apple", "specs": {"RAM": "8GB", "storage": "256GB SSD"}}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
       ('{"name": "Sarah", "age": 25, "city": "Los Angeles", "interests": {"music": "pop", "movies": "comedy"}}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('{"car": "Tesla Model S", "year": 2021, "features": ["Autopilot", "Full Self-Driving"]}'::jsonb, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
       ('{"restaurant": "Italian Bistro", "location": "Downtown", "menu": {"appetizers": ["Bruschetta", "Calamari"], "main_dishes": ["Pasta", "Pizza"], "desserts": ["Tiramisu", "Cannoli"]}}'::jsonb, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('{"book": "Science Fiction Novel", "author": "Stephen King", "publish_year": 2019}'::jsonb, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('{"employee": "Marketing Manager", "department": "Sales", "responsibilities": ["Campaign Management", "Lead Generation"]}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 2),
       ('{"team": "Basketball", "coach": "Steve Kerr", "players": ["Stephen Curry", "Klay Thompson", "Draymond Green"]}'::jsonb, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('{"smartphone": "Samsung Galaxy", "model": "S21", "specs": {"RAM": "12GB", "storage": "256GB"}}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3),
       ('{"travel_destination": "Tropical Island", "activities": ["Snorkeling", "Beach Volleyball"], "accommodation": {"type": "Beachfront Villa", "amenities": ["Private Pool", "Jacuzzi"]}}'::jsonb, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 3);

INSERT INTO banners_tags (banner_id, tag_id) VALUES
                                                 (1, 1),
                                                 (2, 1),         (2, 3), (2, 4),
                                                 (3, 1), (3, 2), (3, 3), (3, 4),
                                                 (4, 1), (4, 2),         (4, 4),
                                                 (5, 1), (5, 2), (5, 3), (5, 4),
                                                                 (6, 3), (6, 4),
                                                 (7, 1), (7, 2),
                                                 (8, 1), (8, 2), (8, 3), (8, 4),
                                                         (9, 2), (9, 3),
                                                 (10, 1),        (10, 3),(10, 4);