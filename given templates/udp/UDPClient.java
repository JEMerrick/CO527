package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;
		String 		message;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		// Construct UDP client class and try to send messages
		UDPClient myClient = new UDPClient();
		
	}

	public UDPClient() {
		// TO-DO: Initialise the UDP socket for sending data
		try {
            sendSoc = new DatagramSocket();
        } catch(SocketException soEx) {
        } catch(SecurityException secEx) {
        } catch(Exception e) {
        }
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int tries = 0;

		//Send the messages to the server
		try {
            sendSoc.connect(serverAddr, recvPort);
            while(tries <= countTo){
                String payload = countTo + ";" + tries;
                this.send(payload, serverAddr, recvPort);
                tries++;
            }
        } catch(Exception e) {
        }
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int payloadSize = payload.length();
		byte[] pktData = payload.getBytes();
		//build datagram packet
		DatagramPacket pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
		
		//send it to the server
		try{
            sendSoc.send(pkt);
		} catch(Exception e) {
		}
		
	}

    public void runLoop(InetAddress serverAddr, int recvPort, int countTo){
        testLoop(serverAddr, recvPort, countTo);
    }
}
