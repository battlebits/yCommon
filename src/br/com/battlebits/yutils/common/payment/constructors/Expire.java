package br.com.battlebits.yutils.common.payment.constructors;

import java.util.UUID;

import br.com.battlebits.yutils.common.payment.enums.RankType;

public class Expire {
	private UUID uuid;
	private long expire;
	private RankType rankType;

	public Expire(UUID uuid, long expire, RankType rankType) {
		this.uuid = uuid;
		this.expire = expire;
		this.rankType = rankType;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getExpire() {
		return expire;
	}

	public RankType getRankType() {
		return rankType;
	}

	public void addLong(long l) {
		this.expire += l;
	}
}
