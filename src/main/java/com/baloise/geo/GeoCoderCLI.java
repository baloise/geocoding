package com.baloise.geo;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.baloise.geo.model.Jvnfras;
import com.baloise.geo.model.NewGeb;
import com.baloise.geo.model.NewPlz1;
import com.baloise.geo.model.NewStr;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GeoCoderCLI {

	Jvnfras<Integer, NewPlz1> plz = new Jvnfras<>();
	Jvnfras<String, NewStr> strassen = new Jvnfras<>();
	Jvnfras<String, NewGeb> gebaeude = new Jvnfras<>();

	public static void main(String[] args) throws Exception {
		Kryo kryo = new Kryo();
		File save = new File("save.bin");
		GeoCoderCLI geo = load(kryo, save);

		System.out.println(geo.plz.size());
		System.out.println(geo.strassen.size());
		System.out.println(geo.gebaeude.size());

		if (!save.exists()) {
			try (Output output = new Output(new FileOutputStream(save));) {
				kryo.writeObject(output, geo);
			}
		}

	}

	private static GeoCoderCLI load(Kryo kryo, File save) throws FileNotFoundException, IOException {
		GeoCoderCLI geo;
		long start = currentTimeMillis();
		if (save.exists()) {
			try (Input input = new Input(new FileInputStream(save))) {
				geo = kryo.readObject(input, GeoCoderCLI.class);
			}
		} else {
			geo = new GeoCoderCLI();
			geo.parseCSV();
		}
		System.out.println(format("loaded in %s msec", currentTimeMillis() - start));
		return geo;
	}

	private void parseCSV() throws IOException {
		Files.lines(Paths.get("Post_Adressdaten20170425.csv"), Charset.forName("ISO-8859-1")).forEach((line) -> {
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

	}

}
