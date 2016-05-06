package br.com.battlebits.ycommon.common.time;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class TimeZoneConversor {

	public static long getCurrentMillsTimeIn(TimeZone timeZone) {
		return convertTime(BattlebitsAPI.getServerTimeZone(), timeZone, System.currentTimeMillis());
	}

	public static long convertTime(TimeZone fromZone, TimeZone toZone, long time) {
		return time + ((fromZone.getAjust() - toZone.getAjust()) * 1000 * 60 * 60);
	}

}
