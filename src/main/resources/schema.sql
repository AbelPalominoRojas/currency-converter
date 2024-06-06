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
