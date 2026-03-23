create table if not exists properties
(
    id         bigserial primary key,
    name       text        not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz,

    constraint uq_properties_name unique (name)
);