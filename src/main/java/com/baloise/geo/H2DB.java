package com.baloise.geo;

import java.io.File;
import java.util.function.Function;

import com.baloise.geo.model.Location;
import com.iciql.Db;

public class H2DB {
	private File h2db;

	public H2DB() {
		h2db = new File("repo/h2db");
		h2db.getParentFile().mkdirs();
		System.out.println("using db @ "+h2db.getAbsolutePath());
	}
	
	public <T> T withDB(Function<Db, T> dbAction) {
		try (Db db = Db.open("jdbc:h2:" + h2db.getAbsolutePath(), "sa", "")) {
			return dbAction.apply(db);
		}
	}

	Void test(Db db) {
		int offset = 2;
		Location p = new Location();
		p.hauskey = ++offset;
		p.strasse = "lange gasse";

		db.insert(p);

		p.hauskey = ++offset;
		db.insert(p);

		System.out.println("------------");

		db.from(new Location()).select().stream().forEach(System.out::println);
		return null;
	}

	public int deleteAll() {
		return withDB(db -> db.from(new Location()).delete());
	}
	
	public static void main(String[] args) {
		H2DB hdb = new H2DB();
		System.out.println(hdb.withDB(db -> db.from(new Location()).selectCount()));
	}

}
