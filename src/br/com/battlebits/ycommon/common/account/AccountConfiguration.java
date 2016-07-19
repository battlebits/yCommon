package br.com.battlebits.ycommon.common.account;

public class AccountConfiguration {

	private boolean ignoreAll;
	private boolean tellEnabled;
	private boolean canPlaySound;
	private boolean showAlerts = true;
	private transient boolean staffChatEnabled = false;
	private transient boolean clanChatEnabled = false;

	public AccountConfiguration() {
		ignoreAll = false;
		tellEnabled = true;
		canPlaySound = true;
	}

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

	public boolean canPlaySound() {
		return canPlaySound;
	}

	public void setCanPlaySound(boolean canPlaySound) {
		this.canPlaySound = canPlaySound;
	}

	public boolean isStaffChatEnabled() {
		return staffChatEnabled;
	}

	public boolean isClanChatEnabled() {
		return clanChatEnabled;
	}

	public void setStaffChatEnabled(boolean staffChatEnabled) {
		this.staffChatEnabled = staffChatEnabled;
	}

	public void setClanChatEnabled(boolean clanChatEnabled) {
		this.clanChatEnabled = clanChatEnabled;
	}

	public boolean canShowAlerts() {
		return showAlerts;
	}

	public void setShowAlerts(boolean showAlerts) {
		this.showAlerts = showAlerts;
	}
}
