package br.com.battlebits.ycommon.common.time;

public enum TimeZone {

	GMT_12(-12), GMT_11(-11), GMT_10(-10), GMT_09(-9), GMT_08(-8), GMT_07(-7), GMT_06(-6), GMT_05(-5), GMT_04(-4), GMT_03(-3), GMT_02(-2), GMT_01(
			-1), GMT0(0), GMT12(12), GMT11(11), GMT10(10), GMT09(9), GMT08(8), GMT07(7), GMT06(6), GMT05(5), GMT04(4), GMT03(3), GMT02(2), GMT01(1);

	private int ajust;

	private TimeZone(int ajust) {
		this.ajust = ajust;
	}

	public int getAjust() {
		return ajust;
	}

	public void setAjust(int ajust) {
		this.ajust = ajust;
	}

	public static TimeZone fromString(String s) {
		if (s.startsWith("-12")) {
			return GMT_12;
		} else if (s.startsWith("-11")) {
			return GMT_11;
		} else if (s.startsWith("-10")) {
			return GMT_10;
		} else if (s.startsWith("-09")) {
			return GMT_09;
		} else if (s.startsWith("-08")) {
			return GMT_08;
		} else if (s.startsWith("-07")) {
			return GMT_07;
		} else if (s.startsWith("-06")) {
			return GMT_06;
		} else if (s.startsWith("-05")) {
			return GMT_05;
		} else if (s.startsWith("-04")) {
			return GMT_04;
		} else if (s.startsWith("-03")) {
			return GMT_03;
		} else if (s.startsWith("-02")) {
			return GMT_02;
		} else if (s.startsWith("-01")) {
			return GMT_01;
		} else if (s.startsWith("01")) {
			return GMT01;
		} else if (s.startsWith("02")) {
			return GMT02;
		} else if (s.startsWith("03")) {
			return GMT03;
		} else if (s.startsWith("04")) {
			return GMT04;
		} else if (s.startsWith("05")) {
			return GMT05;
		} else if (s.startsWith("06")) {
			return GMT06;
		} else if (s.startsWith("07")) {
			return GMT07;
		} else if (s.startsWith("08")) {
			return GMT08;
		} else if (s.startsWith("09")) {
			return GMT09;
		} else if (s.startsWith("10")) {
			return GMT10;
		} else if (s.startsWith("11")) {
			return GMT11;
		} else if (s.startsWith("12")) {
			return GMT12;
		} else {
			return GMT0;
		}
	}

}