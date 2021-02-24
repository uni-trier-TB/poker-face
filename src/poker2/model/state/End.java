package poker2.model.state;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;

public class End extends PokerState
{

	public End(PokerModel model, PokerPlayer winner)
	{
		super(model);

	}

	public void drawThisState()
	{
		PokerPlayer winner = this.model.isThereAWinner();
		this.model.getView().delete();
		this.model.getView().error("Player " + winner.getName() + " wins!", model, 2000);
		this.model.getMusic().playWinSound();
		winner.addMoney(this.model.getPotMoney());
		this.model.resetPot();
		this.model.raiseBlinds();
		model.getView().drawPot(model);
		model.getView().drawPlayer(model);
		model.getView().drawBlinds(model);
	}

	public boolean start()
	{
		Start s = new Start(model);
		this.model.setState(s);
		this.model.getState().start();
		return true;
	}

	public String toString()
	{
		return "End-State";
	}

}
