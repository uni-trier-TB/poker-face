package poker2.v;

import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import poker2.utility.Point;
import poker2.utility.Utility;
import poker2.model.PokerModel;
import poker2.model.holdemevaluator.Card;
import poker2.model.player.PokerPlayer;
import poker2.model.state.PokerState;
import poker2.model.state.PreFlop;
import poker2.model.state.community.CommunityState;
import poker2.model.tables.ITableProperty;

public class View extends Scene
{

	private ImageView stack;
	private ImageView smallBlind;
	private ImageView bigBlind;
	private ImageView smallBlindJeton;
	private ImageView bigBlindJeton;
	private ImageView pileJeton;
	private Label potLabel = new Label("€: 0");
	private Label betLabel = new Label();
	private Circle c;
	private Stage primaryStage;

	private final static String TABLE_PICTURE_PATH = "res\\pics\\tisch2.PNG";
	private final static String SMALL_B_PIC_PATH = "res\\pics\\small.jpg";
	private final static String BIGBLIND_PICTURE_PATH = "res\\pics\\big.jpg";
	private final static String STACK_PICTURE_PATH = "res\\pics\\cards\\stack.jpg";
	private final static String SMALL_BLIND_JETON_PATH = "res\\pics\\jetonSmall.png";
	private final static String BIG_BLINDS_JETON_PATH = "res\\pics\\jetonBIG.png";
	private final static String JETON_PILE_PATH = "res\\pics\\jetonPile.png";

	public View(Pane pane, Stage stage, ITableProperty ip)
	{
		super(pane, ip.getTableSize().x, ip.getTableSize().y, Color.BLACK);
		this.primaryStage = stage;
		this.c = new Circle(0, 0, PokerPlayer.playerSize.x / 2);
		this.c.setFill(null);
		this.c.setStrokeWidth(8);

		this.setLabel(this.potLabel, ip.getPotPosition());
		this.setLabel(this.betLabel, ip.getBetLabelPos());
		this.stack = Utility.setImageView(STACK_PICTURE_PATH, "Class View", ip.getStackPos(), Card.CARD_SIZE);
		this.addRemoveAll(stack);
		this.smallBlind = this.setBlinds(SMALL_B_PIC_PATH, 75, 75);
		this.bigBlind = this.setBlinds(BIGBLIND_PICTURE_PATH, 75, 75);
		this.smallBlindJeton = this.setBlinds(SMALL_BLIND_JETON_PATH, 50, 50);
		this.bigBlindJeton = this.setBlinds(BIG_BLINDS_JETON_PATH, 50, 50);
		this.pileJeton = Utility.setImageView(Utility.setImage(JETON_PILE_PATH, ""), new Point(0, 0),
				new Point(75, 75));

		this.getPane().setBackground(Utility.setBackground(TABLE_PICTURE_PATH));
		stage.setResizable(false);
		stage.setScene(this);
		stage.show();
	}

	public void drawPlayer(PokerModel model)
	{

		for (PokerPlayer p : model.getPlayers())
		{
			p.setEffect(null);
			if (p.equals(model.getCurrentPlayer()))
			{
				double centCirAndRad = p.getFitHeight() / 2;
				c.setTranslateX(p.getX() + centCirAndRad);
				c.setTranslateY(p.getY() + centCirAndRad);

				StrokeTransition st = new StrokeTransition(Duration.millis(500), c, Color.RED, Color.YELLOW);
				st.setCycleCount(10000);
				st.setAutoReverse(true);
				st.play();
				this.addRemoveAll(c);
				p.setEffect(new Bloom(0.1));

				p.setEffect(new Bloom(0.1));

			}
			Label nameL = p.getNameLabel();
			Label moneyL = p.getMoneyLabel();
			Label allL = p.getAllinLabel();
			if (p.isAllin() && p.isActive())
			{
				allL.setText("ALL-IN");
			}
			else
				allL.setText("");
			this.addRemoveAll(p, nameL, moneyL, allL);
		}

	}

	public void drawBlinds(PokerModel pokerModel)
	{
		this.drawBlinds(this.smallBlind, pokerModel.getBlindPlayers()[0]);
		this.drawBlinds(this.bigBlind, pokerModel.getBlindPlayers()[1]);
	}

	public void updateBlinds(PokerModel pokerModel)
	{
		int transDuration = 800;
		int fadDuration = 700;
		int delay = 900;
		this.drawBlinds(this.smallBlind, pokerModel.getBlindPlayers()[0]);
		this.drawBlinds(this.bigBlind, pokerModel.getBlindPlayers()[1]);
		this.animateJetonToPot(pokerModel, this.smallBlindJeton, pokerModel.getBlindPlayers()[0], transDuration,
				fadDuration, delay);
		this.animateJetonToPot(pokerModel, this.bigBlindJeton, pokerModel.getBlindPlayers()[1], transDuration,
				fadDuration, delay);
	}

	private void drawBlinds(ImageView blind, PokerPlayer pokerPlayer)
	{
		this.addRemoveAll(blind);
		TranslateTransition tt = this.getTranslTrans(blind, pokerPlayer.getX(), pokerPlayer.getY(), 500);
		this.setTransition(new SequentialTransition(), 300, tt);
	}

	public void drawBetLabel(PokerModel model)
	{
		PokerState state = model.getState();
		if (model.getCurrentPlayer().isAllin())
		{
			this.betLabel.setText("allin");
			return;
		}
		if (state instanceof PreFlop)
		{
			PreFlop betState = (PreFlop) state;
			String fold = " |FOLD|";
			String call = " CALL " + betState.getNeededBet(model.getCurrentPlayer()) + "|";
			String raise = betState.isRaisePossible() ? "RAISE " + betState.getRaiseBet() + "|" : "";
			if (betState.isCheckPossible())
			{
				call = " CHECK| ";
			}
			this.betLabel.setText(fold + call + raise);
		}
		else if (state instanceof CommunityState)
		{
			CommunityState betState = (CommunityState) state;
			String fold = " |FOLD|";
			String call = "";
			String check = "";
			String raise = "";
			if (betState.isCheckPossible())
			{
				call = " BET " + betState.getBet() + "| ";
				check = " CHECK|";
			}
			else
			{
				call = " CALL " + betState.getNeededBet(model.getCurrentPlayer()) + "| ";
				raise = betState.isRaisePossible() ? "RAISE " + betState.getRaiseBet() + "| " : "";
			}
			this.betLabel.setText(fold + check + call + raise);
		}

	}

	public void fold(PokerModel model)
	{
		final int duration = 200;
		double x = 0;
		double y = 0;
		PokerPlayer p = model.getCurrentPlayer();
		Card[] cards = p.getCards();

		x = this.stack.getX() - cards[0].getX();
		y = this.stack.getY() - cards[0].getY();
		FadeTransition ft1 = this.getFadTrans(cards[0], duration);
		TranslateTransition tT1 = this.getTranslTrans(cards[0], x, y, duration);

		x = this.stack.getX() - cards[1].getX();
		y = this.stack.getY() - cards[1].getY();
		FadeTransition ft2 = this.getFadTrans(cards[1], duration);
		TranslateTransition tT2 = this.getTranslTrans(cards[0], x, y, duration);

		this.setTransition(new ParallelTransition(), 900, tT1, tT2, ft1, ft2);

	}

	public void animateJetonToPot(PokerModel pokerModel, ImageView jeton, PokerPlayer p, int transDuration,
								  int fadDuration, int transDelay)
	{
		this.addRemoveAll(jeton);
		jeton.setTranslateX(p.getX() + p.getFitHeight() / 2);
		jeton.setTranslateY(p.getY() + p.getFitHeight() / 2);
		TranslateTransition tt = this.getTranslTrans(jeton, this.potLabel.getTranslateX(),
				this.potLabel.getTranslateY(), transDuration);
		FadeTransition ft = this.getFadTrans(jeton, fadDuration);

		Transition t = this.setTransition(new SequentialTransition(), transDelay, tt, ft);
		t.setOnFinished(e -> this.updatePot(pokerModel));
	}

	public void drawCard(PokerPlayer p, int i, int j, int k)
	{
		this.animateCard(p, i, j, 800, 800, k);
	}

	private void addEventHandler(Card c)
	{
		c.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			public void handle(MouseEvent event)
			{
				if (c.isShown())
					c.hideCard();
				else
					c.showCard();
			}
		});
	}

	private void animateCard(PokerPlayer p, int i, int j, int translateDuration, int rotateDuration,
							 int delayMultiplyer)
	{
		Card card = p.getCards()[i];
		Point cardPos = p.getCardPos()[i];
		this.getPane().getChildren().remove(card);
		this.getPane().getChildren().add(card);
		this.addEventHandler(card);
		card.setTranslateX(this.stack.getX());
		card.setTranslateY(this.stack.getY());

		RotateTransition rT = this.getRotTrans(card, rotateDuration);
		TranslateTransition tT = this.getTranslTrans(card, cardPos.x, cardPos.y, translateDuration);
		this.setTransition(new ParallelTransition(), delayMultiplyer, tT, rT);

	}

	public void drawFlop(PokerModel model)
	{
		this.drawCommunity(model, 0, 3);
	}

	public void drawTurn(PokerModel model)
	{
		this.drawCommunity(model, 3, 4);
	}

	public void drawRiver(PokerModel model)
	{
		this.drawCommunity(model, 4, 5);
	}

	private void drawCommunity(PokerModel pokerModel, int start, int end)
	{
		Card[] comCards = pokerModel.getCommunityCards();
		Point[] comCardsPos = pokerModel.getCommunityCardsPos();

		for (int i = start; i < end; i++)
		{
			ImageView card = comCards[i];

			card.setTranslateX(this.stack.getX());
			card.setTranslateY(this.stack.getY());
			this.addRemoveAll(card);

			RotateTransition rT = this.getRotTrans(card, 800);
			TranslateTransition tT = this.getTranslTrans(card, comCardsPos[i].x, comCardsPos[i].y, 800);

			Transition t = this.setTransition(new ParallelTransition(), i * 500, rT, tT);
			pokerModel.getMusic().playFlipSound(i * 500);
			t.setOnFinished(e -> this.makeCardVisible((Card) card));

		}
	}

	private void makeCardVisible(Card card)
	{
		RotateTransition r = new RotateTransition(Duration.millis(200), card);
		r.setAxis(Rotate.Y_AXIS);
		r.setByAngle(90);
		card.showCard();
		SequentialTransition st = new SequentialTransition();
		st.getChildren().add(r);
		st.play();

		r = new RotateTransition(Duration.millis(200), card);
		r.setAxis(Rotate.Y_AXIS);
		r.setByAngle(360);
		st = new SequentialTransition();
		st.getChildren().add(r);
		st.play();
		addRemoveAll(card);
	}

	/******************************************************
	 ********************* PRIVATE*************************
	 ******************************************************/

	private Transition setTransition(Transition t, int delay, Animation... animations)
	{
		if (t instanceof ParallelTransition)
			((ParallelTransition) t).getChildren().addAll(animations);
		else if (t instanceof SequentialTransition)
			((SequentialTransition) t).getChildren().addAll(animations);
		t.setDelay(Duration.millis(delay));
		t.play();
		return t;
	}

	private FadeTransition getFadTrans(ImageView iv, int duration)
	{
		FadeTransition ft1 = new FadeTransition(Duration.millis(duration), iv);
		ft1.setFromValue(1.0);
		ft1.setToValue(0.0);
		return ft1;
	}

	private RotateTransition getRotTrans(ImageView iv, int duration)
	{
		RotateTransition rotateTransition = new RotateTransition(Duration.millis(duration), iv);
		rotateTransition.setByAngle(720f);
		rotateTransition.setCycleCount(1);
		return rotateTransition;
	}

	private TranslateTransition getTranslTrans(ImageView iv, double x, double y, int duration)
	{
		TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), iv);
		translateTransition.setToX(x);
		translateTransition.setToY(y);
		translateTransition.setCycleCount(1);
		return translateTransition;
	}

	private ImageView setBlinds(String path, int sizeX, int sizeY)
	{
		ImageView blind = Utility.setImageView(path, "Class View", new Point(0, 0), new Point(sizeX, sizeY));
		double centCirAndRad = blind.getFitWidth() / 2;
		blind.setClip(new Circle(0 + centCirAndRad, 0 + centCirAndRad, centCirAndRad - 10));
		this.addRemoveAll(blind);
		return blind;
	}

	public void drawPot(PokerModel pokerModel)
	{
		this.potLabel.setText(" €: " + pokerModel.getPotMoney() + " ");
	}

	private void setLabel(Label l, Point pos)
	{
		l.setTranslateX(pos.x);
		l.setTranslateY(pos.y);
		l.setFont(new Font("Arial", 30));
		Color col = Color.rgb(150, 150, 150);
		CornerRadii corn = new CornerRadii(10);
		Background background = new Background(new BackgroundFill(col, corn, Insets.EMPTY));
		l.setBackground(background);
		this.addRemoveAll(l);
	}

	public void addRemoveAll(Node... nodes)
	{
		this.getPane().getChildren().removeAll(nodes);
		this.getPane().getChildren().addAll(nodes);
	}

	public void addRemoveAll(Node node)
	{
		this.getPane().getChildren().remove(node);
		this.getPane().getChildren().add(node);
	}

	public Pane getPane()
	{
		return ((Pane) super.getRoot());
	}

	public void updatePot(PokerModel pokerModel)
	{
		this.potLabel.setText("€: " + pokerModel.getPotMoney());
	}

	public void error(String message, PokerModel pokerModel)
	{
		this.error(message, pokerModel, 0);
	}

	public void error(String message, PokerModel pokerModel, int delay)
	{
		Label l = new Label(message);
		l.setAlignment(Pos.CENTER);
		l.setMinWidth(this.getWidth());
		l.setMinHeight(this.getHeight());
		l.setFont(Font.font("Verdana", 60));
		l.setTextFill(Color.WHITE);
		l.setStyle("-fx-background-color: rgba(100,100,100,0.1);");
		this.addRemoveAll(l);

		ScaleTransition st = new ScaleTransition(Duration.millis(1000), l);
		st.setByX(1.1f);
		st.setByY(1.1f);
		st.setAutoReverse(true);

		FadeTransition ft = new FadeTransition(Duration.millis(1000), l);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);

		this.setTransition(new SequentialTransition(), 0, st, ft);
		this.shakeStage();
	}

	public void delete()
	{
		ArrayList<Card> l = new ArrayList<Card>();
		ArrayList<FadeTransition> ft = new ArrayList<FadeTransition>();
		for (Node n : this.getPane().getChildren())
		{
			if (n instanceof Card)
			{
				Card c = (Card) n;
				ft.add(this.getFadTrans(c, 500));
				l.add(c);
			}
		}
//		Animation[] a = new Animation[ft.size()];
//		a = ft.toArray(a);
//		Transition t = this.setTransition(new ParallelTransition(), 0, a);
//		t.setOnFinished(e -> this.getPane().getChildren().removeAll(l));
		this.getPane().getChildren().removeAll(l);
	}

	public void shakeStage()
	{
		Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>()
		{
			int shakeX = 0, shakeY = 0;

			@Override
			public void handle(ActionEvent t)
			{
				shakeX = shakeX == 0 ? 1 : 0;
				shakeY = shakeY == 0 ? 1 : 0;
				if (shakeX == 0)
					primaryStage.setX(primaryStage.getX() + 10);
				else
					primaryStage.setX(primaryStage.getX() - 10);
				if (shakeY == 0)
					primaryStage.setY(primaryStage.getY() + 10);
				else
					primaryStage.setY(primaryStage.getY() - 10);
			}
		}));
		timelineX.setCycleCount(10);
		timelineX.play();
	}

	public void flipUserCards(PokerModel model)
	{
		PokerPlayer[] players = model.getPlayers();

		for (PokerPlayer p : players)
		{
			if (p.isActive())
			{
				Card[] cards = p.getCards();
				this.makeCardVisible(cards[0]);
				this.makeCardVisible(cards[1]);
			}
		}

	}

	public void update(PokerModel model)
	{
		PokerPlayer[] players = model.getPlayers();

		for (PokerPlayer p : players)
		{
			if (p.isActive())
			{
				Card[] cards = p.getCards();
				this.getPane().getChildren().remove(cards[0]);
				this.getPane().getChildren().add(cards[0]);
				this.getPane().getChildren().remove(cards[1]);
				this.getPane().getChildren().add(cards[1]);

			}
		}
	}

	public void callAnimation(PokerModel model, PokerPlayer curPlayer)
	{
		this.animateJetonToPot(model, this.pileJeton, curPlayer, 500, 500, 200);
	}

}
