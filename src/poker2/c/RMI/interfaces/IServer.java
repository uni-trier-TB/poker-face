package poker2.c.RMI.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServer extends Remote
{
	String serverName = "RMIServer";

	boolean addPlayer(String name) throws RemoteException;

	boolean removePlayer(String name) throws RemoteException;

	void update() throws RemoteException;

	boolean startGame() throws RemoteException;

	boolean call(String name, String action) throws RemoteException;

	String getCurrentPlayer() throws RemoteException;

	String getPlayerInformation(String playerName) throws RemoteException;

	String getState() throws RemoteException;

	String getAvailableActions(String playerName) throws RemoteException;

	String getCards(String playerName) throws RemoteException;

	String getMoney(String playerName) throws RemoteException;
}
