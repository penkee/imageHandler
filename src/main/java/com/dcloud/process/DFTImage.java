package com.dcloud.process;

import java.io.IOException;

import com.dcloud.bean.DFTItem;

public class DFTImage{

    public DFTImage() {
		super();
	}

	/** DFT变换的振幅
	 * @details （必填）
	 * @param string
	 * @param string2
	 * @author 彭堃
	 * @throws Exception 
	 * @date 2016年10月15日下午4:22:51
	 */
	public void genDFT_zf(String file, String targetfile) throws Exception {
		new OperateImage().operate(file, targetfile, new ImageHandler() {
			@Override
			public int[][] process(int[][] image) {
				int bu_M=CommonUtils.completionTwo(image.length);
				int bu_N=CommonUtils.completionTwo(image[0].length);
				
				int[][] imageRes=new int[bu_M][bu_N];
				DFTItem[][] imageItem=new DFTItem[bu_M][bu_N];
		        for (int i = 0; i < bu_M; i++) {
		            for (int j = 0; j < bu_N; j++){
		            	
		            	int huidu=0;
		            	if(i<image.length&&j<image[0].length){
		            		huidu=image[i][j];//代表坐标，不是第x行y列
		            		huidu=huidu*((i+j)%2==0?1:-1);//中心化处理  (-1)的x+y
		            	}
		            	
		            	imageItem[i][j]=new DFTItem(huidu*1.0,1.0);
		            }
		        }
		        
		        FastFourierService fastFourierService=new FastFourierService();
		        
		        DFTItem[][] zfRes=fastFourierService.calErweiFFT(imageItem);
        
		        //找出最大值和最小值，转化成0-255之间
		        int max=-1,min=Integer.MAX_VALUE;
		        for (int i = 0; i < zfRes.length; i++) {
		            for (int j = 0; j < zfRes[0].length; j++){
		            	int dftHuiDu=CommonUtils.doubleToInt(Math.sqrt(Math.pow(zfRes[i][j].getShishu(), 2)+Math.pow(zfRes[i][j].getXushu(), 2)));
		            	if(dftHuiDu>max){
		            		max=dftHuiDu;
		            	}
		            	
		            	if(dftHuiDu<min){
		            		min=dftHuiDu;
		            	}
		            	imageRes[i][j]=dftHuiDu;
		            }
		        }
       
		        //对数变换，提高亮度
		        for (int i = 0; i < zfRes.length; i++) {
		            for (int j = 0; j < zfRes[0].length; j++){
		            	
		            	int huidu=ImageUtils.getValidHuidu(imageRes[i][j],min,max);
		            	
		            	int c=1;
		            	double h=(c*   
		             			(
		             					(
		             					Math.log(1+1500.0*(huidu-min+1)/((max-min+1)*1.0))/Math.log(1501)
		             					)
		             			*(max-min+1)+min));
		            	
		            	huidu=ImageUtils.getValidHuidu(CommonUtils.doubleToInt(h));
		            	imageRes[i][j]=huidu;
		            }
		        }
		        return imageRes;
			}
		});
	}
	/**
	 * 频率域滤波,非通用
	 * 理想低通滤波器
	 * @param _D_0 边界
	 * added by Buke at 2017年7月16日
	 * @throws Exception 
	 */
	public void frequencyFilter_ILPF(String file, String targetfile,final int _D_0) throws IOException {
		
		new OperateImage().operate(file, targetfile, new ImageHandler() {
			
			@Override
			public int[][] process(int[][] image) {
				int bu_M=CommonUtils.completionTwo(image.length*2);
				int bu_N=CommonUtils.completionTwo(image[0].length*2);
				
		        int[][] imageBig=new int[bu_M][bu_N];
		        
		        DFTItem[][] imageItem=new DFTItem[bu_M][bu_N];
		        for (int i = 0; i < imageBig.length; i++) {
		            for (int j = 0; j < imageBig[0].length; j++){
		            	int huidu=0;//代表坐标，不是第x行y列
		            	
		            	if(i<image.length&&j<image[0].length){
		            		huidu=image[i][j];
		            	}
		            	
		            	imageBig[i][j]=huidu*((i+j)%2==0?1:-1);//中心化处理  (-1)的x+y
		            	
		            	imageItem[i][j]=new DFTItem(imageBig[i][j]*1.0,0);
		            }
		        }
		        long s=System.currentTimeMillis();
		        FastFourierService fastFourierService=new FastFourierService();
		        
		        
		        DFTItem[][] dftRes=fastFourierService.calErweiFFT(imageItem);
		        System.out.println("DFT消耗："+(System.currentTimeMillis()-s)+"ms");
		        //开始滤波, H和F的阵列相乘
		        for (int u = 0; u < dftRes.length; u++) {
					for (int v = 0; v < dftRes[0].length; v++) {
						//计算D(u,v)
						int d=CommonUtils.doubleToInt(Math.sqrt(Math.pow(u-bu_M/2, 2)+Math.pow(v-bu_N/2, 2)));
		            	
						if(d>_D_0){
							//其实此处是复乘，但是能看出是0
							dftRes[u][v].setShishu(0);
					        dftRes[u][v].setXushu(0);
						}
					}
				}
		        
		        //IDFT还原图像
		        DFTItem[][] preImage= fastFourierService.calErweiIFFT(dftRes);
		        for (int x = 0; x < dftRes.length; x++) {
					for (int y = 0; y < dftRes[0].length; y++) {

						imageBig[x][y]=(int) preImage[x][y].getShishu();//文中说用实数表示，虚数一般是无意义的的舍入差造成
						
						imageBig[x][y]=imageBig[x][y]*((x+y)%2==0?1:-1);//中心化处理  (-1)的x+y
						
						if(x<image.length&&y<image[0].length){
		            		image[x][y]=imageBig[x][y];
		            	}
					}
				}
		        
		        return image;
			}
		});
	}
	
	/**
	 * 频率域滤波,非通用
	 * 布特沃斯低通滤波器
	 * @param _D_0 边界
	 * added by Buke at 2017年7月16日
	 * @throws Exception 
	 */
	public void frequencyFilter_BLPF(String file, String targetfile,final int _D_0) throws IOException {
		
		new OperateImage().operate(file, targetfile, new ImageHandler() {
			
			@Override
			public int[][] process(int[][] image) {
				int bu_M=CommonUtils.completionTwo(image.length*2);
				int bu_N=CommonUtils.completionTwo(image[0].length*2);
				
		        int[][] imageBig=new int[bu_M][bu_N];
		        
		        DFTItem[][] imageItem=new DFTItem[bu_M][bu_N];
		        for (int i = 0; i < imageBig.length; i++) {
		            for (int j = 0; j < imageBig[0].length; j++){
		            	int huidu=0;//代表坐标，不是第x行y列
		            	
		            	if(i<image.length&&j<image[0].length){
		            		huidu=image[i][j];
		            	}
		            	
		            	imageBig[i][j]=huidu*((i+j)%2==0?1:-1);//中心化处理  (-1)的x+y
		            	
		            	imageItem[i][j]=new DFTItem(imageBig[i][j]*1.0,0);
		            }
		        }
		        long s=System.currentTimeMillis();
		        FastFourierService fastFourierService=new FastFourierService();
		        
		        
		        DFTItem[][] dftRes=fastFourierService.calErweiFFT(imageItem);
		        System.out.println("DFT消耗："+(System.currentTimeMillis()-s)+"ms");
		        //开始滤波, H和F的阵列相乘
		        for (int u = 0; u < dftRes.length; u++) {
					for (int v = 0; v < dftRes[0].length; v++) {
						//计算D(u,v)
						int d=CommonUtils.doubleToInt(Math.sqrt(Math.pow(u-bu_M/2, 2)+Math.pow(v-bu_N/2, 2)));
		            	
						double _Huv=1/(1+Math.pow(d/_D_0*1.0,2));
						
						dftRes[u][v].setShishu(dftRes[u][v].getShishu()*_Huv);
						dftRes[u][v].setXushu(dftRes[u][v].getXushu()*_Huv);
					}
				}
		        
		        //IDFT还原图像
		        DFTItem[][] preImage= fastFourierService.calErweiIFFT(dftRes);
		        for (int x = 0; x < dftRes.length; x++) {
					for (int y = 0; y < dftRes[0].length; y++) {

						imageBig[x][y]=(int) preImage[x][y].getShishu();//文中说用实数表示，虚数一般是无意义的的舍入差造成
						
						imageBig[x][y]=imageBig[x][y]*((x+y)%2==0?1:-1);//中心化处理  (-1)的x+y
						
						if(x<image.length&&y<image[0].length){
		            		image[x][y]=imageBig[x][y];
		            	}
					}
				}
		        
		        return image;
			}
		});
	}
}