package Test;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import UI.SongPanel;

public class FrameTest extends Frame {
	SongPanel panel;
	public FrameTest() {
		//setLayout(null);
		panel = new SongPanel();
		this.add(panel);
		panel.setBounds(0,0,1000,1000);
		this.addWindowListener(new WindowAdapter() {
			// 设置关闭
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.pack();
		this.setVisible(true);
		this.setSize(1000, 1000);

	}

	public void init() {
		panel.loadAndDrawImage("C:\\Users\\WuJinPeng\\Desktop\\作业\\java\\cache\\img\\ぷりずむコミュニケート.jpg");
		panel.SetTitle("我阿萨德");
		panel.SetAlbum("bbbbbbbb");
		panel.SetArtist("ccccccc");
		panel.setBounds(0, 0, 800, 800);
		panel.repaint();

	}

	public void init2() {
		//panel.loadAndDrawImage("C:\\Users\\WuJinPeng\\Desktop\\作业\\java\\cache\\img\\MKAlieZ.jpg");
		panel.setBounds(0, 0, 300, 300);
		panel.repaint();

	}

	public static void main(String[] args) throws InterruptedException {
		FrameTest frame = new FrameTest();
		Thread.sleep(1000);
		frame.init();
	}

}