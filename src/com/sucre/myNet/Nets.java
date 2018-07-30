package com.sucre.myNet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * 负责socket发送与接收.接收后的数据交出调用者自己处理.
 * 
 * @author sucre
 *
 */
public class Nets {

	/**
	 * 数据包发送.get/post通用.
	 * 
	 * @param Host 服务器域名
	 * @param Port 服务器端口
	 * @param out  要发送的数据
	 * @return 成功后返回服务器返回的数据,不成功返回错误码.
	 */
	public String goPost(String host, int port, byte[] data) {
		StringBuilder ret= new StringBuilder(data.length);
		// 创建sslsocket工厂
		SocketFactory factory = SSLSocketFactory.getDefault();
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket sslsocket = factory.createSocket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵活.必要时可传文件之用.
				BufferedOutputStream out = new BufferedOutputStream(sslsocket.getOutputStream());

		) {
			//写入要发送的数据并刷新!
			out.write(data);
			out.flush();
			//接收数据,为了解决乱码的情况,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			BufferedReader in=new BufferedReader(new InputStreamReader(sslsocket.getInputStream(),"UTF-8"));
			String str=null;
			char[] rev=new char[1024];
			int len=-1;
			while((len=in.read(rev))!=-1) {
				ret.append(new String(rev,0,len) );
				//由于socket会阻塞,当装不满缓冲区时,当作是结束,
				if(len<1024) {break;}
			}
			
			//安全起见还是关闭一下资源.
			in.close();
			sslsocket.close();
			out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret.toString();
	}

}
