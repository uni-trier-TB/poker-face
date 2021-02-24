package poker2.model.state.community;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;
import poker2.model.state.PokerState;

public abstract class CommunityState extends PokerState
{

	protected boolean check = true;
	protected int bet;
	protected PokerPlayer highestBidder;

	public CommunityState(PokerModel model, int maxBet)
	{
		super(model, maxBet);
		this.bet = this.model.getSmallBlind() * 2;
		this.highestBidder = this.model.getCurrentPlayer();
	}

	public boolean isCheckPossible()
	{
		return this.check;
	}

	public int getBet()
	{
		return this.bet;
	}

	protected void setBet(PokerPlayer curPlayer, String name)
	{
		this.highestBidder = curPlayer;
		int newBet = curPlayer.getBet(this.bet);
		this.curMaxBet = newBet;
		this.betPool.replace(name, this.curMaxBet);
		model.pot += newBet;
		this.check = false;
	}

	protected void setCall(PokerPlayer curPlayer, String name)
	{
		int newBet = curPlayer.getBet(this.getNeededBet(curPlayer));
		this.betPool.replace(name, this.curMaxBet);
		model.pot += newBet;
		this.model.getView().callAnimation(this.model, curPlayer);
	}

	protected void setRaise(PokerPlayer curPlayer, String name)
	{
		this.highestBidder = curPlayer;
		this.curMaxBet = this.raiseBet;
		this.model.pot += curPlayer.getBet(this.getNeededBet(curPlayer));
		this.raiseBet += this.raiseBet + this.model.getSmallBlind() * 2 <= this.getMaxBet()
				? this.model.getSmallBlind() * 2
				: this.getMaxBet();
		this.betPool.replace(name, this.curMaxBet);
	}

	protected boolean isRoundDone(PokerState s, PokerPlayer player)
	{
		model.setNextActivePlayer();
		Map<String, Integer> betPoolCopy = new HashMap<String, Integer>(this.betPool);
		for (PokerPlayer p : this.model.getPlayers())
			if (p.isActive() && p.isAllin())
				betPoolCopy.remove(p.getName());

		int size = new HashSet<Integer>(betPoolCopy.values()).size();
		boolean isDone = false;
		boolean checked = this.check && this.highestBidder.equals(player);
		boolean betted = !this.check && highestBidder.equals(model.getCurrentPlayer());
		if (size == 1 && (checked || betted))
		{
			this.model.setState(s);
			isDone = true;
		}
		super.updateView();
		return isDone;
	}

}
