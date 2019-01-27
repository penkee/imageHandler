/**
 * 
 */
package com.dcloud.app_dao;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import com.dcloud.bean.Scale;
import com.dcloud.process.CommonUtils;
import com.dcloud.process.DFTImage;
import com.dcloud.process.FourierService;
import com.dcloud.process.OperateImage;

/**
 */
public class TestBu {
	public static void main(String[] args) {
		//3---2次方    1
		//4---2次方    2
		//5,6,7--3次方  2
		
		System.out.println(completionTwo(3));
		System.out.println(completionTwo(4));
		System.out.println(completionTwo(5));
		System.out.println(completionTwo(8));
	}
	
	/**
	 * 
	 * @brief 需要补全多少个
	 * @details （必填）
	 * @param a
	 * @return
	 * @author 江东
	 * @date 2017年8月31日下午2:18:25
	 */
	public static int completionTwo(int a){
		
		int max=1;
		while(a>max){
			max=max*2;
		}
		
		return max-a;
	}
	@Test
	public void test() throws Exception {
		OperateImage image=new OperateImage();
		image.blur("src\\resource\\1.jpg",Color.WHITE, Color.BLACK,"src\\resource\\1res.jpg");
	}
	
	@Test
	public void testDFT() throws Exception {
		DFTImage image =new DFTImage();
		image.genDFT_zf("src\\resource\\c.jpg","src\\resource\\1res.jpg");
	}
	
	@Test
	public void matrixChange() throws Exception {
		OperateImage image=new OperateImage();
		
		float[][] matrix={//放大 
				{2,0,0},
				{0,2f,0},
				{0,0,1}
		};
		
		double s=Math.toRadians(30d);
		float[][] matrix1={//旋转
				{(float)(1/Math.cos(s)-Math.sin(s)*Math.tan(s)),-(float)Math.sin(s),0},
				{(float)Math.sin(s),(float)Math.cos(s),0},
				{0,0,1}
		};
		image.matrixChange("src\\resource\\1.jpg","src\\resource\\1res.jpg",matrix1);
	}
	@Test
	public void huiduChange() throws Exception {
		OperateImage image=new OperateImage();
		
		image.huiduChange("src\\resource\\1.jpg","src\\resource\\1res.jpg");
	}
	
	/**
	 * @brief 空间滤波
	 * @details （必填）
	 * @throws Exception
	 * @author 彭堃
	 * @date 2016年10月15日下午4:22:36
	 */
	
	@Test
	public void testFloat() throws Exception {
		Scale s=CommonUtils.getIntFromFloat(1.5f);
		Assert.assertTrue(s.getMax()==2&&s.getMin()==1);
		
		s=CommonUtils.getIntFromFloat(3f);
		Assert.assertTrue(s.getMax()==3&&s.getMin()==3);
	}
	
	
	@Test
	public void testDB() throws Exception {
		System.out.println(Integer.parseInt("14731681192103623".substring(11))%1000);
	}
	
	ExecutorService pool=Executors.newFixedThreadPool(16);
	
	final AtomicInteger seq=new AtomicInteger(Integer.MIN_VALUE);//2位  0-99
	
	Map<String, String> uidMap=new ConcurrentHashMap<String, String>();
	@Test
	public void testSort() throws InterruptedException{
		long s=System.currentTimeMillis();
		//时间戳+机器码+seq  （保证同一台机器一毫秒内“seq ”不重复）
		for (int i = 1; i <= 200; i++) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					String ss=getSeq(""+new Random().nextInt(2));
					if(uidMap.containsKey(ss)){
						System.out.println(ss+"异常重复"+uidMap.get(ss)+"和"+Thread.currentThread().getName());
						return;
					}else{
						uidMap.put(ss, Thread.currentThread().getName());
					}
				}
			});
		}
		
		pool.awaitTermination(8, TimeUnit.MILLISECONDS);
		System.out.println("耗时"+(System.currentTimeMillis()-s)+",size="+uidMap.size());
		for (Entry e : uidMap.entrySet()) {
			System.out.println(e.getKey());
		}
	}
	/**
	 * @brief machineId+(2位的递增序列)+时间戳(0,length-3)+(1位随机码)+时间戳(length-2,length-1)
	 * @details （必填）
	 * @return
	 * @see HelloWorld （可选。如果在输入参数或者返回值中用到了一些类，可以在这里定义超链接）
	 * @author 彭堃
	 * @date 2016年9月20日下午12:51:46
	 */
	private String getSeq(String machineId){
		String strSeq=String.valueOf(Math.abs(seq.addAndGet(1))%100);//0-99

		if(strSeq.length()<2){
			strSeq="00".substring(0,2-strSeq.length())+strSeq;
		}
		String time=String.valueOf(System.currentTimeMillis());
		
		return machineId+"-"+strSeq+"-"+time.substring(0,time.length()-2)+"-"+new Random().nextInt(10)+"-"+time.substring(time.length()-2);
	}
	
	@Test
	public void testCommonUtils_SplitAll() throws Exception {
		int[] a=CommonUtils.splitAll(4,4);
		Assert.assertTrue(a[0]==1&&a[1]==1&&a[2]==1&&a[3]==1);
		
		a=CommonUtils.splitAll(7,4);
		Assert.assertTrue(a[0]==2&&a[1]==2&&a[2]==2&&a[3]==1);
		
		a=CommonUtils.splitAll(11,3);
		Assert.assertTrue(a[0]==4&&a[1]==4&&a[2]==3);
		
		a=CommonUtils.splitAll(1,3);
		Assert.assertTrue(a[0]==1&&a[1]==0&&a[2]==0);
	}
	
	@Test
	public void testDistPage() throws Exception {
		int[] distDataSize={4,8,9,6,7};//每个数据库记录个数
		
		int pageNum=6;//取第几页
		int pageSize=3;//每页个数
		//需要计算的，每个db的开始位置和取的数量
		DistPage[] dp=new DistPage[distDataSize.length];
		
		//如果直到取pageNum时，每个DB能独立给出页，那么不用计算
		int minCanPageNum=Integer.MAX_VALUE;
 		for (int dbsize : distDataSize) {
			int tmp=dbsize/pageSize;
  			if(tmp<minCanPageNum){
				minCanPageNum=tmp;
			}
		}
		//即每个DB能提供minCanPageNum个页
 		if(minCanPageNum*distDataSize.length>=pageNum){
			int dbi=(pageNum-1)%distDataSize.length;//选中第几个DB
			int pcount=(pageNum-1)/distDataSize.length;//跳过的页
			
			System.out.println("快速db-"+dbi+"查询第"+(pcount*pageSize+1)+"-"+(pageSize+(pcount*pageSize+1)-1));
			return;
		}else{
			if(minCanPageNum>0){
				//初始化dp
 				for (int j = 0; j < dp.length; j++) {
					dp[j]=new DistPage();
					dp[j].distDBID=j;
					dp[j].startIndex=pageSize*(minCanPageNum-1);
 					dp[j].size=pageSize;
					dp[j].remain=distDataSize[j]-pageSize*minCanPageNum;
					dp[j].isNeed=false;
				}
			}else{
				//初始化dp
				for (int j = 0; j < dp.length; j++) {
					dp[j]=new DistPage();
					dp[j].distDBID=j;
					dp[j].startIndex=0;
					dp[j].size=0;
					dp[j].remain=distDataSize[j];
					dp[j].isNeed=false;
				}
			}
		}
		//开始计算部分DB带有提供不足，其他DB补的情况下
		for (int i = minCanPageNum*distDataSize.length+1; i <=pageNum; i++) {
			for (int j = 0; j < dp.length; j++) {
				if(dp[j]!=null){
					dp[j].isNeed=false;
				}
			}
			//当前的头
			int head=(i-1)%distDataSize.length;
			
			//如果当前取整页不足，则下一个去补，直到一个循环
			if(dp[head].remain<pageSize){
				dp[head].startIndex+=dp[head].size;
				dp[head].size=dp[head].remain;
				dp[head].remain=0;
				dp[head].isNeed=true;
				//库存没就不需要此节点提供数据
				if(dp[head].size<=0){
					dp[head].isNeed=false;
				}
				
				int findNext=head;
				int needGet=pageSize-dp[head].size;
				do{
					findNext=(findNext+1)%distDataSize.length;
					if(dp[findNext].remain<=0)continue;//无法提供数据
					
					if(dp[findNext].remain<needGet){
						dp[findNext].startIndex+=dp[findNext].size;
						dp[findNext].size=dp[findNext].remain;
						dp[findNext].remain=0;
						dp[findNext].isNeed=true;
						needGet-=dp[findNext].size;
					}else{
						dp[findNext].startIndex+=dp[findNext].size;
						dp[findNext].size=needGet;
						dp[findNext].remain-=needGet;
						dp[findNext].isNeed=true;
						break;
					}
				}while(head!=findNext);
			}else{
				dp[head].startIndex+=dp[head].size;//加上一个的size得出当前start
				dp[head].size=pageSize;
				dp[head].remain-=pageSize;
				dp[head].isNeed=true;
			}
			
			for (DistPage distPage : dp) {
				if(distPage!=null&&distPage.isNeed)
					System.out.println("查询第  "+i+"页的db-"+distPage.distDBID+"查询第"+distPage.startIndex+"-"+(distPage.size+distPage.startIndex-1));
			}
			System.out.println();
		}
		
		for (DistPage distPage : dp) {
			if(distPage!=null&&distPage.isNeed)
				System.out.println("db-"+distPage.distDBID+"查询第"+distPage.startIndex+"-"+(distPage.size+distPage.startIndex-1));
		}
	}
	
	class DistPage{
		public int distDBID;
		public int startIndex;
		public int size;
		public int remain;
		public boolean isNeed;
	}
}
