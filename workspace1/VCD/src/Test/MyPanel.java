package Test;
 
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
 
 
 
public class MyPanel extends Panel{
	private final Image screenImage = new BufferedImage(500, 500, 2);
	private final Graphics2D screenGraphic = (Graphics2D) screenImage
	.getGraphics();
	private BufferedImage backgroundImage;
	
	
	public MyPanel(String path) {
		loadImage(path);
		// 设定焦点在本窗体
		setFocusable(true);
		// 设定初始构造时面板大小,这里先采用图片的大小
		setPreferredSize(new Dimension(500,500));
		// 绘制背景
		drawView();
	}
	
	/**
     * 载入图像
     */
    private void loadImage(String path) {
    	//获得当前类对应的相对位置image文件夹下的背景图像
        //ImageIcon icon = new ImageIcon(path);
        //将图像实例赋给backgroundImage
        //backgroundImage = (BufferedImage) icon.getImage();
        try {
			//backgroundImage = ImageIO.read(new FileInputStream(path));
			BufferedImage im = ImageIO.read(new FileInputStream(path));
			 
			/* 原始图像的宽度和高度 */
			int width = im.getWidth();
			int height = im.getHeight();
			
			//压缩计算
			float resizeTimes = 0.4f;  /*这个参数是要转化成的倍数,如果是1就是转化成1倍*/
			
			/* 调整后的图片的宽度和高度 */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);
 
			/* 新生成结果图片 */
			backgroundImage = new BufferedImage(toWidth, toHeight,
					BufferedImage.TYPE_INT_RGB);
 
			backgroundImage.getGraphics().drawImage(
					im.getScaledInstance(toWidth, toHeight,
							java.awt.Image.SCALE_SMOOTH), 0, 0, null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	private void drawView() {
		screenGraphic.drawImage(backgroundImage, 0, 0, null);
	}
    
	public void paint(Graphics g) {
		g.drawImage(screenImage, 0, 0, null);
	}
    
}