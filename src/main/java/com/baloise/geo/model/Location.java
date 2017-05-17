package com.baloise.geo.model;

import static java.lang.Long.compare;

import com.iciql.Iciql.IQColumn;
import com.iciql.Iciql.IQIndex;
import com.iciql.Iciql.IQIndexes;
import com.iciql.Iciql.IQTable;


@IQTable
@IQIndexes({
  @IQIndex({"lat", "lng"}),
  @IQIndex({"lat"}),
  @IQIndex({"lng"}),
  @IQIndex({"plz"}),
})
public class Location implements Comparable<Location>{
	
	@IQColumn(primaryKey = true)
	public long hauskey;
	@IQColumn
	public String strasse;
	@IQColumn
	public String nr;
	@IQColumn
	public String ort;
	@IQColumn
	public int plz;
	@IQColumn
	public double lat;
	@IQColumn
	public double lng;
	@IQColumn
	public double confidence;
	
	@Override
	public String toString() {
		return "Location [hauskey=" + hauskey + ", strasse=" + strasse + ", nr=" + nr + ", ort=" + ort + ", plz=" + plz + ", lat=" + lat + ", lng="
				+ lng + ", confidence=" + confidence + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (hauskey ^ (hauskey >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (hauskey != other.hauskey)
			return false;
		return true;
	}

	@Override
	public int compareTo(Location o) {
		return compare(hauskey, o.hauskey);
	}
	
	
	
	
}
