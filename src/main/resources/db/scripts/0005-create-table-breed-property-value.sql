create table if not exists breed_property_value
(
    id                bigserial not null,
    breed_id          bigint    not null,
    property_id       bigint    not null,
    property_value_id bigint    not null,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz,

    constraint uq_breed_property_value unique (breed_id, property_id, property_value_id),
    constraint fk_breed_property_value_breed_id_breed foreign key (breed_id) references breeds(id),
    constraint fk_breed_property_value_property_id_property foreign key (property_id) references properties(id),
    constraint fk_breed_property_value_property_value_id_property_value foreign key (property_value_id) references property_values(id)
);