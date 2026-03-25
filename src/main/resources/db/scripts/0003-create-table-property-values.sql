create table if not exists property_values
(
    id          bigserial primary key,
    property_id bigint      not null,
    value       text        not null,
    created_at  timestamptz not null default now(),
    updated_at  timestamptz,

    constraint uq_property_value unique (property_id, value),
    constraint fk_property_values_properties foreign key (property_id) references properties (id)
);