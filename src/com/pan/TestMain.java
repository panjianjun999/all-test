package com.pan;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.utils.IOUtils;

import com.alibaba.fastjson.JSON;
import com.good.sdk.util.SignUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import net.good321.common.define.ExceptionCode;
import net.good321.common.define.ProtocolCode;
import net.good321.common.po.game.player.TbPlayerFriendInfoPo;
import net.good321.common.rmi.ILoginRmi;
import net.good321.frame.utils.DateUtil;
import net.good321.frame.utils.FileUtils;
import net.good321.frame.utils.HttpUtils;
import net.good321.frame.utils.MD5;
import net.good321.frame.utils.OrderedThreadPoolExecutor;
import net.good321.frame.utils.RandomUtil;
import net.good321.frame.utils.StringUtils;
import net.good321.frame.utils.ThreadPool;
import net.good321.frame.utils.Tool;
import net.good321.frame.utils.ZipUtils;
import net.good321.frame.utils.codec.Zip;
import net.good321.proto.FightCheckMsg;
import net.good321.proto.FightCheckMsg.BattleFrameCheckData;
import net.good321.proto.FightCheckMsg.CheckDataAttackDamage;
import net.good321.proto.FightMsg.C2SPlayerActionInput;
import net.good321.proto.GameServerPublicMsg.C2STestCode;
/**
 * 我是dev
 * @author Pan
 * 测试版本:dev3
 */
public class TestMain {

	public static void main(String[] args) {
//		System.out.println("class.getName:" + DateUtil.class.getName());
//		System.out.println("class.getSimpleName:" + DateUtil.class.getSimpleName());
//		System.out.println("class.getCanonicalName:" + DateUtil.class.getCanonicalName());
		
//		testLogFormatJson();
		
//		testCreatePlayer();
		
//		testSyn();
		
//		testSort();
		
//		testShopSpecialPer();
		
//		printMysqlInfo();//打印数据库信息
		
//		testPage();
		
		//测试各种定时器
//		int sleep = 60;
//		testRunByTreadSleep(sleep);//测试定时循环:用线程sleep
//		testRunByTreadTimerSchedule(sleep);//测试定时循环:用定时器-每次设置定时器
//		testRunByTreadTimerScheduleAtFixedRate(sleep);//测试定时循环:用定时器-一次性设置永久循环
		
//		testObj();
		
//		printRedisHits();//打印命中率
		
//		checkRedisMiss();//排查redis命中率
		
//		testJavaMemoryVisuality();//java内存可视性问题
		
//		testArrayBlockingQueue();//测试同步队列
		
//		testLoadProto();
		
//		testThreadPool();//测试线程池
		
//		testScheduleAtFixedRate();//测试定时器
		
//		testGetMixAreaIds();//查找合区后的分区关联
		
//		testStream();
		
//		testOrderSign_Bz_ios();
		
//		testStrZip();
		
//		checkCmdTest();
		
//		testUpdateRes();
		
//		countFileName("D:\\pan\\other\\发票");
		
		testClassLoader();
	}
	
	public static void testClassLoader() {
		// 指定类加载器加载调用
        MyClassLoader classLoader = new MyClassLoader("E:\\tempshIE");
        try {
			classLoader.loadClass("net.good321.robot.TestClass").getMethod("myTest").invoke(null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}
        
        System.out.println("ok");
	}
	
	/**
	 * 统计
	 * @param path
	 */
    public static void countFileName(String path) {
        File file = new File(path);
        int fileCount = 0;
        int numSum = 0;
        List<String> out = new ArrayList<>();
        for (File f:file.listFiles()) {
            if(f.isDirectory()){
                continue;
            }

            String nameStart = f.getName().split("\\.")[0];
            StringBuffer sb = new StringBuffer();
            for (int i = nameStart.length() - 1; i >= 0; i--) {//从后面开始找
                char c = nameStart.charAt(i);
                if (c >= '0' && c <= '9'){
                    sb.insert(0,c);
                }else{
                    break;
                }
            }
            if(sb.length() <= 0){
                continue;
            }

            fileCount++;
            int num = Integer.parseInt(sb.toString());
            String s1 = "文件:" + f.getName() + ",num=" + num;
            System.out.println(s1);
            out.add(s1);
            
            numSum += num;
            String s2 = "\t 文件数=" + fileCount + ",金额=" + num + ",总金额=" + numSum;
            System.out.println(s2);
            out.add(s2);
        }
        
        FileUtils.saveChatTxt(out, path + "\\out.txt");
    }
	
	private static void testUpdateRes() {
		System.out.println("当前时间:" + new Date().toString());
//		String httpUrl = "http://10.6.8.98:7004/hIEres/serverData.do";
		String httpUrl = "http://10.35.7.18:8080/hieres/serverData.do";
		try{
			InputStream input = HttpUtils.httpDownload(httpUrl);
			byte[] buff = IOUtils.toByteArray(input);
			if (buff.length == 0) {
				System.out.println("下载更新资源:下载资源失败, 资源为空");
				return;
			}
			
			try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buff);
					DataInputStream in = new DataInputStream(byteArrayInputStream)) {
				int version = in.readInt(); // 版本
				System.out.println("下载更新资源:version=" + version);
				
				byte[] datas = IOUtils.toByteArray(in);
				if (datas.length == 0) {
					System.out.println("下载更新资源:下载资源失败 ：" + httpUrl);
					return;
				}
				try {
//					ZipUtils.unZip(datas, resLocalPath);//zip格式
//					ZipUtils_7zip.extractile(datas, resLocalPath);//7zip格式
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("下载更新资源:资源更新成功，版本号 =" + version);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * 检查协议压测覆盖率
	 */
	private static void checkCmdTest() {
		//cmd---cmd=2301,总数量=190481,平均耗时=0,统计时长(秒)=2257,TPS=84,min=0,max=258
		//outmsg---cmd=8001,总数量=2832465,平均耗时=22,统计时长(秒)=2258,TPS=1254,min=22,max=34
		List<String> result = FileUtils.readFileReturnList("E:/tempshIE/cmdTestResult.txt");
		
		Field[] fields = ProtocolCode.class.getDeclaredFields();
		for (Field f : fields) {
			String key = f.getName();
//			System.out.println("协议:" + key + ",type=" + f.getType());
			if (f.getType() != int.class) {
				continue;
			}
			
			try {
				int cmd = (Integer) f.get(null);
				System.out.println("协议:" + key + ",value=" + cmd);
			} catch (Exception e) {
				e.printStackTrace();;
			}
		}
	}

	private static void testStrZip() {
		String str1 = FileUtils.readFile("E:/tempshIE/testStr.txt");//模拟源文件
		System.out.println("str1.lenth=" + str1.length());
		System.out.println("str1.substring(100, 200)=" + str1.substring(100, 200));//测试打印
		
		try {
			byte[] data1 = str1.getBytes("UTF-8");//源长度
			System.out.println("data1.lenth=" + data1.length);
			byte[] data2 = Zip.zip(data1);//压缩后长度
			System.out.println("data2.lenth=" + data2.length);
			
			String str2 = new String(data1, "UTF-8");//还原文字
			System.out.println("str2.lenth=" + str2.length());
			System.out.println("str2.substring(100, 200)=" + str2.substring(100, 200));
			
			System.out.println("str1.equals(str2)=" + str1.equals(str2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试b站充值签名:ios
	 */
	private static void testOrderSign_Bz_ios() {
//		假设服务端签名密钥为：pHcNbNb4vtvn8HXT 
//		out_trade_no BILIBILI-1234567890
//		money 600 
//		game_money 1200 
//		product_id com.bilibili.test.item01
//		notify_url http://demo.com/notify（如果无就按空字符串处理，即不参与签名） 
//		将上述五个字段按KEY字典升序排列，并将值拼接在一起，最后拼上服务端密钥 
//		最终参与签名的串：1200600http://demo.com/notifyBILIBILI-1234567890com.bilibili.test.item01pHcNbNb4vtvn8HXT 
//		对参与签名的串进行MD5运算，并将结果转化为小写 计算出的签名：c736b13ba58ff87d9e8ce5d6190fc154
			
		String appKey = "pHcNbNb4vtvn8HXT";
		String out_trade_no = "BILIBILI-1234567890";//922350663000002---商户订单号
		int money = 600;//600---本次交易金额
		int game_money = 1200;//60---游戏内货币
		String product_id = "com.bilibili.test.item01";//苹果商品号
		String notify_url = "http://demo.com/notify";//---支付回调地址
	
		TreeMap<String, String> map = new TreeMap<String, String>();
		map.put("out_trade_no", out_trade_no);
		map.put("money", String.valueOf(money));
		map.put("game_money", String.valueOf(game_money));
		map.put("product_id", product_id);
		map.put("notify_url", notify_url);
		
		//实现1:调用通用接口
		String s1 = SignUtil.getSign(map, appKey).toLowerCase();
		System.out.println("s1=" + s1);
		
		//实现2:自己写
		StringBuffer signCalc = new StringBuffer();
	    for (Map.Entry<String, String> entry : map.entrySet()) {
	    	signCalc.append(String.valueOf(entry.getValue()));
	    }
	    
	    signCalc.append(appKey);//加上key
	    String str2 = signCalc.toString();
	    String s2 = MD5.toMD5(str2).toLowerCase();
		System.out.println("加密前=" + str2);
		System.out.println("md5=" + s2);
	}

	private static void testStream() {
		List<int[]> all = new ArrayList<>();
		all.add(new int[]{1,11});
		all.add(new int[]{2,22});
		all.add(new int[]{3,33});
		all.add(new int[]{4,44});
		
		all.parallelStream().filter(is -> is[1]==33).map(i -> i[0]*i[0]).forEach(System.out::println);
		all.stream().filter(is -> is[1]==33).map(i -> i[0]*i[0]).forEach(System.out::println);
		
		all.parallelStream().limit(2).map(i -> i[0]*i[0]).forEach(System.out::println);
		all.stream().limit(2).map(i -> i[0]*i[0]).forEach(System.out::println);
	}

	/**
	 * 测试游戏服务器多次合区后的关系
	 */
	private static void testGetMixAreaIds() {
		Set<Integer> children = new HashSet<>();
		int[][] areas = {
				{1,2},//下标:0=原分区id,1=合并到目标分区
				{2,3},
				{3,4},
				{4,6},
				{5,6},
				{6,8},
				{7,8},
				{8,0},
		};
		
		int areaId = 8;
		getMixAreaIds(areaId, children, areas);
		
		System.out.println("areaId=" + areaId + ",rs=" + children.toString());
	}

	/**
	 * 获取指定分区id关联的所有合区id,不包括自己
	 * @param areaId 目标分区id
	 * @param children 返回结果
	 * @param areas 原始数据
	 */
	private static void getMixAreaIds(int areaId, Set<Integer> children,int[][] areas) {
		if(areaId == 0) {
			return;
		}
		
		for (int[] is : areas) {
			int oldId = is[0];//原分区id
			int targetId = is[1];//合并到目标分区
			if(targetId == areaId) {
				if(children.contains(oldId) == false) {
					children.add(oldId);
				}
				
				getMixAreaIds(oldId, children, areas);//递归查找
			}
		}
	}

	/**
	 * 测试定时器
	 */
	private static void testScheduleAtFixedRate() {
		class MyDelayRunable implements Runnable {
			@Override
			public void run() {
				long startTime=System.currentTimeMillis();
		        System.out.println("开始run,startTime=" + startTime);
		        try {
		            Thread.sleep(2000);
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }

		        System.out.println("  run end,耗时=" + (System.currentTimeMillis()-startTime));
			}
			
		};
		
		ThreadPool.getTimer().scheduleAtFixedRate(
				new MyDelayRunable(),
				500, //下一次刷新时间
				1000, //之后每次间隔
				TimeUnit.MILLISECONDS);
		
		System.out.println("准备run,startTime=" + System.currentTimeMillis());
	}

	/**
	 * 测试线程池
	 */
	private static void testThreadPool() {
		OrderedThreadPoolExecutor executor = OrderedThreadPoolExecutor.newFixesOrderedThreadPool(1*Runtime.getRuntime().availableProcessors(),"测试线程池");
		
		/**测试结果:
		 * 
		 * 开始测试,runType=1,testCount=10000000
		 * 创建完成,用时=15447
		 * 请求执行完成,用时=12840
		 * 运行完成,总用时=12840
		 * 
		 * 开始测试,runType=2,testCount=10000000
		 * 创建完成,用时=20053
		 * 请求执行完成,用时=6555
		 * 运行完成,总用时=6556
		 */
		
		//方式1:用普通线程池
		//方式2:用ForkJoin模式实现的线程池
		int runType = 2;
		
		int testCount = 1000*10000;//运行总数
		
		System.out.println("开始测试,runType=" + runType + ",testCount=" + testCount);
		
		Map<String, Integer> threadExecutorMap = new ConcurrentHashMap<>();//各个线程处理了多少个
		
		class TestExecutors extends OrderedThreadPoolExecutor.OrderedRunable{
			public TestExecutors(Long key) {
				super(key);
			}

			@Override
			public void run() {
				//模拟执行一点逻辑
				int a = 0;
				for (int i = 0; i < 10000*10000; i++) {
					a += i;
				}
				
//				try {
//					Thread.sleep(10);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				
				String myName = Thread.currentThread().getName();
				Integer count = threadExecutorMap.get(myName);
				if(count == null) {
					threadExecutorMap.put(myName,1);
				}else {
					threadExecutorMap.put(myName,1 + count);
				}
				
//				System.out.println("\t key=" + key + ",run in = " + myName);
			}
		}
		
		//先构建好执行对象,不算到请求执行的时间里
		long ti_init = System.currentTimeMillis();
		TestExecutors[] allTask = new TestExecutors[testCount];
		for (int i = 0; i < testCount; i++) {
			allTask[i] = new TestExecutors(1L);
//			allTask[i] = new TestExecutors((long)i);
		}
		
		long ti_start = System.currentTimeMillis();
		System.out.println("创建完成,用时=" + (ti_start - ti_init));
		
		//开始请求执行
		for (TestExecutors task : allTask) {
			switch (runType) {
			case 1:
				//方式1:用可普通线程池
				ThreadPool.getPool().execute(task);
				break;
			case 2:
				//方式2:用ForkJoin模式实现的线程池
				executor.execute(task);
				break;
			default:
				System.out.println("错误的类型");
				break;
			}
		}
		
		long ti_new = System.currentTimeMillis();
		System.out.println("请求执行完成,用时=" + (ti_new - ti_start));
		
		while(true) {
			int finishCount = 0;
			for (Integer c : threadExecutorMap.values()) {
				finishCount += c;
			}
			
			if(finishCount >= testCount) {
				break;
			}
			
			System.out.println("等待:进度=" + finishCount + "/" + testCount);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		long ti_end = System.currentTimeMillis();
		System.out.println("运行完成,总用时=" + (ti_end - ti_start));
		
		for (String key : threadExecutorMap.keySet()) {
			System.out.println("  各线程处理数量:" + key + " = " + threadExecutorMap.get(key));
		}
		
		System.exit(0);
	}

	private static void testLoadProto() {
		byte[] datas = FileUtils.loadBytes("E:/tempshIE/9020.d");
		try {
			System.out.println("上行战斗校验事件:datas.length=" + datas.length);
			FightCheckMsg.C2SFrameDataReportEvent up = FightCheckMsg.C2SFrameDataReportEvent.parseFrom(datas);//.parseFrom(data);
			System.out.println("上行战斗校验事件:getFrameDatasCount=" + up.getFrameDatasCount());
			
			int max_objBorns = 0;
			int max_useSkills = 0;
			int max_buffs = 0;
			int max_attackDamages = 0;
			int max_buffDamage = 0;
			
//			repeated CheckDataObjBorn objBorns = 2;				//1.战斗对象出生(包括怪物/分身等)
//			repeated CheckDataUseSkill useSkills = 3;			//2.战斗对象释放技能---判断技能合理性和cd
//			repeated CheckDataAddBuff buffs = 4;				//3.战斗对象产生buff
//			repeated CheckDataAttackDamage attackDamages = 5;	//4.战斗对象输出伤害:释放者,被命中者,关联的技能---与2,3结合,判断伤害是否有效,以及是否超出本人最大输出伤害值
			
			for (BattleFrameCheckData b : up.getFrameDatasList()) {
				max_objBorns += b.getObjBornsCount();
				max_useSkills += b.getUseSkillsCount();
				max_buffs += b.getBuffsCount();
				max_attackDamages += b.getAttackDamagesCount();
				
				for (CheckDataAttackDamage e : b.getAttackDamagesList()) {
					max_buffDamage += e.getBuffDamageCount();
				}
			}
			
			System.out.println("上行战斗校验事件:max_objBorns=" + max_objBorns);
			System.out.println("上行战斗校验事件:max_useSkills=" + max_useSkills);
			System.out.println("上行战斗校验事件:max_buffs=" + max_buffs);
			System.out.println("上行战斗校验事件:max_attackDamages=" + max_attackDamages);
			System.out.println("上行战斗校验事件:max_buffDamage=" + max_buffDamage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void testArrayBlockingQueue() {
		ArrayBlockingQueue<String> list = new ArrayBlockingQueue<>(3);
		for (int i = 0; i < 5; i++) {
			System.out.println("s---" + i);
			boolean rs = false;
			try {
				rs = list.add("ddd:" + i);//当队列满的时候会抛出异常
//				list.put("ddd:" + 1);//当队列满的时候会同步等待(卡住)
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("\t d---" + i + ",rs=" + rs);
		}
		
		System.out.println("开始打印:");
		while(list.isEmpty() == false) {
			try {
				System.out.println(list.take());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void testJavaMemoryVisuality() {
		class A{
			List<Integer> receiveActionInFrameList1 = new ArrayList<>();//玩家一帧里上行的动作记录1
			List<Integer> receiveActionInFrameList2 = new ArrayList<>();//玩家一帧里上行的动作记录2
			List<Integer> receiveActionInFrameList = receiveActionInFrameList1;//当前接收玩家输入的,在1和2来回切换
			List<Integer> receiveActionInFrameCache = receiveActionInFrameList2;//当前准备在帧里处理的,,在1和2来回切换,和receiveActionInFrameList互斥
		};
		
		A a = new A();//玩家a
		
		new Thread() {//线程1:定期插入
			public void run() {
				while(true) {
					int random = 20 + RandomUtil.getRandomInt(0, 20);//随机20~40ms
					a.receiveActionInFrameList.add(random);
					System.out.println("---插入后数量数量=" + a.receiveActionInFrameList.size());
					try {
						Thread.sleep(random);//随机sleep
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread() {//线程2:定期切换和读取
			public void run() {
				while(true) {
					try {
						Thread.sleep(60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					if(a.receiveActionInFrameCache.isEmpty() == false){//已经处理的,清理掉
						a.receiveActionInFrameCache.clear();
					}
					
					//互相掉转
					a.receiveActionInFrameCache = a.receiveActionInFrameList;//当前输入的变成待处理
					if(a.receiveActionInFrameList == a.receiveActionInFrameList1) {
						a.receiveActionInFrameList = a.receiveActionInFrameList2;//当前输入的指向另外一个队里
					}else {
						a.receiveActionInFrameList = a.receiveActionInFrameList1;
					}
					
					System.out.println("待处理数量=" + a.receiveActionInFrameCache.size());
					if(a.receiveActionInFrameCache.size() == 0) {
						System.out.println("###############不应该等于0~~~~~~~~~~");
					}
				}
			}
		}.start();
	}

	private static void checkRedisMiss() {
//		System.out.println("TbPlayerAttainInfoPo_52282775".hashCode());
		List<String> list = FileUtils.readFileReturnList("E:/tempshIE/redisInfo.txt");
		System.out.println("miss.size=" + list.size());
		
		Map<Integer,Integer> counts = new HashMap<>();
		for (String string : list) {
//			System.out.println(string);
			String hachcode = string.substring(string.indexOf("hashCode=") + "hashCode=".length(), string.length() - 2);
//			System.out.println(hachcode);
			
			int index = Math.abs(Integer.parseInt(hachcode))%5;
			Integer myCount = counts.get(index);
			if(myCount == null) {
				myCount = 1;
			}else {
				myCount = 1 + myCount;
			}
			counts.put(index,myCount);
		}
		
		for (Integer index : counts.keySet()) {
			System.out.println("redis.miss=" + index + "/" + counts.get(index));
		}
	}

	private static void printRedisHits() {
		long hits1=9193876L;
		long misses1=3600204;
		
		long hits2=9200834;
		long misses2=3602397;
		
		System.out.println("历史命中=" + 1.0*hits2/(hits2 + misses2));
		System.out.println("新增命中=" + 1.0*(hits2 - hits1)/((hits2 - hits1) + (misses2 - misses1)));
	
	}

	/**
	 * 测试对象数量和内存的关系
	 */
	private static void testObj() {
		int index = 0;
		List<Map<Integer,String>> list = new ArrayList<>();
		while(true){
			Map<Integer,String> map = new HashMap<>();
			list.add(map);
			
			System.out.println("输入命令..."); 
			try {
				for (int i = 0; i < 10000; i++) {
					
//					map.put(i, "测试值:" + i);//不用对象池:内存中的Integer持续增加,并且不能释放
					
					map.put(getIntKey(i), "测试值:" + i);//用对象池:Integer数量维持在1-4万内并且会随着小gc减少
				}
				
				index++;
				System.out.println("测试数量:" + map.size()*index); 
				
				Thread.sleep(10*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**Integer,作为key用*/
	private static Map<Integer, Integer> intKey = new ConcurrentHashMap<>();
	/**
	 * 获取一个Integer key
	 */
	public static Integer getIntKey(int i){
		Integer key = intKey.get(i);
		if(key == null){//方法没有同步,可能重复new,不要紧,重要的是长久效率
			key = i;
			intKey.put(key, key);
		}
		
		return key;
	}

	/**
	 * 用定时器-每次设置定时器
	 */
	private static void testRunByTreadTimerSchedule(int sleep) {
		class TestRun implements Runnable{
			long ti = System.currentTimeMillis();
			@Override
			public void run() {
				if(System.currentTimeMillis() - ti > sleep + 3) {
					System.out.println("ByTreadTimer-Schedule.不准:误差" + (System.currentTimeMillis() - ti - sleep));
				}
				
				ti = System.currentTimeMillis();
				
				ThreadPool.getTimer().schedule(this, sleep, TimeUnit.MILLISECONDS);//用定时器方式
			}
		}
		
		TestRun tr = new TestRun(); 
		ThreadPool.getTimer().schedule(tr, sleep, TimeUnit.MILLISECONDS);//用定时器方式:循环设置定时
	}
	
	/**
	 * 用定时器-一次性设置永久循环
	 */
	private static void testRunByTreadTimerScheduleAtFixedRate(int sleep) {
		class TestRun implements Runnable{
			long ti = System.currentTimeMillis();
			@Override
			public void run() {
				if(System.currentTimeMillis() - ti > sleep + 3) {
					System.out.println("ByTreadTimer-ScheduleAtFixedRate.不准:误差" + (System.currentTimeMillis() - ti - sleep));
				}
				
				ti = System.currentTimeMillis();
				
			}
		}
		
		TestRun tr = new TestRun(); 
		ThreadPool.getTimer().scheduleAtFixedRate(tr, 0,sleep, TimeUnit.MILLISECONDS);//用定时器方式:一次性设置永久循环
	}

	/**
	 * 测试定时循环:用线程sleep
	 */
	private static void testRunByTreadSleep(int sleep) {
		new Thread() {//用线程模式刷新房间
			public void run() {
				long ti = System.currentTimeMillis();
				while (true){
					try {
						Thread.sleep(sleep);
						
//						long now = System.currentTimeMillis();
						if(System.currentTimeMillis() - ti > sleep + 3) {
							System.out.println("ByTreadSleep.不准:误差" + (System.currentTimeMillis() - ti - sleep));
						}
						
						ti = System.currentTimeMillis();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 测试分页
	 */
	private static void testPage() {
		List<String> alls = new ArrayList<>();
		for (int i = 0; i < 97; i++) {
			alls.add("tt-" + i);
		}
		
		int fSize = 10;//分开多个文件保存,每个文件保存多少个
		int page = alls.size()/fSize;
		if(alls.size()%fSize > 0) {
			page++;
		}
		for (int i = 0; i < page; i++) {
			int from = fSize*i;
			int to = from + fSize;
			to = Math.min(to, alls.size());
			List<String> os = alls.subList(from, to);
			System.out.println(i + "=" + os);
		}
	}

	private static void printMysqlInfo() {
//		show global status like 'Com_select';  -- 获得服务器启动到目前查询操作执行的次数；
//		show global status like 'Com_insert';  -- 获得服务器启动到目前插入操作执行的次数；
//		show global status like 'Com_update';  -- 获得服务器启动到目前更新操作执行的次数；
//		show global status like 'Com_delete';  -- 获得服务器启动到目前删除操作执行的次数；
		long Com_select = 714379687L;//  -- 获得服务器启动到目前查询操作执行的次数；
		long Com_insert = 246197479L;//-- 获得服务器启动到目前插入操作执行的次数；
		long Com_update = 315425485L;//-- 获得服务器启动到目前更新操作执行的次数；
		long Com_delete = 72316241L;//-- 获得服务器启动到目前删除操作执行的次数；

		//-- 计算读百分比：
		System.out.println("读百分比:" + 1.0*Com_select / (Com_select+Com_insert+Com_update+Com_delete)* 100 + "%");
		//-- 计算写百分比：
		System.out.println("写百分比:" + 1.0*(Com_insert+Com_update+Com_delete) / (Com_select+Com_insert+Com_update+Com_delete) * 100 + "%");
	}

	private static void testHashSetAndList() {
//		List<String> oldNames = new ArrayList<>();
		HashSet<String> oldNames = new HashSet<>();//set要快很多
		for (int i = 0; i < 100*10000; i++) {
			oldNames.add("你好啊:" + i);
		}
		System.out.println("插入完成:size=" + oldNames.size());
		long ti = System.currentTimeMillis();
		for (int i = 0; i < 101; i++) {
			String name = "你好啊:" + i*10000;
			System.out.println("对比:" + name + "=" + oldNames.contains(name));
		}
		System.out.println("对比完成:耗时=" + (System.currentTimeMillis() - ti));
	}
	
	private static void testRandomName() {
		Map<Integer, ArrayList<String>> randomNames = new ConcurrentHashMap<>();
		char[] ts = {'a','b','c'};
		for (int i = 0; i < ts.length; i++) {//6列,男女各3
			for (int n = 0; n < 100; n++) {
				String name = ts[i] + "-" + n;
				ArrayList<String> list = randomNames.get(i);
				if (list == null) {
					list = new ArrayList<>();
					randomNames.put(i, list);
				}
				
				list.add(name);
			}
		}
		
		ArrayList<String> list0 = randomNames.get(0);
		ArrayList<String> list1 = randomNames.get(1);
		ArrayList<String> list2 = randomNames.get(2);
		System.out.println("list.size:" + list0.size() + "/" + list1.size() + "/" + list2.size());
		Set<String> allNames = new HashSet<>();
		for (String string0 : list0) {
			for (String string1 : list1) {
				for (String string2 : list2) {
//					String name = string0 + string1 + string2;
//					allNames.add(name);
				}
			}
		}
		
		System.out.println("Set.size:" + allNames.size());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		while(true){
			System.out.println("输入命令:<stop/test/cleanalarm/clearGameRedis/testmsg/testmail/other...>"); 
			try {
				String str = br.readLine().trim();
				System.out.println("输入命令:" + str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 测试商店好感度
	 */
	private static void testShopSpecialPer() {
		for (int i = 0; i < 200; i++) {
			int allCount = i/20 + 1;//总数
			System.out.print("假设商品总数=" + allCount + ": \t");
			int notBuyCount = allCount - 1;//未买
			int specialPer = 0;
			for (; notBuyCount >= 0; notBuyCount--) {
				if(notBuyCount == 0) {//最后一个,直接加满不多说
					specialPer = 100;
				}else {
					//老老实实随机吧
					int feerPer = 100 - specialPer;//差多少才满
					if(feerPer > notBuyCount) {
						int addPerMin = 10;//差多少才满
						int addPerMax = feerPer;//差多少才满
						int rIndex = allCount - notBuyCount;
						if(rIndex <= 2) {//前两次随机，都限制在10~30的范围
							addPerMax = Math.min(addPerMax, 30);
						}else {//之后的随机，和自己平分最大值
							addPerMax = addPerMax/(notBuyCount + 1);
						}
						
						if(addPerMin > addPerMax) {
							addPerMin = addPerMax;
						}
						
						int add = RandomUtil.getRandomInt(addPerMin, addPerMax);
						specialPer += add;//不能直接爆满,确保后面起码一个商品最少还有1
						if(specialPer == 100) {
							System.out.print("addPerMin=" + addPerMin + "addPerMax=" + addPerMax + "add=" + add + "add=" + add + "--");
						}
					}else if(feerPer == notBuyCount){//根据前面的规则,起码有1以上的
						specialPer += 1;
						
						if(specialPer == 100) {
							System.out.print("ee=" + specialPer + "\t");
						}
					}else {
						System.out.println("什么鬼,不可能啊,好感度无法分配:specialPer=" + specialPer 
								+ ",notBuyCount=" + notBuyCount);
					}
				}
				
				System.out.print(specialPer + "\t");
			}
			System.out.println();
		}
	}


	static int index = 0;
	static int getRandomInt() {
//		return RandomUtil.getRandomInt(50, 1000);
		
		index++;
		int temp = index%1000;
		
		if(temp >= 40 && temp <= 49) {
			temp = 1;
		}
		else if(temp >= 60 && temp <= 69) {
			temp = 999;
		}
		else {
			temp = 50;
		}
		
		System.out.println(index +":" + temp);
		return temp;
	}
	
	/**
	 * 测试排序
	 */
	private static void testSort() {
		List<Integer> list = new ArrayList<>();
		list.add(3);
		list.add(1);
		list.add(1);
		list.add(2);
		list.add(2);
		list.add(3);
		Collections.sort(list, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
//				return o1 - o2;
				
				if(o1 < o2) {
					return 1;
				}else {
					return -1;
				}
			}
		});
		
//		for (Integer i : list) {
//			System.out.println(i);
//		}
	}

	/**
	 * ����ͬ��
	 */
	private static void testSyn() {
		final Set<Long> matchingPlayers = new LinkedHashSet<>();
		
		new Thread(){//�߳�1:ģ�����
			public void run(){
				while(true){
					try {
						Thread.sleep(10);
						
						//�����߼�
						matchingPlayers.add(System.currentTimeMillis()%Integer.MAX_VALUE);
						
						if(matchingPlayers.size() %100 == 0) {
							System.out.println("add.matchingPlayers.size()=" + matchingPlayers.size());
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
		new Thread(){//�߳�2:ģ��������Ƴ�
			public void run(){
				while(true){
					try {
						Thread.sleep(10);
						
						//�����߼�
						Iterator<Long> iterator = matchingPlayers.iterator();//2.Ϊս��ƥ�����
						while (iterator.hasNext()) {
							iterator.next();
							if(System.currentTimeMillis()%2 == 0) {//���һ��
								iterator.remove();//��ȫ�Ƴ��Ѿ�ƥ������
							}
						}
						
						if(matchingPlayers.size() %100 == 0) {
							System.out.println("remove.matchingPlayers.size()=" + matchingPlayers.size());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	
	}

	/**
	 * ���Էֲ�ʽlogin����id��Ч��
	 */
	private static void testCreatePlayer() {
		//1���,all=:1641,��ʱ:11754
		//2���,all=:1998,��ʱ:13246
				
		Set<Long> allIds = new HashSet<>();
		int c = 1000;//��������
		new Thread(){//����һ����������ȫ����߳�,ÿ��һ��ʱ����Զ����Ӹ��߷ַ�����,��������ʧ��ʱ�Զ�����
			public void run(){
				String url = "rmi://127.0.0.1:6001/login";
				System.out.println("��ʼ��LoginRmi:" + url);
				ILoginRmi loginRmi = (ILoginRmi) Tool.getRmiClient(url, ILoginRmi.class);
				
				long ti = System.currentTimeMillis();
				
				for (int i = 0; i < c; i++) {
//					long id = loginRmi.getNewPlayerId(1000, 100);//�������id
					long id = loginRmi.getUIDs()[0];//����getUIDs()
					allIds.add(id);
					
					if(i%100==0) {
						System.out.println("1����:" + i + "/" + c);
					}
				}
				
				System.out.println("1���,all=:" + allIds.size() + ",��ʱ:" + (System.currentTimeMillis() - ti));
			}
		}.start();
		
		new Thread(){//����һ����������ȫ����߳�,ÿ��һ��ʱ����Զ����Ӹ��߷ַ�����,��������ʧ��ʱ�Զ�����
			public void run(){
				String url = "rmi://10.6.8.98:6001/login";
				System.out.println("��ʼ��LoginRmi:" + url);
				ILoginRmi loginRmi = (ILoginRmi) Tool.getRmiClient(url, ILoginRmi.class);
				
				long ti = System.currentTimeMillis();
				
				for (int i = 0; i < c; i++) {
//					long id = loginRmi.getNewPlayerId(2000, 200);//�������id
					long id = loginRmi.getUIDs()[0];//����getUIDs()
					allIds.add(id);
					
					if(i%100==0) {
						System.out.println("2����:" + i + "/" + c);
					}
				}
				
				System.out.println("2���,all=:" + allIds.size() + ",��ʱ:" + (System.currentTimeMillis() - ti));
			}
		}.start();
	}
	
	private static void testProto() {
		C2STestCode.Builder up = C2STestCode.newBuilder();
		up.setCode("122abcִ������ָ��deie");
		
		byte[] data = up.build().toByteArray();
		System.out.println("data.length=" + data.length);
		try {
			ByteBuf bf1 = Unpooled.buffer();
			bf1.writeByte(3);
			bf1.writeByte(2);
			bf1.writeByte(1);
			bf1.writeBytes(data);
			
			ByteBufInputStream is = new ByteBufInputStream(bf1);
			System.out.println("readByte1=" + is.readByte());
			System.out.println("readByte2=" + is.readByte());
			System.out.println("readByte3=" + is.readByte());
			
			C2STestCode down = C2STestCode.parseFrom(is);//.parseFrom(data);
			System.out.println("getCode=" + down.getCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ����ʱ������
	 */
	public static void testDayReset() {
		int RESET_STAGE_WEEK = 2;//GameSetting.getSetting("RESET_STAGE_WEEK");//ÿ�����ڼ����ã�������Ϊ��һ�죩
		int RESET_STAGE_TIME = 5;//GameSetting.getSetting("RESET_STAGE_TIME");//ÿ�켸������
		int RESET_STAGE_MONTH = 3;//GameSetting.getSetting("RESET_STAGE_MONTH");//ÿ�¼�������
		int hours = DateUtil.getHourNow();//Сʱ
		int day = DateUtil.getDayNow();
		
		
//		int loopType = dacPoObj.getDefine().getLoopType();//��������
//		dacPoObj.getPo().setEditTime(new Date());
		Date date = DateUtil.parseTime("2018-05-01 04:59:00");//����ʱ��:yyyy-MM-dd HH:mm:ss
		
		System.out.println("ÿ�����ڼ�����:\t" + (RESET_STAGE_WEEK - 1));
		System.out.println("ÿ�켸������:\t" + RESET_STAGE_TIME);
		System.out.println("ÿ�¼�������:\t" + RESET_STAGE_MONTH);
		
		System.out.println("����ʱ��:" + DateUtil.format(date, DateUtil.TIME_PATTERN));
		System.out.println("��ǰʱ��:" + DateUtil.format(new Date(), DateUtil.TIME_PATTERN));
		
//		if(loopType == 0) {//��������
//			
//		}else if(loopType == 1) {//ÿ������
			//���һ���ڵ�ĳ��ʱ���ж��Ƿ����������
			if(!DateUtil.isSameDay1(date, new Date(), hours)) {//DateUtil.canReset(date, RESET_STAGE_TIME)
//					dacPoObj.getPo().setEnterCount(0);//����������
//					dacPoObj.save();
				System.out.println("ÿ������:" + true);
			}else {
				System.out.println("ÿ������:" + false);
			}
//		}else if(loopType == 2) {//ÿ������
			if(DateUtil.isMayResetForWeek(date, RESET_STAGE_WEEK, RESET_STAGE_TIME)){
//				dacPoObj.getPo().setEnterCount(0);//����������
//				dacPoObj.save();
				System.out.println("ÿ������:" + true);
			}else {
				System.out.println("ÿ������:" + false);
			}
//		}else if(loopType == 3) {//ÿ������
			if(!DateUtil.isSameMonth(date, new Date())
					&& day >= RESET_STAGE_MONTH ){
				if(day == RESET_STAGE_MONTH && hours >= RESET_STAGE_TIME) {//���ô������õĵ���
//					dacPoObj.getPo().setEnterCount(0);//����������
					System.out.println("ÿ������:" + true);
				}else if(day > RESET_STAGE_MONTH) {//���������õ������κ�ʱ�򶼿�������
//					dacPoObj.getPo().setEnterCount(0);
					System.out.println("ÿ������:" + true);
				}else {
					System.out.println("ÿ������:" + false);
				}
//				dacPoObj.save();
			}else {
				System.out.println("ÿ������:" + false);
			}
//		}
		
	}
	
	public static void testLongToString(){
		int radix = 32;//����
		for (int i = 1; i < 100; i++) {
			long num1 = Long.MAX_VALUE/i;//Long.MAX_VALUE - 13243234;
			String numStr = Long.toString(num1, radix).toUpperCase();//.toUpperCase();
			long num2 = Long.parseLong(numStr, radix);//.toLowerCase()
			
			System.out.println(num1 + "\t" + numStr + "\t" + num2);
		}
	}
	
	/**
	 * ����map��keySet��values��˳��һ����:һ�µ�
	 */
	public static void testMapSetAndValues(){
		Map<Integer,Integer> testMap = new HashMap<>();
		testMap.put(3, 30);
		testMap.put(4, 40);
		testMap.put(2, 20);
		testMap.put(1, 10);
		testMap.put(5, 50);
		
		List<Integer> itemIds = new ArrayList<Integer>();
		List<Integer> itemNums = new ArrayList<Integer>();
		itemIds.addAll(testMap.keySet());
		itemNums.addAll(testMap.values());
		
		for (int i = 0; i < itemIds.size(); i++) {
			System.out.println(itemIds.get(i) + ":" + itemNums.get(i));
		}
	}

}
