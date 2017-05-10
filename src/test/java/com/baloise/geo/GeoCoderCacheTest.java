package com.baloise.geo;

import static org.junit.Assert.assertEquals;

import java.nio.file.Paths;

import org.junit.Test;

import com.baloise.geo.model.Gebaeude;

public class GeoCoderCacheTest {

	@Test
	public void getPath() throws Exception {
		GeoCoderCache cache = new GeoCoderCache(null, Paths.get("."));
		Gebaeude geb = new Gebaeude();
		geb.HAUSKEY = 123456789L;
		assertEquals("89\\6789\\123456789", cache.getPath(geb ).toString());
		
	}

}
