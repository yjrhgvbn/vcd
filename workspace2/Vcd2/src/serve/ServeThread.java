package serve;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import serve.SeekSql;

public class ServeThread implements Runnable {

	ServerSocket dataServerScoket;
	SeekSql sql;
	Socket socket;
	ObjectOutputStream obout;
	BufferedInputStream buin;
	BufferedOutputStream buout;
	Socket dataSocket;
	BufferedOutputStream dataBuout;

	boolean isSending;

	public ServeThread(Socket socket) {
		this.socket = socket;
		sql = new SeekSql();
		try {
			dataServerScoket = new ServerSocket(9091);
			dataSocket = dataServerScoket.accept();
			obout = new ObjectOutputStream(socket.getOutputStream());
			buin = new BufferedInputStream(socket.getInputStream());
			buout = new BufferedOutputStream(socket.getOutputStream());
			dataBuout = new BufferedOutputStream(dataSocket.getOutputStream());
			isSending = false;
			System.out.println("���������ݿͻ��ˣ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] inputData = new byte[524];
				int n = buin.read(inputData);
				//int n = 0;
				if(n<=0)
					continue;
				System.out.println("���յ��˿ͻ��˵�����");
				String inputMsg[] = new String(inputData, 0, n, "UTF-8").split(":");
				if (inputMsg[0].equals(Setting.request.GetList.toString())) {
					SendList(inputMsg[1]);
				} else if (inputMsg[0].equals(Setting.request.GetSong.toString())) {
					// ���͸裬�����ǰ��û���������ǰ�жϲ������¸�
					if (isSending)
						isSending = false;
					new Thread(new Runnable() {
						@Override
						public void run() {
							SendRowSong(inputMsg[1]);
						}
					}).start();
				} else if (inputMsg[0].equals(Setting.request.GetImage.toString())) {
					SendImage(inputMsg[1]);
				} else if (inputMsg[0].equals(Setting.request.GetMessage.toString())) {
					SendMessage(inputMsg[1]);
				} else if (inputMsg[0].equals(Setting.request.Disconect.toString())) {
					break;
				}
			}
			close();
		} catch (IOException e) {
			if (e.getMessage() == "Connection reset") {
				close();
			} else {
				e.printStackTrace();
			}
		}
	}

	private void close() {
		try {
			dataSocket.close();
			dataServerScoket.close();
			socket.close();
			obout.close();
			buin.close();
			buout.close();
			sql.close();
			System.out.println("һ���ͻ��������ѹر�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SendList(String arear) {
		try {
			for (Setting.Arears a : Setting.Arears.values()) {
				if (a.toString().equals(arear)) {
					obout.writeObject(sql.GetList(a));
					obout.flush();
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SendImage(String name) {
		try {
			File f = new File(sql.GetIamgePath(name));
			FileInputStream fis = new FileInputStream(f);
			buout.write(String.valueOf(f.length()).getBytes("UTF-8"));
			buout.flush();
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = fis.read(buf)) != -1)
			{
				buout.write(buf,0,len);
			}
			buout.flush();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void SendMessage(String name) {
		try {
			obout.writeObject(sql.GetMessage(name));
			obout.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private synchronized void SendRowSong(String name) {
		try {
			String path = sql.GetSongPath(name);
			int numBytes = 40960 + 1;
			byte[] audioBytes = new byte[numBytes];
			// ��һ���ֽ���Ϊ������־��1��ʾ�������,2��ʾ�����ж�
			audioBytes[0] = (byte) 0;
			isSending = true;
			FileInputStream fileStream = new FileInputStream(path);
			while (fileStream.read(audioBytes, 1, audioBytes.length - 1) != -1) {
				if (!isSending)
					break;
				dataBuout.write(audioBytes, 0, audioBytes.length);
			}
			// ������־
			if (!isSending)
				audioBytes[0] = (byte) 2;
			else
				audioBytes[0] = (byte) 1;
			dataBuout.write(audioBytes, 0, audioBytes.length);
			System.out.println("end");
			dataBuout.flush();
			fileStream.close();
			isSending = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void SendDecodeSong(String name) {
		try {
			String path = sql.GetSongPath(name);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));
			AudioFormat audioFormat = audioInputStream.getFormat();
			if (audioFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
				audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16,
						audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
				audioInputStream = AudioSystem.getAudioInputStream(audioFormat, audioInputStream);
			}
			int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
			int numBytes = 1024 * bytesPerFrame + 1;
			byte[] audioBytes = new byte[numBytes];
			String Format = String.valueOf(audioFormat.getSampleRate()) + ":"
					+ String.valueOf(audioFormat.getSampleSizeInBits()) + ":"
					+ String.valueOf(audioFormat.getChannels()) + ":" + String.valueOf(audioFormat.getFrameSize()) + ":"
					+ String.valueOf(audioFormat.getFrameRate()) + ":" + String.valueOf(numBytes);
			dataBuout.write(Format.getBytes("UTF-8"));
			dataBuout.flush();
			// int i = 0;
			// int n = 100;
			// byte[] nextque = new byte[524];
			audioBytes[0] = (byte) 0;
			while (audioInputStream.read(audioBytes, 1, audioBytes.length - 1) != -1) {
				dataBuout.write(audioBytes, 0, audioBytes.length);
				// i++;
				// ÿ�ν��յ�������ŷ���
				// if (i == n) {
				// int size = buin.read(nextque);
				// Integer.parseInt(new String(nextque, 0, size, "UTF-8"));
				// }
			}
			audioBytes[0] = (byte) 1;
			dataBuout.write(audioBytes, 0, audioBytes.length);
			System.out.println("end");
			dataBuout.flush();
		} catch (IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}

}
