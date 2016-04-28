package br.com.battlebits.ycommon.common.payment.constructors;

import java.util.UUID;

import br.com.battlebits.ycommon.common.payment.enums.RankType;

public class Expire {
	private UUID uuid;
	private long expire;
	private long duration;
	private RankType rankType;

	public Expire(UUID uuid, long duration, RankType rankType) {
		this.uuid = uuid;
		this.duration = duration;
		this.expire = System.currentTimeMillis() + duration;
		this.rankType = rankType;
	}

	public UUID getUuid() {
		return uuid;
	}

	public long getExpire() {
		return expire;
	}
	
	public long getDuration() {
		return duration;
	}

	public RankType getRankType() {
		return rankType;
	}

	public void addLong(long duration) {
		this.expire += duration;
		this.duration += duration;
	}
}
