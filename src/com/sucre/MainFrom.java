package com.sucre;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.sucre.listUtil.MutiList;
import com.sucre.mainUtil.MyUtil;
import com.sucre.myThread.Thread4Net;

/**
 * 建立一个winform以方便操作及显示最新反馈.
 * 
 * @author sucre
 * @version 0.01
 */
public class MainFrom extends JFrame implements ActionListener {

	

	// 创建一个button,并设置标题
	JButton loadin = new JButton("导入id");
	JButton loadVid = new JButton("导入vid");
	JButton addVid = new JButton("加入vid");
	JButton loadCookie = new JButton("导入cookie");
	JButton login = new JButton("登录");
	JButton vote = new JButton("开始");

	// 创建一个textbox
	JTextField feedBack = new JTextField(2);
	JTextField fileName = new JTextField(3);
	JTextField tNum = new JTextField(4);

	// 创建一个label
	JLabel messageLabel = new JLabel();

	// 定义帮忙信息,jlabel 支持html内容
	String help = "<html> 1,在文本框输入d1,回车,等于删第一个vid.<br></html> ";
	
    //定义一个自己.
	static MainFrom f= new MainFrom("统一窗体");
	// 用构造器新建一个JFrame,并设置标题.
	private MainFrom(String title) {
		this.f=f;
		// 定义x,y,w,h,位置及大小
		int x = 500, y = 40, w = 100, h = 20;
		JFrame jf = new JFrame(title);
		// 设置关闭按钮的操作.
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置窗体大小不可调整
		jf.setResizable(false);
		// 设置窗体布局为null,以便设置按钮
		jf.setLayout(null);
		// 设置窗体在屏幕的位置
		jf.setLocation(400, 150);
		// 设置窗体为可见状态
		jf.setVisible(true);
		// 设置窗体大小.
		jf.setSize(750, 650);

		// JButton addVid= new JButton("加入vid");
		// 设置按钮位置大小.
		loadin.setBounds(x, y, w, h);
		loadVid.setBounds(x, y + h, w, h);
		addVid.setBounds(x, y + (h * 2), w, h);
		loadCookie.setBounds(x, y + (h * 3), w, h);
		login.setBounds(x + (w), y, w, h);
		vote.setBounds(x + (w), y + h, w, h);

		// 设置textbox位置
		feedBack.setBounds(10, 20, 650, 20);
		fileName.setBounds(x - w, y + h, w, h);
		tNum.setBounds(660, 20, 30, 20);
		// 设置label位置
		messageLabel.setBounds(10, 20, 350, 500);

		// 为textbox设置内容
		fileName.setText("id.txt");
		messageLabel.setText(help);
		tNum.setText("10");
		// 把按钮增加到窗体
		jf.add(loadin);
		jf.add(loadVid);
		jf.add(addVid);
		jf.add(loadCookie);
		jf.add(login);
		jf.add(vote);
		// 加载textbox到窗体
		jf.add(feedBack);
		jf.add(fileName);
		jf.add(tNum);
		// 加载label到窗体
		jf.add(messageLabel);
		// 添加控件之后要重画窗体.
		jf.repaint();

		// 为按钮增加事件.
		loadin.addActionListener(this);
		loadVid.addActionListener(this);
		addVid.addActionListener(this);
		loadCookie.addActionListener(this);
		login.addActionListener(this);
		vote.addActionListener(this);
		// 为textfield添加事件
		feedBack.addActionListener(this);

	}

//接收按钮事件.
	public void actionPerformed(ActionEvent e) {
		// 取具体按钮事件
		Object b = e.getSource();
		// 按钮loadin被点击.
		if (b.equals(loadin)) {
			lodin_click();
			// 按钮loadVid被点击.
		} else if (b.equals(loadVid)) {
			loadVid_click();
			// 按钮addVid被点击.
		} else if (b.equals(addVid)) {
			addVid_click();
			// 按钮loadCookie被点击.
		} else if (b.equals(loadCookie)) {
			loadCookie_click();
			// 按钮login被点击.
		} else if (b.equals(login)) {
			login_click();
			// 按钮vote被点击!
		} else if (b.equals(vote)) {
			vote_click();
			// feedback文本框按下了回车
		} else if (b.equals(feedBack)) {
			feedBack_command();
		}

	}

	// 按钮lodin被点击.
	private void lodin_click() {
		feedBack.setText(MyUtil.loadList(fileName.getText().toString(), MyUtil.listId));
		messageLabel.setText(MyUtil.listId.get(0));
	}

	// 按钮loadvid被点击.
	private void loadVid_click() {
		feedBack.setText(MyUtil.loadList(fileName.getText().toString(), MyUtil.listVid));
		messageLabel.setText(MyUtil.listVid.get(0));
	}

	// 按钮addvid被点击.
	private void addVid_click() {
		// 加入元素到list中.
		MyUtil.listVid.add(fileName.getText().toString());
		feedBack.setText("vid加入成功<==>" + String.valueOf(MyUtil.listVid.getSize()));
		//messageLabel.setText(MyUtil.listVid.get(0));
	}

	// 按钮addCookie被点击.
	private void loadCookie_click() {
		feedBack.setText(MyUtil.loadList(fileName.getText().toString(), MyUtil.listCookie));
		messageLabel.setText(MyUtil.listCookie.get(0));
	}

	// 按钮login被点击.
	private void login_click() {
		if(MyUtil.listVid.getSize()!=0) {
		Thread4Net tt = new SinaLogin(MyUtil.listId.getSize() - 1,false);
		Thread t = new Thread(tt);
		t.start();
		}else {
			messageLabel.setText("vid不能为空...");
		}
	}

	// 按钮login被点击.
	private void vote_click() {
		feedBack.setText("vote");
		feedBack.setText(MyUtil.listId.toString());
	}

	// 文本框按下回车!
	/**
	 * 文本框写入命令,然后回车执行.
	 */
	private void feedBack_command() {
		// 取文本框内容
		String str = feedBack.getText().toString();
		// 要进行删除操作!
		if (str.startsWith("d")) {
			// 取得要删除的索引,java从零开始,所以要减1
			int index = (Integer.parseInt(str.substring(1, str.length()))) - 1;
			feedBack.setText(MyUtil.listVid.remove(index));
		}else if(str.equals("showvid")) {
			//打印第一个vid
			messageLabel.setText(MyUtil.listVid.get(0));
			
		} else if (str.equals("help")) {
			messageLabel.setText(help);
		}

	}
	//打印最新情况
	public void prints(String data) {
		messageLabel.setText(data);
	}
	//定义方法暴露自己,以显示当前进度.
	public static MainFrom GetInstance() {
	return f ;
	}
}
