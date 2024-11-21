CREATE SCHEMA IF NOT EXISTS schema;

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role TEXT CHECK (role IN ('Employee', 'Manager', 'Admin')) DEFAULT 'Employee'
);

CREATE TABLE IF NOT EXISTS software (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    access_levels TEXT CHECK (access_levels IN ('Read', 'Write', 'Admin')) NOT NULL
);

CREATE TABLE IF NOT EXISTS request (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    software_id INT NOT NULL,
    access_type TEXT CHECK (access_type IN ('Read', 'Write', 'Admin')) NOT NULL,
    reason TEXT, 
    status TEXT CHECK (status IN ('Pending', 'Approved', 'Rejected')),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_software FOREIGN KEY (software_id) REFERENCES software(id) ON DELETE CASCADE
);
