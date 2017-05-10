package com.baloise.geo;

import static java.lang.String.format;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoCoderCache implements GeoCoder {
	final GeoCoder delegate;
	final Path repo;
	private ObjectMapper jacksonObjectMapper = new ObjectMapper();
	
	public GeoCoderCache(GeoCoder delegate, Path repo) {
		this.delegate = delegate;
		this.repo = repo;
		repo.toFile().mkdirs();
	}

	@Override
	public Location locate(Gebaeude geb) throws IOException {
		if(inError(geb)) throw new IOException(geb.HAUSKEY + "is in error");
		try {
			return contains(geb) ? get(geb) : store(geb, delegate.locate(geb));
		} catch (IOException e) {
			error(geb, e.getMessage());
			throw e;
		}
	}

	private boolean inError(Gebaeude geb) {
		return resolveError(geb).toFile().exists();
	}

	private void error(Gebaeude geb, String message) {
		try {
			Files.write(resolveError(geb), message.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Location store(Gebaeude geb, Location loc) throws IOException {
		File file = resolve(geb).toFile();
		file.getParentFile().mkdirs();
		jacksonObjectMapper.writeValue(file, loc);
		return loc;
	}

	private Location get(Gebaeude geb) throws IOException {
		return jacksonObjectMapper.readValue(resolve(geb).toFile(), Location.class);
	}

	private boolean contains(Gebaeude geb) {
		return resolve(geb).toFile().exists();
	}

	private Path resolveError(Gebaeude geb) {
		return resolve(geb, ".error.txt");
	}
	
	private Path resolve(Gebaeude geb) {
		return resolve(geb, ".json");
	}
	
	private Path resolve(Gebaeude geb, String postFix) {
		return repo.resolve(Paths.get(format("%s/%s/%s%s", geb.HAUSKEY.longValue() % 100, geb.HAUSKEY.longValue() % 10000, geb.HAUSKEY, postFix)));
	}

}
