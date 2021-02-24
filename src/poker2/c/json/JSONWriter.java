package poker2.c.json;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class JSONWriter
{
	private BufferedWriter bw;

	public JSONWriter(Socket s) throws IOException
	{
		this.bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	}

	public void close() throws IOException
	{
		if (this.bw != null)
			this.bw.close();
	}

	public void sendMessage(String m)
	{
		try
		{
			this.bw.write(m);
			this.bw.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
