CREATE TABLE users (
	id NUMERIC NOT NULL PRIMARY KEY,
	name CHAR(50),	
	screen_name CHAR(50),	
	description CHAR(250),
	verified BOOLEAN,
	created_at TIMESTAMP,
	followers_count INT,
	friends_count INT,
	statuses_count INT,
	listed_count INT,
	favourites_count INT,
	url CHAR(100),
	time_zone CHAR(50),
	location CHAR(50)
);