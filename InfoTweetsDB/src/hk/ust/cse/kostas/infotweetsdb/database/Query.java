/*
 * Created on February 20, 2015 by kostas-κγ   
 *
 * This is part of the InfoTweetsDB project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package hk.ust.cse.kostas.infotweetsdb.database;

/**
 *
 *
 * @author kostas-κγ   
 */
public final class Query {
    final static String relation = "sortedTweets1K";    

    protected static final String joinReplyToPostID = 
	"SELECT t1.id AS t1id, t2.id AS t2id, t1.content AS t1content, t2.content AS t2content "
	+"FROM " + relation + " t1 "
	+"INNER JOIN " + relation + " t2 ON t1.id = t2.replyToPostId;";

    protected static final String selectSortedTweetsNoRetweets = 
	"SELECT id, content FROM " + relation + " where retweetedFromPostId = 0;";

}