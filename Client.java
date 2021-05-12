import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {

	public static void main(String[] args) {
				
		System.out.println("Client Status: Operational...");
		BufferedReader scan = new BufferedReader(new InputStreamReader(System.in));
		
		try (Socket sock = new Socket("127.0.0.1",6013)){
			PrintWriter cout = new PrintWriter(sock.getOutputStream(),true);
			BufferedReader cin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line=null;
			System.out.println("Connection to server Successful...");
			
			do {
				System.out.print("Enter Text: ");
				line = scan.readLine();
				cout.println(line);
				if("exit".equalsIgnoreCase(line)) {
					sock.close();
					System.out.println("====================================================");
					System.out.println("Exited from the server!");
					System.out.println("====================================================");
					System.exit(0);
				}
				System.out.println("Server Responds With: "+cin.readLine());
				
				
			} while (!"exit".equalsIgnoreCase(line));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

