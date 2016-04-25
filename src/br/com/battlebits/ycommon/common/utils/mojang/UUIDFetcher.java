package br.com.battlebits.ycommon.common.utils.mojang;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UUIDFetcher {

	public String mojangURL = "https://api.mojang.com/users/profiles/minecraft";
	public String craftApiURL = "https://craftapi.com/api/user/uuid";

	public UUID getUUIDFromString(String id) {
		return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" + id.substring(20, 32));
	}

	public boolean validate(String username) {
		if (username.length() < 3)
			return false;
		if (username.length() > 16)
			return false;
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

	public abstract UUID getUUID(String name);
}