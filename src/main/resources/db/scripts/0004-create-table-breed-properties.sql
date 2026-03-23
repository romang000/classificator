create table if not exists breed_properties
(
    id          bigserial primary key,
    breed_id    bigint not null,
    property_id bigint not null,
    created_at   timestamptz not null default now(),
    updated_at   timestamptz,

    constraint fk_breed_properties_breeds foreign key (breed_id) references breeds (id),
    constraint fk_breed_properties_properties foreign key (property_id) references properties (id)
);