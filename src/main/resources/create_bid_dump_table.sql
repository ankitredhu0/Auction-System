CREATE TABLE bid_dump (
    id SERIAL PRIMARY KEY,
    item_id BIGINT,
    bidding_price BIGINT,
    user_id BIGINT
);