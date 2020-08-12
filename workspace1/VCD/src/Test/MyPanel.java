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
		// �趨�����ڱ�����
		setFocusable(true);
		// �趨��ʼ����ʱ����С,�����Ȳ���ͼƬ�Ĵ�С
		setPreferredSize(new Dimension(500,500));
		// ���Ʊ���
		drawView();
	}
	
	/**
     * ����ͼ��
     */
    private void loadImage(String path) {
    	//��õ�ǰ���Ӧ�����λ��image�ļ����µı���ͼ��
        //ImageIcon icon = new ImageIcon(path);
        //��ͼ��ʵ������backgroundImage
        //backgroundImage = (BufferedImage) icon.getImage();
        try {
			//backgroundImage = ImageIO.read(new FileInputStream(path));
			BufferedImage im = ImageIO.read(new FileInputStream(path));
			 
			/* ԭʼͼ��Ŀ�Ⱥ͸߶� */
			int width = im.getWidth();
			int height = im.getHeight();
			
			//ѹ������
			float resizeTimes = 0.4f;  /*���������Ҫת���ɵı���,�����1����ת����1��*/
			
			/* �������ͼƬ�Ŀ�Ⱥ͸߶� */
			int toWidth = (int) (width * resizeTimes);
			int toHeight = (int) (height * resizeTimes);
 
			/* �����ɽ��ͼƬ */
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