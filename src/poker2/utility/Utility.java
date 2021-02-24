package poker2.utility;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Utility
{

	public static Image setImage(String path, String message)
	{
		File file = new File(path);
		try
		{
			if (!file.exists())
			{
				throw new Exception(message);
			}
			return new Image(file.toURI().toURL().toString());
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	public static Background setBackground(final String TABLE_PICTURE_PATH)
	{
		Image img = Utility.setImage(TABLE_PICTURE_PATH,
				"Background image not found in class view.PokerTable - setScene(Stage stage)");
		BackgroundImage bgImg = new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
		return new Background(bgImg);
	}

	public static ImageView setImageView(Image image, Point position, Point size)
	{
		ImageView iv = new ImageView(image);
		iv.setX(position.x);
		iv.setY(position.y);
		iv.setFitHeight(size.y);
		iv.setFitWidth(size.x);

		return iv;
	}

	public static ImageView setImageView(String path, String message, Point offset, Point size)
	{
		return Utility.setImageView(Utility.setImage(path, message), offset, size);
	}
}
