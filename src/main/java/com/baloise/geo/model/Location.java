package com.baloise.geo.model;

public class Location {
	
	public double latitude, longitude, confidence;
	public Representation representation;
	
	public Location withRepresentation(Representation representation) {
		this.representation = representation;
		return this;
	}
}
