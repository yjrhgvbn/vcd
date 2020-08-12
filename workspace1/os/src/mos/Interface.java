/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mos;

import static java.awt.Color.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static java.lang.System.exit;
import javax.swing.*;

import java.util.Timer;
import java.util.TimerTask;
import mos.Schedule;
import mos.Schedule.Rule;
import mos.Action;

@SuppressWarnings("serial")
public class Interface extends JFrame implements ActionListener, ItemListener {

	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	JTextField text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11;
	JButton button1, button2, button3, button4, button5, button6, button7, button8, button9, jbutton1, jbutton2;
	JLabel label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12,
			label13, label14, label15, lable2_1, lable2_2;
	JPanel panel1, panel2;
	JTabbedPane tabbedpane;

	JRadioButton radiob1, radiob2, radiob3, runrad1, runrad2, runrad3;
	JTable c, g, c1, jt1, jt2, createTable;
	JLabel timer, mean1, mean2, memory, printer;
	String waitValue[][], createValue[][], runValue[][], readyValue[][], finshValue[][], enterValue[][];
	Schedule schedule = new Schedule(Rule.SJFP);
	Action action = new Action();
	int rowSize = 6, columnSize = 50;
	String title[] = { "进程名称", "到达时间", "服务时间", "内存请求大小", "打印机请求数量", "等待时间" };
	boolean isRun = false, isStart = false;
	int runType = 0;
	int time = 0;

	public Interface() {

		setSize(1000, 900);
		setLocation((width - 1000) / 2, (height - 900) / 2);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		panel1 = new JPanel();// 进程创建
		panel1.setLayout(null);
		panel2 = new JPanel();// 结果预览
		tabbedpane = new JTabbedPane();
		tabbedpane.addTab("进程创建", null, panel1, "进程创建");
		tabbedpane.addTab("结果预览", null, panel2, "结果预览");
		tabbedpane.setFont(new Font("黑体", Font.BOLD, 20));
		add(tabbedpane);
		
		text1 = new JTextField("50");
		text1.setBounds(100, 50, 100, 30);
		panel1.add(text1);
		text2 = new JTextField("50");
		text2.setBounds(450, 50, 100, 30);
		panel1.add(text2);
		text3 = new JTextField();// name
		text3.setBounds(60, 140, 100, 30);
		panel1.add(text3);
		text4 = new JTextField();// printer
		text4.setBounds(190, 140, 100, 30);
		panel1.add(text4);
		text5 = new JTextField();// needTime
		text5.setBounds(320, 140, 100, 30);
		panel1.add(text5);
		text6 = new JTextField();// priority
		text6.setBounds(450, 140, 100, 30);
		panel1.add(text6);
		text7 = new JTextField();// memoryNeed
		text7.setBounds(590, 140, 100, 30);
		panel1.add(text7);

		Font font1 = new Font("宋体", Font.BOLD, 18);
		Font font2 = new Font("黑体", Font.BOLD, 16);
		label0 = new JLabel("初始设置");
		label0.setFont(font2);
		label0.setForeground(gray);
		label0.setBounds(20, 20, 100, 30);
		panel1.add(label0);
		label1 = new JLabel("内存");
		label1.setFont(font1);
		label1.setBounds(50, 50, 50, 30);
		panel1.add(label1);
		label2 = new JLabel("MB");
		label2.setFont(font1);
		panel1.add(label2);
		label2.setBounds(210, 50, 50, 30);
		JLabel memoryTitle = new JLabel("剩余:");
		memoryTitle.setFont(font1);
		memoryTitle.setBounds(260, 50, 50, 30);
		panel1.add(memoryTitle);
		memory = new JLabel("50");
		memory.setFont(font1);
		memory.setBounds(310, 50, 50, 30);
		panel1.add(memory);
		label3 = new JLabel("打印机");
		label3.setFont(font1);
		label3.setBounds(380, 50, 60, 30);
		panel1.add(label3);
		label4 = new JLabel("台");
		label4.setFont(font1);
		label4.setBounds(560, 50, 50, 30);
		panel1.add(label4);
		JLabel printerTitle = new JLabel("剩余:");
		printerTitle.setFont(font1);
		printerTitle.setBounds(600, 50, 50, 30);
		panel1.add(printerTitle);
		printer = new JLabel("50");
		printer.setFont(font1);
		printer.setBounds(650, 50, 50, 30);
		panel1.add(printer);
		label5 = new JLabel("新建进程");
		label5.setFont(font2);
		label5.setForeground(gray);
		label5.setBounds(20, 80, 100, 30);
		panel1.add(label5);
		label6 = new JLabel("进程名称");
		label6.setFont(font1);
		label6.setBounds(60, 110, 100, 30);
		panel1.add(label6);
		label7 = new JLabel("打印机请求");
		label7.setFont(font1);
		label7.setBounds(190, 110, 100, 30);
		panel1.add(label7);
		label8 = new JLabel("服务时间");
		label8.setFont(font1);
		label8.setBounds(320, 110, 100, 30);
		panel1.add(label8);
		label9 = new JLabel("优先级");
		label9.setFont(font1);
		label9.setBounds(450, 110, 120, 30);
		panel1.add(label9);
		label10 = new JLabel("内存请求大小");
		label10.setFont(font1);
		label10.setBounds(590, 110, 120, 30);
		panel1.add(label10);
		label11 = new JLabel("请选择模拟方法：");
		label11.setFont(new Font("宋体", Font.BOLD, 21));
		label11.setBounds(60, 190, 200, 30);
		panel1.add(label11);
		label12 = new JLabel("当前占用CPU的进程");
		label12.setFont(font2);
		label12.setForeground(gray);
		label12.setBounds(20, 230, 200, 30);
		panel1.add(label12);
		label13 = new JLabel("就绪队列");
		label13.setFont(font2);
		label13.setForeground(gray);
		label13.setBounds(20, 440, 200, 30);
		panel1.add(label13);
		label14 = new JLabel("等待队列");
		label14.setFont(font2);
		label14.setForeground(gray);
		label14.setBounds(20, 600, 200, 30);
		panel1.add(label14);

		radiob1 = new JRadioButton("先来先服务调度算法");
		radiob1.setBounds(240, 195, 150, 20);
		panel1.add(radiob1);
		radiob2 = new JRadioButton("非抢占式短进程优先级调度算法");
		radiob2.setBounds(400, 195, 200, 20);
		panel1.add(radiob2);
		radiob3 = new JRadioButton("抢占式短进程优先级调度算法");
		radiob3.setBounds(600, 195, 200, 20);
		panel1.add(radiob3);
		radiob1.addItemListener(this);
		radiob2.addItemListener(this);
		radiob3.addItemListener(this);
		// 单选
		ButtonGroup group = new ButtonGroup();
		group.add(radiob1);
		group.add(radiob2);
		group.add(radiob3);
		radiob1.setSelected(true);

		button1 = new JButton("初始设置");
		button1.setBounds(730, 50, 120, 30);
		panel1.add(button1);
		button2 = new JButton("新建进程");
		button2.setBounds(800, 125, 120, 40);
		panel1.add(button2);
		button3 = new JButton("开始模拟");
		button3.setBounds(800, 185, 120, 40);
		panel1.add(button3);
		button4 = new JButton("阻塞");
		button4.setBounds(840, 260, 90, 40);
		panel1.add(button4);
		button7 = new JButton("唤醒");
		button7.setBounds(840, 660, 90, 40);
		panel1.add(button7);
		button8 = new JButton("退出");
		button8.setBounds(840, 450, 90, 40);
		panel1.add(button8);
		button9 = new JButton("重置");
		button9.setBounds(840, 390, 90, 40);
		panel1.add(button9);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		// button5.addActionListener(this);
		// button6.addActionListener(this);
		button7.addActionListener(this);
		button8.addActionListener(this);
		button9.addActionListener(this);

		readyValue = new String[columnSize][rowSize];
		c = new JTable(readyValue, title);
		JScrollPane d = new JScrollPane(c);
		d.setBounds(20, 470, 800, 130);
		d.setPreferredSize(new Dimension(200, 200));
		panel1.add(d);
		waitValue = new String[columnSize][rowSize];
		g = new JTable(waitValue, title);
		JScrollPane h = new JScrollPane(g);
		h.setBounds(20, 640, 800, 150);
		h.setPreferredSize(new Dimension(200, 200));
		panel1.add(h);
		String runTitle[] = { "进程名称", "到达时间", "服务时间", "内存请求大小", "打印机请求数量", "还需执行时间" };
		runValue = new String[2][rowSize];
		c1 = new JTable(runValue, runTitle);
		JScrollPane d1 = new JScrollPane(c1);
		d1.setBounds(20, 260, 800, 40);
		d1.setPreferredSize(new Dimension(200, 200));
		panel1.add(d1);

		// 创建队列
		label13 = new JLabel("创建队列");
		label13.setFont(font2);
		label13.setForeground(gray);
		label13.setBounds(20, 300, 200, 30);
		panel1.add(label13);
		createValue = new String[columnSize][rowSize];
		createTable = new JTable(createValue, title);
		JScrollPane createSP = new JScrollPane(createTable);
		createSP.setBounds(20, 330, 800, 100);
		createSP.setPreferredSize(new Dimension(200, 200));
		panel1.add(createSP);

		// 结果预览
		Font f = new Font("黑体", Font.BOLD, 22);
		lable2_1 = new JLabel("输入进程队列情况：");
		lable2_1.setFont(f);
		lable2_1.setBounds(200, 50, 80, 80);
		panel2.add(lable2_1);

		enterValue = new String[columnSize][rowSize];
		jt1 = new JTable(enterValue, title);
		JScrollPane jsp1 = new JScrollPane(jt1);
		jsp1.setPreferredSize(new Dimension(950, 200));
		panel2.add(jsp1);

		lable2_2 = new JLabel("执行后进程队列的情况：");
		lable2_2.setFont(f);
		lable2_2.setBounds(400, 50, 80, 80);
		panel2.add(lable2_2);

		String finshTitle[] = { "进程名称", "到达时间", "服务时间", "开始服务时间", "完成时间", "周转时间", "带权周转时间" };
		finshValue = new String[columnSize][7];
		jt2 = new JTable(finshValue, finshTitle);
		JScrollPane jsp2 = new JScrollPane(jt2);
		jsp2.setPreferredSize(new Dimension(950, 200));
		panel2.add(jsp2);
		JLabel meanTitle = new JLabel("平均周转时间：");
		meanTitle.setFont(f);
		panel2.add(meanTitle);
		mean1 = new JLabel("0");
		mean1.setFont(f);
		panel2.add(mean1);
		JLabel mean2Title = new JLabel("平均带权周转时间：");
		mean2Title.setFont(f);
		panel2.add(mean2Title);
		mean2 = new JLabel("0");
		mean2.setFont(f);
		panel2.add(mean2);

		// 改不了
		jbutton1 = new JButton("重置");
		jbutton1.setFont(f);
		jbutton1.setPreferredSize(new Dimension(80, 50));
		panel2.add(jbutton1);
		jbutton2 = new JButton("退出");
		jbutton2.setFont(f);
		jbutton2.setPreferredSize(new Dimension(80, 50));
		panel2.add(jbutton2);
		jbutton1.addActionListener(this);
		jbutton2.addActionListener(this);

		runrad1 = new JRadioButton("自动");
		runrad1.setBounds(240, 220, 150, 20);
		panel1.add(runrad1);
		runrad2 = new JRadioButton("长按");
		runrad2.setBounds(400, 220, 200, 20);
		panel1.add(runrad2);
		runrad3 = new JRadioButton("长按独立");
		runrad3.setBounds(600, 220, 200, 20);
		panel1.add(runrad3);
		runrad1.addItemListener(this);
		runrad2.addItemListener(this);
		runrad3.addItemListener(this);
		// 单选
		ButtonGroup group2 = new ButtonGroup();
		group2.add(runrad1);
		group2.add(runrad2);
		group2.add(runrad3);
		initText();
		runrad1.setSelected(true);

		ExtendsCom();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// TODO code application logic here
		Interface in = new Interface();
		in.RunTimer();
	}

	private void ExtendsCom() {
		JLabel timerTitle = new JLabel("时间");
		panel1.add(timerTitle);
		timerTitle.setBounds(730, 110, 120, 30);
		timerTitle.setFont(new Font("宋体", Font.BOLD, 18));
		timer = new JLabel("0");
		panel1.add(timer);
		timer.setBounds(735, 140, 120, 30);
		timer.setFont(new Font("宋体", Font.BOLD, 18));

		button3.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				isStart = true;
				if (runType == 0)
					isRun = true;
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (runType == 2 || runType == 1) {
					isStart = true;
					isRun = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (runType == 2 || runType == 1)
					isRun = false;
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	}

	public void RunTimer() {
		new Timer().schedule(new TimerTask() {// 定时任务
			@Override
			public void run() {
				if (!isStart)
					return;
				if (runType == 0 || runType == 2 || (runType == 1 && isRun)) {
					time = time + 1;
					timer.setText(String.valueOf(time));
					runSchedule();
				}
			}
		}, 1000, 800);
	}

	private void runSchedule() {
		if (isRun == true) {
			schedule.run();
			updateShow();
		}
	}

	// 直接全部更新
	private void updateShow() {
		action.copyArray(waitValue, schedule.wait.getTable());
		action.copyArray(createValue, schedule.create.getTable());
		action.copyArray(runValue, schedule.run.getTable());
		action.copyArray(readyValue, schedule.ready.getTable());
		action.copyArray(finshValue, schedule.finish.getTable());
		action.copyArray(enterValue, schedule.enter.getTable());
		memory.setText(String.valueOf(schedule.memoryLeft));
		printer.setText(String.valueOf(schedule.printerLeft));
		mean1.setText(action.avg(finshValue, finshValue[0].length - 2));
		mean2.setText(action.avg(finshValue, finshValue[0].length - 1));
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				c.updateUI();
				g.updateUI();
				c1.updateUI();
				jt1.updateUI();
				jt2.updateUI();
				createTable.updateUI();
			}
		});
		// 直接用会报空指针异常，不过没影响
		// c.updateUI();g.updateUI();c1.updateUI();jt1.updateUI();jt2.updateUI();createTable.updateUI();
	}

	private void initText() {
		text3.setText(action.getName());
		text5.setText(String.valueOf(action.RandomInt(10)));
		text7.setText(String.valueOf(action.RandomInt(10)));
		text4.setText(String.valueOf(action.RandomInt(10)));
		text6.setText(String.valueOf(action.RandomInt(10)));
	}

	private void createNew() {
		String name, t;
		int arrive_time, need_time, memoryNeed, printerNeed;
		name = text3.getText();
		if (name.equals("")) {
			name = action.getName();
		}
		action.SetName(name);
		text3.setText(action.getName());
		arrive_time = Integer.parseInt(timer.getText());
		// need_time
		t = text5.getText();
		if (t.equals("")) {
			need_time = action.RandomInt(10);
		} else {
			need_time = Integer.parseInt(t);
		}
		text5.setText(String.valueOf(action.RandomInt(10)));
		// memory
		t = text7.getText();
		if (t.equals("")) {
			memoryNeed = action.RandomInt(10);
		} else {
			memoryNeed = Integer.parseInt(t);
		}
		text7.setText(String.valueOf(action.RandomInt(10)));
		// printer
		t = text4.getText();
		if (t.equals("")) {
			printerNeed = action.RandomInt(10);
		} else {
			printerNeed = Integer.parseInt(t);
		}
		text4.setText(String.valueOf(action.RandomInt(10)));
		text6.setText(String.valueOf(action.RandomInt(10)));
		schedule.add(name, arrive_time, need_time, memoryNeed, printerNeed);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("退出")) {
			exit(0);
		} else if (e.getActionCommand().equals("重置")) {
			schedule.rest();
			action.initArray(waitValue);
			action.initArray(createValue);
			action.initArray(runValue);
			action.initArray(readyValue);
			action.initArray(finshValue);
			action.initArray(enterValue);
			action.init();
			initText();
			time = 0;
			isRun = false;
			isStart = false;
			timer.setText("0");
			updateShow();
		} else if (e.getActionCommand().equals("初始设置")) {
			int m = Integer.parseInt(text1.getText());
			int p = Integer.parseInt(text2.getText());
			schedule.initResource(m, p);
			text1.setText(String.valueOf(schedule.memoryMax));
			text2.setText(String.valueOf(schedule.printerMax));
			updateShow();
		} else if (e.getActionCommand().equals("新建进程")) {
			createNew();
			updateShow();
		} else if (e.getActionCommand().equals("开始模拟")) {
		} else if (e.getActionCommand().equals("阻塞")) {
			schedule.block();
		} else if (e.getActionCommand().equals("就绪")) {

		} else if (e.getActionCommand().equals("唤醒")) {
			schedule.awake();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub

		if (radiob1.isSelected()) {
			schedule.setRule(Rule.FCFS);
		}
		if (radiob2.isSelected()) {
			schedule.setRule(Rule.SJF);
			System.out.print("a");
		}
		if (radiob3.isSelected()) {
			schedule.setRule(Rule.SJFP);
		}
		if (e.getSource() == runrad1) {
			runType = 0;
			isStart = false;
		} else if (e.getSource() == runrad2) {
			runType = 1;
			isRun = false;
		} else if (e.getSource() == runrad3) {
			runType = 2;
			isRun = false;
		}
		/*
		 * 
		 * if(runrad2.isSelected()) { System.out.print("b"); } if(runrad3.isSelected())
		 * { }
		 */
	}
}
