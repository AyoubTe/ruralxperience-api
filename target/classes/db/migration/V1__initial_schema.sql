-- V1__initial_schema.sql
-- RuralXperience — complete initial database schema
-- Spring Boot Mastery / 2026

CREATE TABLE users (
    id             BIGSERIAL PRIMARY KEY,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,
    first_name     VARCHAR(50)  NOT NULL,
    last_name      VARCHAR(50)  NOT NULL,
    avatar_url     VARCHAR(500),
    phone_number   VARCHAR(30),
    role           VARCHAR(20)  NOT NULL CHECK (role IN ('EXPLORER','HOST','ADMIN')),
    enabled        BOOLEAN      NOT NULL DEFAULT TRUE,
    email_verified BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE host_profiles (
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
    bio            TEXT,
    location       VARCHAR(255) NOT NULL,
    latitude       DOUBLE PRECISION,
    longitude      DOUBLE PRECISION,
    is_verified    BOOLEAN      NOT NULL DEFAULT FALSE,
    total_earnings NUMERIC(10,2) NOT NULL DEFAULT 0,
    website_url    VARCHAR(500),
    created_at     TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    emoji       VARCHAR(10),
    icon_url    VARCHAR(500),
    sort_order  INTEGER      NOT NULL DEFAULT 0,
    active      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE experiences (
    id                BIGSERIAL PRIMARY KEY,
    host_id           BIGINT        NOT NULL REFERENCES host_profiles(id),
    category_id       BIGINT        NOT NULL REFERENCES categories(id),
    title             VARCHAR(150)  NOT NULL,
    short_description VARCHAR(500)  NOT NULL,
    full_description  TEXT          NOT NULL,
    price_per_person  NUMERIC(10,2) NOT NULL,
    duration_days     INTEGER       NOT NULL CHECK (duration_days >= 1),
    max_guests        INTEGER       NOT NULL CHECK (max_guests >= 1),
    location          VARCHAR(255)  NOT NULL,
    latitude          DOUBLE PRECISION,
    longitude         DOUBLE PRECISION,
    country           VARCHAR(100)  NOT NULL DEFAULT 'France',
    status            VARCHAR(30)   NOT NULL DEFAULT 'DRAFT'
                          CHECK (status IN ('DRAFT','PENDING_REVIEW','PUBLISHED','REJECTED','ARCHIVED')),
    average_rating    NUMERIC(3,2)  NOT NULL DEFAULT 0,
    review_count      INTEGER       NOT NULL DEFAULT 0,
    cover_photo_url   VARCHAR(500),
    rejection_reason  TEXT,
    created_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT NOW(),
    published_at      TIMESTAMP
);

CREATE TABLE experience_photos (
    id            BIGSERIAL PRIMARY KEY,
    experience_id BIGINT       NOT NULL REFERENCES experiences(id) ON DELETE CASCADE,
    url           VARCHAR(500) NOT NULL,
    storage_key   VARCHAR(500) NOT NULL,
    alt_text      VARCHAR(255),
    sort_order    INTEGER      NOT NULL DEFAULT 0,
    uploaded_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE TABLE daily_agenda_items (
    id            BIGSERIAL PRIMARY KEY,
    experience_id BIGINT       NOT NULL REFERENCES experiences(id) ON DELETE CASCADE,
    day_number    INTEGER      NOT NULL,
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    start_time    VARCHAR(10),
    end_time      VARCHAR(10)
);

CREATE TABLE bookings (
    id                  BIGSERIAL PRIMARY KEY,
    explorer_id         BIGINT        NOT NULL REFERENCES users(id),
    experience_id       BIGINT        NOT NULL REFERENCES experiences(id),
    start_date          DATE          NOT NULL,
    end_date            DATE          NOT NULL,
    number_of_guests    INTEGER       NOT NULL CHECK (number_of_guests >= 1),
    total_price         NUMERIC(10,2) NOT NULL,
    status              VARCHAR(20)   NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING','CONFIRMED','DECLINED','CANCELLED','COMPLETED')),
    special_requests    TEXT,
    cancellation_reason TEXT,
    host_message        TEXT,
    created_at          TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP     NOT NULL DEFAULT NOW(),
    confirmed_at        TIMESTAMP,
    completed_at        TIMESTAMP,
    CONSTRAINT chk_dates CHECK (end_date > start_date)
);

CREATE TABLE reviews (
    id              BIGSERIAL PRIMARY KEY,
    booking_id      BIGINT    NOT NULL UNIQUE REFERENCES bookings(id),
    experience_id   BIGINT    NOT NULL REFERENCES experiences(id),
    explorer_id     BIGINT    NOT NULL REFERENCES users(id),
    rating          INTEGER   NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         TEXT,
    host_reply      TEXT,
    host_replied_at TIMESTAMP,
    is_visible      BOOLEAN   NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE wishlist_items (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT    NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    experience_id BIGINT    NOT NULL REFERENCES experiences(id) ON DELETE CASCADE,
    added_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_wishlist_user_experience UNIQUE (user_id, experience_id)
);

CREATE TABLE notifications (
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    type         VARCHAR(50)  NOT NULL,
    title        VARCHAR(255) NOT NULL,
    message      TEXT         NOT NULL,
    related_id   BIGINT,
    related_type VARCHAR(50),
    is_read      BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW(),
    read_at      TIMESTAMP
);

CREATE TABLE audit_logs (
    id           BIGSERIAL PRIMARY KEY,
    admin_id     BIGINT       NOT NULL,
    admin_email  VARCHAR(255) NOT NULL,
    action       VARCHAR(100) NOT NULL,
    entity_type  VARCHAR(100) NOT NULL,
    entity_id    BIGINT,
    details      TEXT,
    ip_address   VARCHAR(50),
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- Indexes
CREATE INDEX idx_experience_status       ON experiences(status);
CREATE INDEX idx_experience_host         ON experiences(host_id);
CREATE INDEX idx_experience_category     ON experiences(category_id);
CREATE INDEX idx_experience_rating       ON experiences(average_rating DESC);
CREATE INDEX idx_booking_explorer        ON bookings(explorer_id);
CREATE INDEX idx_booking_experience      ON bookings(experience_id);
CREATE INDEX idx_booking_status          ON bookings(status);
CREATE INDEX idx_booking_dates           ON bookings(start_date, end_date);
CREATE INDEX idx_notification_user       ON notifications(user_id);
CREATE INDEX idx_notification_read       ON notifications(is_read);
CREATE INDEX idx_audit_admin             ON audit_logs(admin_id);
CREATE INDEX idx_audit_entity            ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_action            ON audit_logs(action);
CREATE INDEX idx_audit_created_at        ON audit_logs(created_at DESC);
CREATE INDEX idx_review_experience       ON reviews(experience_id);
