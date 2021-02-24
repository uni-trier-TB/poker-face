package poker2.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import poker2.model.holdemevaluator.Card;
import poker2.model.music.Music;
import poker2.model.player.PokerPlayer;
import poker2.model.state.Start;
import poker2.model.state.PokerState;
import poker2.model.tables.ITableProperty;
import poker2.model.tables.TableProperty1900p;
import poker2.utility.Point;
import poker2.utility.Point3D;
import poker2.v.View;

public class PokerModel
{
	private PokerPlayer[] players = new PokerPlayer[PokerModel.MAX_PLAYERS];
	private Card[] communityCard = new Card[5];
	private Point[] communityPos = new Point[5];
	private Card[] deck = new Card[52];
	private PokerPlayer currentPlayer;
	private PokerPlayer smallBlindPlayer;
	private PokerPlayer bigBlindPlayer;
	private int deckPointer;
	private int smallBlind;
	public int pot;

	private View view;
	private PokerState state;
	private Music music;

	public static int startMoney;
	public static final int MAX_PLAYERS = 6;

	Stage stage;

	public PokerModel(Stage stage, int startMoney, int smallBlind)
	{
		this.stage = stage;
		PokerModel.startMoney = startMoney;
		this.smallBlind = smallBlind;
		ITableProperty tp = new TableProperty1900p();
		this.music = new Music();
		this.communityPos = tp.getCommunityPosition();
		this.view = new View(new Pane(), stage, tp);
		this.deck = Card.getDeck();
		this.initEmptyPlayers(tp);
		state = new Start(this);

	}

	public Card nextCard()
	{
		return this.deck[this.deckPointer++];
	}

	public void shuffleDeck()
	{
		this.communityCard = new Card[5];
		List<Card> tempCards = Arrays.asList(this.deck);
		Collections.shuffle(tempCards);
		this.deck = tempCards.toArray(this.deck);
		this.deckPointer = 0;
	}

	/**********************************
	 *********** GETTER/SETTER*********
	 **********************************/

	public void updateBlindsPlayer()
	{
		this.smallBlindPlayer = this.smallBlindPlayer.nextActivePlayer();
		this.bigBlindPlayer = this.smallBlindPlayer.nextActivePlayer();
		this.currentPlayer = this.bigBlindPlayer.nextActivePlayer();

		this.getBlindsMoney();

		this.view.drawPlayer(this);
		this.view.updateBlinds(this);
		this.music.playJetonSound();
	}

	public void setNextActivePlayer()
	{
		this.currentPlayer = this.currentPlayer.nextActivePlayer();
	}

	public void getBlindsMoney()
	{
		this.pot += this.smallBlindPlayer.getBet(this.smallBlind);
		this.pot += this.bigBlindPlayer.getBet(this.smallBlind * 2);
	}

	public PokerPlayer getPlayerByName(String name)
	{
		for (PokerPlayer p : this.players)
			if (p.getName().equals(name))
				return p;
		return null;
	}

	/**********************************
	 ************ PRIVATE/INIT*********
	 **********************************/

	private void initEmptyPlayers(ITableProperty tp)
	{
		int lastPlayer = PokerModel.MAX_PLAYERS - 1;

		Point[] playerPositions = tp.getPlayersPosition();
		Point3D[][] cardPositions = tp.getCardPositions();

		this.players[lastPlayer] = new PokerPlayer(playerPositions[lastPlayer], cardPositions[lastPlayer]);

		for (int i = PokerModel.MAX_PLAYERS - 2; i >= 0; i--)
			this.players[i] = new PokerPlayer(this.players[i + 1], playerPositions[i], cardPositions[i]);

		// first player is next to last player
		this.players[lastPlayer].setNextPlayer(this.players[0]);
		// dummy player
		this.smallBlindPlayer = new PokerPlayer(this.players[0], new Point(0, 0), new Point3D[]
		{ new Point3D(0, 0, 0), new Point3D(0, 0, 0) });

		this.view.drawPlayer(this);
	}

	public void print()
	{
		for (int i = 0; i < this.players.length; i++)
		{
			PokerPlayer p = this.players[i];
			System.out.println(p.getName() + "\t money:" + p.getMoney() + "\t SB: " + p.equals(this.smallBlindPlayer)
					+ "\t BB: " + p.equals(this.bigBlindPlayer) + "\t Card1: " + p.getCards()[0] + "\t\t Card2: "
					+ p.getCards()[1]);
		}
		System.out.println();
		for (Card c : this.communityCard)
			System.out.println(c);

		System.out.println();
		System.out.println("pot: " + this.pot);

	}

	public void dealCards()
	{
		int k = 0;
		for (int i = 0; i < 2; i++)
			for (int j = 0; j < this.players.length; j++)
			{
				PokerPlayer p = this.players[j];
				if (!p.isEmpty() && p.isActive())
				{
					p.setCard(i, this.nextCard());
					this.view.drawCard(p, i, j, k++ * 300);
				}

			}
		for (int i = 0; i < this.communityCard.length; i++)
			this.communityCard[i] = this.nextCard();
	}

	public void raiseBlinds()
	{
		this.smallBlind *= 2;
	}

	public int getSmallBlind()
	{
		return this.smallBlind;
	}

	public PokerPlayer[] getPlayers()
	{
		return this.players;
	}

	public PokerPlayer[] getBlindPlayers()
	{
		return new PokerPlayer[]
		{ this.smallBlindPlayer, this.bigBlindPlayer };
	}

	public PokerPlayer getCurrentPlayer()
	{
		return this.currentPlayer;
	}

	public Card[] getCommunityCards()
	{
		return this.communityCard;
	}

	public Point[] getCommunityCardsPos()
	{
		return this.communityPos;
	}

	public int getPotMoney()
	{
		return this.pot;
	}

	public View getView()
	{
		return this.view;
	}

	public PokerState getState()
	{
		return this.state;
	}

	public void setState(PokerState s)
	{
		this.state = s;
		this.state.drawThisState();
	}

	public PokerPlayer isThereAWinner()
	{
		PokerPlayer winner = null;
		int activePlayers = 0;
		for (PokerPlayer p : this.players)
		{
			if (p.isActive())
			{
				winner = p;
				activePlayers++;
			}
		}
		return activePlayers == 1 ? winner : null;
	}

	public void resetPot()
	{
		this.pot = 0;
	}

	public Music getMusic()
	{
		return this.music;

	}

	public void newCards()
	{
		this.deck = Card.getDeck();

	}

	public void delete()
	{
		for (PokerPlayer p : this.players)
		{
			Card[] cards = p.getCards();
			this.view.getPane().getChildren().remove(cards[0]);
			this.view.getPane().getChildren().remove(cards[1]);
		}
	}

}
