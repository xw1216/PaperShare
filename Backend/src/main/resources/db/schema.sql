drop table if exists rel_paper_area;
drop table if exists rel_user_area, rel_user_repo_focus;
drop table if exists rel_repo_paper;
drop table if exists comment, note, session, check_code;
drop table if exists area, paper;
drop table if exists repo, user;

create table user
(
    id          varchar(127) not null primary key,
    email       varchar(255) not null unique,
    name        varchar(255) not null,
    pass        varchar(511) not null,
    motto       varchar(1023),
    role        enum ('USER', 'ADMIN'),
    status      enum ('NORMAL', 'BAN'),
    signup_time datetime     not null,
    signin_time datetime     not null,
    unique user_email_index (email)
);

create table check_code
(
    id          varchar(127) not null primary key,
    email       varchar(255) not null unique,
    code        varchar(31)  not null,
    expire_time datetime     not null,
    unique check_code_email_index (email)
);

create table session
(
    id          varchar(127) not null primary key,
    user_id     varchar(127) not null unique,
    secret      varchar(255) not null,
    expire_time datetime     not null,
    close       tinyint(1)   not null default 0,
    constraint fk_session_user_id foreign key (user_id) references user (id) on delete cascade
);

create table repo
(
    id          varchar(127) not null primary key,
    user_id     varchar(127) not null,
    title       varchar(127) not null,
    cont        varchar(511)          default '' not null,
    visible     tinyint(1)   not null default 1,
    create_time datetime     not null,
    update_time datetime     not null,
    constraint fk_repo_user_id foreign key (user_id) references user (id) on delete cascade
);

create table paper
(
    id      varchar(127) not null primary key,
    title   varchar(511) not null,
    brief   text         not null,
    author  varchar(511) not null,
    year    varchar(63)  not null,
    journal varchar(511),
    doi     varchar(255) not null unique,
    path    longtext,
    unique index_paper_title (title)
);

create table note
(
    id          varchar(127) not null primary key,
    title       varchar(511) not null default '',
    cont        text         not null,
    page        integer      not null default 0,
    repo_id     varchar(127) not null,
    paper_id    varchar(127) not null,
    user_id     varchar(127) not null,
    create_time datetime     not null,
    update_time datetime     not null,
    constraint fk_note_repo_id foreign key (repo_id) references repo (id) on delete cascade,
    constraint fk_note_paper_id foreign key (paper_id) references paper (id) on delete cascade,
    constraint fk_note_user_id foreign key (user_id) references user (id) on delete cascade
);

create table comment
(
    id          varchar(127) not null primary key,
    cont        varchar(255) not null,
    user_id     varchar(127) not null,
    paper_id    varchar(127) not null,
    create_time datetime     not null,
    update_time datetime     not null,
    constraint fk_comment_user_id foreign key (user_id) references user (id) on delete cascade,
    constraint fk_comment_repo_id foreign key (paper_id) references paper (id) on delete cascade
);

create table area
(
    id   varchar(127) not null primary key,
    name varchar(127) not null unique
);

create table rel_user_repo_focus
(
    user_id    varchar(127) not null,
    repo_id    varchar(127) not null,
    focus_time datetime     not null,
    constraint pk_rel_user_repo primary key (user_id, repo_id),
    constraint fk_rel_focus_user_id foreign key (user_id) references user (id) on delete cascade,
    constraint fk_rel_focus_repo_id foreign key (repo_id) references repo (id) on delete cascade
);

create table rel_user_area
(
    user_id varchar(127) not null,
    area_id varchar(127) not null,
    constraint pk_rel_user_area primary key (user_id, area_id),
    constraint fk_rel_user_area_user foreign key (user_id) references user (id) on delete cascade,
    constraint fk_rel_user_area_area foreign key (area_id) references area (id) on delete cascade
);

create table rel_paper_area
(
    paper_id varchar(127) not null,
    area_id  varchar(127) not null,
    constraint pk_rel_paper_area primary key (paper_id, area_id),
    constraint fk_rel_paper_area_paper foreign key (paper_id) references paper (id) on delete cascade,
    constraint fk_rel_paper_area_area foreign key (area_id) references area (id) on delete cascade
);

create table rel_repo_paper
(
    repo_id  varchar(127) not null,
    paper_id varchar(127) not null,
    constraint pk_rel_repo_paper primary key (repo_id, paper_id),
    constraint fk_rel_repo_paper_repo foreign key (repo_id) references repo (id) on delete cascade,
    constraint fk_rel_repo_paper_paper foreign key (paper_id) references paper (id) on delete cascade
);
