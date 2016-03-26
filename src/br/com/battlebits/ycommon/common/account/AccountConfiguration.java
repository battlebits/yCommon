package br.com.battlebits.ycommon.common.account;

public class AccountConfiguration {

	private boolean ignoreAll;
	private boolean tellEnabled;

	@Override
	public String toString() {
		return super.toString();
	}

	public boolean isIgnoreAll() {
		return ignoreAll;
	}

	public void setIgnoreAll(boolean ignoreAll) {
		this.ignoreAll = ignoreAll;
	}

	public boolean isTellEnabled() {
		return tellEnabled;
	}

	public void setTellEnabled(boolean tellEnabled) {
		this.tellEnabled = tellEnabled;
	}
}
