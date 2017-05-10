package com.baloise.geo;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;

import com.baloise.geo.model.Format;
import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;
import com.baloise.geo.model.Representation;

public class GeoCoderCacheTest {

	@Test
	public void getPath() throws Exception {
		GeoCoderCache cache = new GeoCoderCache(null, Paths.get("."));
		Gebaeude geb = new Gebaeude();
		geb.HAUSKEY = 123456789L;
		assertEquals("89\\6789\\123456789", cache.getPath(geb ).toString());
		
	}
	
	@Test
	public void code() throws Exception {
		GeoCoderCache cache = new GeoCoderCache(
				geb -> new Location().withRepresentation(new Representation(Format.GEOCODE_XYZ_V1, ""+geb.HAUSKEY)), 
				Paths.get("target/repo")
				);
		Gebaeude geb = new Gebaeude();
		geb.HAUSKEY = 123456789L;
		Location location = cache.locate(geb);
		assertEquals("123456789", location.representation.body);
	}

}
