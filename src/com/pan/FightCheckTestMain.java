package com.pan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import net.good321.frame.utils.RegexUtil;

/**
 * 战斗校验测试
 * @author Pan
 *
 */
public class FightCheckTestMain {
	public static void main(String[] args) {
//		fightCheck();//战斗校验
		
//		String cmd = "C:/Windows/system32/calc.exe";
//		String cmd = "E:/pan/work/goodhIE/trunk/code/replay/10.6.8.98/StandaloneWindows64/没有心跳的少女.exe --timeout 30";
		String cmd = "E:/pan/work/goodhIE/trunk/code/replay/10.6.8.98/StandaloneWindows64/Run.bat";
		runbat(2*60,cmd);
	}
	
	public static void fightCheck() {
		System.out.println("调用战斗校验:start");
		
//		cmd /c dir 是执行完dir命令后关闭命令窗口。 
//		cmd /k dir 是执行完dir命令后不关闭命令窗口。
//		cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。
//		cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
//		String cmd = "cmd /c start xxx";//打开cmd界面执行指定脚本
		String cmd = "C:/Users/Administrator/Desktop/fightCheck/StandaloneWindows64/Run.bat";
		cmd = cmd + " -i E:/tempshIE/fightCheck/10-20-21_388848.data";
		cmd = cmd + " -o E:/tempshIE/fightCheck/10-20-21_388848.out";
		
		runbat(5*60,cmd);
		
		System.out.println("调用战斗校验:end");
	}
	
	/**
	 * 运行脚本
	 * @param timeoutS 超时秒
	 * @param cmd
	 */
	public static void runbat(int timeoutS, String cmd) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			
			new Thread() {//读取输出
				public void run() {
					String rs = readData(p.getInputStream());
					
					if(rs != null && rs.isEmpty() == false) {
						System.out.println("runbat:客户端返回结果=" + rs);
						String result = RegexUtil.find(rs, "\\[ExitCode\\]:([0-9]{1})", 1);
						System.out.println("runbat:校验结果结果=" + result);
					}
					
					System.out.println("runbat:读取[输出]线程退出");
				}
			}.start();
			
			new Thread() {//读取异常
				public void run() {
					String error = readData(p.getErrorStream());
					if(error != null && error.isEmpty() == false) {
						System.err.println("runbat:客户端返回异常=" + error);
					}
					
					System.out.println("runbat:读取[异常]线程退出");
				}
			}.start();
			
			if (!p.waitFor(timeoutS, TimeUnit.SECONDS)) {//等待执行
				System.out.println("runbat:执行超时");
			}

			p.destroyForcibly();
			System.out.println("runbat:执行成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("runbat:执行异常");
		}
	}
	
	/**
	 * 读取流
	 * @param is
	 * @return
	 * @throws IOException
	 */
    public static String readData(InputStream is){
//        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
//        BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream(), "GBK"));
//        BufferedReader brError = new BufferedReader(new InputStreamReader(outputStream, "GBK"));
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
			String tmp = null;
			while ((tmp = br.readLine()) != null) {
//				System.out.println("readOutput.debug:" + tmp);
				result.append(tmp).append("\n");
			}
			
			is.close();
		}catch (Exception e) {
			e.printStackTrace();
			result.append("读取结果异常");
		}
		
		return result.toString();
    }
}
