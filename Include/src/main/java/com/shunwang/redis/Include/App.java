package com.shunwang.redis.Include;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * Hello world!
 *
 */
public class App {
	
    // 数量
    public final static int LIST_LEN = 30;
    
    // 详情页和播放页列表key
    public final static String REDIS_KEY_RECOMMEND_LIST = "recommend_list";
    
    // 热门影视推荐列表
    public final static String REDIS_KEY_RECOMMEND_HOT_LIST = "recommend_list_hot_1";
    
    // 搜索前十
    public final static String REDIS_KEY_SEARCH_LIST = "search_list";
    
    // 自有cp前缀
    public final static String CP_ZIYOU_KEY_PREFIX = "z";
    
    // 第三方cp前缀
    public final static String CP_THIRD_KEY_PREFIX = "t";
    
    // 字段值分割符
    public final static String FILED_SPLIT_STR = "_";
    
    // 行值分割符
    public final static String LINE_SPLIT_STR = "\t";
    
    // 字符串值分割
    public final static String VALUE_SPLIT_STR = ",";
    
    public final static String MAP_KEY_SPLIT_STR = ":";
    
    public static void main( String[] args)
    {
    	args = new String[]{"192.168.22.247","6379","0", "",""};
    	//args = new String[]{"127.0.0.1","6379","0", "",""};
    	
    	String ip = args[0];
    	String port = args[1];
    	String password = args[2];
    	String toDO = args[3];  //
    	String path = "";
    	if (args.length > 4) {
    		path = args[4];
    	}
    	RedisPool redis =new RedisPool(ip, Integer.parseInt(port), password);
    	//readFile1(args[3]);
    	//RedisPool redis =new RedisPool("192.168.25.147",6379,"0");
    	//getData("qg:1");
    	//readFile1("E:\\recommend_hot.hot");
    	
    	readFile1("D:\\hot\\hot_random2.txt");
    	//readFile("D:\\hot\\sim_random2.txt");
    	
    	
    	if ("-rm".equals(toDO)) {
    		clear();
    	}
    	// 搜索
    	else if ("-r1".equals(toDO)) {
    		readSearchHot(path);
    	}
    	// 推荐
    	else if ("-r2".equals(toDO)) {
    		readFile1(path);
    		
    	}
    	// 猜你喜欢
    	else if ("-r3".equals(toDO)) {
    		readFile(path);
    	}
    }
    
    public static void getData(String key) {
    	                                                                     
    	Jedis jedis = RedisPool.getJedis();
    	
    	try{
    		String line = jedis.hget(REDIS_KEY_RECOMMEND_HOT_LIST, key);
    		if(line == null || line.length() == 0){
    			return;
    		}
    		String [] data = line.split(LINE_SPLIT_STR);
    		String recommend [] = data[1].split(VALUE_SPLIT_STR);
    		List<String> list = new ArrayList<String>(Arrays.asList(recommend));
    		for (String s : list) {
    			System.out.println(s);
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally{
    		if (jedis != null) {
    			jedis.close();
    		}
    	}
    }
    
    public static final void clear(){
    	Jedis jedis = RedisPool.getJedis();
    	try {
    		jedis.del(REDIS_KEY_RECOMMEND_LIST, REDIS_KEY_RECOMMEND_HOT_LIST, REDIS_KEY_SEARCH_LIST);
    		System.out.println("大数据==================清除成功=========================");
    	}catch (Exception e) {
    		System.out.println("大数据==================清除出错=========================");
    	}finally{
    		jedis.close();
    	}
    }
    
	public static final void readSearchHot(String path){

		Jedis jedis = RedisPool.getJedis();
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
			br = new BufferedReader(isr);
			String line = br.readLine();
			System.out.println("读取搜索热数据："+line);
			jedis.set(REDIS_KEY_SEARCH_LIST, line);
			System.out.println("搜索热数据==================导入成功=========================");
		} catch (FileNotFoundException e) {
			System.out.println("搜索热数据==================文件不存在=========================");
		}catch (IOException e) {
			System.out.println("搜索热数据==================解析出错=========================");
		}finally{
			try{ 
				if (br!=null) {
					br.close();
				}
			} catch (IOException e) {
			}
			jedis.close();
		}
	}

	/**
	 * 读取文件热数据推荐hot
	 * @title readFile
	 * @param jedis
	 * @return void    返回类型
	 */
	public static final void readFile1(String path){
		Jedis jedis = RedisPool.getJedis();
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
			br = new BufferedReader(isr);
			String line = br.readLine();
			String [] s = null;
			Map<String, String> mapPro = new HashMap<String, String>();
			System.out.println("大数据推荐数据==================开始读取=========================");
			while ((line = br.readLine()) != null) {
			   s = line.split(LINE_SPLIT_STR);
			   String pro_name = s[0];
			   String type_name = s[1];
			   String length = s[2];
			   String video_list = s[3];
			   
			   mapPro.put(pro_name + MAP_KEY_SPLIT_STR + type_name, length + LINE_SPLIT_STR + video_list);
			}
			
			jedis.hmset(REDIS_KEY_RECOMMEND_HOT_LIST, mapPro);
			System.out.println("大数据推荐数据==================导入成功=========================");
		} catch (FileNotFoundException e) {
			System.out.println("大数据推荐数据==================文件不存在=========================");
		}catch (IOException e) {
			System.out.println("大数据推荐数据==================解析出错=========================");
		}finally{
			try{ 
				if (br!=null) {
					br.close();
				}
			} catch (IOException e) {
			}
			jedis.close();
		}
	}
	
	/**
	 * 读取文件相关推荐
	 * @title readFile
	 * @param jedis
	 * @return void    返回类型
	 */
	public static final void readFile(String path){
		Jedis jedis = RedisPool.getJedis();
		BufferedReader br = null;
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
			br = new BufferedReader(isr);
			String line = br.readLine();
			int i = 0;
			String [] s = null;
			Map<String, String> map = new HashMap<String, String>();
			while ((line = br.readLine()) != null) {
			   i++;
			   s = line.split(LINE_SPLIT_STR);
			   map.put(s[0], s[1] + LINE_SPLIT_STR + s[2]);
			   if (i%5000 == 0) {
				   jedis.hmset(REDIS_KEY_RECOMMEND_LIST, map);
				   map = new HashMap<String, String>();
				   System.out.println("影片猜你喜欢开始导入=======>"+line);
			   }
			}
			
			if (i%5000 != 0) {
				jedis.hmset(REDIS_KEY_RECOMMEND_LIST, map);
			}
			System.out.println("影片猜你喜欢数据==================导入成功=========================");
		} catch (FileNotFoundException e) {
			System.out.println("影片猜你喜欢数据==================文件不存在=========================");
		}catch (IOException e) {
			System.out.println("影片猜你喜欢数据==================解析出错=========================");
		}finally{
			try {
				br.close();
			} catch (IOException e) {
			}
			jedis.close();
		}
	}
}
