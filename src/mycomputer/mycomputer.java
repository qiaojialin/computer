package mycomputer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.*;
import java.util.*; 

public class mycomputer {
 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				myFrame frame = new myFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}

class myFrame extends JFrame implements ActionListener
{
	private JPanel panel; 
	private JButton button[] = new JButton[20];
	private JTextField text;
	private int flag;
	
	public myFrame()
	{
		//设置框架长宽
		this.setSize(300, 250);
		//设置框架位置
		this.setLocation(300, 250);
		//设置框架标题
		this.setTitle("计算器"); 
		
		//放置编辑框
		text = new JTextField();
		this.add(text,BorderLayout.NORTH);
		text.setEditable(true);
		
		//编辑框是否在下次输入前清除
		flag = 0;
		
		//创建面板和按钮
		panel=new JPanel(new GridLayout(5,4,10,10));
		this.add(panel);
		String button_icon[]={"(",")","CE","C","7","8","9","/","4","5","6","*","1","2","3","-","0",".","=","+"};
		for(int i=0;i<button_icon.length;i++)
		{
			button[i]=new JButton(button_icon[i]);
			panel.add(button[i]);
			button[i].addActionListener(this);
		}
		button[2].setForeground(Color.RED);
		button[3].setForeground(Color.RED);
		
	}
	

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();	
		
		if(flag == 1)
			text.setText("");
		
		if(source == button[2] || source == button[3])  // CE　C
		{
			text.setText("");
			return;
		}
		else if(source == button[18])   //   =
		{
			
			try {
				compute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			flag = 1;
			return;
		}
		//构造显示的字符串
		String tmp = ((JButton)event.getSource()).getText();
		text.setText(text.getText() + tmp);
		flag = 0;
	}
	
	
	//判断表达式中的括号错误和小数点错误
	public boolean check(String s)
	{
		int kuohao = 0;
		if(s.charAt(0) == '.')
			return false;
		if(s.charAt(s.length()-1) == '.')
			return false;
		
		for(int i=0; i<s.length(); i++)
		{
			if(s.charAt(i) == '(')
				kuohao++;
			if(s.charAt(i) == ')')
				kuohao--;
			if(kuohao < 0)
				return false;		
			if(i<s.length()-1 && s.charAt(i) == '(' && s.charAt(i+1) == ')')
				return false;
			
			if(i != 0 && i != s.length()-1)
			{
				if(s.charAt(i) == '.' && (s.charAt(i-1) > '9'||s.charAt(i-1)<'0') )
					return false;
				if(s.charAt(i) == '.' && (s.charAt(i+1) > '9'||s.charAt(i+1)<'0') )
					return false;
			}
		}
		if(kuohao != 0)
			return false;
		return true;
	}
	
	
	//计算表达式
	public void compute() throws IOException
	{
		String t = text.getText();
		
		if(check(t))
		{	
			Stack<Double> snum = new Stack<Double>();   //push pop peek
			Stack<Character> sope = new Stack<Character>();
		
			for(int i=0; i<t.length(); i++)
			{
				char c = t.charAt(i);
			
				//数字
				if(c >= '0' && c <= '9')
				{
					double num = c - '0';
					int flag = 0;
					double m = 0.1;
					while(i+1 < t.length() && (t.charAt(i+1) >= '0' && t.charAt(i+1) <= '9' || t.charAt(i+1) == '.'))
					{
						if(t.charAt(i+1) == '.')
						{
							flag = 1;
							i++;
							continue;
						}
						else if(flag == 0)
						{
							num = num * 10 + (double)(t.charAt(i+1) - '0');
							i++;
							continue;
						}
						else if(flag == 1)
						{
							num = num + (double)(t.charAt(i+1) - '0') * m;
							m *= 0.1;
							i++;
							continue;
						}
					}
					snum.push(num);
					//		System.out.println("snum push:" + num);
				}
			
				//+ -
				if(c == '+' || c == '-')
				{
					//解决负号问题
					if(c == '-' && (!sope.empty() && sope.peek() == '(' || sope.empty() && snum.empty() ) )
						snum.push((double)0);
				
					if(!sope.empty() && (sope.peek() == '+' || sope.peek() == '-' || sope.peek() == '*' || sope.peek() == '/')) //操作符栈顶是+ - * /
					{
						if(snum.size()<2)
						{
							text.setText("error");
							return;
						}
						if(sope.size()<1)
						{
							text.setText("error");
							return;
						}
						double a = snum.pop();
						double b = snum.pop();
						char o = sope.pop();
						double h = 0;
						if(o == '+')
							h = a + b;
						if(o == '-')
							h = b - a;
						if(o == '*')
							h = Arith.mul(a, b);
						if(o == '/')
							h = Arith.div(b, a);
						snum.push(h);
					//	System.out.println("a=" + a + ' ' + "b=" + b + ' '+ "o=" + o);
					//	System.out.println("snum push:" + h);
					}
					sope.push(c);
				
					//	System.out.println("sope push:" + c);
				}
			
				//* /
				if(c == '*' || c == '/')
				{
					if(!sope.empty() && (sope.peek() == '*' || sope.peek() == '/' ))  //操作符栈顶是 + -
					{
						if(snum.size()<2)
						{
							text.setText("error");
							return;
						}
						if(sope.size()<1)
						{
							text.setText("error");
							return;
						}
						double a = snum.pop();
						double b = snum.pop();
						char o = sope.pop();
						double h = 0;
						if(o == '*')
							h = Arith.mul(a, b);
						if(o == '/')
							h = Arith.div(b, a);
						snum.push(h);
						//		System.out.println("a=" + a + ' ' + "b=" + b + ' '+ "o=" + o);
						//		System.out.println("snum push:" + h);
					}
					sope.push(c);
					//	System.out.println("sope push:" + c);
				}
			
				// (
				if(c == '(')
				{
					sope.push(c);
					//	System.out.println("sope push:" + c);
				}
			
				// )
				if(c == ')')
				{
					while(sope.peek() != '(')
					{
						if(snum.size()<2)
						{
							text.setText("error");
							return;
						}
						if(sope.size()<1)
						{
							text.setText("error");
							return;
						}
						double a = snum.pop();
						double b = snum.pop();
						double h = 0;
						char o = sope.pop();
						if(o == '+')
							h = a + b;
						if(o == '-')
							h = b - a;
						if(o == '*')
							h = Arith.mul(a, b);
						if(o == '/')
							h = Arith.div(b, a);
						snum.push(h);
						//		System.out.println("a=" + a + ' ' + "b=" + b + ' '+ "o=" + o);
						//		System.out.println("snum push:" + h);
					}
					sope.pop();
				}
			
			}
		
			//扫描完字符串开始计算
			while(!sope.empty())
			{
				if(snum.size()<2)
				{
					text.setText("error");
					return;
				}
				if(sope.size()<1)
				{
					text.setText("error");
					return;
				}
				double a = snum.pop();
				double b = snum.pop();
				double h = 0;
				char o = sope.pop();
				if(o == '+')
					h = a + b;
				if(o == '-')
					h = b - a;
				if(o == '*')
					h = Arith.mul(a, b);
				if(o == '/')
					h = Arith.div(b, a);
				snum.push(h);
				//			System.out.println(h);
				//			System.out.println("a=" + a + ' ' + "b=" + b + ' '+ "o=" + o);
				//			System.out.println("snum push:" + h);
			}
			if(snum.size()<1)
			{
				text.setText("error");
				return;
			}
			double result = snum.pop();
			text.setText(result + "");
		
			Date d=new Date();
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
		
			String history = df.format(d) + "   " + t + "=" + result + "\r\n";
			
			//将历史记录写入本地文件
			File file = new File("local_history.txt");
			if(!file.exists())
			{
				try{
					file.createNewFile();
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			try{
				FileWriter out = new FileWriter(file,true);
				out.write(history);
				out.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		
			//将历史记录上传服务器
			new Thread(new uploadthread(history)).start();
			
		}
		else
		{
			text.setText("error");
		}
		
	}
	
}


