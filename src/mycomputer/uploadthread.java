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
			System.out.println("TELLER���Ѿ��ɹ������ӵ�ESB�ˣ�");	 
			PrintWriter pw;
			pw = new PrintWriter(s.getOutputStream(),true);
			pw.println(history);//��socketͨ��д����Ϣ
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     //��װ���������	        
	}
}
