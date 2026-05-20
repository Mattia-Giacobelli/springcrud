-- Create admin if not exists
INSERT INTO permissions(permission_type)
SELECT 'ADMIN'
WHERE NOT EXISTS(SELECT 1 FROM permissions WHERE permission_type = 'ADMIN');

-- Create user if not exists
INSERT INTO permissions(permission_type)
SELECT 'USER'
WHERE NOT EXISTS(SELECT 1 FROM permissions WHERE permission_type = 'USER');

-- email admin@gmail.com, nome admin password admin!1234
INSERT INTO users (username, email, password, permission_id)
SELECT
'Admin',
'admin@gmail.com',
'$2b$10$piasoScClqba/sI2BCzwIeKBKzzGNMeRnTrG4XTiR8X0lfUX5KyF6', -- bcrypt('admin!1234')
(SELECT id FROM permissions WHERE permission_type = 'ADMIN')
WHERE NOT EXISTS (
SELECT 1 FROM users WHERE email = 'admin@gmail.com');