CREATE TABLE tweets (
	message_id NUMERIC NOT NULL,
	author_id NUMERIC,
	message CHAR(200),
	informative BOOLEAN,
	category INT,
	is_reply BOOLEAN,
	reply_to_message_id NUMERIC,
	reply_to_author_id NUMERIC,
	is_retweet BOOLEAN,
	retweet_from_message_id NUMERIC,
	retweet_from_author_id NUMERIC
);