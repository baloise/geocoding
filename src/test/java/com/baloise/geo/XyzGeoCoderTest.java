package com.baloise.geo;

import static com.baloise.geo.XyzGeoCoder.replaceSpecialChars;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XyzGeoCoderTest {

	
	@Test
	public void testReplaceSpecialChars() throws Exception {
		assertEquals("Casd aeE UE ", replaceSpecialChars("Çasd äÊ Ü "));
	}

}
