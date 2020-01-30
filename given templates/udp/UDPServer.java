package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	private void run() {
        //set packet size
		int pacSize = 0;
		byte[] pacData;
		DatagramPacket pac;
		
		//setting the timeout value (in ms)
        int timeout = 30000;
		close = false;
		
		while(close == false){
            		//create a new packet of data
            		pacData = new byte[packSize];
            		//make a datagramPacket for this data
            		recvPac = new DatagramPacket(pacData, pacSize);
           		try{
              		  	/*enables/disables 'so_timeout' in ms, when recieve() is called the 
              		 	 recvSoc will only block for this amount of time, if the timeout expires 
              		 	 an exception is raised*/
              		  	recvSoc.setSoTimeout(timeout);
              		  	recvSoc.recieve(recvPac);
               			 //getData returns data buffer, doing new string with this constructor removes any excess and passes only the string required
                		String dataIn = new String(pac.getData(), pac.getOffset(), pac.getLength());
                		processMessage(dataIn);
           		}catch(Exception e) {
           		}
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

        try{
            msg = new MessageInfo(data);
        }
        catch(Exception e){
        }
        
		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages < 0){
            //from common/messageInfo
            totalMessages = msg.totalMessages;
            receivedMessages = new int[totalMessages];
            System.out.print("Recieving " + totalMessages + " messages\n");
        }
        
        //set the message index in the recieved messages array to 1 to indicate they have been recieved
        receivedMessages[msg.messageNum] = 1;
        System.out.print(msg.toString());

		//identifies any missing messages by seeing if their index = 0 and prints the index of missing ones
		if(totalMessages - msg.messageNum == 1){
            close = true;
            System.out.print("\nThe messages that were lost are: ");
            for(int i = 0; i < receivedMessages.length; i++){
                if(receivedMessages[i] == 0){
                    System.out.print(i + " ");
                }
            }

	}
	}


	public UDPServer(int rp) {
		
		//constructs a datagram socket and binds it to the specified port on the local host machince
        try{
            recvSoc = new DatagramSocket(rp);
        } catch(Exception e) {
        }
        
        
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer myServer = new UDPServer(recvPort);
		myServer.run();
	}

}
