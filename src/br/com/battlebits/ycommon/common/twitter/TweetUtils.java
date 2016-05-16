package br.com.battlebits.ycommon.common.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TweetUtils {

	public static boolean tweet(TwitterAccount account, String tweet) {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey(account.getConsumerKey()).setOAuthConsumerSecret(account.getConsumerSecret()).setOAuthAccessToken(account.getAccessToken()).setOAuthAccessTokenSecret(account.getAccessSecret());
		Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		try {
			twitter.updateStatus(tweet);
			return true;
		} catch (TwitterException e) {
			e.printStackTrace();
			return false;
		}
	}

}
