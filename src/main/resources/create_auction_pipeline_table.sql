CREATE TABLE auction_pipeline (
    item_id BIGINT PRIMARY KEY,
    item_category VARCHAR(255),
    item_name VARCHAR(255),
    description VARCHAR(255),
    media_url VARCHAR(255),
    auction_slot TIMESTAMP,
    base_price BIGINT,
    condition VARCHAR(255),
    buying_year VARCHAR(255),
    auction_state VARCHAR(255),
    auction_id BIGINT,
    minimum_price BIGINT,
    winning_amount BIGINT,
    winner BIGINT
);
