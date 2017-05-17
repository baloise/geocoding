package com.baloise.geo;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Objects;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import com.baloise.geo.http.Throttle;
import com.baloise.geo.http.UnirestProxyHelper;
import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class XyzGeoCoder implements GeoCoder {

	Throttle throttle = new Throttle(1005);
	Mapper mapper = new Mapper();
	
	@Override
	public Location locate(Gebaeude geb) throws IOException {
		return locate(formatQuery(geb), geb);
	}

	String formatQuery(Gebaeude geb) {
		return replaceSpecialChars(format("%s %s%s, %s %s Switzerland",
						notNull(geb.strasse.STRBEZ2L), 
						notNull(geb.HNR), 
						notNull(geb.HNRA), 
						notNull(geb.strasse.plz.POSTLEITZAHL), 
						notNull(geb.strasse.plz.ORTBEZ18))
				);
	}

	 static final String replaceSpecialChars(String input) {
	     return input
	    		 
	    		 //replace all lower Umlauts
//	    		 .replaceAll("ü", "ue")
                 .replaceAll("ö", "oe")
                 .replaceAll("ä", "ae")
                 .replaceAll("ß", "ss")

                 //first replace all capital umlaute in a non-capitalized context (e.g. Übung)
	             .replaceAll("Ü(?=[a-zäöüß])", "Ue")
                 .replaceAll("Ö(?=[a-zäöüß])", "Oe")
                 .replaceAll("Ä(?=[a-zäöüß])", "Ae")

                 //now replace all the other capital umlaute
	     		 .replaceAll("Ü", "UE")
	             .replaceAll("Ö", "OE")
	             .replaceAll("Ä", "AE")
	     
	     		 //accents
	             .replaceAll("[ÁÂÀ]", "A")
	             .replaceAll("[áâàã]", "a")
	             .replaceAll("[ÉÊÈ]", "E")
	             .replaceAll("[éêè]", "e")
	             .replaceAll("[ÓÔÒ]", "O")
	             .replaceAll("[óôòõ]", "o")
	             .replaceAll("Ç", "C")
	             .replaceAll("ç", "c")
	             .replaceAll("Ñ", "N")
	             .replaceAll("ñ", "n")
	             
	             ;
	}

	String notNull(Object o) {
		return Objects.toString(o, "");
	}

	public Location locate(String address, Gebaeude geb) throws IOException {
		try {
			String url = format("https://geocode.xyz/%s?json=1&region=Europe", address);
			JSONObject json = getJSON(url);
			checkError(url, json);
			Location loc = mapper.map(geb);
			loc.confidence = json.getJSONObject("standard").getDouble("confidence");
			loc.lng  = json.getDouble("longt");
			loc.lat = json.getDouble("latt");
			if(loc.lat == 0d) {
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

	private JSONObject getJSON(String url) throws UnirestException {
		UnirestProxyHelper.initProxy();
		throttle.throttle();
		return Unirest.get(url).asJson().getBody().getObject();
	}

	void checkError(String url, JSONObject json) throws IOException {
		if(json.has("error")) {
			throw new IOException(format("%s\n%s" , url, json.getJSONObject("error").getString("description")));
		}
	}

	public static void main(String[] args) throws IOException {
		XyzGeoCoder xyz = new XyzGeoCoder();
		Location locate = xyz.locate("lksjdlfkjsd", new Gebaeude());
		System.out.println(locate);
		System.out.println();
	}
}
