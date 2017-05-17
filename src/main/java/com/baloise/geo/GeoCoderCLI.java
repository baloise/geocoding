package com.baloise.geo;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Jvnfras;
import com.baloise.geo.model.Location;
import com.baloise.geo.model.Postleitzahl;
import com.baloise.geo.model.Strasse;

public class GeoCoderCLI {

	Jvnfras<Postleitzahl> plz = new Jvnfras<>();
	Jvnfras<Strasse> strassen = new Jvnfras<>();
	Jvnfras<Gebaeude> gebaeude = new Jvnfras<>();

	public static void main(String[] args) throws Exception {
		GeoCoderCLI geo = new GeoCoderCLI();
		
		H2DB h2 = new H2DB();
		Mapper mapper = new Mapper();
		if(h2.withDB(db -> db.from(new Location()).selectCount()) <1) {
			//h2.withDB(db -> db.dropTable(Location.class));
			System.out.println("Initial load");
			geo.parseCSV();
			System.out.println("mapping");
			List<Location> locations = 
					geo.gebaeude.values().stream()
					.map(mapper::map)
					.collect(Collectors.toList());
			
			System.out.println("writing db");
			h2.withDB(db -> {db.insertAll(locations); return null;});
		}
		
		h2.withDB(db -> {
			Location l = new Location();
			db.from(l).where(l.lng).atMost(0D).and(l.lat).atMost(0D).limit(100).select().stream().forEach(loc -> {
				System.out.println(loc);
			});
			return null;
		});
		
	}


	private void parseCSV() throws IOException {
		Path path = Paths.get("Post_Adressdaten20170425.csv");
		System.out.println("loding from " +path);
		Files.lines(path, Charset.forName("ISO-8859-1")).forEach(this::parseLine);
	}

	private void parseLine(String line) {
		String[] tokens = line.split(";");
		int REC_ART = Integer.valueOf(tokens[0]);
		switch (REC_ART) {
		case Postleitzahl.REC_ART:
			plz.put(Postleitzahl.load(tokens));
			break;
		case Strasse.REC_ART:
			strassen.put(Strasse.load(tokens,plz));
			break;
		case Gebaeude.REC_ART:
			gebaeude.put(Gebaeude.load(tokens, strassen));
			break;
		default:
			break;
		}
	}

}
