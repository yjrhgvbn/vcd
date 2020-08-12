package serve;

import serve.Setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.flac.FlacFileReader;
import org.jaudiotagger.audio.flac.metadatablock.MetadataBlockDataPicture;
import org.jaudiotagger.tag.*;
import org.jaudiotagger.tag.flac.FlacTag;

//数据库的建立和保存对应图片，不合window命名规则的符号全换'#'了
public class BulidSql {

	public static void main(String[] agrs) {
		build(Setting.AsiaPath, Setting.Arears.Asia);
		build(Setting.ElsePath, Setting.Arears.Else);
		build(Setting.JapanPath, Setting.Arears.Japan);
		build(Setting.OmeidaPath, Setting.Arears.Omeida);
	}

	static void build(String path, Setting.Arears arearType) {
		String sql = null;
		try {
			File files = new File(path); 
			File[] fs = files.listFiles(); 
			Class.forName(Setting.JDBC_DRIVER);
			@SuppressWarnings("unused")
			String Table = "CREATE TABLE `songslist` (" + "`name` varchar(255) NOT NULL,"
					+ "`album` varchar(255) DEFAULT NULL," + "`artist` varchar(255) DEFAULT NULL,"
					+ "`song` varchar(255) DEFAULT NULL," + "`arear` varchar(255) DEFAULT NULL,"
					+ "`image` varchar(255) DEFAULT NULL," + "PRIMARY KEY (`name`))";
			Connection conn = DriverManager.getConnection(Setting.DB_URL, Setting.USER, Setting.PASS);
			Statement stmt = null;
			stmt = conn.createStatement();
			// stmt.execute(Table);
			for (File file : fs) {
				if (!file.getName().endsWith(".flac"))
					continue;
				FlacFileReader reader = new FlacFileReader();
				FlacTag tag = (FlacTag) reader.read(file).getTag();
				tag.getVorbisCommentTag();
				String name = tag.getFirst(FieldKey.TITLE);
				sql = "SELECT COUNT(*) FROM songslist WHERE name = '" + sqlStrCheck(name) + "';";
				ResultSet rs = stmt.executeQuery(sql);	
				rs.next();
				if (rs.getInt(1) != 0)
					continue;
				String album = tag.getFirst(FieldKey.ALBUM);
				String artist = tag.getFirst(FieldKey.ARTIST);
				String arear = arearType.toString();
				String song = path + "/" + file.getName();
				String imagepath = Setting.ImagePath + "/" + ImgStrCheck(name) + ".jpg";
				sql = "INSERT INTO songslist VALUES ('" + sqlStrCheck(name) + "','" + sqlStrCheck(album) + "','"
						+ sqlStrCheck(artist) + "','" + sqlStrCheck(song) + "','" + sqlStrCheck(arear) + "','"
						+ sqlStrCheck(imagepath) + "');";
				stmt.execute(sql);
				rs.close();
				// 图片
				List<MetadataBlockDataPicture> m = tag.getImages();
				if (m.size() > 0) {
					byte[] b1 = m.get(0).getImageData();
					FileOutputStream fout = new FileOutputStream(imagepath);
					fout.write(b1);
					fout.close();
				} else {
					FileInputStream defimg = new FileInputStream(Setting.DefautImagePath);
					FileOutputStream fout = new FileOutputStream(imagepath);
					defimg.transferTo(fout);
					defimg.close();
					fout.close();
				}
			}
			stmt.close();
			conn.close();
		} catch (ClassNotFoundException | SQLException | CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			e.printStackTrace();
		}
	}

	static String sqlStrCheck(String sql) {
		if (sql.contains("'")) {
			String[] t = sql.split("'");
			sql = t[0];
			for (int i = 1; i < t.length; i++) {
				sql += sql + "\\'" + t[i];
			}
		}
		return sql;
	}

	static String ImgStrCheck(String name) {
		if (name.contains("\\")) {
			String t = "";
			for (int i = 0; i < name.length(); i++) {
				if (name.charAt(i) == '\\')
					t += "#";
				else
					t += name.charAt(i);
			}
			name = t;
		}
		String forbin[] = { "？", "、", "/", "*", "<", ">", "|" };
		for (String x : forbin) {
			if (name.contains(x))
				name = name.replaceAll(x, "#");
		}
		return name;
	}
}
