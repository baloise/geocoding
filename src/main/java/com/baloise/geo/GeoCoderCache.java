package com.baloise.geo;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;

public class GeoCoderCache implements GeoCoder {
	final GeoCoder delegate;
	final Path repo;
	
	public GeoCoderCache(GeoCoder delegate,Path repo) {
		this.delegate = delegate;
		this.repo = repo;
	}

	@Override
	public Location code(Gebaeude geb) throws IOException {
		return contains(geb) ? get(geb) : store(delegate.code(geb));
	}

	private Location store(Location code) {
		return code;
	}

	private Location get(Gebaeude geb) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean contains(Gebaeude geb) {
		Path path = repo.resolve(getPath(geb));
		System.out.println(path);
		return false;
	}

	Path getPath(Gebaeude geb) {
		Path path = Paths.get(format("%s/%s/%s", geb.HAUSKEY.longValue() % 100, geb.HAUSKEY.longValue() % 10000, geb.HAUSKEY));
		return path;
	}

}
