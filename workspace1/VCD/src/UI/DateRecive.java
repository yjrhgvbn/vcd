package UI;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import UI.ClientSetting;

public class DateRecive {
	Socket socket;
	Socket dataSocket;
	ObjectInputStream obin;
	BufferedOutputStream buout;
	BufferedInputStream buin;
	BufferedInputStream dataBuin;

	Music music;

	public DateRecive() {
		try {
			socket = new Socket(ClientSetting.serveIP, ClientSetting.servePort);
			dataSocket = new Socket(ClientSetting.serveIP, ClientSetting.servePortData);
			obin = new ObjectInputStream(socket.getInputStream());
			buout = new BufferedOutputStream(socket.getOutputStream());
			buin = new BufferedInputStream(socket.getInputStream());
			dataBuin = new BufferedInputStream(dataSocket.getInputStream());
			music = new Music(buout, dataBuin);
			music.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> GetList(String arearName) {
		try {
			// 列表请求格式，GetList:{地区民} GetList:Asia
			String res = ClientSetting.request.GetList.toString() + ":" + arearName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			@SuppressWarnings("unchecked")
			ArrayList<String> listData = (ArrayList<String>) obin.readObject();
			return listData;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String GetAndSaveImage(String songName) {
		try {
			String path = ClientSetting.ImgCachePath + "/" + PathStrCheck(songName) + ".jpg";
			File imgFile = new File(path);
			if (imgFile.exists()) {
				return path;
			}
			String res = ClientSetting.request.GetImage.toString() + ":" + songName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			FileOutputStream fos = new FileOutputStream(imgFile);
			byte[] buf = new byte[1024];
			int len = 0;
			// 往字节流里写图片数据
			len = buin.read(buf);
			int filelength = Integer.parseInt(new String(buf, 0, len, "UTF-8"));
			while ((len = buin.read(buf)) != -1) {
				fos.write(buf, 0, len);
				if (filelength <= imgFile.length())
					break;
			}
			fos.flush();
			fos.close();
			return path;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<String> GetMessage(String songName) {
		try {
			// 列表请求格式，GetList:{地区民} GetList:Asia
			String res = ClientSetting.request.GetMessage.toString() + ":" + songName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			@SuppressWarnings("unchecked")
			ArrayList<String> listData = (ArrayList<String>) obin.readObject();
			return listData;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void playNew(String name) {
		music.Play(name);
	}

	public void stop() {
		music.SetStop();
	}

	public void pause() {
		music.SetPause();
	}

	public void play() {
		music.SetPlay();
	}

	public void close() {
		try {
			String res = ClientSetting.request.Disconect.toString();
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			music.close();
			socket.close();
			dataSocket.close();
			obin.close();
			buout.close();
			buin.close();
			dataBuin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private String PathStrCheck(String name) {
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
	public static void main(String[] args) throws InterruptedException {
		DateRecive d = new DateRecive();
		Thread.sleep(100);
		d.playNew("失眠飞行");
		Thread.sleep(10000);
		d.playNew("Go Time");
		Thread.sleep(10000);
		d.playNew("Hey KONG");
		Thread.sleep(1000);
		//d.GetAndSaveImage("失眠飞行");
		while (true) {
		}
	}

}
