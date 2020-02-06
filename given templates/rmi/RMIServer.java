/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if(totalMessages == -1){
            totalMessages = msg.totalMessages;
            receivedMessages = new int[totalMessages];
        }

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		System.out.println(msg.messageNum);
		if(msg.messageNum == totalMessages -1){
			int lost = 0;
			for(int i = 0; i < receivedMessages.length; i++){
				if(receivedMessages[i] != 1){
					lost++;
				}
			}
			double percentage_loss = ((totalMessages - lost) / totalMessages) * 100;
			System.out.println("No. lost: " + lost);

		}
    }


	public static void main(String[] args) {
		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null){
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Instantiate the server class
		// TO-DO: Bind to RMI registry
        try {
            RMIServer myServer = new RMIServer();
            rebindServer("fred", myServer);
        } catch (Exception e){
					System.out.println("Exception when making new server/rebinding" + e.getMessage());
					System.exit(-1);
        }

	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		try{
            Registry registry = LocateRegistry.getRegistry();

            // TO-DO:
            // Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
            // Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
            // expects different things from the URL field.

            registry.rebind(serverURL, server);
        } catch (Exception e){
					e.printStackTrace();
					System.exit(-1);
        }
	}
}
