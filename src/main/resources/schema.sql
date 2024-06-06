CREATE TABLE IF NOT EXISTS exchange_rates (
    id BIGINT AUTO_INCREMENT,
    origin_currency VARCHAR(3) NOT NULL,
    origin_amount DECIMAL(19, 4) NOT NULL,
    exchange_rate_amount DECIMAL(19, 4) NOT NULL,
    destination_currency VARCHAR(3) NOT NULL,
    destination_amount DECIMAL(19, 4) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_at TIMESTAMP,
    updated_by VARCHAR(255),

    CONSTRAINT pk_exchange_rates primary key (id)
);


CREATE TABLE users (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT pk_users primary key (id),
    CONSTRAINT unique_email_users unique (email)
);
