package poker2.c.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import javafx.application.Platform;
import poker2.c.Logger;
import poker2.model.PokerModel;
import poker2.model.player.PokerPlayer;
import poker2.model.state.PokerState;
import poker2.model.state.PreFlop;
import poker2.model.state.community.CommunityState;
import poker2.model.state.community.Flop;
import poker2.model.state.community.River;
import poker2.model.state.community.Turn;

public class JSONReader extends Thread
{
	/**
	 * success value of reply
	 */
	private boolean success;
	/**
	 * reply message
	 */
	private String replyM = "";
	/**
	 * reads string data from socket
	 */
	private BufferedReader br;
	/**
	 * writes reply message back to player client
	 */
	private JSONWriter jw;
	/**
	 * game model
	 */
	private PokerModel model;
	private Logger log = new Logger("JSON-LOG\n");

	private final static String SUC_STRING = "{\"status\" : \"Success\" , \"message\" : ";
	private final static String FALS_STRING = "{\"status\" : \"Error\" , \"message\" : ";

	private final static int JSON_PORT = 1850;

	public JSONReader(PokerModel model)
	{
		this.model = model;
		this.start();
	}

	public void run()
	{
		System.out.println("JSON start");
		// awaits socket connection
		try (ServerSocket ss = new ServerSocket(JSON_PORT))
		{
			while (true)
			{
				try
				{
					System.out.println("Wait for Connection");
					Socket s = ss.accept();
					System.out.println("JSON new Connection");
					this.init(s);
					while (!s.isClosed())
					{
						// new incoming string
						String message = this.br.readLine();
						if (message != null)
						{
							try
							{
								this.log.write(message);
								// create a JSONObject object to work with string
								JSONObject json = new JSONObject(message);
								String method = (String) json.get("method");
								method = method.toLowerCase();
								String name = (String) json.get("name");
								String action = (String) json.get("action");

								this.execute(method, name, action);
							} catch (JSONException e)
							{
								System.err.println("JSON-Exception. Wrong format sent");
								jw.sendMessage(
										"\"status\" : \"Error\" , \"message\" : \"JSON-Exception - wrong format?\"}\n");
								e.printStackTrace();
							}
						}
						else
							break;
					}
				} catch (IOException e)
				{
					System.err.println("Connection was closed or disconnected");
					e.printStackTrace();
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * inits a BufferedReader and BufferedWriter Object for every incoming
	 * connection
	 * 
	 * @param s socket which provides streams for Reader and Writer
	 * @throws IOException
	 */
	private void init(Socket s) throws IOException
	{

		if (this.br != null)
			try
			{
				this.br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}

		if (this.jw != null)
			try
			{
				this.jw.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		this.br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		this.jw = new JSONWriter(s);
	}

	/**
	 * execute method processes the incoming JSON-string
	 * 
	 * @param method
	 * @param name
	 * @param action
	 */
	public void execute(String method, String name, String action)
	{
		PokerState state = this.model.getState();
		Runnable task = null;
		method = method.toLowerCase();
		if (method.equals("addplayer"))
		{
			task = () -> {
				success = state.addPlayer(name);
				replyM = success ? SUC_STRING + "\"Player added\"}\n"
						: FALS_STRING + "\"Table full or player already on table\"}\n";
				jw.sendMessage(replyM);
			};

		}
		else if (method.equals("update"))
		{
			task = () -> {
				model.getView().update(model);
				replyM = SUC_STRING + "\"updated View\"}\n";
				jw.sendMessage(replyM);
			};
		}
		else if (method.equals("start"))
		{
			task = () -> {
				success = state.start();
				replyM = success ? SUC_STRING + "\"Game started\"}\n"
						: FALS_STRING + "\"Wrong state or not enough players\"}\n";
				jw.sendMessage(replyM);
			};
		}
		else if (method.equals("removeplayer"))
		{
			task = () -> {
				success = state.removePlayer(name);
				replyM = success ? SUC_STRING + "\"Player removed\"}\n"
						: FALS_STRING + "\"No player with that name\"}\n";
				jw.sendMessage(replyM);
			};
		}
		else if (method.equals("call"))
		{
			task = () -> {
				success = state.bet(name, action, 0);
				replyM = success ? SUC_STRING + "\"bet successful\"}\n"
						: FALS_STRING + "\"couldn't bet. Wrong player or action?\"}\n";
				jw.sendMessage(replyM);
			};
		}
		else if (method.equals("info"))
		{
			PokerPlayer player = this.model.getPlayerByName(name);
			if (player == null)
			{
				jw.sendMessage(
						"\"status\" : \"Error\" , \"message\" : \"No player with such name or wrong state\" } \n");
			}
			else
			{
				StringBuffer sb = new StringBuffer("{ \"status\" : \"Success\" , \"name\" : ");
				sb.append("\"");
				sb.append(player.getName());
				sb.append("\" , ");
				sb.append("\"money\" : ");
				sb.append("\"");
				sb.append(player.getMoney());
				sb.append("\" , ");
				sb.append("\"Card1\" : ");
				sb.append("\"");
				sb.append(player.getCards()[0]);
				sb.append("\" , ");
				sb.append("\"Card2\" : ");
				sb.append("\"");
				sb.append(player.getCards()[1]);
				sb.append("\" }");
				sb.append("\n");
				jw.sendMessage(sb.toString());
			}
			return;
		}
		else if (method.equals(("status")))
		{
			jw.sendMessage(SUC_STRING + "\"" + this.model.getState().toString() + "\" }\n");
			return;
		}
		else if (method.equals(("current")))
		{
			String curPlayerName = this.model.getCurrentPlayer().getName();
			if (curPlayerName != null)
				jw.sendMessage(SUC_STRING + "\"" + curPlayerName + "\" }\n");
			else
				jw.sendMessage(FALS_STRING + "\"No current player yet \" }\n");
			return;
		}
		else if (method.equals(("actions")))
		{
			String messageContent = "fold";
			PokerPlayer currentPlayer = this.model.getCurrentPlayer();
			if(!currentPlayer.isAllin()){
				
				PokerState currentState = this.model.getState();
				if(currentState instanceof PreFlop) {
					PreFlop PFstate = (PreFlop) currentState;
					
					if(PFstate.isCheckPossible()) messageContent = messageContent.concat(",check");
					else messageContent = messageContent.concat(",call");
				
					if(PFstate.isRaisePossible()) messageContent = messageContent.concat(",raise");
				}
				else {
					if(currentState instanceof CommunityState) 
					{
						CommunityState CState = (CommunityState) currentState;
						if(CState.isCheckPossible()) messageContent = messageContent.concat(",check,bet");
						else messageContent = messageContent.concat(",call");
						
						if(CState.isRaisePossible() && CState.getCurMaxBet() > 0) {
							messageContent = messageContent.concat(",raise");
						}
					}
					
				}
			} else {
				messageContent = "none";
			}
			jw.sendMessage(SUC_STRING + "\"" + messageContent + "\" }\n");
			return;
		} else if (method.equals("money")) {
			PokerPlayer p = this.model.getPlayerByName(name);
			String money = "0";
			if (p != null)
				money = "" + p.getMoney();

			jw.sendMessage(SUC_STRING + "\"" + money + "\" }\n");
			return;
		} else if (method.equals("cards")) {
			JSONObject obj = new JSONObject();
			JSONArray cArr = new JSONArray();
			int max = 0;

			if (state instanceof Flop)
				max = 3;
			else if (state instanceof Turn)
				max = 4;
			else if (state instanceof River)
				max = 5;
			for (int i = 0; i < 5; i++) {
				cArr.put(i < max ? this.model.getCommunityCards()[i].getCardNumber() : 0);
			}
			obj.put("communitycards", cArr);

			PokerPlayer p = this.model.getPlayerByName(name);
			if (p != null) {
				JSONArray myArr = new JSONArray();
				myArr.put(p.getCards()[0].getCardNumber());
				myArr.put(p.getCards()[1].getCardNumber());
				obj.put("mycards", myArr);
			}
			JSONObject out = new JSONObject();
			out.put("status", "Success");
			out.put("message", obj.toString());
			jw.sendMessage(out.toString() + "\n");
			return;
		}
		Platform.runLater(task);
	}
}
