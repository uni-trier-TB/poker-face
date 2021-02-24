package poker2.model.holdemevaluator;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import poker2.utility.Utility;
import poker2.utility.Point;

public final class Card extends ImageView implements Comparable<Card>
{
	private boolean visible = false;
	private CardValue value;
	private Suit suit;
	private int cardNumber;

	private final static String PATH_CARD = "res\\pics\\cards\\";
	private final static int NO_IMAGE = -1;
	public final static Image BACK_CARD = Utility.setImage(PATH_CARD + "00.jpg", "Class Card Back_Card");
	public static Point CARD_SIZE;

	public Card(CardValue value, Suit suit, int cardNumber)
	{
		this(cardNumber);
		this.value = value;
		this.suit = suit;
	}

	public Card(CardValue value, Suit suit)
	{
		this(value, suit, Card.NO_IMAGE);
	}

	public Card(int cardNumber)
	{
		this.cardNumber = cardNumber;
		this.setImage(BACK_CARD);
		this.setFitWidth(Card.CARD_SIZE.x);
		this.setFitHeight(Card.CARD_SIZE.y);
	}

	public void showCard()
	{
		if (cardNumber != -1)
		{
			String fullPath = Card.PATH_CARD;
			fullPath += cardNumber < 10 ? "0" + cardNumber : "" + cardNumber;
			fullPath += ".jpg";
			this.setImage(Utility.setImage(fullPath, "Class Card in Constructor"));
			this.visible = true;
		}
	}

	public void hideCard()
	{
		this.setImage(BACK_CARD);
		this.visible = false;
	}

	public boolean isShown()
	{
		return this.visible;
	}

	public CardValue getValue()
	{
		return value;
	}

	public Suit getSuit()
	{
		return suit;
	}

	public int getNumber()
	{
		return this.value.getValue() * this.suit.getValue();
	}

	@Override
	public String toString()
	{
		return this.value.name() + " of " + this.suit.name() /* + ": " + this.getNumber() */;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Card card = (Card) o;

		return getValue() == card.getValue() && getSuit() == card.getSuit();
	}

	@Override
	public int hashCode()
	{
		int result = getValue().hashCode();
		result = 31 * result + getSuit().hashCode();
		return result;
	}

	@Override
	public int compareTo(Card o)
	{
		final boolean otherIsAnAce = CardValue.ACE.equals(o.getValue());
		final boolean iamAnAce = CardValue.ACE.equals(this.value);
		if (iamAnAce && !otherIsAnAce)
		{
			return 1;
		}
		if (!iamAnAce && otherIsAnAce)
		{
			return -1;
		}
		return this.value.compareTo(o.getValue());
	}

	public static Card[] getDeck()
	{
		Card[] cards = new Card[52];
		cards[0] = new Card(CardValue.TWO, Suit.SPADES, 1);
		cards[1] = new Card(CardValue.THREE, Suit.SPADES, 2);
		cards[2] = new Card(CardValue.FOUR, Suit.SPADES, 3);
		cards[3] = new Card(CardValue.FIVE, Suit.SPADES, 4);
		cards[4] = new Card(CardValue.SIX, Suit.SPADES, 5);
		cards[5] = new Card(CardValue.SEVEN, Suit.SPADES, 6);
		cards[6] = new Card(CardValue.EIGHT, Suit.SPADES, 7);
		cards[7] = new Card(CardValue.NINE, Suit.SPADES, 8);
		cards[8] = new Card(CardValue.TEN, Suit.SPADES, 9);
		cards[9] = new Card(CardValue.JACK, Suit.SPADES, 10);
		cards[10] = new Card(CardValue.QUEEN, Suit.SPADES, 11);
		cards[11] = new Card(CardValue.KING, Suit.SPADES, 12);
		cards[12] = new Card(CardValue.ACE, Suit.SPADES, 13);

		cards[13] = new Card(CardValue.TWO, Suit.HEARTS, 14);
		cards[14] = new Card(CardValue.THREE, Suit.HEARTS, 15);
		cards[15] = new Card(CardValue.FOUR, Suit.HEARTS, 16);
		cards[16] = new Card(CardValue.FIVE, Suit.HEARTS, 17);
		cards[17] = new Card(CardValue.SIX, Suit.HEARTS, 18);
		cards[18] = new Card(CardValue.SEVEN, Suit.HEARTS, 19);
		cards[19] = new Card(CardValue.EIGHT, Suit.HEARTS, 20);
		cards[20] = new Card(CardValue.NINE, Suit.HEARTS, 21);
		cards[21] = new Card(CardValue.TEN, Suit.HEARTS, 22);
		cards[22] = new Card(CardValue.JACK, Suit.HEARTS, 23);
		cards[23] = new Card(CardValue.QUEEN, Suit.HEARTS, 24);
		cards[24] = new Card(CardValue.KING, Suit.HEARTS, 25);
		cards[25] = new Card(CardValue.ACE, Suit.HEARTS, 26);

		cards[26] = new Card(CardValue.TWO, Suit.DIAMONDS, 27);
		cards[27] = new Card(CardValue.THREE, Suit.DIAMONDS, 28);
		cards[28] = new Card(CardValue.FOUR, Suit.DIAMONDS, 29);
		cards[29] = new Card(CardValue.FIVE, Suit.DIAMONDS, 30);
		cards[30] = new Card(CardValue.SIX, Suit.DIAMONDS, 31);
		cards[31] = new Card(CardValue.SEVEN, Suit.DIAMONDS, 32);
		cards[32] = new Card(CardValue.EIGHT, Suit.DIAMONDS, 33);
		cards[33] = new Card(CardValue.NINE, Suit.DIAMONDS, 34);
		cards[34] = new Card(CardValue.TEN, Suit.DIAMONDS, 35);
		cards[35] = new Card(CardValue.JACK, Suit.DIAMONDS, 36);
		cards[36] = new Card(CardValue.QUEEN, Suit.DIAMONDS, 37);
		cards[37] = new Card(CardValue.KING, Suit.DIAMONDS, 38);
		cards[38] = new Card(CardValue.ACE, Suit.DIAMONDS, 39);

		cards[39] = new Card(CardValue.TWO, Suit.CLUBS, 40);
		cards[40] = new Card(CardValue.THREE, Suit.CLUBS, 41);
		cards[41] = new Card(CardValue.FOUR, Suit.CLUBS, 42);
		cards[42] = new Card(CardValue.FIVE, Suit.CLUBS, 43);
		cards[43] = new Card(CardValue.SIX, Suit.CLUBS, 44);
		cards[44] = new Card(CardValue.SEVEN, Suit.CLUBS, 45);
		cards[45] = new Card(CardValue.EIGHT, Suit.CLUBS, 46);
		cards[46] = new Card(CardValue.NINE, Suit.CLUBS, 47);
		cards[47] = new Card(CardValue.TEN, Suit.CLUBS, 48);
		cards[48] = new Card(CardValue.JACK, Suit.CLUBS, 49);
		cards[49] = new Card(CardValue.QUEEN, Suit.CLUBS, 50);
		cards[50] = new Card(CardValue.KING, Suit.CLUBS, 51);
		cards[51] = new Card(CardValue.ACE, Suit.CLUBS, 52);

		return cards;
	}

}