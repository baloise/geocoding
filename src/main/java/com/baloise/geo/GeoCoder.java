package com.baloise.geo;

import java.io.IOException;

import com.baloise.geo.model.Gebaeude;
import com.baloise.geo.model.Location;

@FunctionalInterface
public interface GeoCoder {
	Location locate(Gebaeude geb) throws IOException;
}