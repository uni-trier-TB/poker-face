package poker2.c.local;

import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.application.Platform;
import poker2.c.Logger;
import poker2.model.PokerModel;
import poker2.model.state.PokerState;

public class LocalController extends Thread
{
	private PokerModel model;
	private Logger log = new Logger("Local Controller");

	public LocalController(PokerModel model)
	{
		this.model = model;

	}

	public void run()
	{
		@SuppressWarnings("unused")
		final PokerState state = model.getState();
		Runnable task = null;
//		task = () -> { state.addPlayer("Randy"); };
//		Platform.runLater(task);
//		task = () -> { state.addPlayer("Troi"); };
//		Platform.runLater(task);
//		task = () -> { state.addPlayer("Lula"); };
//		Platform.runLater(task);
////
////		task = () -> { model.getPlayers()[2].addMoney(-9980); };
////		Platform.runLater(task);
////
//		task = () -> { model.getState().start(); };
//		Platform.runLater(task);
////////////
//		task = () -> { model.getState().bet("Lula", "call", 0); };
//		Platform.runLater(task);
////
//		task = () -> { model.getState().bet("Randy", "call", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Troi", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Lula", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Randy", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Troi", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Lula", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Randy", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Troi", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Lula", "check", 0); };
//		Platform.runLater(task);
//
//		task = () -> { model.getState().bet("Randy", "check", 0); };
//		Platform.runLater(task);

		while (true)
		{
			@SuppressWarnings("resource")
			Scanner s = new Scanner(System.in);
			System.out.println();
			System.out.println("Nummer eingeben");
			System.out.println("1 : add new Player");
			System.out.println("2 : start game");
			System.out.println("3 : call");
			System.out.println("4 : Get Information");
			int num = 0;
			try
			{
				num = s.nextInt();
				log.write(num + "");
			} catch (InputMismatchException e)
			{
				e.printStackTrace();
				log.write("Num Eingabefehler");
				log.endLine();
				continue;
			}
			if (num == 1)
			{
				System.out.println("Name eingeben: ");
				String name = s.next();
				log.write(" " + name);
				task = () -> { model.getState().addPlayer(name); };
				Platform.runLater(task);
				// model.getState().addPlayer(name);
			}
			else if (num == 2)
			{
				task = () -> { model.getState().start(); };
				Platform.runLater(task);
			}
			else if (num == 3)
			{
				System.out.println("Name eingeben: ");
				String name = s.next();
				System.out.println("Bet eingeben: ");
				String bet = s.next();
				log.write(" " + name + " " + bet);
				task = () -> { model.getState().bet(name, bet, 0); };
				Platform.runLater(task);
			}
			else if (num == 4)
			{
				// String name = s.next();
			}
			else if (num == 5)
			{
				System.out.println("Update...");
				task = () -> { this.model.getView().update(model); };
				Platform.runLater(task);

			}
			log.endLine();
		}
	}
}
