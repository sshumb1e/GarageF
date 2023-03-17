WITH inserted_id AS (

INSERT INTO "owner" (name, age)
VALUES ('Test owner', 77) RETURNING id)

INSERT INTO "car" (model, color, owner_id)
VALUES ('Test car', 'Black', (select id from inserted_id));