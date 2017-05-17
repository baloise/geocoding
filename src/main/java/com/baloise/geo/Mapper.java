package com.baloise.geo;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;

public class Mapper {

	public Location map(Gebaeude geb)  {
		Location ret = new Location();
		ret.hauskey = geb.HAUSKEY;
		ret.nr = geb.HNR + geb.HNRA;
		ret.strasse = geb.strasse.STRBEZ2L;
		ret.ort = geb.strasse.plz.ORTBEZ27;
		ret.plz = geb.strasse.plz.POSTLEITZAHL;
		return ret;
	}

}
