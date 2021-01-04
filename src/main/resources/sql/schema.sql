CREATE TABLE IF NOT EXISTS users
(
    id_user         INT         NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(45) NOT NULL,
    last_name  VARCHAR(45) NOT NULL,
    username   VARCHAR(45) NOT NULL,
    password   VARCHAR(200) NOT NULL,
    salt       VARCHAR(200) NOT NULL,
    role       VARCHAR(45) NOT NULL DEFAULT 'REGULAR',
    registered INT         NULL     DEFAULT 0,
    PRIMARY KEY (id_user),
    UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS cities
(
    id_city         INT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(200) NOT NULL,
    country     VARCHAR(200) NOT NULL,
    description VARCHAR(500) NULL,
    PRIMARY KEY (id_city)
);

CREATE TABLE IF NOT EXISTS comments
(
    id_comment         INT          NOT NULL AUTO_INCREMENT,
    comment    VARCHAR(500) NOT NULL,
    fk_city_id INT          NULL,
    created_date DATE NULL,
    modified_date DATE NULL,
    PRIMARY KEY (id_comment),
    CONSTRAINT fk_city FOREIGN KEY (fk_city_id) REFERENCES cities (id_city)
)