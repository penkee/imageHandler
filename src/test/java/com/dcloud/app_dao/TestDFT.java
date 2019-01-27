/**
 * 
 */
package com.dcloud.app_dao;

import org.junit.Test;

import com.dcloud.bean.DFTItem;
import com.dcloud.process.CommonUtils;
import com.dcloud.process.DFTImage;
import com.dcloud.process.FourierService;

/**
 * @brief 这是一个Java API注解的例子（必填）
 * @details （必填）
 * @author 江东
 * @date 2017年3月28日下午5:28:56
 */
public class TestDFT {
	
	@Test
	public void calDFT(){
		// TODO Auto-generated method stub
				int[][] image={{1,1,1,1},
						{1,1,1,1},
						{1,1,1,1},
						{1,1,1,1},
				};
				System.out.println("开始计算DFT...");
				
				DFTItem[][] dft=new DFTItem[image.length][image[0].length];
				
				for (int u = 0; u < image.length; u++) {
					for (int v = 0; v < image[0].length; v++) {

						DFTItem res=FourierService.calDFT(u, v, image);
						dft[u][v]=res;
					}
				}
				
				CommonUtils.showDFT(dft);
				
				System.out.println("开始计算IDFT...");
				
				DFTItem[][] idft=new DFTItem[image.length][image[0].length];
				for (int x = 0; x < image.length; x++) {
					for (int y = 0; y < image[0].length; y++) {

						DFTItem res=FourierService.calIDFT(x, y, dft);
						idft[x][y]=res;
					}
				}
				CommonUtils.showDFT(idft);
	}
	
	/**
	 * DFT图
	 *  
	 * @throws Exception 
	 */
	@Test
	public void genDFT_zf() throws Exception{
		
		DFTImage image =new DFTImage();
		image.genDFT_zf("src\\resource\\bit-8.jpg","src\\resource\\bit-8res.jpg");
	}
	
	/**
	 * 频率域滤波
	 *  
	 * g(x,y)=ξ(H(u,v)F(u,v))
	 * 
	 * H(u,v)=0{u=v=0},1{u!=0,v!=0}
	 * added by Buke at 2017年7月16日
	 * @throws Exception 
	 */
	@Test
	public void frequencyFilter_ILPF() throws Exception{
		
		DFTImage image =new DFTImage();
		image.frequencyFilter_ILPF("src\\resource\\1.jpg","src\\resource\\1res.jpg",40);
	}
	
	/**
	 * 频率域滤波
	 *  
	 * g(x,y)=ξ(H(u,v)F(u,v))
	 * 
	 * H(u,v)=0{u=v=0},1{u!=0,v!=0}
	 * added by Buke at 2017年7月16日
	 * @throws Exception 
	 */
	@Test
	public void frequencyFilter_BLPF() throws Exception{
		System.out.println("BLPF1");
		DFTImage image =new DFTImage();
		image.frequencyFilter_BLPF("src\\resource\\21.jpg","src\\resource\\21res.jpg",160);
	}
	
}
