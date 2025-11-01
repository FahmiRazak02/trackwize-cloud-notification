CREATE DATABASE IF NOT EXISTS trackwize_db;
USE trackwize_db;

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    contact_number VARCHAR(255),
    name VARCHAR(255),
    status VARCHAR(255) DEFAULT 'ACTIVE',
    created_by bigint NOT NULL DEFAULT '0',
    created_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint DEFAULT NULL,
    updated_date timestamp NULL DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO users (email, password, contact_number, name, status, created_by)
VALUES
('fahmirazak0201@gmail.com', '$2a$12$Hb/G4rOnWl15Q1opGT3i8eszCWOT4DGnyOOw4fApM6uYuHYfmFbia', '0145000973', 'Fahmi Razak', 'ACTIVE', 0),
('abukasim93@gmail.com', '$2a$12$Hb/G4rOnWl15Q1opGT3i8eszCWOT4DGnyOOw4fApM6uYuHYfmFbia', '0145000972', 'Abu Kassim', 'ACTIVE', 0);

CREATE TABLE tokens (
    token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    access_token VARCHAR(500) NOT NULL,
    refresh_token VARCHAR(500) NOT NULL,
    status VARCHAR(255) DEFAULT 'ACTIVE',
    created_by bigint NOT NULL DEFAULT '0',
    created_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by bigint DEFAULT NULL,
    updated_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);