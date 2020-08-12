package Test;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import UI.ClientSetting;

public class test3
{
    public test3()
    {
        JFrame frame = new JFrame();
        frame.setLayout(null);
        JButton button = new JButton("confirm");
        JLabel j = new JLabel("asas");
        ImageIcon icon = new ImageIcon(ClientSetting.ImgCachePath+"/play.jpg");
        button.setIcon(icon);
        //button.setBounds(0, 0, 500, 500);
        j.setBounds(200, 200, 500, 500);
        j.setIcon(icon);
        frame.add(button);
        frame.add(j);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
    public static void main(String[] args)
    {
        new test3();
    }
}