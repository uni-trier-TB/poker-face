package poker2.model.state;

import java.util.ArrayList;

import poker2.model.PokerModel;
import poker2.model.holdemevaluator.Card;
import poker2.model.holdemevaluator.evaluator.HandOutcome;
import poker2.model.holdemevaluator.evaluator.HoldemEvaluator;
import poker2.model.player.PokerPlayer;

public class Winner extends PokerState
{

	public Winner(PokerModel model)
	{
		super(model);
	}

	public void drawThisState()
	{
		this.model.getView().flipUserCards(this.model);

		Card[] community = model.getCommunityCards();

		HoldemEvaluator e = new HoldemEvaluator();

		int num = 0;
		ArrayList<PokerPlayer> winners = new ArrayList<PokerPlayer>();

		for (PokerPlayer p : model.getPlayers())
		{
			if (p.isActive())
			{
				if (winners.isEmpty())
				{
					winners.add(p);
					continue;
				}
				HandOutcome ho1 = e.calculate(winners.get(0).getCards(), community);
				HandOutcome ho2 = e.calculate(p.getCards(), community);
				num = ho1.compareTo(ho2);

				if (num < 0)
				{
					winners.clear();
					winners.add(p);
				}
				else if (num == 0)
				{
					winners.add(p);
				}

			}
		}

		String winnerNames = "";
		int money = this.model.getPotMoney() / winners.size();
		if (winners.size() == 1)
		{
			PokerPlayer w = winners.get(0);
			winnerNames = "Player " + w.getName() + " wins!";
			w.addMoney(money);

		}
		else if (winners.size() > 1)
		{
			winnerNames = "Players <";
			for (PokerPlayer p : winners)
			{
				winnerNames += p.getName() + " ";
				p.addMoney(money);
			}
			winnerNames += "> win!";
		}

		this.model.resetPot();
		this.model.getView().error(winnerNames, model, 200);
		this.model.getMusic().playWinSound();

	}

	public boolean start()
	{
		this.model.getView().delete();
		checkForLosers();
		model.raiseBlinds();
		model.getView().drawPot(model);
		model.getView().drawPlayer(model);
		model.getView().drawBlinds(model);

		Start s = new Start(model);
		this.model.setState(s);
		this.model.getState().start();
		return true;
	}

	private void checkForLosers()
	{
		for (PokerPlayer p : this.model.getPlayers())
		{
			if (!p.isEmpty() && p.getMoney() <= 0)
			{
				p.setEmpty();
			}
		}
	}

	public String toString()
	{
		return "Winner-State";
	}

}
