package com.baloise.geo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Jvnfras;
import com.baloise.geo.model.Postleitzahl;
import com.baloise.geo.model.Strasse;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class GeoCoderCLI {

	Jvnfras<Postleitzahl> plz = new Jvnfras<>();
	Jvnfras<Strasse> strassen = new Jvnfras<>();
	Jvnfras<Gebaeude> gebaeude = new Jvnfras<>();
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
		gebaeude.values().stream().limit(1).forEach(this::encode);
	}
	
	private void encode(Gebaeude geb) {
		try {
			System.out.println(geoCodeXyz.code(geb).representation);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
