package serve;


public class Setting {
	// �豣��Ŀ¼
	static public enum Arears {
		Asia, Omeida, Japan, Else
	};
	static final public String AsiaPath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/song/Asia";
	static final public String OmeidaPath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/song/Omeida";
	static final public String JapanPath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/song/Japan";
	static final public String ElsePath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/song/Else";
	static final public String ImagePath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/images";
	static final public String DefautImagePath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/images/default.jpg";
	// ���ݿ�����
	static final public String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	static final public String DB_URL = "jdbc:mysql://localhost:3306/mydata?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=True";
	// ���ݿ���û��������룬��Ҫ�����Լ�������
	static final public String USER = "root";
	static final public String PASS = "199945748abc";
	
	static public enum request {
		GetList, GetSong, Disconect, GetImage, GetMessage
	};
	static public enum reply {
		Song, List
	};
}
