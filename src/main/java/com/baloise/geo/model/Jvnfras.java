package com.baloise.geo.model;

import java.util.SortedMap;
import java.util.TreeMap;

public class Jvnfras<T extends Comparable<T>, J extends Jvnfra<T>> {
	
	SortedMap<T, J> jvnfras = new TreeMap<>();
	
	public J get(T id) {
		return jvnfras.get(id);
	}
	
	public void put(J jvnfra) {
		jvnfras.put(jvnfra.getID(), jvnfra);
	}

	public int size() {
		return jvnfras.size();
	}
	
}

