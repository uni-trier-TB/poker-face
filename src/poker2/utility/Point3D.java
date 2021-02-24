package poker2.utility;

public class Point3D extends Point
{

	public double r;

	public Point3D(double x, double y)
	{
		super(x, y);
		// TODO Auto-generated constructor stub
	}

	public Point3D(double x, double y, double r)
	{
		this(x, y);
		this.r = r;
	}

	public Point3D(Point p, double r)
	{
		this(p.x, p.x, r);
	}
}
