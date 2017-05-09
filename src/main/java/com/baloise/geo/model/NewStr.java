package com.baloise.geo.model;

public class NewStr extends Jvnfra<String>{

	public final static int REC_ART = 4;
	/**
	 * 1
	 */
	public String STRID;
	/**
	 * 2
	 */
	public int ONRP;
	/**
	 * 6
	 */
	public String STRBEZ2L;
	
	@Override
	public String getID() {
		return STRID;
	}
	
	public static NewStr load(String[] tokens) {
		NewStr ret = new NewStr();
		ret.STRID = tokens[1];
		ret.ONRP = Integer.valueOf(tokens[2]);
		ret.STRBEZ2L = tokens[6];
		return ret ;
	}
}
