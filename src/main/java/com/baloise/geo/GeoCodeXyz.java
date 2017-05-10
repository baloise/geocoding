package com.baloise.geo;

import static java.lang.String.format;

import java.io.IOException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GeoCodeXyz implements GeoCoder {

	static {
		UnirestProxyHelper.initProxy();
	}

	@Override
	public Location code(Gebaeude geb) throws IOException {
		try {
			String url = format("https://geocode.xyz/%s %s%s, %s %s?json=1&region=Europe", geb.strasse.STRBEZ2L, geb.HNR, geb.HNRA, geb.strasse.plz.POSTLEITZAHL, geb.strasse.plz.ORTBEZ18);
			JSONObject json = Unirest.get(url).asJson().getBody().getObject();
			Location loc = new Location();
			loc.representation =json.toString();
			loc.confidence = json.getDouble("longt");
			loc.latitude = json.getDouble("latt");
			return loc;
		} catch (UnirestException e) {
			if(e.getCause() instanceof ConnectTimeoutException) {
				throw new IOException("Maybe you should set -Dhttp_proxy=http://user:pwd@host:port", e);
			}
			throw new IOException(e);
		}
	}

	
}
