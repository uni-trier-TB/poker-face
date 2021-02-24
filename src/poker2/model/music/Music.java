package poker2.model.music;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Music
{
	private MediaPlayer backgroundMusic;
	private MediaPlayer sound;

	private final static String BACKGROUND_PATH = ".\\res\\music\\casino.mp3";
	private final static String JETON_PATH = ".\\res\\music\\chip.mp3";
	private final static String FLIP_PATH = ".\\res\\music\\flip.mp3";
	private final static String ERROR_PATH = ".\\res\\music\\error.mp3";
	private final static String LEGENDARY_PATH = ".\\res\\music\\legendary.mp3";
	private final static String DARTH_PATH = ".\\res\\music\\darth.mp3";
	private final static String GANDALF_PATH = ".\\res\\music\\gandalf.mp3";
	private final static String SEVEN_PATH = ".\\res\\music\\seven.wav";
	private final static String SNAPE_PATH = ".\\res\\music\\snape.mp3";
	private final static String WIN_PATH = ".\\res\\music\\tada.mp3";
	private final static String WASTED_PATH = ".\\res\\music\\wasted.wav";

	public Music()
	{
		this.initBackgroundMusic();
	}

	private void initBackgroundMusic()
	{
		Media hit = new Media(new File(BACKGROUND_PATH).toURI().toString());
		this.backgroundMusic = new MediaPlayer(hit);
		this.backgroundMusic.setVolume(0.15);
		Runnable task = () -> { backgroundMusic.seek(Duration.ZERO); };
		this.backgroundMusic.setOnEndOfMedia(task);
		// backgroundMusic.play();
	}

	public void playFlipSound(int delay)
	{
		this.playSound(FLIP_PATH, delay);
	}

	public void playJetonSound()
	{
		this.playSound(JETON_PATH, 900);
	}

	public void playErrorSound()
	{
		this.playSound(ERROR_PATH, 0);
	}

	private void playSound(String path, int delay)
	{
		Media hit = new Media(new File(path).toURI().toString());
		sound = new MediaPlayer(hit);
		sound.setVolume(0.2);
		Runnable task = () -> {
			try
			{
				Thread.sleep(delay);
				sound.play();
			} catch (InterruptedException e)
			{}
		};
		new Thread(task).start();
	}

	public void playWinSound()
	{
		this.playSound(WIN_PATH, 0);
	}

	public void playSetHumanSound(int playerNum)
	{
		if (playerNum == 2)
		{
			this.playSound(LEGENDARY_PATH, 0);
		}
		else if (playerNum == 3)
		{
			this.playSound(DARTH_PATH, 0);
		}
		else if (playerNum == 4)
		{
			this.playSound(GANDALF_PATH, 0);
		}
		else if (playerNum == 5)
		{
			this.playSound(SEVEN_PATH, 0);
		}
		else if (playerNum == 6)
		{
			this.playSound(SNAPE_PATH, 0);
		}

	}

	public void playWastedSound()
	{
		this.playSound(WASTED_PATH, 0);
	}
}
