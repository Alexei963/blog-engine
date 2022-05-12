create table users
(
    id           integer      not null auto_increment,
    is_moderator TINYINT      not null,
    reg_time     datetime(6) not null,
    name         varchar(255) not null,
    email        varchar(255) not null,
    password     varchar(255) not null,
    code         varchar(255),
    photo        TEXT,
    primary key (id)
);

create table posts
(
    id                integer      not null auto_increment,
    is_active         TINYINT      not null,
    moderation_status enum('NEW', 'ACCEPTED', 'DECLINED') default 'NEW' not null,
    moderator_id      integer,
    user_id           integer      not null,
    time              datetime(6) not null,
    title             varchar(255) not null,
    text              TEXT         not null,
    view_count        integer      not null,
    primary key (id)
);

create table post_votes
(
    id      integer not null auto_increment,
    user_id integer not null,
    post_id integer not null,
    time    datetime(6) not null,
    value   TINYINT not null,
    primary key (id)
);

create table tags
(
    id   integer not null auto_increment,
    name varchar(255) not null,
    primary key (id)
);

create table tag2post
(
    post_id integer not null,
    tag_id  integer not null,
    primary key (post_id, tag_id)
);

create table post_comments
(
    id        integer not null auto_increment,
    parent_id integer,
    post_id   integer not null,
    user_id   integer not null,
    time      datetime(6) not null,
    text      TEXT    not null,
    primary key (id)
);

create table captcha_codes
(
    id          integer  not null auto_increment,
    time        datetime(6) not null,
    code        TINYTEXT not null,
    secret_code TINYTEXT not null,
    primary key (id)
);

create table global_settings
(
    id    integer      not null auto_increment,
    code  varchar(255) not null,
    name  varchar(255) not null,
    value varchar(255) not null,
    primary key (id)
);

alter table post_comments
    add constraint post_comments_post_fk foreign key (post_id) references posts (id);
alter table post_comments
    add constraint post_comments_user_fk foreign key (user_id) references users (id);
alter table post_votes
    add constraint post_votes_posts_fk foreign key (post_id) references posts (id);
alter table post_votes
    add constraint post_votes_users_fk foreign key (user_id) references users (id);
alter table posts
    add constraint posts_users_fk foreign key (user_id) references users (id);
alter table tag2post
    add constraint tag2post_tags_fk foreign key (tag_id) references tags (id);
alter table tag2post
    add constraint tag2post_posts_fk foreign key (post_id) references posts (id);