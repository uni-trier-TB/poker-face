package poker2.model.state.community;

import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;
import poker2.model.state.Winner;

public class River extends CommunityState
{
	public River(PokerModel model)
	{
		super(model, 0);
		// this.model.setNextActivePlayer();

	}

	public boolean bet(String name, String action, int bet)
	{
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
		else if (action.equals("bet"))
		{
			if (this.check)
				super.setBet(curPlayer, name);
			else
			{
				this.error("bet not possible");
				return false;
			}
		}
		else if (action.equals("call"))
		{
			if (!this.check)
			{
				super.setCall(curPlayer, name);
			}
			else
			{
				this.error("no call possible");
				return false;
			}
		}
		else if (action.equals("raise"))
		{
			if (super.isRaisePossible() && this.curMaxBet > 0)
			{
				this.setRaise(curPlayer, name);
			}
			else
			{
				this.error("no raise possible");
				return false;
			}

		}
		else if (action.equals("check"))
		{
			if (!this.check)
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
		boolean w = super.isWinner();
		boolean r = super.isRoundDone(new Winner(this.model), curPlayer);
		if (w || r)
			return true;
		return true;
	}

	public void drawThisState()
	{
		this.model.getView().drawRiver(model);
	}

	public String toString()
	{
		return "River-State";
	}

}