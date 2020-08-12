package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SongPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private Image screenImage = new BufferedImage(500, 500, 2);
	private Image emptyScreenImage = new BufferedImage(500, 500, 2);
	private Graphics2D screenGraphic = (Graphics2D) screenImage.getGraphics();
	private BufferedImage songImage;
	int toWidth = 300, toHeight = 300;
	int imgPosx = 0, imgPosy = 0;
	int titlePosX = 350, titlePosY = 100;
	int artistPosX = 350, artistPosY = 200;
	int albumPosX = 350, albumPosY = 300;
	int screenSizeX = 500, screenSizeY = 500;
	boolean isRepaint = false;

	public SongPanel() {
		setLayout(null);
		setFocusable(true);
		this.setVisible(true);

	}

	public void loadAndDrawImage(String path) {
		try {
			BufferedImage im = ImageIO.read(new FileInputStream(path));
			songImage = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);
			songImage.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0,
					0, null);
			screenGraphic.drawImage(songImage, imgPosx, imgPosy, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAndDrawImage(String path, int x, int y) {
		imgPosx = x;
		imgPosy = y;
		loadAndDrawImage(path);
	}

	public void InitImageSize(int width, int height) {
		toWidth = width;
		toHeight = height;
	}

	public void SetTitle(String title, int x, int y) {
		titlePosX = x;
		titlePosY = y;
		SetTitle(title);
	}

	public void SetTitle(String title) {
		screenGraphic.setColor(Color.black);
		screenGraphic.setFont(new Font("宋体", Font.ROMAN_BASELINE, 40));
		screenGraphic.drawString(title, titlePosX, titlePosY);
	}

	public void SetArtist(String artist, int x, int y) {
		artistPosX = x;
		artistPosY = y;
		SetArtist(artist);
	}

	public void SetArtist(String artist) {
		screenGraphic.setColor(Color.black);
		screenGraphic.setFont(new Font("宋体", Font.ROMAN_BASELINE, 25));
		screenGraphic.drawString("歌手:"+artist, artistPosX, artistPosY);
	}

	public void SetAlbum(String album, int x, int y) {
		albumPosX = x;
		albumPosY = y;
		SetAlbum(album);
	}

	public void SetAlbum(String album) {
		screenGraphic.setColor(Color.black);
		screenGraphic.setFont(new Font("宋体", Font.ROMAN_BASELINE, 25));
		screenGraphic.drawString("专辑:"+album, albumPosX, albumPosY);
	}

	public void SetScreenSize(int x, int y) {
		screenSizeX = x;
		screenSizeY = y;
		screenImage = new BufferedImage(x, y, 2);
		emptyScreenImage = new BufferedImage(x, y, 2);
	}

	public void ResetScreen() {
		screenImage = new BufferedImage(screenSizeX, screenSizeY, 2);
		screenGraphic = (Graphics2D) screenImage.getGraphics();
		isRepaint = true;
	}

	public void paint(Graphics g) {
		if (isRepaint) {
			g.drawImage(emptyScreenImage, 0, 0, null);
			isRepaint = false;
			g.drawImage(screenImage, 0, 0, null);
		}
	}
}
