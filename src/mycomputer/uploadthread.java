package mycomputer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class uploadthread implements Runnable 
{
	private String history;
	
	public uploadthread(String s)
	{
		history = s;
	}
	public void run() 
	{
		String url="127.0.0.1";
		int port=8888;			
		Socket s;
		try {
			s = new Socket(url,port);
			System.out.println("TELLER端已经成功的连接到ESB端！");	 
			PrintWriter pw;
			pw = new PrintWriter(s.getOutputStream(),true);
			pw.println(history);//向socket通道写入消息
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     //封装输入输出流	        
	}
}
