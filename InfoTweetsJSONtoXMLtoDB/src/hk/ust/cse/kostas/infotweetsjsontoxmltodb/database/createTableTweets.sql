CREATE TABLE tweets (
       id NUMERIC NOT NULL PRIMARY KEY,
       author_id NUMERIC,           
       text CHAR(300),
       created_at TIMESTAMP,
       retweet_count INT, 	
       favorite_count INT,      
       informative BOOLEAN,
       category INT
);