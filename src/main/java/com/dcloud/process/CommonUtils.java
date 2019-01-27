/**
 * 
 */
package com.dcloud.process;

import java.math.BigDecimal;

import com.dcloud.bean.DFTItem;
import com.dcloud.bean.Scale;

/**
 * @details （必填）
 * @author 彭堃
 * @date 2016年9月5日下午5:35:21
 */
public class CommonUtils {
	/**
	 * @brief 取浮点型的最大最小整型范围
	 * @details （必填）
	 * @throws Exception
	 * @author 彭堃
	 * @date 2016年9月5日下午5:35:40
	 */
	public static Scale getIntFromFloat(float a){
		new Animal.Head();
		Scale scale=new Scale();
		int inta=(int)a;
		if(a==inta){
			scale.setMax(inta);
			scale.setMin(inta);
		}else{
			scale.setMax((int)Math.ceil(a));
			scale.setMin((int)Math.floor(a));
		}
		return scale;
	}
	/**
	 * @brief 最大化平摊，平均分
	 * @details 例如7分成4分，是2，2，2，1，而不是1，1，1，4。11分成4，4，3,而不是3，3，5
	 * @author 彭堃
	 * @throws Exception 
	 * @date 2016年9月5日下午5:35:40
	 */
	public static int[] splitAll(int all,int count) throws Exception{
		if(all<0||count<=0){
			throw new Exception("all>=0,count>=0");
		}
		int[] res=new int[count];
		for (int i = 0; i < res.length; i++) {
			res[i]=all/count;
		}
		
		int yu=all%count;
		if(yu!=0){
			for (int i = 0; i < yu; i++) {
				res[i]++;
			}
		}
		return res;
	}
	/**
	 * 
	 * @brief double四舍五入int
	 * @details （必填）
	 * @param d
	 * @return
	 * @author 江东
	 * @date 2017年3月28日下午7:57:42
	 */
	public static int doubleToInt(double d){
		return new BigDecimal(d).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
	}
	
	/**
	 * 
	 * @brief double四舍五入int
	 * @details （必填）
	 * @param d
	 * @return
	 * @author 江东
	 * @date 2017年3月28日下午7:57:42
	 */
	public static double doubleTo2(double d){
		return new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	public static void showDFT(DFTItem[] x){
		for (int i = 0; i < x.length; i++) {
			System.out.println(String.format("%.2f,%.2f", x[i].getShishu(),x[i].getXushu()));
		}
	}
	public static void showDFT(DFTItem[][] x){
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++){
				System.out.print(String.format("%.2f,%.2f\t", x[i][j].getShishu(),x[i][j].getXushu()));
			}
			System.out.println();
		}
	}
	public static DFTItem[][] copyDFT(DFTItem[][] x){
		DFTItem[][] res=new DFTItem[x.length][x[0].length];
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[0].length; j++){
				res[i][j]=new DFTItem();
				res[i][j].setShishu(x[i][j].getShishu());
				res[i][j].setXushu(x[i][j].getXushu());
			}
		}
		return res;
	}
	
	/**
	 * 
	 * @brief 最接近2的幂最小值
	 * @details 比如 3->4,12->16
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
		
		return max;
	}
	
	public static void main(String[] args) {
		DFTItem[][] x=new DFTItem[2][2];
		x[0][0]=new DFTItem();
		x[0][1]=new DFTItem();
		x[1][0]=new DFTItem();
		x[1][1]=new DFTItem();
		
		x[0][0].setShishu(1d);
		x[0][1].setShishu(2d);
		x[1][0].setShishu(3d);
		x[1][1].setShishu(4d);
		
		showDFT(x);
	}
}
