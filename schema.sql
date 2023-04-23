drop table if exists books;

create table books(
    bookId varchar(255),
    title text,
    description MEDIUMTEXT,
    authors MEDIUMTEXT,
    publishedDate  varchar(255),
    urlLink varchar(255),
    imageUrl  MEDIUMTEXT,
    previewLink MEDIUMTEXT,
    user_id varchar(255),


    primary key(bookId),
    constraint fk_user_id
	    foreign key(user_id) 
        references users(id)

);

drop table if exists users

create table users(
    id varchar(255),
    username varchar(255),
    email varchar(255),
    password varchar(255),

    primary key(id)
);
