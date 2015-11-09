package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;


public class server {
	
	private ServerSocket ss=null;
	private int port=8888;
	
	public server() throws IOException
	{
		//启动服务器，并指定端口号
		ss=new ServerSocket(port);
		System.out.println("服务器已经成功的启动！");
	}
	
	public void Communite(){
		try 
		{
			//监听客户端的连接请求并接受，如果没有将一直等待下去
			while(true)
			{
				Socket s=ss.accept();
				System.out.println("成功的接收到一个客户端的连接请求！");
				
				BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
					
				//接收从客户端发来的消息
				String str;
				str = br.readLine();
				
				//在服务器存盘
				BufferedWriter bw= new BufferedWriter(new FileWriter(new File("server_history.txt"),true));
				bw.write(str + "\r\n");
				bw.close();
			}	
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) throws IOException
	{
		//启动服务
		new server().Communite();
	}
}

