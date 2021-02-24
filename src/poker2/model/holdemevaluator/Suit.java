package poker2.model.holdemevaluator;

public enum Suit
{
	CLUBS(4), HEARTS(2), SPADES(1), DIAMONDS(3);

	private final int value;

	Suit(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
}