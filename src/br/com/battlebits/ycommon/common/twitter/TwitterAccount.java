package br.com.battlebits.ycommon.common.twitter;

public enum TwitterAccount {
	BATTLEBITSMC("l9cKixd3YBCcOaa0r7Adf639K", "N0T6Hm2fOU6Pg5jRDK7Oun2JpuXj2G06mZQGqe9AfTZ8iEzFkK", "2972745022-pVbEjEYXxTMSuAIZSJp66x9jmUYbYuQsohR6BcU", "ZbVx5JewMYay2eNDvcTrM5KcuEZJizDZtFygmGyzkfbCY"), //
	BATTLEBANS("aknJl79d9M4wLxvFlkp42ghsV", "JHgI9frnQ48l2defKNEylmdHUbN5MRImLkxvxf8biK5BtlVmsk", "731984028744699906-DN2Gjcxdvm0tdGExKySBhJ3bZegHxWv", "oI9aKhyAT4aKCvzXry53EaIM9jv1IRQjSdOKh18lixeTC");

	private String consumerKey;
	private String consumerSecret;
	private String accessToken;
	private String accessSecret;

	private TwitterAccount(String consumerKey, String consumerSecret, String accessToken, String accessSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.accessSecret = accessSecret;
		this.accessToken = accessToken;
	}

	public String getAccessSecret() {
		return accessSecret;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}
}
