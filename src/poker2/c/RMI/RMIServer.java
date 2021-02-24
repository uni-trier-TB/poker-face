package poker2.c.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import poker2.c.Logger;
import poker2.c.RMI.interfaces.IServer;
import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;

@SuppressWarnings("serial")
public class RMIServer extends UnicastRemoteObject implements IServer
{
	private PokerModel model;
	private Logger log = new Logger("RMI");

	public RMIServer(PokerModel model) throws RemoteException
	{
		this.model = model;
	}

	@Override
	public boolean addPlayer(String name) throws RemoteException
	{
		this.log.write("addPlayer " + name);
		Callable<Boolean> c1 = () -> model.getState().addPlayer(name);
		final FutureTask<Boolean> query = new FutureTask<Boolean>(c1);
		Platform.runLater(query);
		try
		{
			return query.get();
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean startGame() throws RemoteException
	{
		this.log.write("startGame");
		Callable<Boolean> c1 = () -> model.getState().start();
		final FutureTask<Boolean> query = new FutureTask<Boolean>(c1);
		Platform.runLater(query);
		try
		{
			return query.get();
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean call(String name, String action) throws RemoteException
	{
		this.log.write("call " + name + " " + action);
		Callable<Boolean> c1 = () -> model.getState().bet(name, action, 0);
		final FutureTask<Boolean> query = new FutureTask<Boolean>(c1);
		Platform.runLater(query);
		try
		{
			return query.get();
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getCurrentPlayer() throws RemoteException
	{
		this.log.write("getCurrentPlayer");
		Callable<String> c1 = () -> model.getCurrentPlayer().getName();
		final FutureTask<String> query = new FutureTask<String>(c1);
		Platform.runLater(query);
		try
		{
			return query.get();
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return "Something went wrong by getting Player name. Player wron spelled?";
	}

	@Override
	public String getPlayerInformation(String playerName) throws RemoteException
	{
		this.log.write("getPlayerInformation");
		PokerPlayer p = this.model.getCurrentPlayer();
		if (p != null)
		{
			String name = p.getName();
			String money = p.getMoney() + "";
			String card1 = p.getCards()[0].toString();
			String card2 = p.getCards()[1].toString();
			return "Player name: " + name + " Money: " + money + " Card1: " + card1 + " Card2: " + card2;
		}
		return "game not started or simply no player with that name";
	}

	@Override
	public boolean removePlayer(String name) throws RemoteException
	{
		this.log.write("removePlayer " + name);
		Callable<Boolean> c1 = () -> model.getState().removePlayer(name);
		final FutureTask<Boolean> query = new FutureTask<Boolean>(c1);
		Platform.runLater(query);
		try
		{
			return query.get();
		} catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void update() throws RemoteException
	{
		this.log.write("update");
		Runnable r1 = () -> model.getView().update(model);
		Platform.runLater(r1);

	}

	@Override
	public String getState() throws RemoteException
	{
		this.log.write("state");
		return this.model.getState().toString();
	}

}
