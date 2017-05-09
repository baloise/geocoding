package com.baloise.geo;

import static java.lang.String.format;

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
	transient GeoCodeXyz geoCodeXyz = new GeoCodeXyz();
	
	public static void main(String[] args) throws Exception {
		Kryo kryo = new Kryo();
		File save = new File("save.bin");
		GeoCoderCLI geo = load(kryo, save);

		geo.encode();
		
		if (!save.exists()) {
			try (Output output = new Output(new FileOutputStream(save));) {
				kryo.writeObject(output, geo);
			}
		}

	}

	private void encode() {
		gebaeude.values().stream().limit(3).forEach(this::encode);
	}
	
	private void encode(NewGeb geb) {
		geoCodeXyz.code(geb);
	}
	
	private static GeoCoderCLI load(Kryo kryo, File save) throws FileNotFoundException, IOException {
		GeoCoderCLI geo;
		if (save.exists()) {
			try (Input input = new Input(new FileInputStream(save))) {
				geo = kryo.readObject(input, GeoCoderCLI.class);
			}
		} else {
			geo = new GeoCoderCLI();
			geo.parseCSV();
		}
		return geo;
	}

	private void parseCSV() throws IOException {
		Files.lines(Paths.get("Post_Adressdaten20170425.csv"), Charset.forName("ISO-8859-1")).forEach(this::parseLine);
	}

	private void parseLine(String line) {
		String[] tokens = line.split(";");
		int REC_ART = Integer.valueOf(tokens[0]);
		switch (REC_ART) {
		case NewPlz1.REC_ART:
			plz.put(NewPlz1.load(tokens));
			break;
		case NewStr.REC_ART:
			strassen.put(NewStr.load(tokens,plz));
			break;
		case NewGeb.REC_ART:
			gebaeude.put(NewGeb.load(tokens, strassen));
			break;
		default:
			break;
		}
	}

}
