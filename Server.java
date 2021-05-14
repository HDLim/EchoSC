import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static int clientCounter = 0;
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket server = null;
		int count=0;
		
		System.out.println("-----------------------SERVER NOW ONLINE---------------------");
		
		try {
			
			//--------------------------------- Setting up the server
			server = new ServerSocket(6013);
			server.setReuseAddress(true);
			System.out.println("Waiting for Clients to connect...");
			
			//--------------------------------- Accepting clients
			while (true) {
				Socket client = server.accept();
				System.out.println("A Client has connected: "+ client.getInetAddress().getHostAddress());
				count++;
				clientCounter++;
				//----------------------------- Using threading for multiple clients
				Threader cSock = new Threader(client,count);
				
				new Thread(cSock).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private static class Threader implements Runnable {
		private final Socket cSock;
		private int cCount = 0;
	    
		public Threader(Socket sock,int x){
			this.cSock = sock;
			this.cCount = x;
		}
		
		@Override
		public void run() {
			
			BufferedReader sin = null;
			PrintWriter sout = null;
			
			try {	
				
				sin = new BufferedReader(new InputStreamReader(this.cSock.getInputStream()));
				sout = new PrintWriter(this.cSock.getOutputStream(),true);
				String line;
				
				while ((line=sin.readLine()) != null) {
					
					if(!"exit".equalsIgnoreCase(line)) {
						System.out.println("Sent from Client["+cCount+"]: "+line);
						sout.println(line);
							
					}else {
						System.out.println("====================================================");
						System.out.println("Client["+cCount+"] has disconnected from the server! ");
						System.out.println("====================================================");
						clientCounter--;
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (sout!=null) {
						sout.close();
					}
					if (sin!=null) {
						sin.close();
					}
					
					cSock.close();
					if (clientCounter==0){
						System.out.println("\nNo Client is connected to the server...Now exiting....");
						System.exit(0);
					}
					
										
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
	}

}
