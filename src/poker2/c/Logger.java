package poker2.c;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
	private FileWriter writer;

	public Logger(String loggerType)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH_mm_ss ");
		Date date = new Date(System.currentTimeMillis());
		File file = new File(formatter.format(date) + ".txt");
		try
		{
			this.writer = new FileWriter(file);
			this.write(loggerType + "");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> { close(); }));
	}

	public void write(String s)
	{
		try
		{
			this.writer.write(s + "\n");
			this.writer.flush();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		try
		{
			if (this.writer != null)
				this.writer.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void endLine()
	{
		this.write("\n");
		try
		{
			this.writer.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
