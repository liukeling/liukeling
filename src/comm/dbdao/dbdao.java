package comm.dbdao;

import java.io.IOException;
import java.util.Properties;

public class dbdao {
	public static int yunduankou;
	public static int qqduankou;
	public static String fuwuip;
	public static String yuntext;
	public static void init() throws IOException{

		Properties prp = new Properties();
		prp.load(dbdao.class.getResourceAsStream("db.properties"));
		
		qqduankou = Integer.parseInt(prp.getProperty("qqduankou"));
		yunduankou = Integer.parseInt(prp.getProperty("yunduankou"));
		fuwuip = prp.getProperty("fuwuip");
		yuntext = prp.getProperty("yuntext");
	}
}
