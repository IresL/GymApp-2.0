-- USERS
create table if not exists users (
  id bigserial primary key,
  first_name varchar(100) not null,
  last_name  varchar(100) not null,
  username   varchar(150) not null unique,
  password_hash varchar(255) not null,
  is_active  boolean not null
);

-- TRAINING TYPES (reference)
create table if not exists training_types (
  id bigserial primary key,
  name varchar(50) not null unique
);

-- TRAINEES
create table if not exists trainees (
  id bigserial primary key,
  user_id bigint not null unique,
  date_of_birth date,
  address varchar(255),
  constraint fk_trainee_user foreign key (user_id) references users(id) on delete cascade
);

-- TRAINERS
create table if not exists trainers (
  id bigserial primary key,
  user_id bigint not null unique,
  specialization_id bigint not null,
  constraint fk_trainer_user foreign key (user_id) references users(id) on delete cascade,
  constraint fk_trainer_specialization foreign key (specialization_id) references training_types(id)
);

-- TRAINEE2TRAINER (many-to-many)
create table if not exists trainee2trainer (
  trainee_id bigint not null,
  trainer_id bigint not null,
  primary key (trainee_id, trainer_id),
  constraint fk_t2t_trainee foreign key (trainee_id) references trainees(id) on delete cascade,
  constraint fk_t2t_trainer foreign key (trainer_id) references trainers(id) on delete cascade
);

-- TRAININGS
create table if not exists trainings (
  id bigserial primary key,
  trainee_id bigint not null,
  trainer_id bigint not null,
  training_name varchar(200) not null,
  training_type_id bigint not null,
  training_date date not null,
  training_duration integer not null,
  constraint fk_training_trainee foreign key (trainee_id) references trainees(id) on delete cascade,
  constraint fk_training_trainer foreign key (trainer_id) references trainers(id),
  constraint fk_training_type foreign key (training_type_id) references training_types(id)
);

create index if not exists idx_trainings_trainee on trainings(trainee_id);
create index if not exists idx_trainings_trainer on trainings(trainer_id);
create unique index if not exists ux_users_username on users(username);
create unique index if not exists ux_training_types_name on training_types(name);
