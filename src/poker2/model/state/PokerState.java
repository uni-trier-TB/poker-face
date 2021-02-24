package poker2.model.state;

import java.util.HashMap;
import java.util.Map;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;

public abstract class PokerState
{
	protected int curMaxBet;
	protected int raiseBet;
	protected int maxBet;
	protected PokerModel model;
	protected Map<String, Integer> betPool = new HashMap<String, Integer>();

	public PokerState(PokerModel model)
	{
		this(model, model.getSmallBlind() * 2);
	}

	public PokerState(PokerModel model, int curMaxBet)
	{
		this.model = model;
		int smallBlind = model.getSmallBlind();
		this.curMaxBet = curMaxBet;
		this.raiseBet = smallBlind * 4;
		this.maxBet = smallBlind * 8;
		this.initPlayers();
	}

	public boolean start()
	{
		this.error();
		return false;
	}

	public boolean addPlayer(String name)
	{
		this.error();
		return false;
	}

	public boolean removePlayer(String name)
	{
		this.error();
		return false;
	}

	public boolean bet(String name, String action, int bet)
	{
		this.error();
		return false;
	}

	protected void error()
	{
		this.error("Wrong State!\n Can't do that right now");
	}

	protected void error(String message)
	{
		this.model.getView().error(message, this.model);
		this.model.getMusic().playErrorSound();
	}

	protected void setNextState(PokerState s)
	{
		this.model.setState(s);
	}

	private void initPlayers()
	{
		for (PokerPlayer p : model.getPlayers())
			if (p.isActive())
				this.betPool.put(p.getName(), 0);
	}

	public int getRaiseBet()
	{
		return this.raiseBet;
	}

	public void drawThisState()
	{}

	public boolean isRaisePossible()
	{
		return this.curMaxBet < this.maxBet;
	}

	protected void updateView()
	{
		model.getView().drawPot(model);
		model.getView().drawPlayer(model);
		model.getView().drawBlinds(model);
		model.getView().drawBetLabel(model);
	}

	protected void raise(PokerPlayer curPlayer)
	{
		int possibleBet = this.raiseBet + this.model.getSmallBlind() * 2;
		this.curMaxBet = this.raiseBet;
		this.model.pot += curPlayer.getBet(this.getNeededBet(curPlayer));
		this.raiseBet = possibleBet <= this.getMaxBet() ? possibleBet : this.getMaxBet();
		this.betPool.replace(curPlayer.getName(), this.curMaxBet);
	}

	public int getNeededBet(PokerPlayer p)
	{
		return this.curMaxBet - this.betPool.get(p.getName());
	}

	public int getMaxBet()
	{
		return this.maxBet;
	}

	protected boolean isRightPlayer(String name)
	{
		String curPlayerName = model.getCurrentPlayer().getName();
		if (!curPlayerName.equals(name))
		{
			this.error("Wrong player \"" + name + "\". It's \"" + curPlayerName + "\"turn");
			return false;
		}

		return true;
	}

	public boolean isWinner()
	{
		PokerPlayer winner = this.model.isThereAWinner();
		if (winner != null)
		{
			this.model.setState(new End(this.model, winner));
			return true;
		}
		return false;
	}

	public void fold(PokerPlayer curPlayer)
	{
		this.model.getMusic().playWastedSound();
		this.betPool.remove(curPlayer.getName());
		curPlayer.setActive(false);
		model.getView().fold(model);
	}

}
