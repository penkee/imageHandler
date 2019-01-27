/**
 * 
 */
package com.dcloud.process;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.dcloud.bean.DFTItem;

/**
 * @brief 傅立叶变换服务
 * @details  DFT
 * @author 江东
 * @date 2017年3月28日下午3:38:05
 */
public class FourierService {
	/**
	 * 
	 * @brief DFT变换，值是振幅
	 * @details
	 * 		 v^2 + u^2开平方，可以表示振幅。夹角可以表示相位。
			  频谱图一般可以分为振幅频谱和相位频谱。
	 * @param image
	 * @return
	 * @author 江东
	 * @date 2017年3月28日下午4:48:43
	 */
	public static int[][] changeZFByDFT(int[][] image){
		int[][] res=new int[image.length][image[0].length];
		
		for (int u = 0; u < res.length; u++) {
			for (int v = 0; v < res[0].length; v++) {
				DFTItem dftRes= calDFT(u,v,image);
				res[u][v]= CommonUtils.doubleToInt(Math.sqrt(Math.pow(dftRes.getShishu(), 2)+Math.pow(dftRes.getXushu(), 2)));
			}
		}
		return res;
	}
	
	/**
	 * 
	 * @brief DFT变换，值是振幅
	 * @details
	 * 		 v^2 + u^2开平方，可以表示振幅。夹角可以表示相位。
			  频谱图一般可以分为振幅频谱和相位频谱。
	 * @param image
	 * @return
	 * @author 江东
	 * @date 2017年3月28日下午4:48:43
	 */
	public static DFTItem[][] changeByDFT(int[][] image){
		DFTItem[][] res=new DFTItem[image.length][image[0].length];
		
		for (int u = 0; u < res.length; u++) {
			for (int v = 0; v < res[0].length; v++) {
				long s=System.currentTimeMillis();
				DFTItem dftRes= calDFT(u,v,image);
				
				System.out.println(System.currentTimeMillis()-s);
				res[u][v]= dftRes;
			}
		}
		return res;
	}
	
	/**
	 * @brief 计算DFT
	 * @details 离散
	 * @param u
	 * @param v
	 * @param image
	 * @return DFTItem
	 * @author 江东
	 * @date 2017年3月28日下午5:26:31
	 */
	public static DFTItem calDFT(int u, int v, int[][] image) {
		// TODO Auto-generated method stub
		int M=image.length,N=image[0].length;
		
		DFTItem dfiItem=new DFTItem();
		
		for (int x = 0; x < M; x++) {
			for (int y = 0; y < N; y++) {
				if(u*x==0&&v*y==0){
					dfiItem.setShishu(dfiItem.getShishu()+image[x][y]*1);
					continue;
				}
				double k=u*x/(M*1.0)+v*y/(N*1.0);
				double w=-2*3.14*k;
				w=CommonUtils.doubleTo2(w);
				
				double cw=0,sw=0;
				if(cmap.get(w)==null){
					cw=Math.cos(w);
					cmap.put(w, cw);
				}else{
					cw=cmap.get(w);
				}
				
				if(smap.get(w)==null){
					sw=Math.sin(w);
					smap.put(w, sw);
				}else{
					sw=smap.get(w);
				}
				
				
				dfiItem.setShishu(dfiItem.getShishu()+image[x][y]*cw);
				dfiItem.setXushu(dfiItem.getXushu()+image[x][y]*sw);
			}
		}
		return dfiItem;
	}
	
	static Map<Double,Double> cmap=new HashMap<Double,Double>();
	static Map<Double,Double> smap=new HashMap<Double,Double>();
	/**
	 * @brief 计算IDFT
	 * @details 离散
	 * @param x
	 * @param y
	 * @param F(u,v)
	 * @return DFTItem
	 * @author 江东
	 * @date 2017年3月28日下午5:26:31
	 */
	public static DFTItem calIDFT(int x, int y, DFTItem[][] Fuv) {
		int M=Fuv.length,N=Fuv[0].length;
		
		DFTItem dfiItem=new DFTItem();
		
		for (int u = 0; u < M; u++) {
			for (int v = 0; v < N; v++) {
				double w=2*3.14*(u*x/(M*1.0)+v*y/(N*1.0));
				
				double shibu=(Fuv[u][v].getShishu()*Math.cos(w)-Fuv[u][v].getXushu()*Math.sin(w))/(M*N*1.0);
				double xubu=(Fuv[u][v].getShishu()*Math.sin(w)+Fuv[u][v].getXushu()*Math.cos(w))/(M*N*1.0);
				//System.out.println(String.format("x=%s,y=%s,实部=%s,虚部=%s", x,y,shibu,xubu));
				dfiItem.setShishu(dfiItem.getShishu()+shibu);
				dfiItem.setXushu(dfiItem.getXushu()+xubu);
			}
		}	
		//System.out.println(String.format("x=%s,y=%s,实部=%s,虚部=%s", x,y,dfiItem.getShishu(),dfiItem.getXushu()));
		return dfiItem;
	}

	/**
	 * 
	 * @brief DFT变换，值是相位
	 * @details
	 * 		 v^2 + u^2开平方，可以表示振幅。夹角可以表示相位。
			  频谱图一般可以分为振幅频谱和相位频谱。
	 * @param image
	 * @return
	 * @author 江东
	 * @date 2017年3月28日下午4:48:43
	 */
	public static int[][] changeXWByDFT(int[][] image){
		int[][] res=new int[image.length][image[0].length];
		
		for (int u = 0; u < res.length; u++) {
			for (int v = 0; v < res[0].length; v++) {
				
			}
		}
		return res;
	}

}
