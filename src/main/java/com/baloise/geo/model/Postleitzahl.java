package com.baloise.geo.model;

public class Postleitzahl extends Jvnfra{

	public final static int REC_ART = 1;
	/**
	 * 1
	 */
	public Long ONRP;
	/**
	 * 4
	 */
	public int POSTLEITZAHL;
	/**
	 * 7
	 */
	public String ORTBEZ18;
	/**
	 * 8
	 */
	public String ORTBEZ27;
	
	public static Postleitzahl load(String[] tokens) {
		Postleitzahl ret = new Postleitzahl();
		ret.ONRP = Long.valueOf(tokens[1]);
		ret.POSTLEITZAHL = Integer.valueOf(tokens[4]);
		ret.ORTBEZ18 = tokens[7];
		ret.ORTBEZ27 = tokens[8];
		return ret ;
	}

	@Override
	public Long getID() {
		return ONRP;
	}
}
