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
		//��������������ָ���˿ں�
		ss=new ServerSocket(port);
		System.out.println("�������Ѿ��ɹ���������");
	}
	
	public void Communite(){
		try 
		{
			//�����ͻ��˵��������󲢽��ܣ����û�н�һֱ�ȴ���ȥ
			while(true)
			{
				Socket s=ss.accept();
				System.out.println("�ɹ��Ľ��յ�һ���ͻ��˵���������");
				
				BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter pw=new PrintWriter(s.getOutputStream(),true);
					
				//���մӿͻ��˷�������Ϣ
				String str;
				str = br.readLine();
				
				//�ڷ���������
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
		//��������
		new server().Communite();
	}
}

