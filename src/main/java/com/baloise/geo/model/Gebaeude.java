package com.baloise.geo.model;

import java.util.Arrays;

public class Gebaeude extends Jvnfra<String> {

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
	public Strasse strasse;

	@Override
	public String getID() {
		return HAUSKEY;
	}
	
	public static Gebaeude load(String[] tokens, Jvnfras<String, Strasse> strassen) {
		try {
			Gebaeude ret = new Gebaeude();
			ret.HAUSKEY = tokens[1];
			ret.STRID = tokens[2];
			ret.HNR = integerOrNull(tokens[3]);
			ret.HNRA = tokens[4];
			ret.strasse = strassen.get(ret.STRID);
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
