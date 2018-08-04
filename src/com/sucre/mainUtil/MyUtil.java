package com.sucre.mainUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import com.sucre.listUtil.MutiList;

/**
 * 常用工具类.
 * 
 * @author sucre
 *
 */
public class MyUtil {
	// 定义各种List
	public static MutiList listId = new MutiList();
	public static MutiList listVid = new MutiList();
	public static MutiList listCookie = new MutiList();

	/**
	 * @param fileName 为当前目录下的文件名,带后缀
	 * @param list     为要加载进入的list对象
	 * @return 成功则返回list的size,不成功则返回错误信息.
	 */
	public static String loadList(String fName, MutiList list) {

		try {
			// 加载文件
			list.loadFromFile(fName);
			return ("导入成功<==>" + String.valueOf(list.getSize()));
		} catch (IOException e) {
			return (e.getMessage());
		}

	}

	/**
	 * aes加密方法,默认为AES/ECB/NoPadding,128位数据块,偏移量为空.
	 * 
	 * @param keys 加密 的密码
	 * @param data 要加密的数据
	 * @return 成功返回base64格式的字符串,不成功返回null!
	 */
	public static String aesEncrypt(String keys, String data) {
		String ret = null;
		try {
			// 创建cipher对象,这是一个单例模式.
			Cipher c = Cipher.getInstance("AES/ECB/NoPadding");
			// 创建一个key对象.
			SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");
			// 初始化为加密模式.
			c.init(Cipher.ENCRYPT_MODE, key);
			// 加密并返回byte[]
			byte[] result = c.doFinal(data.getBytes());
			// 对加密结果进行base64编码
			ret = new String(Base64.getEncoder().encode(result));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * AES解密算法.默认AES/ECB/NoPadding,128位数据块,偏移量为空.
	 * 
	 * @param keys 加密时的密码
	 * @param data 要解密的数据
	 * @return 成功返回明文,不成功返回null
	 */
	public static String aesDecrypt(String keys, String data) {

		try {
			// 创建cipher对象,这是一个单例模式.
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			// 创建一个key对象
			SecretKeySpec key = new SecretKeySpec(keys.getBytes(), "AES");
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 执行操作,先对字符作base64解码,再解密.
			byte[] result = cipher.doFinal(Base64.getDecoder().decode(data));
			// byte 转string 并返回
			return (new String(result));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * 将字符串或文件压缩
	 * 
	 * @param data 为要压缩的字符串 @return,成功返回byte[]数组.
	 */
	public static byte[] gZip(String data) {
		// 用来接收压缩后的数据
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (
				// 创建压缩对象
				GZIPOutputStream g = new GZIPOutputStream(baos);

		) {
			// 压缩操作
			g.write(data.getBytes());
			g.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * 生成指定范围内的随机数,
	 * 
	 * @param u 范围上限
	 * @param l 范围下限
	 * @return String 类型的随机数字
	 */
	public static String getRand(int u, int l) {
		Random r = new Random();
		return String.valueOf(((r.nextInt(u - l) + l)));
	}

	/**
	 * 随机生成一串字母数字组合的字符串
	 * 
	 * @param n 要生成字符的长度
	 * @return 长度为n的随机字符串
	 */
	public static String makeNonce(int n) {
		return makeSome(n, "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	/**
	 * 随机生成一串数字组合的字符串
	 * 
	 * @param n 要生成字符的长度
	 * @return 长度为n的随机字符串
	 */
	public static String makeNumber(int n) {
		return makeSome(n, "0123456789");
	}

	private static String makeSome(int n, String key) {
		String ret = "";
		if (n != 0) {

			for (int i = 1; i <= n; i++) {
				int p = Integer.parseInt(getRand(key.length(), 0));
				ret += key.charAt(p);
			}
		}
		return ret;
	}

	/**
	 * 取当前时间.13位
	 * 
	 * @return 当前时间毫秒
	 */
	public static String getTime() {
		long t = System.currentTimeMillis();
		return String.valueOf(t);
	}

	/**
	 * 取当前时间.10位
	 * 
	 * @return 当前时间毫秒
	 */
	public static String getTimeB() {
		long t = System.currentTimeMillis();
		return String.valueOf(t).substring(0, 10);
	}

	/**
	 * 取出所有cookie数据
	 * 
	 * @param 包含所有数据的http报头
	 * @return http报头的所有setcookie 数据
	 */
	public static String getAllCookie(String data) {
		String temp = "";
		int cookieIndex = 0;
		int endIndex = 0;
		String str2find = "Set-Cookie:";
		// 循环取出字符
		while ((cookieIndex = data.indexOf(str2find, cookieIndex)) != -1) {
			// 判断cookie是否符合规格!
			if ((endIndex = data.indexOf(";", cookieIndex)) != -1) {
				// 取出字符
				temp += data.substring(cookieIndex + str2find.length(), endIndex + 1);
				cookieIndex = endIndex;
			}
		}

		return temp;
	}

	/**
	 * 追加数据到文件!文件默认位置为当前目录
	 * 
	 * @param fileName 要保存的文件名,请带上后缀
	 * @param data     要追加保存的数据,默认会自动加上换行符!
	 */
	public static void outPutData(String fileName, String data) {
		if ("".equals(data)) {
			return;
		}
		// 定义文件
		File file = new File(fileName);
		try (
				// 创建文件流
				OutputStream out = new FileOutputStream(file, true);) {
			// 写入文件!
			data += "\r\n";
			out.write(data.getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 截取某一段字符
	 * 
	 * @param start   要截取字符的开始位置
	 * @param ends    要截取字符的结束位置
	 * @param str2mid 源字符串
	 * @return 返回start 和ends 所包含位置的字符串
	 */
	public static String midWrod(String start, String ends, String str2mid) {
		int begin, last;
		if ("".equals(str2mid)) {
			return "";
		}
		begin = str2mid.indexOf(start);
		// 找到字符
		if (begin != -1) {
			last = str2mid.indexOf(ends, begin + start.length());
			if (last != -1) {
				String ret = str2mid.substring(begin + start.length(), last);
				return ret;
			}
		}

		return "";
	}

	/**
	 * 
	 * @param dataStr 要转换的字符例如\u64cd\u4f5c\u6210\u529f
	 * @return 返回明名
	 */
	public static String decodeUnicode(String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	/**
	 * 线程休眠.优雅一些,不用每次都try
	 */
	public static void sleeps(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
   
	/** 
        * 执行CMD命令,并返回String字符串 
     */  
    public static String executeCmd(String strCmd)  {  
        Process p;
        StringBuilder sbCmd = new StringBuilder(); 
		try {
			p = Runtime.getRuntime().exec("cmd /c " + strCmd);
		
        
        BufferedReader br = new BufferedReader(new InputStreamReader(p  
                .getInputStream()));  
        String line;  
        while ((line = br.readLine()) != null) {  
            sbCmd.append(line + "\n");  
        }  
		} catch (Exception e) {
			e.printStackTrace();
		}  
        
        
        return sbCmd.toString();  
    }  
  
   /**
    *  建立 一个宽带连接	   
    * @param adslTitle 宽带连接的铝盘
    * @param adslName 宽带连接的账号
    * @param adslPass 宽带连接的密码
    * @return
    */
    public static boolean connAdsl(String adslTitle, String adslName, String adslPass)  {  
        System.out.println("正在建立连接.");  
        String adslCmd = "rasdial " + adslTitle + " " + adslName + " "  
                + adslPass;  
        String tempCmd = executeCmd(adslCmd);  
        // 判断是否连接成功  
        if (tempCmd.indexOf("已连接") > 0) {  
            System.out.println("已成功建立连接.");  
            return true;  
        } else {  
            System.err.println(tempCmd);  
            System.err.println("建立连接失败");  
            return false;  
        }  
    }  
  
    /**
     * 断开adsl拨号
     * @param adslTitle 宽带连接的名称
     * @return
     */
    public static boolean cutAdsl(String adslTitle)  {  
        String cutAdsl = "rasdial " + adslTitle + " /disconnect";  
        String result = executeCmd(cutAdsl);  
         
        if (result.indexOf("没有连接")!=-1){  
            System.err.println(adslTitle + "连接不存在!");  
            return false;  
        } else {  
            System.out.println("连接已断开");  
            return true;  
        }  
    }  
	
}
