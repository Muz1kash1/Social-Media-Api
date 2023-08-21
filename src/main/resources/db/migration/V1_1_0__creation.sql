create table users
(
    id       bigserial not null unique,
    username text      not null unique,
    mail     text      not null unique,
    password text      not null,
    primary key (id)
);
create table friend_requests
(
    id          bigserial not null unique,
    sender_id   bigserial not null,
    receiver_id bigserial not null,
    status      text      not null,
    foreign key (sender_id) references users (id),
    foreign key (receiver_id) references users (id),
    primary key (id)
);
create table friends
(
    id       bigserial not null unique,
    user1_id bigserial not null,
    user2_id bigserial not null,
    foreign key (user1_id) references users (id),
    foreign key (user2_id) references users (id),
    primary key (id)
);
create table posts
(
    id         bigserial not null unique,
    user_id    bigserial not null,
    title      text      not null,
    text       text      not null,
    created_at timestamp not null,
    pictures   text[] not null,
    foreign key (user_id) references users (id),
    primary key (id)
);
create table subscriptions
(
    id           bigserial not null unique,
    follower_id  bigserial not null,
    following_id bigserial not null,
    foreign key (follower_id) references users (id),
    foreign key (following_id) references users (id),
    primary key (id)
);
create table chat_requests
(
    id          bigserial not null unique,
    sender_id   bigserial not null,
    receiver_id bigserial not null,
    status      text,
    foreign key (sender_id) references users (id),
    foreign key (receiver_id) references users (id),
    primary key (id)
);