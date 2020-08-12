package UI;

import java.io.*;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFormat.Encoding;

import UI.ClientSetting;

public class Music extends Thread {
	BufferedOutputStream buout;
	BufferedInputStream buin;

	boolean isExit = false;
	boolean isStop;
	boolean isPlay;
	boolean isDone;// 缓存完成标志
	boolean isToNew;// 缓存完成标志
	int reciveSize;// 没缓存完成前表示已缓存的大小
	String nowPlayName;
	String songPath;

	private final Object lock = new Object();
	private final Object downlock = new Object();
	

	public Music(BufferedOutputStream buout, BufferedInputStream buin) {
		this.buout = buout;
		this.buin = buin;
		isStop = true;// 播放是否停止
		isPlay = false; // 控制暂停和播放
		isDone = false; // 判断缓存是否完整完成
		isToNew = false;
		reciveSize = 0;// 已接受的文件大小
		nowPlayName = "";
	}

	public void SetPause() {
		this.isPlay = false;
	}

	public void SetStop() {
		this.isStop = true;
	}

	public void SetPlay() {
		this.isPlay = true;
	}

	@Override
	public void run() {
		while (!isExit) {
			synchronized (lock) {
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				DownloadAndPlay();
				// 没下载完成删除文件
				if (!isDone) {
					File f = new File(songPath);
					f.delete();
				}
			}
		}
	}

	public void Play(String songName) {
		// 是否正在播放或者播放对象
		if (nowPlayName.equals(songName) && !isStop)
			return;
		// 播放新歌曲
		if (isStop == false)// 未播放完成
		{
			isToNew = true;
			isPlay = true;
		} else if (isStop == true)// 播放完成
		{
			isToNew = false;
			isStop = false;
			isPlay = true;
		}
		reciveSize = 0;
		nowPlayName = songName;
		songPath = ClientSetting.SongCachePath + "/" +  PathStrCheck(songName) + ".flac";
		synchronized (lock) {
			lock.notify();
		}
	}

	// 判断缓存和播放
	private void DownloadAndPlay() {
		try {
			File songFile = new File(songPath);
			// 有歌缓存直接播放本地
			isDone = true;
			if (!songFile.exists()) {
				// 歌曲请求格式 GetSong：{歌名}
				String res = ClientSetting.request.GetSong.toString() + ":" + nowPlayName;
				buout.write(res.getBytes("UTF-8"));
				buout.flush();
				//Copy(songPath);
				isDone = false;
				// 文件接收线程，保存歌曲到本地
				new Thread(new Runnable() {
					@Override
					public void run() {
						download(songPath);
					}
				}).start();
			}
			playWithCache();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void playWithCache() {
		try {
			// 解码播放
			isStop = false;
			while(!isDone);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File (songPath));
			AudioFormat audioFormat = audioInputStream.getFormat();
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
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
			byte[] audioBytes = new byte[numBytes];
			int nowPlayNum = 0;
			while (true) {
				if (isPlay) {
					if (!sourceDataLine.isActive())
						sourceDataLine.start();
				}
				if (!isPlay) {
					if (sourceDataLine.isRunning())
						sourceDataLine.stop();
					continue;
				} else if (isToNew || isStop) {
					break;
				} else if (!isDone && nowPlayNum >= reciveSize - 100) {
					continue;
				}
				//if(audioInputStream.available() == 0)
				//	break;
				if(audioInputStream.read(audioBytes)==-1)
					break;
				//audioInputStream.read(audioBytes)
					sourceDataLine.write(audioBytes, 0, audioBytes.length);
				nowPlayNum++;
			}
			isStop = true;
			isToNew = false;
			System.out.println("endPlay");
			audioInputStream.close();
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void download(String songPath) {
		try {
			synchronized (downlock) {
				isDone = false;
				int num = 40960 + 1;
				byte[] Bytes = new byte[num];
				FileOutputStream o = new FileOutputStream(songPath);
				reciveSize = 0;
				while (buin.read(Bytes) != -1) {
					if (Bytes[0] == (byte) 1) {
						isDone = true;
						break;
					} else if (Bytes[0] == (byte) 2) {
						isDone = false;
						break;
					}
					o.write(Bytes, 1, Bytes.length - 1);
					o.flush();
					reciveSize++;
				}
				o.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Copy(String songPath) {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(ClientSetting.SongCachePath + "/default.flac");
			output = new FileOutputStream(songPath);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, bytesRead);
			}
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 不缓存播放
	public void playWithNoCache(String songName) {
		try {
			String res = ClientSetting.request.GetSong.toString() + ":" + songName;
			buout.write(res.getBytes("UTF-8"));
			buout.flush();
			byte[] formatBytes = new byte[524];
			int n = buin.read(formatBytes);
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
			// int i = 0;
			byte[] audioBytes = new byte[Integer.parseInt(formatStr[5])];
			while (buin.read(audioBytes) != -1) {
				if (audioBytes[0] == (byte) 1)
					break;
				sourceDataLine.write(audioBytes, 1, audioBytes.length - 1);
				// PS 为什么这里注释了？因为我懒得写功能了
				// 每次播放器快读进100单位audioBytes时再请求服务器发送100单位的audioBytes
				// 暂停或停止时不发送请求，防止网络堵塞
				// i++;
				// if ((i + 10) % 100 == 0) //提前请求
				// buout.write(String.valueOf(i + 100).getBytes());
			}
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();

		} catch (Exception e) {
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
	public void close()
	{
		isExit = true;
		isStop = true;
	}

}
