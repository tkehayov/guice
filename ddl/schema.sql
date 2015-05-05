CREATE TABLE balance
(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT,
    cash NUMERIC(131089)
);
CREATE TABLE balance_id_seq
(
    sequence_name VARCHAR NOT NULL,
    last_value BIGINT NOT NULL,
    start_value BIGINT NOT NULL,
    increment_by BIGINT NOT NULL,
    max_value BIGINT NOT NULL,
    min_value BIGINT NOT NULL,
    cache_value BIGINT NOT NULL,
    log_cnt BIGINT NOT NULL,
    is_cycled BOOL NOT NULL,
    is_called BOOL NOT NULL
);
CREATE TABLE sessions
(
    id SERIAL PRIMARY KEY NOT NULL,
    user_id INT NOT NULL,
    session VARCHAR(40),
    expires BIGINT
);
CREATE TABLE sessions_id_seq
(
    sequence_name VARCHAR NOT NULL,
    last_value BIGINT NOT NULL,
    start_value BIGINT NOT NULL,
    increment_by BIGINT NOT NULL,
    max_value BIGINT NOT NULL,
    min_value BIGINT NOT NULL,
    cache_value BIGINT NOT NULL,
    log_cnt BIGINT NOT NULL,
    is_cycled BOOL NOT NULL,
    is_called BOOL NOT NULL
);
CREATE TABLE transaction_history
(
    id SERIAL DEFAULT nextval('transaction_history_id_seq'::regclass) NOT NULL,
    user_id INT NOT NULL,
    funds NUMERIC(131089),
    type VARCHAR(15),
    date BIGINT
);
CREATE TABLE transaction_history_id_seq
(
    sequence_name VARCHAR NOT NULL,
    last_value BIGINT NOT NULL,
    start_value BIGINT NOT NULL,
    increment_by BIGINT NOT NULL,
    max_value BIGINT NOT NULL,
    min_value BIGINT NOT NULL,
    cache_value BIGINT NOT NULL,
    log_cnt BIGINT NOT NULL,
    is_cycled BOOL NOT NULL,
    is_called BOOL NOT NULL
);
CREATE TABLE users
(
    id SERIAL PRIMARY KEY NOT NULL,
    username VARCHAR(20),
    password VARCHAR(40)
);
CREATE TABLE users_id_seq
(
    sequence_name VARCHAR NOT NULL,
    last_value BIGINT NOT NULL,
    start_value BIGINT NOT NULL,
    increment_by BIGINT NOT NULL,
    max_value BIGINT NOT NULL,
    min_value BIGINT NOT NULL,
    cache_value BIGINT NOT NULL,
    log_cnt BIGINT NOT NULL,
    is_cycled BOOL NOT NULL,
    is_called BOOL NOT NULL
);
CREATE UNIQUE INDEX balance_user_id_key ON balance (user_id);
CREATE UNIQUE INDEX sessions_user_id_key ON sessions (user_id);
CREATE UNIQUE INDEX users_username_key ON users (username);

