create table if not exists item (
    id serial primary key,
    description text,
    created timestamp,
    done boolean
);