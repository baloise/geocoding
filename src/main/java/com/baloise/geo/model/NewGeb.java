package com.baloise.geo.model;

import java.util.Arrays;

public class NewGeb extends Jvnfra<String> {

	public final static int REC_ART = 6;
	/**
	 * 1
	 */
	public String HAUSKEY;
	/**
	 * 2
	 */
	public String  STRID;
	/**
	 * 3
	 */
	public Integer HNR;
	
	/**
	 * 4
	 */
	public String HNRA;

	@Override
	public String getID() {
		return HAUSKEY;
	}
	
	public static NewGeb load(String[] tokens) {
		try {
			NewGeb ret = new NewGeb();
			ret.HAUSKEY = tokens[1];
			ret.STRID = tokens[2];
			ret.HNR = integerOrNull(tokens[3]);
			ret.HNRA = tokens[4];
			return ret ;
		} catch (NumberFormatException e) {
			System.err.println("Could not load tokens "+ Arrays.asList(tokens));
			throw e;
		}
	}

	private static Integer integerOrNull(String string) {
		return (string == null || string.isEmpty()) ? null : Integer.valueOf(string);
	}
}
