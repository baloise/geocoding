package com.baloise.geo.model;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

public class Jvnfras<J extends Jvnfra> {
	
	SortedMap<Long, J> jvnfras = new TreeMap<>();
	
	public J get(long id) {
		return jvnfras.get(id);
	}
	
	public void put(J jvnfra) {
		jvnfras.put(jvnfra.getID(), jvnfra);
	}

	public int size() {
		return jvnfras.size();
	}

	public Collection<J> values() {
		return jvnfras.values();
	}
	
}

