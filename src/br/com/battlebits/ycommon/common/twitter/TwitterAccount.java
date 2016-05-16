package br.com.battlebits.ycommon.common.twitter;

public enum TwitterAccount {
	BATTLEBITSMC("Y2lYtwoymGslYCVakGK1M0nvv", "OcjNWd1wzN1AVEGjKAhd1e88otlfB5DJ60FNINSEuzmFIXzGlc", "2972745022-92JuPoZ7dkHzc0YMDJusC17gcRO8qjyjeTwa6CW", "iLGnslOn8Lh9sQY28l4uIsnLCceRRAYZzcPy03AzRJ3Ji"), //
	BATTLEBANS("5iYAeypiCXODosyvMrRompfZ8", "f2qPOGirbQMI2sKIPMHcteYJTCwKla4eT0Eu0skHwDvxLhjqxb", "731984028744699906-Vgg4SaYhQ1WJ1PnTiYDNW8OKKfGLHxQ", "vLGokQJdPtGou2IRFM5FuGGJh8AHDUklGJhirusn7U2qY");

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
