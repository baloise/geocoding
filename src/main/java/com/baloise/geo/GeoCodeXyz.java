package com.baloise.geo;

import static java.lang.String.format;

import java.io.IOException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.baloise.geo.http.Throttle;
import com.baloise.geo.http.UnirestProxyHelper;
import com.baloise.geo.model.Format;
import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;
import com.baloise.geo.model.Representation;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GeoCodeXyz implements GeoCoder {

	static {
		UnirestProxyHelper.initProxy();
	}
	
	Throttle throttle = new Throttle(1005);

	@Override
	public Location locate(Gebaeude geb) throws IOException {
		return locate(format("%s %s%s, %s %s", geb.strasse.STRBEZ2L, geb.HNR, geb.HNRA, geb.strasse.plz.POSTLEITZAHL, geb.strasse.plz.ORTBEZ18));
	}

	public Location locate(String address) throws IOException {
		try {
			String url = format("https://geocode.xyz/%s?json=1&region=Europe", address);
			throttle.throttle();
			JSONObject json = Unirest.get(url).asJson().getBody().getObject();
			checkError(json);
			Location loc = new Location();
			loc.representation = new Representation(Format.GEOCODE_XYZ_V1, json.toString());
			loc.confidence = json.getJSONObject("standard").getDouble("confidence");
			loc.longitude = json.getDouble("longt");
			loc.latitude = json.getDouble("latt");
			if(loc.latitude == 0d) {
				throw new IOException("latitude still not set");
			}
			return loc;
		} catch (UnirestException e) {
			if(e.getCause() instanceof ConnectTimeoutException) {
				throw new IOException("Maybe you should set -Dhttp_proxy=http://user:pwd@host:port", e);
			}
			throw new IOException(e);
		}
	}

	private void checkError(JSONObject json) throws IOException {
		if(json.has("error")) {
			throw new IOException(json.getJSONObject("error").getString("description"));
		}
	}

	public static void main(String[] args) throws IOException {
		GeoCodeXyz xyz = new GeoCodeXyz();
		Location locate = xyz.locate("lksjdlfkjsd");
		System.out.println(locate.representation.body);
		System.out.println();
	}
}
