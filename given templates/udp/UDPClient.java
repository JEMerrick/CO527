package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.interfaces.ECKey;
import java.security.spec.ECFieldFp;

import common.MessageInfo;

public class UDPClient {
	private DatagramSocket sendSoc;

	public UDPClient() {
		try {
            sendSoc = new DatagramSocket();
        } catch(SocketException soEx) {
			System.out.println("Exception while creating socket " + soEx.getMessage());
        } catch(SecurityException secEx) {
			System.out.println("Exception while creating socket " + secEx.getMessage());
        } catch(Exception e) {
			System.out.println("Exception while creating socket " + e.getMessage());
        }
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {
		int tries = 0;

		try {
            sendSoc.connect(serverAddr, recvPort);
            while(tries <= countTo){
                String payload = countTo + ";" + tries;
                this.send(payload, serverAddr, recvPort);
                tries++;
            }
        } catch(Exception e) {
			System.out.println("Exception while connecting " + e.getMessage());
        }
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int payloadSize = payload.length();
		byte[] pktData = payload.getBytes();
		DatagramPacket pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
		
		try {
            sendSoc.send(pkt);
		} catch(Exception e) {
			System.out.println("Exception while sending " + e.getMessage());
		}
	}

    public void runLoop(InetAddress serverAddr, int recvPort, int countTo){
        testLoop(serverAddr, recvPort, countTo);
	}
	
	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int	recvPort;
		int countTo;
		String message;

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
		UDPClient myClient = new UDPClient();
		runLoop(serverAddr, recvPort, countTo);
	}
}
