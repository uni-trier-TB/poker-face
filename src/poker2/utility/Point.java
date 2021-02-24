package poker2.utility;

public class Point
{
	public int x;
	public int y;

	public Point(double x, double y)
	{
		this((int) x, (int) y);
	}

	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public String toString()
	{
		return x + " " + " y: " + y;
	}
}
