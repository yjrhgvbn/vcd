package serve;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import serve.ServeThread;

public class mainserve {
	@SuppressWarnings({ "resource", "rawtypes" })
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket serverscok = new ServerSocket(9092);
		ExecutorService executor = Executors.newFixedThreadPool(5);
		while (true) {
			Socket socket = serverscok.accept();
			System.out.println("连接上客户端：");
			Future future = executor.submit(new ServeThread(socket));
			try {
				future.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
