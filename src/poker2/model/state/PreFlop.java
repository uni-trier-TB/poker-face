package poker2.model.state;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;
import poker2.model.state.community.Flop;

public class PreFlop extends PokerState
{
	private boolean NoRaise = true;

	public PreFlop(PokerModel model)
	{
		super(model);
		int smallBlind = model.getSmallBlind();
		PokerPlayer[] blindPlayers = model.getBlindPlayers();
		this.betPool.replace(blindPlayers[0].getName(), smallBlind);
		this.betPool.replace(blindPlayers[1].getName(), smallBlind * 2);

	}

	public boolean bet(String name, String action, int bet)
	{
		boolean isCheckPossible = false;
		PokerPlayer curPlayer = model.getCurrentPlayer();
		action = action.toLowerCase();

		if (!this.isRightPlayer(name))
			return false;

		if (curPlayer.isAllin())
		{
			if (!action.equals("allin"))
			{
				super.error("All In can't do anything");
			}
		}
		else if (action.equals("fold"))
		{
			super.fold(curPlayer);
		}
		else if (action.equals("call"))
		{
			int newBet = curPlayer.getBet(this.getNeededBet(curPlayer));
			if (newBet == 0)
			{
				this.error("no call possible");
				return false;
			}
			model.pot += newBet;
			this.betPool.replace(name, this.curMaxBet);
			this.model.getView().callAnimation(model, curPlayer);
		}
		else if (action.equals("raise"))
		{
			if (this.isRaisePossible())
			{
				this.NoRaise = false;
				super.raise(curPlayer);
			}
			else
			{
				this.error("no raise possible");
			}

		}
		else if (action.equals("check"))
		{
			if (this.isCheckPossible())
			{
				isCheckPossible = true;
			}
			else
			{
				this.model.getView().error("No check possible", model);
				return false;
			}
		}
		else
		{
			this.error("action " + action + " not known");
			return false;
		}
		if (this.isWinner() || this.roundIsDone(action, isCheckPossible))
			return true;

		model.setNextActivePlayer();
		this.updateView();

		return true;
	}

	public boolean roundIsDone(String action, boolean isCheckPossible)
	{
		Map<String, Integer> betPoolCopy = new HashMap<String, Integer>(this.betPool);
		for (PokerPlayer p : this.model.getPlayers())
			if (p.isActive() && p.isAllin())
				betPoolCopy.remove(p.getName());

		Set<Integer> values = new HashSet<Integer>(betPoolCopy.values());

		boolean noRaise = this.NoRaise && this.isBigBlind();
		boolean bigBlindFolds = values.size() == 1 || action.equals("fold");
		boolean sameBet = values.size() == 1 && !this.NoRaise;

		if (noRaise && bigBlindFolds || sameBet || isCheckPossible)
		{
			this.model.setState(new Flop(this.model));
			this.updateView();
			return true;
		}
		return false;
	}

	public boolean isCheckPossible()
	{
		return this.isBigBlind() && this.NoRaise;
	}

	private boolean isBigBlind()
	{
		return model.getCurrentPlayer().equals(model.getBlindPlayers()[1]);
	}

	public String toString()
	{
		return "Pre-Flop-State";
	}

}
