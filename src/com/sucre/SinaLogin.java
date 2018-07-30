package com.sucre;

import com.sucre.mainUtil.MyUtil;
import com.sucre.myNet.Nets;
import com.sucre.myThread.Thread4Net;

/**
 * 登录新浪微博,取得cookie并保存起来!
 * 
 * @author sucre
 *
 */
public class SinaLogin extends Thread4Net {
	// 索引参数
	private int index;
	private String[] l;
	public MainFrom f;
	// 传递参数给父类.
	protected SinaLogin(int u, boolean isCricle) {
		super(u, isCricle);
		f = MainFrom.GetInstance();
	}

	// 覆盖父类方法.
	@Override
	public int doWork(int index) {
		this.index = index;
	   f.prints("正在登录:" + String.valueOf(index+1));
		// 分配cookie或者账号
		l = MyUtil.listId.get(index).split("[^@.\\w]");
		// 发送网络请求登录
		Nets p = new Nets();
		String ret = p.goPost("m.weibo.cn", 443, getT());
		String cookie = MyUtil.getAllCookie(ret);
	    ret = p.goPost("passport.weibo.cn", 443, loginData());
		// 处理返回数据
        cookie += MyUtil.getAllCookie(ret);
        if (!"".equals(cookie)) {
			MyUtil.outPutData("cookie.txt", cookie + "|" + l[0]+ "|" + l[1] +"|"+ MyUtil.midWrod("uid\":\"", "\"}", ret));
			f.prints("正在取st:" + String.valueOf(index+1));
			ret = p.goPost("m.weibo.cn", 443, getSt(cookie));
			String st=MyUtil.midWrod("st: '", "',", ret);
			if(!"".equals(st)) {
				f.prints("正在二次登录:" + String.valueOf(index+1));
				ret = p.goPost("m.weibo.cn", 443, getTask(st,cookie));
				f.prints("休息3秒后输入邀请码." + String.valueOf(index+1));
				//延时操作
				MyUtil.sleeps(3000);
				ret = p.goPost("m.weibo.cn", 443, submit(cookie,st,MyUtil.listVid.get(0)));
				String msg=MyUtil.midWrod("msg\":\"","\",\"",ret);
				f.prints(MyUtil.decodeUnicode(msg) + String.valueOf(index+1));
				if(msg.equals("\\u64cd\\u4f5c\\u6210\\u529f")) {
					//延时操作
					f.prints("休息3秒后签到." + String.valueOf(index+1));
					MyUtil.sleeps(5000);
					//签到
					chackin(cookie,index);
				}
			}
		}else {
			f.prints("登录失败:" + String.valueOf(index+1));
		}
		return 0;
	}
	
	public void chackin(String cookie,int index) {
		Nets p = new Nets();
		f.prints("正在签到." + String.valueOf(index+1));
		String ret = p.goPost("m.weibo.cn", 443, chackinData(cookie));
		String st=MyUtil.midWrod("st: '", "',", ret);
		f.prints("正在签到.并休息5秒"+st + String.valueOf(index+1));
		MyUtil.sleeps(5000);
		ret = p.goPost("m.weibo.cn", 443, getInfo(cookie,st));
		ret = p.goPost("m.weibo.cn", 443, getTask(st,cookie));
		f.prints("签到完成.休息9秒后登录下一个号."+st+"<==>" + String.valueOf(index+1));
		
	}
	
    //取_T_WM
    private byte[] getT() {
    	StringBuilder data = new StringBuilder(1100);
    	data.append("GET https://m.weibo.cn/ HTTP/1.1\r\n");
    	data.append("Host: m.weibo.cn\r\n");
    	data.append("Connection: keep-alive\r\n");
    	data.append("Upgrade-Insecure-Requests: 1\r\n");
    	data.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.60\r\n");
    	data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\r\n");
    	data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
    	data.append("\r\n");
    	data.append("\r\n");
    	
    	return data.toString().getBytes();
    }
	// 登录数据包
	private byte[] loginData() {

		StringBuilder data = new StringBuilder(1100);
		String d = "username=" + l[0] + "&password=" + l[1]
				+ "&savestate=1&r=https%3A%2F%2Fm.weibo.cn%2F&ec=0&pagerefer=https%3A%2F%2Fm.weibo.cn%2F&entry=mweibo&wentry=&loginfrom=&client_id=&code=&qq=&mainpageflag=1&hff=&hfp=";

		data.append("POST https://passport.weibo.cn/sso/login HTTP/1.1\r\n");
		data.append("Host: passport.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: " + d.length() + "\r\n");
		data.append("Origin: https://passport.weibo.cn\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.60\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Accept: */*\r\n");
		data.append(
				"Referer: https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=https%3A%2F%2Fm.weibo.cn%2F\r\n");
		
		data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
		data.append(
				"Cookie: _T_WM=eb01ad54b6253088f2eac89ebb02817d; WEIBOCN_FROM=1110006030; MLOGIN=0; M_WEIBOCN_PARAMS=uicode%3D10000011%26fid%3D102803; login=903b2dfe558d757c50a86dbd7d018964\r\n");
		data.append("\r\n");
		data.append(d + "\r\n");
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}
	//登录.com域名.
    private byte[] loginWeibo(String st) {
    	StringBuilder data = new StringBuilder(1000);
    	data.append("GET https://passport.weibo.com/sso/crossdomain?entry=mweibo&action=login&proj=1&ticket="+ st +"&savestate=1&callback=jsonpcallback1532920619552 HTTP/1.1\r\n");
    	data.append("Host: passport.weibo.com\r\n");
    	data.append("Connection: keep-alive\r\n");
    	data.append("User-Agent: Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36 OPR/54.0.2952.60\r\n");
    	data.append("Accept: */*\r\n");
    	data.append("Referer: https://passport.weibo.cn/signin/login?entry=mweibo&res=wel&wm=3349&r=https%3A%2F%2Fm.weibo.cn%2F\r\n");
    	data.append("Accept-Language: zh-CN,zh;q=0.9\r\n");
    	data.append("\r\n");
    	data.append("\r\n");
    	
    	return data.toString().getBytes();
    }
	// 取st
	private byte[] getSt(String C) {

		StringBuilder data = new StringBuilder(1000);

		data.append("GET https://m.weibo.cn/c/lightckin/inviteCode HTTP/1.1\r\n");
		data.append("Host: m.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
		data.append("x-user-agent: HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2)\r\n");
		data.append("X-Requested-With: com.sina.weibolite\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: " + C + "\r\n");
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}

	// 二次登录
	private byte[] getTask(String st, String C) {

		StringBuilder data = new StringBuilder(1000);

		data.append("GET https://m.weibo.cn/c/lightckin/getTask?st=" + st + " HTTP/1.1\r\n");
		data.append("Host: m.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Accept: application/json, text/plain, */*\r\n");
		data.append("X-Requested-With: XMLHttpRequest\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append(
				"User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2)\r\n");
		data.append("Referer: https://m.weibo.cn/c/lightckin\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: " + C + " \r\n");
		data.append("\r\n");
		data.append("\r\n");

		return data.toString().getBytes();
	}
	
	//确认邀请码
	private byte[] submit(String C,String st,String vid) {
		StringBuilder data = new StringBuilder(1100);
		String d="code="+ vid +"&st="+ st;
		data.append("POST https://m.weibo.cn/c/lightckin/submitCode?st="+ st +" HTTP/1.1\r\n");
		data.append("Host: m.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Content-Length: "+ d.length() +"\r\n");
		data.append("Accept: application/json, text/plain, */*\r\n");
		data.append("Origin: https://m.weibo.cn\r\n");
		data.append("X-Requested-With: XMLHttpRequest\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append("User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2)\r\n");
		data.append("Content-Type: application/x-www-form-urlencoded\r\n");
		data.append("Referer: https://m.weibo.cn/c/lightckin/inviteCode\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: "+ C +"\r\n");
		data.append("\r\n");
		data.append( d + "\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}
	//chackin
	private byte[] chackinData(String C) {
		StringBuilder data = new StringBuilder(1000);
		data.append("GET https://m.weibo.cn/c/lightckin HTTP/1.1\r\n");
		data.append("Host: m.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r\n");
		data.append("x-user-agent: HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append("User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2)\r\n");
		data.append("X-Requested-With: com.sina.weibolite\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie:" + C + "\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}
	//getinfo
	private byte[] getInfo(String C ,String st) {
		StringBuilder data = new StringBuilder(1000);
		data.append("GET https://m.weibo.cn/c/lightckin/getInfo?st="+ st +" HTTP/1.1\r\n");
		data.append("Host: m.weibo.cn\r\n");
		data.append("Connection: keep-alive\r\n");
		data.append("Accept: application/json, text/plain, */*\r\n");
		data.append("X-Requested-With: XMLHttpRequest\r\n");
		data.append("x-wap-profile: http://wap1.huawei.com/uaprof/HONOR_Che2-TL00_UAProfile.xml\r\n");
		data.append("User-Agent: Mozilla/5.0 (Linux; Android 4.4.2; Che2-TL00 Build/HonorChe2-TL00) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36 Weibo (HUAWEI-Che2-TL00__weibo_lightning__1.3.8__android__android4.4.2)\r\n");
		data.append("Referer: https://m.weibo.cn/c/lightckin\r\n");
		data.append("Accept-Language: zh-CN,en-US;q=0.8\r\n");
		data.append("Cookie: "+ C +"\r\n");
		data.append("\r\n");
		data.append("\r\n");
		return data.toString().getBytes();
	}
}
