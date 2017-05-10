package com.baloise.geo.model;

public class Strasse extends Jvnfra{

	public final static int REC_ART = 4;
	/**
	 * 1
	 */
	public Long STRID;
	/**
	 * 2
	 */
	public int ONRP;
	/**
	 * 6
	 */
	public String STRBEZ2L;
	
	public Postleitzahl plz;
	
	@Override
	public Long getID() {
		return STRID;
	}
	
	public static Strasse load(String[] tokens, Jvnfras<Postleitzahl> plz) {
		Strasse ret = new Strasse();
		ret.STRID = Long.valueOf(tokens[1]);
		ret.ONRP = Integer.valueOf(tokens[2]);
		ret.STRBEZ2L = tokens[6];
		ret.plz = plz.get(ret.ONRP);
		return ret ;
	}

}
