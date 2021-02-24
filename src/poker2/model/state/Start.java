package poker2.model.state;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;

public class Start extends PokerState
{

	public Start(PokerModel model)
	{
		super(model);
	}

	public boolean start()
	{
		int numOfPlay = 0;
		PokerPlayer[] players = model.getPlayers();

		for (PokerPlayer p : players)
			if (!p.isEmpty())
				numOfPlay++;

		if (numOfPlay < 2)
		{
			model.getView().error("Need at least 2 players", model);
			return false;
		}
		this.model.newCards();
		model.shuffleDeck();
		for (PokerPlayer p : players)
			if (!p.isEmpty())
			{
				p.setActive(true);
			}

		model.updateBlindsPlayer();
		model.dealCards();
		super.setNextState(new PreFlop(super.model));
		model.getView().drawBetLabel(model);

		return true;

	}

	public void drawThisState()
	{}

	public boolean addPlayer(String name)
	{
		boolean fullTable = true;
		PokerPlayer[] players = this.model.getPlayers();
		name = name.trim();
		for (PokerPlayer p : players)
		{
			if (p.getName().toLowerCase().equals(name.toLowerCase()))
			{
				String message = "Player " + name + " already on table";
				model.getView().error(message, model);
				return false;
			}

		}

		for (int i = 0; i < players.length; i++)
		{
			if (players[i].isEmpty())
			{
				fullTable = false;
				players[i].setHumanPlayer(name, this.model.getMusic());
				break;
			}
		}

		if (fullTable)
		{
			super.error("Table is full");
			return false;
		}

		model.getView().drawPlayer(model);
		return true;
	}

	public boolean removePlayer(String name)
	{
		PokerPlayer[] players = this.model.getPlayers();

		for (PokerPlayer p : players)
			if (p.getName().equals(name))
			{
				p.setEmpty();
				return true;
			}
		super.error("No player with name " + name);
		model.getView().drawPlayer(model);
		return false;
	}

	public String toString()
	{
		return "Start-State";
	}

}
