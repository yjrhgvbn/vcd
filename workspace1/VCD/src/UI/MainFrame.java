package UI;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import UI.SongPanel;
import UI.ClientSetting;
import UI.DateRecive;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private JFrame mainJF;
	private JPanel areasJP;// 地区面板
	private JPanel songsJP;// 歌曲列表面板
	private JScrollPane scrollPane;// 歌曲滚动列表面板
	private JButton playButton = new JButton();
	private JButton stopButton = new JButton();
	private JLabel playSongJL = new JLabel("光年之外");
	@SuppressWarnings("rawtypes")
	private JList list;
	private DateRecive dateRecive;
	private SongPanel songDetailPanel;
	private String nowShowSongName = "光年之外";
	private String nowPlaySongName = "";
	boolean isPlay = false;
	ImageIcon playIcon = new ImageIcon(ClientSetting.ImgCachePath + "/play.jpg");
	ImageIcon pauseIcon = new ImageIcon(ClientSetting.ImgCachePath + "/pause.jpg");

	public MainFrame() {
		init();
		initEven();
		SetArea();
		dateRecive = new DateRecive();
	}

	public static void main(String[] agrs) throws InterruptedException {
		@SuppressWarnings("unused")
		MainFrame mainFrame = new MainFrame();
	}

	private void SetArea() {
		String[] areaNameStr = ClientSetting.areaNameStr_ch;
		for (int i = 0; i < areaNameStr.length; i++) {
			JLabel l = new JLabel(areaNameStr[i]);
			l.setBounds(i * 100, 10, 200, 50);
			l.setLocation(i * 100, 10);
			l.setFont(new Font(getTitle(), 30, 30));
			l.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for (int i = 0; i < areaNameStr.length; i++)
						areasJP.getComponent(i).setForeground(Color.black);
					ListUpdate(((JLabel) e.getComponent()).getText());
					l.setForeground(Color.red);
				}
			});
			areasJP.add(l);
		}
	}

	private synchronized void SongShow(String name) {// TODO
		songDetailPanel.ResetScreen();
		int whidth = songDetailPanel.getWidth();
		int height = songDetailPanel.getHeight();
		songDetailPanel.loadAndDrawImage(dateRecive.GetAndSaveImage(name), (int) (whidth * 0.5 - 350),
				(int) (height * 0.5 - 200));
		songDetailPanel.SetTitle(nowShowSongName, (int) (whidth * 0.5 + 50), 150);
		ArrayList<String> mes = dateRecive.GetMessage(name);
		songDetailPanel.SetAlbum(mes.get(0), (int) (whidth * 0.5 + 50), 250);
		songDetailPanel.SetArtist(mes.get(1), (int) (whidth * 0.5 + 50), 350);
		songDetailPanel.repaint();
	}

	@SuppressWarnings("unchecked")
	private void ListUpdate(String areaName) {
		String sendname = null;
		for (int i = 0; i < ClientSetting.areaNameStr_ch.length; i++) {
			if (areaName.equals(ClientSetting.areaNameStr_ch[i])) {
				sendname = ClientSetting.areaNameStr[i];
				break;
			}
		}
		List<String> listData = dateRecive.GetList(sendname);
		// System.out.println(listData);
		list.setListData(listData.toArray());
	}

	@SuppressWarnings("rawtypes")
	private void init() {
		mainJF = new JFrame("VCD");
		mainJF.setBounds(300, 100, 1000, 600);
		mainJF.setLayout(null);
		areasJP = new JPanel();
		songsJP = new JPanel();
		songDetailPanel = new SongPanel();
		areasJP.setBackground(Color.white);
		songsJP.setBackground(Color.white);
		songsJP.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		list = new JList();
		songsJP.setLayout(new BorderLayout(0, 0));
		setContentPane(songsJP);
		scrollPane = new JScrollPane();
		songsJP.add(scrollPane);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(list);
		songDetailPanel.setBackground(Color.lightGray);
		playButton.setIcon(pauseIcon);
		stopButton.setIcon(new ImageIcon(ClientSetting.ImgCachePath + "/stop.jpg"));
		mainJF.add(areasJP);
		mainJF.add(songsJP);
		mainJF.add(songDetailPanel);
		mainJF.add(playButton);
		mainJF.add(stopButton);
		mainJF.add(playSongJL);
		mainJF.setVisible(true);
	}

	private void initEven() {
		mainJF.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int whidth = mainJF.getWidth();
				int height = mainJF.getHeight();
				areasJP.setBounds(0, 0, whidth, 50);
				songsJP.setBounds(0, 50, 200, height - 50);
				songDetailPanel.setBounds(200, 50, whidth - 200, height - 150);
				songDetailPanel.SetScreenSize(whidth - 200, height - 150);
				scrollPane.setBounds(0, 0, 200, height - 60);
				playButton.setBounds(whidth - 180, height - 93, 50, 50);
				stopButton.setBounds(whidth - 100, height - 93, 50, 50);
				playSongJL.setBounds(230, height - 93, 300, 50);
				playSongJL.setFont(new Font("宋体", Font.ROMAN_BASELINE, 20));
				// ListUpdate(ClientSetting.areaNameStr_ch[0]);
				// SongShow();
			}
		});
		list.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				String name = list.getSelectedValue().toString();
				SongShow(list.getSelectedValue().toString());
				playButton.setIcon(playIcon);
				nowShowSongName = name;
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

		});

		mainJF.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// dateRecive.close();
				System.exit(0);
			}
		});
		playButton.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (nowShowSongName.equals(nowPlaySongName)) {
					if (isPlay) {
						isPlay = false;
						playButton.setIcon(playIcon);
						dateRecive.pause();
					} else {
						isPlay = true;
						playButton.setIcon(pauseIcon);
						dateRecive.play();
					}
				} else {
					nowPlaySongName = nowShowSongName;
					dateRecive.playNew(nowShowSongName);
					playSongJL.setText(nowPlaySongName);
					playButton.setIcon(pauseIcon);
					isPlay = true;
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		});
		stopButton.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				nowPlaySongName = "";
				playButton.setIcon(pauseIcon);
				dateRecive.stop();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
