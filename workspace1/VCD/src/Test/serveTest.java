package Test;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class serveTest {
	
	public static void main(String[] args) throws Exception{
		//IP 10.102.11.194
		ServerSocket ss = new ServerSocket(9091);
		Socket s = ss.accept();
		System.out.println("new connection");
		ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
		out.writeObject("Asdsad");
		}
}
