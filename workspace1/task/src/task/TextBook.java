package task;

import java.awt.Font;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class TextBook extends Book {
	String id;
	String category;
	String pubDate;
	String price;
	
	private JFrame mainJF = new JFrame("教科书管理系统");
	private TextField bookName_tf = new TextField();
	private TextField bookID_tf = new TextField();
	private TextField author_tf = new TextField();
	private TextField pubDate_tf = new TextField();
	private TextField price_tf = new TextField();
	
	public TextBook()
	{
		mainJF.setSize(800, 500);
		mainJF.setVisible(true);
		mainJF.setLayout(null);
		AddLabel("书名", 0, 0);
		AddLabel("书号", 250, 0);
		AddLabel("作者", 500, 0);
		AddLabel("分类", 0, 100);
		AddLabel("出版日期", 250, 100);
		AddLabel("价格", 500, 100);
		
		AddTextField(bookName_tf,80,0);
		AddTextField(bookID_tf,330,0);
		AddTextField(author_tf,580,0);
		AddTextField(pubDate_tf,350,100);
		AddTextField(price_tf,580,100);
		
		Object [] choise = {"a","b","c"};
		
		mainJF.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// dateRecive.close();
				System.exit(0);
			}
		});
	}
	
	//添加Label
	private void AddLabel(String name, int x, int y)
	{
		JLabel label = new JLabel(name);
		mainJF.add(label);
		label.setFont(new Font("宋体", Font.ROMAN_BASELINE, 20));
		label.setBounds(x, y, 80, 30);
	}
	private void AddTextField(TextField textField,int x,int y)
	{
		mainJF.add(textField);
		textField.setBounds(x, y, 100, 30);
		ArrayList<String[]>  a = new ArrayList<String[]>();
		String b[] = null;
		a.toArray(b);
	}	
	
	public static void main(String[] agrs)
	{
		ArrayList<String[]>  a = new ArrayList<String[]>();
		a.add(new String[]{"a","b"});
		String b[][] = null;
		System.out.print(((Object[][])a.toArray())[0][0]);
	}
}
