package poker2;

import java.io.IOException;

import javafx.application.Application;

public class MainPoker
{
	public static final int JSON_PORT = 1850;

	public static void main(String[] args) throws IOException
	{

		Application.launch(MyApp.class, args);

	}
}