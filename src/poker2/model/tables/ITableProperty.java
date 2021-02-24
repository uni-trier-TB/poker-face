package poker2.model.tables;

import poker2.utility.Point;
import poker2.utility.Point3D;

public interface ITableProperty
{

	public abstract Point[] getPlayersPosition();

	public abstract Point getTableSize();

	public abstract Point3D[][] getCardPositions();

	public abstract Point[] getCommunityPosition();

	public abstract Point getPotPosition();

	public abstract Point getStackPos();

	public abstract Point getBetLabelPos();

}
