package com.baloise.geo.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class NewGebTest {
	@Test
	public void parsInt() throws Exception {
		assertEquals(1, Integer.valueOf("001").intValue());
	}
}
