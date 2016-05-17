package br.com.battlebits.ycommon.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class GeoIpUtils {

	public static HashMap<String, IpCityResponse> ipStorage = new HashMap<>();

	public static IpCityResponse getIpStatus(String ipAdress) throws IOException {
		if (ipStorage.containsKey(ipAdress)) {
			return ipStorage.get(ipAdress);
		} else {
			String url = "http://api.ipinfodb.com/v3/ip-city/?format=json&key=d7859a91e5346872d0378a2674821fbd60bc07ed63684c3286c083198f024138&ip=" + ipAdress;
			IpCityResponse ipCityResponse = BattlebitsAPI.getGson().fromJson(getUrlSource(url), IpCityResponse.class);
			if ("OK".equals(ipCityResponse.getStatusCode())) {
				System.out.println(ipCityResponse.getCountryCode() + ", " + ipCityResponse.getRegionName() + ", " + ipCityResponse.getCityName());
				ipStorage.put(ipAdress, ipCityResponse);
				return ipCityResponse;
			} else {
				System.out.println("API status message is '" + ipCityResponse.getStatusMessage() + "'");
			}
		}
		return null;
	}

	private static String getUrlSource(String url) throws IOException {
		URL url2 = new URL(url);
		URLConnection yc = url2.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF-8"));
		StringBuilder a = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			a.append(inputLine);
		}
		in.close();
		return a.toString();
	}

	/**
	 * <pre>
	 * Example request:
	 * http://api.ipinfodb.com/v3/ip-city/?format=json&key=API_KEY&ip=IP_ADDRESS
	 *
	 * Example response:
	 * {
	 * 	"statusCode" : "OK",
	 * 	"statusMessage" : "",
	 * 	"ipAddress" : "74.125.45.100",
	 * 	"countryCode" : "US",
	 * 	"countryName" : "UNITED STATES",
	 * 	"regionName" : "CALIFORNIA",
	 * 	"cityName" : "MOUNTAIN VIEW",
	 * 	"zipCode" : "94043",
	 * 	"latitude" : "37.3861",
	 * 	"longitude" : "-122.084",
	 * 	"timeZone" : "-07:00"
	 * }
	 * </pre>
	 */
	public static class IpCityResponse {

		private String statusCode;
		private String statusMessage;
		private String ipAddress;
		private String countryCode;
		private String countryName;
		private String regionName;
		private String cityName;
		private String zipCode;
		private String latitude;
		private String longitude;
		private String timeZone;

		public String getStatusCode() {
			return statusCode;
		}

		public void setStatusCode(String statusCode) {
			this.statusCode = statusCode;
		}

		public String getStatusMessage() {
			return statusMessage;
		}

		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}

		public String getIpAddress() {
			return ipAddress;
		}

		public void setIpAddress(String ipAddress) {
			this.ipAddress = ipAddress;
		}

		public String getCountryCode() {
			return countryCode;
		}

		public void setCountryCode(String countryCode) {
			this.countryCode = countryCode;
		}

		public String getCountryName() {
			return countryName;
		}

		public void setCountryName(String countryName) {
			this.countryName = countryName;
		}

		public String getRegionName() {
			return regionName;
		}

		public void setRegionName(String regionName) {
			this.regionName = regionName;
		}

		public String getCityName() {
			return cityName;
		}

		public void setCityName(String cityName) {
			this.cityName = cityName;
		}

		public String getZipCode() {
			return zipCode;
		}

		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getTimeZone() {
			return timeZone;
		}

		public void setTimeZone(String timeZone) {
			this.timeZone = timeZone;
		}

		@Override
		public String toString() {
			return "IpCityResponse{" + "statusCode='" + statusCode + '\'' + ", statusMessage='" + statusMessage + '\'' + ", ipAddress='" + ipAddress + '\'' + ", countryCode='" + countryCode + '\'' + ", countryName='" + countryName + '\'' + ", regionName='" + regionName + '\'' + ", cityName='" + cityName + '\'' + ", zipCode='" + zipCode + '\'' + ", latitude='" + latitude + '\'' + ", longitude='" + longitude + '\'' + ", timeZone='" + timeZone + '\'' + '}';
		}
	}
}
