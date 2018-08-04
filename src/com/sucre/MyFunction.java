package com.sucre;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import com.sucre.listUtil.MutiList;
import com.sucre.mainUtil.MyUtil;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;

public class MyFunction extends Thread4Net {

	//索引参数
	private int index;
	public MainFrom f;
	// 传递参数给父类.
	MyFunction(int u,boolean isCirCle) {
		super(u,isCirCle);
		f=MainFrom.GetInstance();
	}

	// 覆盖父类方法.
	public int doWork(int index) {
		this.index=index;
		
		Nets p=new Nets();
		for(int i=0;i<=MyUtil.listVid.getSize()-1;i++) {
			String vid=MyUtil.listVid.get(i);
			String ret=p.goPost("api.weibo.cn", 443, liveInfo(vid));
			f.prints("正在播放:" + String.valueOf(index+1) +"<==>" + String.valueOf(i) );
		    if(ret.indexOf("100000")==-1) {
		    	String tname=Thread.currentThread().getName();
		    	if("ipchange".equals(tname)) {
		    	MyUtil.cutAdsl(f.ipname.getText());
		    	MyUtil.sleeps(1000);
		    	MyUtil.connAdsl(f.fileName.getText(), f.ipaccount.getText(), f.ippsd.getText());
		    	}
		    }
		}
		
		return index;
	}
	
	
	private byte[] liveInfo(String vid) {
		
		StringBuilder data=new StringBuilder(900);
		String d="live_id="+ vid ;
		data.append("POST https://api.weibo.cn/2/live/getinfo?networktype=wifi&moduleID=700&wb_version=3449&c=android&i=6hj7im7&s=c3156281&ft=0&ua=oppo-oppo%20r11__weibo__7.9.1__android__android5.1.1&wm=5091_0008&v_f=2&v_p=50&from=1007498895823&gsid=_2AkMvpe-6f8NhqwJRmP8RxWPqZYh_yAvEieKZ-R5hJRM3HRl-3D9kqkgLtRV9i9aROB8dVPyTQGLu6CtwzEaZmw..&lang=zh_CN&skin=default&oldwm=5091_0008&sflag=1 HTTP/1.1\r\n");
		data.append("Accept: */*\r\n");
		data.append("Accept-Language: zh-cn\r\n");
		data.append("Cache-Control: no-cache\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("User-Agent: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2)\r\n");
		data.append("Host: api.weibo.cn\r\n");
		data.append("Content-Length: "+ d.length() +"\r\n");
		data.append("Connection: Keep-Alive\r\n");
		data.append("\r\n");
		data.append( d + "\r\n");
		data.append("\r\n");
		
		return data.toString().getBytes();
	}
	
	
	
	
	
	
	/**
	 * 组合多种数据成为http数据包,这里真的很难写得优雅!!请原谅!
	 * @return 数据包.
	 */
	private byte[] throwBatch() {
		//1f2dbc412976863258aa8f450f299f7c|dirs6dm|62772b60|R_xW5_q|c12eff4f7422e24aa153918615a3508373d91bac|1007523439607|_2AkMvpUuAf8NhqwJRmP8Qzmjmb4Rxyg_EieKZ-bpbJRM3HRl-3D9kqlUttRWJWcA8gH4muqPim7vBRGT4uVmwwg..|894466129320806
		String[] cookie=MyUtil.listId.get(index).split("\\|");
		//153201219880116470851532008800
		String xv=MyUtil.getTimeB()+MyUtil.getTimeB();
		String xvk=MyUtil.getTimeB();
		
		StringBuilder data=new StringBuilder(800);
		StringBuilder data1=new StringBuilder(2000);
		StringBuilder data2=new StringBuilder(1200);
		data.append("POST https://api.weibo.cn/2/client/throw_batch?networktype=wifi&isgzip=1&moduleID=700&wb_version=3654&c=android&i="+ cookie[1] +"&s="+ cookie[2] +"&ft=0&ua=HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2&wm=9006_2001&aid=01A"+ cookie[3] +".&v_f=2&v_p=62&from=1086395010&gsid="+ cookie[6] +"&lang=zh_CN&skin=default&oldwm=9006_2001&sflag=1&cum=D1857508 HTTP/1.1\r\n");
		data.append("X-Validator: "+ MyUtil.aesEncrypt("obiewelibom_anis", xv+xvk +"")+"\r\n");
		data.append("User-Agent: Che2-TL00_4.4.2_weibo_8.6.3_android\r\n");
		data.append("Content-Type: multipart/form-data;boundary=------------1532012211221\r\n");
		data.append("X-Log-Uid: "+ cookie[5] +"\r\n");
		data.append("Host: api.weibo.cn\r\n");
		data.append("Connection: Keep-Alive\r\n");
		
		//第二个字符串
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"v_f\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("2\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"moduleID\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("700\r\n");
		data1.append("--------------1532012211221\n");
		data1.append("Content-Disposition: form-data; name=\"wb_version\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("3654\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"c\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("android\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"wm\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("9006_2001\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"from\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("1086395010\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"aid\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("01A"+ cookie[3] +".\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"networktype\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("wifi\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"skin\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("default\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"i\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append(cookie[1]+"\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"isgzip\"\r\n");
		data1.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		data1.append("1\r\n");
		data1.append("--------------1532012211221\r\n");
		data1.append("Content-Disposition: form-data; name=\"throw\"\r\n");
		data1.append("Content-Type: application/octet-stream\r\n");
		data1.append("Content-Transfer-Encoding: 8bit\r\n");
		data1.append("\r\n");
		//zip压缩vid
		String g=MyUtil.listVid.get(index).replace("myuid", cookie[5]);
		byte datas[]=xorEncode(MyUtil.gZip(g),xvk.getBytes());
		
		//第三个字符串
		data2.append("\r\n--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"gsid\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append( cookie[6]+ "\r\n");
		data2.append("--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"s\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append(cookie[2] + "\r\n");
		data2.append("--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"oldwm\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append("9006_2001\r\n");
		data2.append("--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"v_p\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append("62\r\n");
		data2.append("--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"ua\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append("HUAWEI-Che2-TL00__weibo__8.6.3__android__android4.4.2\r\n");
		data2.append("--------------1532012211221\r\n");
		data2.append("Content-Disposition: form-data; name=\"ft\"\r\n");
		data2.append("Content-Type: text/plain;charset:\"UTF-8\"\r\n");
		data2.append("Content-Transfer-Encoding: 8bit\r\n");
		data2.append("\r\n");
		data2.append("0\r\n");
		data2.append("--------------1532012211221--\r\n");
		data2.append("\r\n");
		data2.append("\r\n");
		//计算长度
		int len=data1.length()+data2.length()+datas.length;
		data.append("Content-Length: "+ (len )+"\r\n");
		data.append("\r\n");
		
		
		byte[] ret=new byte[len+data.length()];
		
		data.append(data1);
		int lens=data.toString().getBytes().length;
		System.arraycopy(data.toString().getBytes() ,0, ret, 0, lens);
		
		System.arraycopy(datas ,0, ret, lens, datas.length);
		lens+=datas.length;
		System.arraycopy(data2.toString().getBytes() ,0, ret, lens, data2.length());
		
		return ret;	
	}
    byte[] xorEncode(byte[] srczip,byte[] key) {
    	byte[] ret=new byte[srczip.length];
    	
    	int i = 0;
    	while(i < (srczip.length)-1) {
    		ret[i] = (byte) (srczip[i] ^ key[i % (key.length )]);
    	//ret[i] =srczip[i];
    	 i ++;
    	}
    	
    	return ret;
    }
}
