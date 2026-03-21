-- V2__seed_data.sql
-- Seed categories and default admin account

INSERT INTO categories (name, description, emoji, sort_order) VALUES
  ('Farm Life',        'Discover the rhythms of working farms', '🌾', 1),
  ('Artisan Crafts',   'Learn traditional crafts from master artisans', '🏺', 2),
  ('Fishing & Sea',    'Coastal and river fishing with local fishermen', '🎣', 3),
  ('Shepherding',      'Mountain pastures and pastoral traditions', '🐑', 4),
  ('Winemaking',       'From vine to glass — wine and cider production', '🍷', 5),
  ('Forest & Nature',  'Foraging, tracking, and wilderness skills', '🌲', 6),
  ('Cooking & Food',   'Regional recipes and traditional cuisine', '🍳', 7),
  ('Horse & Equestrian','Riding, care, and equestrian heritage', '🐴', 8);

-- Default admin: admin@ruralxperience.com / Admin@2026!
-- BCrypt hash of "Admin@2026!"
INSERT INTO users (email, password, first_name, last_name, role, enabled, email_verified)
VALUES (
  'admin@ruralxperience.com',
  '$2a$10$QSIujVKDvNV6S0YqfO4ymOwB4hqehjaWKayYFg85dTJvld0Z0Pvla',
  'Admin',
  'RuralXperience',
  'ADMIN',
  true,
  true
);
