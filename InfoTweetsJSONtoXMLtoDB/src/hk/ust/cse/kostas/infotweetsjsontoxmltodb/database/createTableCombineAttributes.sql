select tweets.id tweets.author_id, tweets.text, tweets.created_at, tweets.retweet_count, tweets.favorite_count,labels.informative,labels.category 
from tweets, labels
where tweets.author_id = labels.author_id;

select count(1)
from tweets, labels
where tweets.author_id = labels.author_id;

insert into tweetslabels (
select tweets.id, tweets.author_id, tweets.text, tweets.created_at, tweets.retweet_count, tweets.favorite_count,labels.informative,labels.category 
from tweets, labels
where tweets.id = labels.message_id);

create table temp;
insert into tweetslabels(
select tweets.id, tweets.author_id, tweets.text, tweets.created_at, tweets.retweet_count, tweets.favorite_count,labels.informative,labels.category 
from tweets, labels, retweets
where retweets.retweet_message_id = labels.message_id and tweets.id = retweets.original_message_id
);


