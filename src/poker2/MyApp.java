package poker2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.stage.Stage;
import poker2.c.RMI.RMIServer;
import poker2.c.RMI.interfaces.IServer;
import poker2.c.json.JSONReader;
import poker2.c.local.LocalController;
import poker2.model.PokerModel;

public class MyApp extends Application
{

	@SuppressWarnings("unused")
	private PokerModel model;

	@Override
	public void start(Stage stage) throws Exception
	{
		this.model = new PokerModel(stage, 10000, 10);

		Parameters p = this.getParameters();

		int size = p.getRaw().size();
		if (size == 0)
		{
			LocalController c = new LocalController(this.model);
			c.start();
		}
		else if (size > 1)
		{
			throw new Exception("not more than one argument");
		}
		else if (size == 1)
		{
			String arg = p.getRaw().get(0);
			arg = arg.toLowerCase();
			if (arg.equals("json"))
			{
				new JSONReader(this.model);
			}
			else if (arg.equals("rmi"))
			{
				new Thread()
				{
					public void run()
					{
						RMIServer rmi;
						try
						{
							System.out.println("RMI wird gestartet");
							LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

							rmi = new RMIServer(model);
							Naming.bind(IServer.serverName, rmi);
							// new RMIClient(model);
							System.out.println("RMI läuft");
						} catch (RemoteException | MalformedURLException | AlreadyBoundException e)
						{
							System.err.println("Couldn't start RMI Server");
							e.printStackTrace();
						}
					}
				}.start();

			}
		}
	}

	@Override
	public void stop()
	{
		System.out.println("Stage is closing");
		// Save file
	}

}
