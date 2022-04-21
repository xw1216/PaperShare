drop table if exists rel_paper_cited, rel_paper_area, rel_paper_author;
drop table if exists rel_user_area, rel_user_repo_focus;
drop table if exists rel_note_comment, rel_repo_paper;
drop table if exists comment, note, session, check_code;
drop table if exists author, area, paper;
drop table if exists repo, user;

create table user
(
    id          bigint       not null primary key,
    email       varchar(255) not null unique,
    name        varchar(255) not null,
    pass        varchar(511) not null,
    role        enum ('USER', 'ADMIN'),
    status      enum ('NORMAL', 'FREEZE', 'BAN'),
    orcid       varchar(31) unique,
    signup_time datetime     not null,
    signin_time datetime     not null,
    unique user_email_index (email)
);

create table check_code
(
    id          bigint       not null primary key,
    email       varchar(255) not null unique,
    code        varchar(31)  not null,
    expire_time datetime     not null,
    unique check_code_email_index (email)
);

create table session
(
    id          bigint       not null primary key,
    user_id     bigint       not null unique,
    secret      varchar(255) not null,
    expire_time datetime     not null,
    close       tinyint(1)   not null default 0,
    constraint fk_session_user_id foreign key (user_id) references user (id) on delete cascade
);

create table repo
(
    id          bigint       not null primary key,
    name        varchar(127) not null,
    visible     tinyint(1)   not null default 1,
    description varchar(511)          default '',
    create_time datetime     not null,
    update_time datetime     not null
);

create table paper
(
    id        bigint       not null primary key,
    title     varchar(511) not null,
    year      integer      not null,
    journal   varchar(511),
    publisher varchar(511),
    doi       varchar(255) not null unique,
    path      varchar(511),
    unique index_paper_doi (doi)
);

create table note
(
    id          bigint     not null primary key,
    content     varchar(1023)       default '',
    visible     tinyint(1) not null default 1,
    pos_x       integer    not null,
    pos_y       integer    not null,
    repo_id     bigint     not null,
    paper_id    bigint     not null,
    user_id     bigint     not null,
    create_time datetime   not null,
    update_time datetime   not null,
    constraint fk_note_repo_id foreign key (repo_id) references repo (id) on delete cascade,
    constraint fk_note_paper_id foreign key (paper_id) references paper (id) on delete cascade,
    constraint fk_note_user_id foreign key (user_id) references user (id) on delete cascade
);

create table comment
(
    id          bigint       not null primary key,
    cont        varchar(255) not null,
    user_id     bigint       not null,
    note_id     bigint       not null,
    create_time datetime     not null,
    update_time datetime     not null,
    constraint fk_comment_user_id foreign key (user_id) references user (id) on delete cascade,
    constraint fk_comment_note_id foreign key (note_id) references note (id) on delete cascade
);

create table area
(
    id        bigint       not null primary key,
    name      varchar(127) not null unique,
    popular   bigint       not null default 0,
    parent_id bigint,
    constraint fk_area_id foreign key (parent_id) references area (id)
);

create table author
(
    id      bigint       not null primary key,
    name    varchar(127) not null,
    popular bigint       not null default 0,
    email   varchar(255),
    orcid   varchar(31),
    wosid   varchar(31)
);

create table rel_note_comment
(
    note_id bigint not null,
    cmt_id  bigint not null,
    constraint pk_rel_note_comment primary key (note_id, cmt_id),
    constraint fk_rel_note_comment_note_id foreign key (note_id) references note (id) on delete cascade,
    constraint fk_rel_note_comment_cmt_id foreign key (cmt_id) references comment (id) on delete cascade
);

create table rel_user_repo_focus
(
    user_id    bigint                         not null,
    repo_id    bigint                         not null,
    visible    tinyint(1)                     not null default 1,
    focus_type enum ('watch', 'star', 'fork') not null default 'star',
    focus_time datetime                       not null,
    constraint pk_rel_user_repo primary key (user_id, repo_id),
    constraint fk_rel_focus_user_id foreign key (user_id) references user (id) on delete cascade,
    constraint fk_rel_focus_repo_id foreign key (repo_id) references repo (id) on delete cascade
);

create table rel_user_area
(
    user_id bigint not null,
    area_id bigint not null,
    constraint pk_rel_user_area primary key (user_id, area_id),
    constraint fk_rel_user_area_user foreign key (user_id) references user (id) on delete cascade,
    constraint fk_rel_user_area_area foreign key (area_id) references area (id) on delete cascade
);

create table rel_repo_paper
(
    repo_id  bigint not null,
    paper_id bigint not null,
    constraint pk_rel_repo_paper primary key (repo_id, paper_id),
    constraint fk_rel_repo_paper_repo foreign key (repo_id) references repo (id) on delete cascade,
    constraint fk_rel_repo_paper_paper foreign key (paper_id) references paper (id) on delete cascade
);

create table rel_paper_area
(
    paper_id bigint not null,
    area_id  bigint not null,
    constraint pk_rel_paper_area primary key (paper_id, area_id),
    constraint fk_rel_paper_area_paper foreign key (paper_id) references repo (id) on delete cascade,
    constraint fk_rel_paper_area_area foreign key (area_id) references area (id) on delete cascade
);

create table rel_paper_author
(
    paper_id  bigint not null,
    author_id bigint not null,
    constraint pk_rel_paper_author primary key (paper_id, author_id),
    constraint fk_rel_paper_author_paper foreign key (paper_id) references paper (id) on delete cascade,
    constraint fk_rel_paper_author_author foreign key (author_id) references author (id) on delete cascade
);

create table rel_paper_cited
(
    source_id bigint not null,
    cited_id  bigint not null,
    constraint pk_rel_paper_cited primary key (cited_id, source_id),
    constraint fk_rel_paper_cited_source foreign key (source_id) references paper (id) on delete cascade,
    constraint fk_rel_paper_cited_cited foreign key (cited_id) references paper (id) on delete cascade
);
