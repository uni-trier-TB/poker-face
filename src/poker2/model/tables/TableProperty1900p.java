package poker2.model.tables;

import poker2.model.PokerModel;
import poker2.model.holdemevaluator.Card;
import poker2.model.player.PokerPlayer;
import poker2.utility.Point;
import poker2.utility.Point3D;

public class TableProperty1900p implements ITableProperty
{

	private final int TABLE_WIDTH = 1900;
	private final int TABLE_HEIGHT = 1000;
	private final int CARD_WIDTH = 90;
	private final int CARD_HEIGHT = 120;
	private final int PLAYER_SIZE = 200;
	private final int STACK_POSITION_X = 920;
	private final int STACK_POSITION_Y = 180;

	public TableProperty1900p()
	{
		Card.CARD_SIZE = new Point(this.CARD_WIDTH, this.CARD_HEIGHT);
		PokerPlayer.setPlayerSize(new Point(this.PLAYER_SIZE, this.PLAYER_SIZE));
	}

	public Point[] getPlayersPosition()
	{
		Point[] playersOffset = new Point[PokerModel.MAX_PLAYERS];
		playersOffset[0] = new Point(1550, 26);
		playersOffset[1] = new Point(1600, 720);
		playersOffset[2] = new Point(1200, 750);
		playersOffset[3] = new Point(500, 750);
		playersOffset[4] = new Point(60, 700);
		playersOffset[5] = new Point(180, 26);

		return playersOffset;
	}

	@Override
	public Point getTableSize()
	{
		return new Point(this.TABLE_WIDTH, this.TABLE_HEIGHT);
	}

	public Point3D[][] getCardPositions()
	{
		Point3D[][] pos = new Point3D[6][2];
		pos[0][0] = new Point3D(1500, 225, 45);
		pos[0][1] = new Point3D(1435, 160, 45);
		pos[1][0] = new Point3D(1560, 610, 135);
		pos[1][1] = new Point3D(1625, 545, 135);
		pos[2][0] = new Point3D(1220, 620, 0);
		pos[2][1] = new Point3D(1315, 620, 0);
		pos[3][0] = new Point3D(525, 620, 0);
		pos[3][1] = new Point3D(620, 620, 0);
		pos[4][0] = new Point3D(340, 600, 45);
		pos[4][1] = new Point3D(275, 535, 45);
		pos[5][0] = new Point3D(350, 225, 135);
		pos[5][1] = new Point3D(410, 165, 135);
		return pos;
	}

	@Override
	public Point[] getCommunityPosition()
	{
		Point[] deck = new Point[5];
		deck[0] = new Point(650, 350);
		deck[1] = new Point(750, 350);
		deck[2] = new Point(850, 350);
		deck[3] = new Point(950, 350);
		deck[4] = new Point(1050, 350);

		return deck;
	}

	@Override
	public Point getPotPosition()
	{
		return new Point(1200, 350);
	}

	@Override
	public Point getStackPos()
	{
		return new Point(STACK_POSITION_X, STACK_POSITION_Y);
	}

	@Override
	public Point getBetLabelPos()
	{
		return new Point(730, 800);
	}

}
