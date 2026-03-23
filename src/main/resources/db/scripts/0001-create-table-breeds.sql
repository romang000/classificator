create table if not exists breeds
(
    id   bigserial primary key,
    name text not null,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz
);