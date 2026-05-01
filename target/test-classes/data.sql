-- RuralXperience Test Data

-- ==========================================
-- 1. USERS
-- ==========================================
-- Admin (ID 1)
INSERT INTO users (id, email, password, first_name, last_name, role, enabled, email_verified, created_at, updated_at) VALUES
    (1, 'admin@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Super', 'Admin', 'ADMIN', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Hosts (IDs 2 to 6)
INSERT INTO users (id, email, password, first_name, last_name, role, enabled, email_verified, created_at, updated_at) VALUES
                                                                                                                          (2, 'host1@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Jean', 'Dupont', 'HOST', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (3, 'host2@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Marie', 'Martin', 'HOST', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (4, 'host3@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Pierre', 'Bernard', 'HOST', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (5, 'host4@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Sophie', 'Dubois', 'HOST', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (6, 'host5@ruralxperience.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Luc', 'Thomas', 'HOST', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Explorers (IDs 7 to 15)
INSERT INTO users (id, email, password, first_name, last_name, role, enabled, email_verified, created_at, updated_at) VALUES
                                                                                                                          (7, 'explorer1@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Alice', 'Wonder', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (8, 'explorer2@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Bob', 'Marley', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (9, 'explorer3@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Charlie', 'Chaplin', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (10, 'explorer4@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Diana', 'Prince', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (11, 'explorer5@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Ethan', 'Hunt', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (12, 'explorer6@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Fiona', 'Gallagher', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (13, 'explorer7@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'George', 'Lucas', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (14, 'explorer8@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Hannah', 'Abbott', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                          (15, 'explorer9@test.com', '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla', 'Ian', 'Wright', 'EXPLORER', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- ==========================================
-- 2. HOST PROFILES
-- ==========================================
INSERT INTO host_profiles (id, user_id, bio, location, latitude, longitude, is_verified, total_earnings, created_at) VALUES
                                                                                                                         (1, 2, 'Third-generation dairy farmer passionate about organic agriculture.', 'Normandy, France', 49.1828, -0.3706, true, 1250.00, CURRENT_TIMESTAMP),
                                                                                                                         (2, 3, 'Pottery master with 20 years of experience in traditional ceramics.', 'Provence, France', 43.5297, 5.4474, true, 3400.00, CURRENT_TIMESTAMP),
                                                                                                                         (3, 4, 'Breton fisherman sailing the Atlantic coast. Love sharing the sea.', 'Brittany, France', 48.3903, -4.4860, true, 800.00, CURRENT_TIMESTAMP),
                                                                                                                         (4, 5, 'Sommelier and vineyard owner producing organic natural wines.', 'Bordeaux, France', 44.8377, -0.5791, true, 5600.00, CURRENT_TIMESTAMP),
                                                                                                                         (5, 6, 'Forager and woodland expert teaching survival and nature craft.', 'Vosges, France', 48.1767, 6.4500, false, 0.00, CURRENT_TIMESTAMP);


-- ==========================================
-- 3. CATEGORIES
-- ==========================================
INSERT INTO categories (id, name, description, emoji, sort_order, active) VALUES
                                                                              (1, 'Farming', 'Learn farm management.', '🚜', 1, true),
                                                                              (2, 'Cooking', 'Traditional recipes.', '🥖', 2, true),
                                                                              (3, 'Fishing', 'Ocean and river fishing.', '🎣', 3, true),
                                                                              (4, 'Animals', 'Interact with farm animals.', '🐑', 4, true),
                                                                              (5, 'Wine', 'Vineyard tours and tasting.', '🍷', 5, true),
                                                                              (6, 'Survival', 'Forest and survival skills.', '🏕️', 6, true),
                                                                              (7, 'Local Food', 'Market tours and eating.', '🧀', 7, true),
                                                                              (8, 'Riding', 'Horseback adventures.', '🐎', 8, true);


-- ==========================================
-- 4. EXPERIENCES
-- ==========================================
INSERT INTO experiences (id, host_id, category_id, title, short_description, full_description, price_per_person, duration_days, max_guests, location, status, average_rating, review_count, created_at, updated_at) VALUES
                                                                                                                                                                                                                        (1, 1, 1, 'A Day in the Dairy Farm', 'Milk cows and make your own butter.', 'Join us for an early morning milking session...', 45.00, 1, 4, 'Normandy', 'PUBLISHED', 4.8, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (2, 1, 1, 'Tractor Driving 101', 'Learn to drive a real farm tractor.', 'A safe, enclosed field where you will learn the basics...', 80.00, 1, 2, 'Normandy', 'PUBLISHED', 4.5, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (3, 1, 4, 'Spring Lambing Experience', 'Help welcome new life to the farm.', 'Stay with us during the busy spring season...', 120.00, 3, 4, 'Normandy', 'PUBLISHED', 5.0, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (4, 1, 1, 'Organic Composting Masterclass', 'Turn waste into black gold.', 'Deep dive into soil health and composting.', 25.00, 1, 10, 'Normandy', 'DRAFT', 0.0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (5, 2, 2, 'Intro to Wheel Throwing', 'Create your first ceramic bowl.', 'Step-by-step guidance on the potter''s wheel...', 65.00, 1, 6, 'Provence', 'PUBLISHED', 4.9, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (6, 2, 2, 'Advanced Glazing Techniques', 'Master the art of ceramic glazing.', 'For those who already know the basics.', 90.00, 2, 4, 'Provence', 'PUBLISHED', 4.7, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (7, 2, 7, 'Provencal Cooking Class', 'Cook with fresh market ingredients.', 'We start at the local market, buy fresh vegetables...', 110.00, 1, 5, 'Provence', 'PUBLISHED', 5.0, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (8, 2, 2, 'Kiln Building Workshop', 'Build a traditional wood-fired kiln.', 'A heavy-duty weekend building a brick kiln...', 200.00, 4, 3, 'Provence', 'PENDING_REVIEW', 0.0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (9, 3, 3, 'Dawn Fishing on the Atlantic', 'Catch your own dinner on the open sea.', 'Depart at 5 AM. We target sea bass and mackerel.', 150.00, 1, 3, 'Brittany', 'PUBLISHED', 4.6, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (10, 3, 3, 'Oyster Farming Tour', 'Visit the oyster beds at low tide.', 'Learn how oysters are cultivated...', 55.00, 1, 8, 'Brittany', 'PUBLISHED', 4.8, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (11, 3, 3, 'Net Mending & Seamanship', 'Learn the forgotten skills of sailors.', 'Knot tying, net mending, and maritime history.', 30.00, 1, 10, 'Brittany', 'REJECTED', 0.0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (12, 3, 3, 'Weekend Sailor', 'Two days sailing the rugged coast.', 'Sleep on the boat and learn coastal navigation.', 350.00, 2, 2, 'Brittany', 'PUBLISHED', 4.9, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (13, 4, 5, 'Grape Harvest Experience', 'Hand-pick grapes during the Vendanges.', 'Join the harvest crew, pick grapes...', 0.00, 1, 15, 'Bordeaux', 'PUBLISHED', 4.9, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (14, 4, 5, 'Natural Wine Tasting', 'Taste 6 organic wines in the cellar.', 'A guided tour of our barrel room...', 40.00, 1, 12, 'Bordeaux', 'PUBLISHED', 4.4, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (15, 4, 5, 'Vine Pruning Workshop', 'Winter vine care.', 'Learn the delicate art of winter pruning...', 35.00, 1, 8, 'Bordeaux', 'ARCHIVED', 4.0, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (16, 4, 7, 'Wine & Cheese Pairing', 'The ultimate French experience.', 'Local cheeses paired perfectly with our estate wines.', 60.00, 1, 10, 'Bordeaux', 'PUBLISHED', 5.0, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (17, 5, 6, 'Wild Mushroom Foraging', 'Find and identify edible mushrooms.', 'A walk through the Vosges forest...', 45.00, 1, 8, 'Vosges', 'PUBLISHED', 4.7, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (18, 5, 6, 'Overnight Wilderness Survival', 'Fire making, shelter building, and tracking.', 'Leave your phone behind...', 180.00, 2, 4, 'Vosges', 'PUBLISHED', 4.8, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (19, 5, 8, 'Forest Trail Riding', 'Horseback riding through ancient woods.', 'A gentle ride suitable for beginners...', 75.00, 1, 4, 'Vosges', 'PENDING_REVIEW', 0.0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                                                                                                        (20, 5, 6, 'Animal Tracking', 'Identify footprints and scat.', 'Learn to read the forest floor like a book.', 35.00, 1, 6, 'Vosges', 'DRAFT', 0.0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- ==========================================
-- 5. EXPERIENCE PHOTOS
-- ==========================================
INSERT INTO experience_photos (experience_id, url, storage_key, sort_order, uploaded_at) VALUES
                                                                                             (1, 'https://example.com/photos/farm1.jpg', 'farm1_key', 1, CURRENT_TIMESTAMP),
                                                                                             (5, 'https://example.com/photos/pottery1.jpg', 'pottery1_key', 1, CURRENT_TIMESTAMP),
                                                                                             (9, 'https://example.com/photos/fishing1.jpg', 'fishing1_key', 1, CURRENT_TIMESTAMP),
                                                                                             (13, 'https://example.com/photos/wine1.jpg', 'wine1_key', 1, CURRENT_TIMESTAMP),
                                                                                             (17, 'https://example.com/photos/forest1.jpg', 'forest1_key', 1, CURRENT_TIMESTAMP);


-- ==========================================
-- 6. DAILY AGENDA ITEMS
-- ==========================================
INSERT INTO daily_agenda_items (experience_id, day_number, title, description, start_time, end_time) VALUES
                                                                                                         (1, 1, 'Morning Milking', 'Meet at the barn to milk the cows.', '06:00', '08:00'),
                                                                                                         (1, 1, 'Butter Churning', 'Turn fresh cream into butter.', '09:00', '11:00'),
                                                                                                         (12, 1, 'Setting Sail', 'Safety briefing and departure.', '09:00', '12:00'),
                                                                                                         (12, 2, 'Navigation basics', 'Reading charts and compass work.', '10:00', '14:00');


-- ==========================================
-- 7. BOOKINGS
-- ==========================================
INSERT INTO bookings (id, explorer_id, experience_id, start_date, end_date, number_of_guests, total_price, status, created_at, updated_at) VALUES
                                                                                                                                               (1, 7, 1, CURRENT_DATE - INTERVAL '10' DAY, CURRENT_DATE - INTERVAL '9' DAY, 2, 90.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (2, 8, 5, CURRENT_DATE - INTERVAL '5' DAY,  CURRENT_DATE - INTERVAL '4' DAY, 1, 65.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (3, 9, 7, CURRENT_DATE - INTERVAL '2' DAY,  CURRENT_DATE - INTERVAL '1' DAY, 2, 220.00, 'COMPLETED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (4, 10, 9, CURRENT_DATE + INTERVAL '5' DAY,  CURRENT_DATE + INTERVAL '6' DAY, 3, 450.00, 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (5, 11, 13, CURRENT_DATE + INTERVAL '10' DAY, CURRENT_DATE + INTERVAL '11' DAY, 1, 0.00, 'CONFIRMED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (6, 12, 18, CURRENT_DATE + INTERVAL '15' DAY, CURRENT_DATE + INTERVAL '17' DAY, 2, 360.00, 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (7, 13, 10, CURRENT_DATE + INTERVAL '20' DAY, CURRENT_DATE + INTERVAL '21' DAY, 4, 220.00, 'DECLINED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                                                                               (8, 14, 14, CURRENT_DATE - INTERVAL '15' DAY, CURRENT_DATE - INTERVAL '14' DAY, 2, 80.00, 'CANCELLED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- ==========================================
-- 8. REVIEWS
-- ==========================================
INSERT INTO reviews (booking_id, experience_id, explorer_id, rating, comment, host_reply, host_replied_at, created_at) VALUES
                                                                                                                           (1, 1, 7, 5, 'Amazing experience! The butter tasted incredible.', 'Thank you Alice, glad you enjoyed it!', CURRENT_DATE - INTERVAL '8' DAY, CURRENT_TIMESTAMP),
                                                                                                                           (2, 5, 8, 4, 'Very relaxing, but my bowl came out a bit wonky.', NULL, NULL, CURRENT_TIMESTAMP),
                                                                                                                           (3, 7, 9, 5, 'Best ratatouille I have ever had. Marie is a fantastic teacher.', 'Merci Charlie! You are a natural chef.', CURRENT_DATE - INTERVAL '1' DAY, CURRENT_TIMESTAMP);


-- ==========================================
-- 9. WISHLIST ITEMS
-- ==========================================
INSERT INTO wishlist_items (user_id, experience_id, added_at) VALUES
                                                                  (7, 10, CURRENT_TIMESTAMP), (7, 18, CURRENT_TIMESTAMP),
                                                                  (8, 13, CURRENT_TIMESTAMP), (8, 1, CURRENT_TIMESTAMP),
                                                                  (15, 9, CURRENT_TIMESTAMP), (15, 14, CURRENT_TIMESTAMP), (15, 17, CURRENT_TIMESTAMP);


-- ==========================================
-- 10. NOTIFICATIONS
-- ==========================================
INSERT INTO notifications (user_id, type, title, message, related_id, related_type, is_read, created_at) VALUES
                                                                                                             (2, 'BOOKING_REQUEST', 'New Booking Request', 'Alice Wonder requested to book A Day in the Dairy Farm.', 1, 'BOOKING', true, CURRENT_TIMESTAMP),
                                                                                                             (7, 'BOOKING_CONFIRMED', 'Booking Confirmed!', 'Your booking for A Day in the Dairy Farm is confirmed.', 1, 'BOOKING', true, CURRENT_TIMESTAMP),
                                                                                                             (5, 'NEW_REVIEW', 'New Review', 'Charlie left a 5-star review.', 3, 'REVIEW', false, CURRENT_TIMESTAMP);


-- ==========================================
-- 11. AUDIT LOGS
-- ==========================================
INSERT INTO audit_logs (admin_id, admin_email, action, entity_type, entity_id, details, ip_address, created_at) VALUES
                                                                                                                    (1, 'admin@ruralxperience.com', 'APPROVE_EXPERIENCE', 'EXPERIENCE', 1, 'Approved "A Day in the Dairy Farm"', '192.168.1.1', CURRENT_TIMESTAMP),
                                                                                                                    (1, 'admin@ruralxperience.com', 'REJECT_EXPERIENCE', 'EXPERIENCE', 11, 'Rejected "Net Mending" due to insufficient safety details.', '192.168.1.1', CURRENT_TIMESTAMP);


-- ==========================================
-- 12. SEQUENCES
-- ==========================================
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE host_profiles ALTER COLUMN id RESTART WITH 100;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 100;
ALTER TABLE experiences ALTER COLUMN id RESTART WITH 100;
ALTER TABLE experience_photos ALTER COLUMN id RESTART WITH 100;
ALTER TABLE daily_agenda_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE bookings ALTER COLUMN id RESTART WITH 100;
ALTER TABLE reviews ALTER COLUMN id RESTART WITH 100;
ALTER TABLE wishlist_items ALTER COLUMN id RESTART WITH 100;
ALTER TABLE notifications ALTER COLUMN id RESTART WITH 100;
ALTER TABLE audit_logs ALTER COLUMN id RESTART WITH 100;