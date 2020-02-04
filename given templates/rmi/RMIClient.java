package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.MalformedURLException;


import common.MessageInfo;

public class RMIClient {
	public static void main(String[] args) {
		RMIServerI iRMIServer = null;
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		//String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Bind to RMIServer
		try {
			Registry r = LocateRegistry.getRegistry(args[0]);
			iRMIServer = (RMIServerI)Naming.lookup("fred");

      for(int i = 0; i < numMessages; i++){
				iRMIServer.receiveMessage(new MessageInfo(numMessages, i));
		  }
		}
		catch (RemoteException | NotBoundException | MalformedURLException e) {
			System.out.println("Exception while receiving message " + e.getMessage());
			System.exit(-1);
		}

		// TO-DO: Attempt to send messages the specified number of times
	}
}
