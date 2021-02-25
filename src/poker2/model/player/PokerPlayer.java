package poker2.model.player;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import poker2.utility.Utility;
import poker2.model.PokerModel;
import poker2.model.holdemevaluator.Card;
import poker2.model.music.Music;
import poker2.utility.Point;
import poker2.utility.Point3D;

public class PokerPlayer extends ImageView
{
	private boolean empty = true;
	private boolean active;
	private String name = ".";
	private Label nameLabel = new Label();
	private Label moneyLabel = new Label();
	private Label allinLabel = new Label();
	private Card[] cards = new Card[2];
	private Point3D[] cardPos = new Point3D[2];

	private int money;

	private PokerPlayer nextPlayer;

	public static Point playerSize;
	private static final String EMPTY_PIC_PATH = "res\\pics\\pfp\\pfp.png";
	private static int numberOfPlayers = 0;

	public PokerPlayer(Point playerPosition, Point3D[] cardPosition)
	{
		this.setImage(Utility.setImage(EMPTY_PIC_PATH, "Class Poker Player empty pic"));
		this.setX(playerPosition.x);
		this.setY(playerPosition.y);
		this.setFitWidth(playerSize.x);
		this.setFitHeight(playerSize.y);
		this.cardPos[0] = cardPosition[0];
		this.cardPos[1] = cardPosition[1];
		double centCirAndRad = playerSize.x / 2;

		Circle c = new Circle(this.getX() + centCirAndRad, this.getY() + centCirAndRad, centCirAndRad);
		this.setClip(c);
		this.setOpacity(0.6);

		this.setLabel(this.nameLabel, 0, 150);
		this.setLabel(this.moneyLabel, 150, 0);
		this.allinLabel.setFont(new Font("Arial", 60));
		this.allinLabel.setTextFill(Color.WHITE);
		this.allinLabel.setTranslateX(this.getX() + 15);
		this.allinLabel.setTranslateY(this.getY() + 70);

	}

	public PokerPlayer(PokerPlayer nextPlayer, Point playerPosition, Point3D[] cardPosition)
	{
		this(playerPosition, cardPosition);
		this.setNextPlayer(nextPlayer);
	}

	private void setLabel(Label l, int x, int y)
	{
		l.setTranslateX(this.getX() + x);
		l.setTranslateY(this.getY() + y);
		l.setFont(new Font("Arial", 30));

		Stop[] stops = new Stop[]
				{ new Stop(0, Color.GRAY), new Stop(1, Color.RED) };
		LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		l.setTextFill(lg1);
		Color col = Color.rgb(32, 32, 32);
		CornerRadii corn = new CornerRadii(10);
		Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
		l.setBackground(background);
	}

	/**********************************
	 ************ GETTER/SETTER********
	 **********************************/

	public void setNextPlayer(PokerPlayer nextPlayer)
	{
		this.nextPlayer = nextPlayer;
	}

	public boolean isEmpty()
	{
		return this.empty;
	}

	public String getName()
	{
		return this.name;
	}

	public PokerPlayer getNext()
	{
		return this.nextPlayer;
	}

	public void setHumanPlayer(String name, Music music)
	{
		String s = ++numberOfPlayers + ".PNG";
		this.setImage(Utility.setImage("res\\pics\\pfp\\" + s, "setHumanPlayer"));
		music.playSetHumanSound(numberOfPlayers);
		this.name = name;
		this.empty = false;
		this.active = false;
		this.money = PokerModel.startMoney;
		this.nameLabel.setText(" " + this.name + " ");
	}

	public void setEmpty()
	{
		this.name = ".";
		this.empty = true;
		this.active = false;
		this.setImage(Utility.setImage(EMPTY_PIC_PATH, "Class PokerPlayer setEmpty"));
		this.setOpacity(0.6);
		this.nameLabel.setText("");
	}

	public int getMoney()
	{
		return this.money;
	}

	public PokerPlayer nextActivePlayer()
	{
		return !this.nextPlayer.isEmpty() && this.nextPlayer.isActive() ? this.nextPlayer
				: this.nextPlayer.nextActivePlayer();
	}

	public boolean isActive()
	{
		return this.active;
	}

	public void setActive(boolean b)
	{
		this.active = b;
		double opactiy = b ? 1.0 : 0.6;
		this.setOpacity(opactiy);
	}

	public int getBet(int smallBlind)
	{
		int amount = this.money - smallBlind;
		if (amount < 0)
		{
			smallBlind = this.money;
			this.money = 0;

		}
		else
		{
			this.money = amount;
		}
		return smallBlind;
	}

	public void setCard(int index, Card card)
	{
		this.cards[index] = card;
	}

	public Card[] getCards()
	{
		return this.cards;
	}

	public static void setPlayerSize(Point point)
	{
		PokerPlayer.playerSize = point;

	}

	public Point3D[] getCardPos()
	{
		return this.cardPos;
	}

	public Label getNameLabel()
	{
		return this.nameLabel;
	}

	public Label getMoneyLabel()
	{
		String text = this.isEmpty() ? "" : " €: " + this.money + " ";
		this.moneyLabel.setText(text);
		return this.moneyLabel;
	}

	public void addMoney(int potMoney)
	{
		this.money += potMoney;
	}

	public boolean isAllin()
	{
		return this.money <= 0;
	}

	public Label getAllinLabel()
	{
		return this.allinLabel;
	}

	public void clearCards()
	{
		this.cards = new Card[2];

	}

}
