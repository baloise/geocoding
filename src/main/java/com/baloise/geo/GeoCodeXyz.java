package com.baloise.geo;

import static java.lang.String.format;

import com.baloise.geo.model.NewGeb;

public class GeoCodeXyz {

	public void code(NewGeb geb) {
		System.out.println(format("%s %s%s, %s %s", geb.strasse.STRBEZ2L, geb.HNR, geb.HNRA,  geb.strasse.plz.POSTLEITZAHL,  geb.strasse.plz.ORTBEZ18));
	}

}
