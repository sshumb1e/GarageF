CREATE TABLE IF NOT EXISTS "owner"
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 ),
    name text,
    age text,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS "car"
(
    id integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 ),
    model text,
    color text,
    owner_id integer,
    FOREIGN KEY (owner_id)
        REFERENCES "owner" (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);