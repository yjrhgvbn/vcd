package Test;

import java.io.*;
import java.net.Socket;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;

import UI.ClientSetting;

import UI.Music;

public class test {
	Socket socket;
	Socket dataSocket;
	ObjectInputStream obin;
	BufferedOutputStream buout;
	BufferedInputStream buin;
	BufferedInputStream dataBuin;

	Music music;

	public test() {
		try {
			socket = new Socket(ClientSetting.serveIP, ClientSetting.servePort);
			dataSocket = new Socket(ClientSetting.serveIP, ClientSetting.servePortData);
			obin = new ObjectInputStream(socket.getInputStream());
			buout = new BufferedOutputStream(socket.getOutputStream());
			buin = new BufferedInputStream(socket.getInputStream());
			dataBuin = new BufferedInputStream(dataSocket.getInputStream());
			music = new Music(buout, dataBuin);
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
			System.out.println(listData);
			return listData;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void play(String songName) {
		try {
			String res = ClientSetting.request.GetSong.toString() + ":" + songName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			byte[] formatBytes = new byte[524];
			int n = dataBuin.read(formatBytes);

			String formatStr[] = new String(formatBytes, 0, n, "UTF-8").split(":");
			AudioFormat audioFormat = new AudioFormat(Encoding.PCM_SIGNED, Float.parseFloat(formatStr[0]),
					Integer.parseInt(formatStr[1]), Integer.parseInt(formatStr[2]), Integer.parseInt(formatStr[3]),
					Float.parseFloat(formatStr[4]), false);
			// 打开输出设备
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			int i = 0;
			byte[] audioBytes = new byte[Integer.parseInt(formatStr[5])];
			while (dataBuin.read(audioBytes) != -1) {
				if (audioBytes[0] == (byte) 1)
					break;
				sourceDataLine.write(audioBytes, 1, audioBytes.length - 1);
				System.out.println(i++);
			}
			System.out.println("end");
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	public void play2(String songName) {
		try {
			String res = ClientSetting.request.GetSong.toString() + ":" + songName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						int num = 1025;
						byte[] Bytes = new byte[num];
						FileOutputStream o;
						o = new FileOutputStream("D:\\temp.flac");
						while (dataBuin.read(Bytes) != -1) {
							if (Bytes[0] == (byte) 1)
								break;
							o.write(Bytes, 1, Bytes.length - 1);
						}
						o.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();

			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:\\temp.flac"));
			AudioFormat audioFormat = audioInputStream.getFormat();
			// AudioFormat a = new AudioFormat(Encoding.PCM_SIGNED, (float) 44100.0, 16, 2,
			// 4, (float) 44100.0, false);
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				System.out.println(audioFormat.getEncoding());
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16,
						audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat,
					AudioSystem.NOT_SPECIFIED);
			SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
			int numBytes = 1024 * bytesPerFrame;
			System.out.print(numBytes);
			byte[] audioBytes = new byte[numBytes];
			int i = 0;
			while (audioInputStream.read(audioBytes) != -1) {
				System.out.println(i++);
				sourceDataLine.write(audioBytes, 0, audioBytes.length);
			}
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		test d = new test();
		//d.play2("光年之外");
		//d.music.start();
		//d.music.CheckAndDown("光年之外");
		d.music.start();
		Thread.sleep(100);
		d.music.Play("小半");
		Thread.sleep(5000);
		System.out.println("xiaobang");
		//d.music.SetStop();
		//Thread.sleep(1000);
		d.music.Play("光年之外");
		//System.out.print("new");
		//d.music.SetStop();
		//d.music.CheckAndDown("小半");
		//while (true) {
		//}
	}
}
