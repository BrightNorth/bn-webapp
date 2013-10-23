
create table user (
  email varchar(100) not null primary key,
  password varchar(64) not null,
  roles varchar(255) not null
);

insert into user(email,password,roles) values ('user@example.com', '$2a$10$rt/PWKymdUxoqIwRCUi76upun8OgXrltWmOrRafg2gCPNhHSCa6hq', 'user');
insert into user(email,password,roles) values ('guest@example.com', '$2a$10$rt/PWKymdUxoqIwRCUi76upun8OgXrltWmOrRafg2gCPNhHSCa6hq', 'guest');
