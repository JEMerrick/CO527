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
		int pacSize = 0;
		byte[] pacData;
		DatagramPacket pac;
        int timeout = 30000;
		close = false;
		
		while(close == false){
			pacData = new byte[pacSize];
			pac = new DatagramPacket(pacData, pacSize);
			try {
				recvSoc.setSoTimeout(timeout);
				recvSoc.receive(pac);
				String dataIn = new String(pac.getData(), pac.getOffset(), pac.getLength());
				processMessage(dataIn);
			} catch(Exception e) {
				System.out.println("Exception while receiving data " + e.getMessage());
			}
		}
	}

	public void processMessage(String data) {
		MessageInfo msg = null;

        try {
            msg = new MessageInfo(data);
        } catch(Exception e) {
			System.out.println("Exception while making new message " + e.getMessage());
        }
        
		if(totalMessages < 0) {
            totalMessages = msg.totalMessages;
            receivedMessages = new int[totalMessages];
            System.out.print("Recieving " + totalMessages + " messages\n");
        }
        
        receivedMessages[msg.messageNum] = 1;
        System.out.print(msg.toString());

		if(totalMessages - msg.messageNum == 1) {
            close = true;
            System.out.print("\nThe messages that were lost are: ");
            for(int i = 0; i < receivedMessages.length; i++){
                if(receivedMessages[i] == 0){
                    System.out.print(i + " ");
                }
            }

		}
	}

	public UDPServer(int port) {
        try {
            recvSoc = new DatagramSocket(port);
        } catch(Exception e) {
			System.out.println("Exception while creating new socket " + e.getMessage());
        }
	}

	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		int recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer myServer = new UDPServer(recvPort);
		myServer.run();
	}
}
