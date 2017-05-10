package com.baloise.geo.model;

public abstract class Jvnfra implements Comparable<Jvnfra> {
	public abstract Long getID();
	
	@Override
	public boolean equals(Object obj) {
		return getID().equals(((Jvnfra)obj).getID());
	}
	
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
	@Override
	public int compareTo(Jvnfra o) {
		return getID().compareTo(o.getID());
	}
}

