package UI;

public class ClientSetting {
	static final String[] areaNameStr = { "Asia", "Omeida", "Japan", "Else" };
	static final String[] areaNameStr_ch = { "����", "ŷ��", "����", "����" };
	public static final String serveIP = "127.0.0.1";
	public static final int servePort = 9092;
	public static final int servePortData = 9091;
	public static final String SongCachePath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/cache/song";
	public static final String ImgCachePath = "C:/Users/WuJinPeng/Desktop/��ҵ/java/cache/img";
	static public enum request {
		GetList, GetSong, GetImage, GetMessage, Disconect
	};
	static public enum reply {
		Song, List
	};
}
