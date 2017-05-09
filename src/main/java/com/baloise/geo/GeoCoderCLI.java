package com.baloise.geo;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.baloise.geo.model.Jvnfras;
import com.baloise.geo.model.NewGeb;
import com.baloise.geo.model.NewPlz1;
import com.baloise.geo.model.NewStr;

public class GeoCoderCLI {

	public GeoCoderCLI() {
	}

	public static void main(String[] args) throws Exception {
		Jvnfras<Integer, NewPlz1> plz = new Jvnfras<>();
		Jvnfras<String, NewStr> strassen = new Jvnfras<>();
		Jvnfras<String, NewGeb> gebaeude = new Jvnfras<>();
		
		Files.lines(
				Paths.get("Post_Adressdaten20170425.csv"),
				Charset.forName("ISO-8859-1")
		).forEach((line) -> {
			String[] tokens = line.split(";");
			int REC_ART = Integer.valueOf(tokens[0]);
			switch (REC_ART) {
			case NewPlz1.REC_ART:
				plz.put(NewPlz1.load(tokens));
				break;
			case NewStr.REC_ART:
				strassen.put(NewStr.load(tokens));
				break;
			case NewGeb.REC_ART:
				gebaeude.put(NewGeb.load(tokens));
				break;
			default:
				break;
			}
			
		});
		
		System.out.println(plz.size());
		System.out.println(strassen.size());
		System.out.println(gebaeude.size());
		
	}
	
	

}
