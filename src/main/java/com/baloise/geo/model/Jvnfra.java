package com.baloise.geo.model;

public abstract class Jvnfra<T extends Comparable<T>> implements Comparable<Jvnfra<T>> {
	public abstract T getID();
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		return getID().equals(((Jvnfra<T>)obj).getID());
	}
	
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
	@Override
	public int compareTo(Jvnfra<T> o) {
		return getID().compareTo(o.getID());
	}
}

