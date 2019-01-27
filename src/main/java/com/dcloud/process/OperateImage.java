package com.dcloud.process;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.net.ssl.HttpsURLConnection;

import com.dcloud.bean.DFTItem;
import com.dcloud.bean.Scale;

public class OperateImage{

    public OperateImage() {
		super();
	}

    /**
     * @brief 模糊处理
     * @details （必填）
     * @param file
     * @param srcColor
     * @param targetColor
     * @param targetfile
     * @throws Exception
     * @author 彭堃
     * @date 2016年9月2日下午3:22:42
     */
    public void blur(String file, Color srcColor, Color targetColor,String targetfile) throws Exception{
        URL http;
        if(file.trim().startsWith("https")){
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else if(file.trim().startsWith("http")){
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else{
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        int linklength=3;
        int step=(linklength+1)/2;
        for (int i = 0; i < bi.getWidth(); i+=step) {
            for (int j = 0; j < bi.getHeight(); j+=step){
            	int rgb=bi.getRGB(i,j);
            	
            	int red=(rgb&(255<<16))>>16;
            	int green=(rgb&(255<<8))>>8;
            	int blue=rgb&(255);
            	
            	List<Point> links=ImageUtils.findLink(i,j, bi.getWidth(), bi.getHeight(),linklength);
            	
            	for (Point point : links) {
            		int _rgb=bi.getRGB(point.x,point.y);
                	
                	int _red=(_rgb&(255<<16))>>16;
                	int _green=(_rgb&(255<<8))>>8;
                	int _blue=_rgb&(255);
                	
                	red+=_red;
                	green+=_green;
                	blue+=_blue;
				}
            	red=red/(1+links.size());
            	green=green/(1+links.size());
            	blue=blue/(1+links.size());
                //System.out.println(String.format("red=%s,green=%s,blue=%s",red,green,blue));
                int newRGB=new Color(red, green, blue).getRGB();
                bi.setRGB(i, j, newRGB);
                
                for (Point point : links) {
                	 bi.setRGB(point.x, point.y, newRGB);
				}
            }
        }
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = it.next();
        File f = new File(targetfile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
    }
    
    /**
     * @brief 矩阵变化
     * @details （必填）
     * @param file
     * @param srcColor
     * @param targetColor
     * @param targetfile
     * @throws Exception
     * @author 彭堃
     * @date 2016年9月2日下午3:22:42
     */
    public void matrixChange(String file,String targetfile,float[][] matrix) throws Exception{
        URL http;
        if(file.trim().startsWith("https")){
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else if(file.trim().startsWith("http")){
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else{
            http = new File(file).toURI().toURL();
        }
        BufferedImage biOld = ImageIO.read(http.openStream());
        BufferedImage biNew = new BufferedImage(biOld.getWidth(), biOld.getHeight(), BufferedImage.TYPE_INT_RGB);
        int step=1;
        for (int i = 0; i < biNew.getWidth(); i+=step) {
            for (int j = 0; j < biNew.getHeight(); j+=step){
            	//通过矩阵反向映射找到在原图中位置
            	Float newPoint=ImageUtils.matrixCal(i-biNew.getWidth()/2,j-biNew.getHeight()/2,matrix);
            	newPoint.x+=biNew.getWidth()/2;
            	newPoint.y+=biNew.getHeight()/2;
            	
            	Scale scaleX=CommonUtils.getIntFromFloat(newPoint.x);
            	Scale scaleY=CommonUtils.getIntFromFloat(newPoint.y);
            	
            	int allR=0;int allG=0;int allB=0;
            	
            	int scaleXminlimit=scaleX.getMin()>=0?scaleX.getMin():0;
            	int scaleXmaxlimit=scaleX.getMax()<biOld.getWidth()?scaleX.getMax():biOld.getWidth()-1;
            	
            	int scaleYminlimit=scaleY.getMin()>=0?scaleY.getMin():0;
            	int scaleYmaxlimit=scaleY.getMax()<biOld.getHeight()?scaleY.getMax():biOld.getHeight()-1;
            	
            	int count=0;
            	for (int x = scaleXminlimit; x <= scaleXmaxlimit; x++) {
					for (int y = scaleYminlimit; y <= scaleYmaxlimit; y++) {
						int _rgb=biOld.getRGB(x,y);
	                	
	                	int _red=(_rgb&(255<<16))>>16;
	                	int _green=(_rgb&(255<<8))>>8;
	                	int _blue=_rgb&(255);
	                	
	                	allR+=_red;
	                	allG+=_green;
	                	allB+=_blue;
	                	
	                	count++;
					}
				}
            	int newRGB=Color.BLACK.getRGB();
            	if(count>0){
            		newRGB=new Color(allR/count, allG/count, allB/count).getRGB();
            	}
            	biNew.setRGB(i, j, newRGB);
            }
        }
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = it.next();
        File f = new File(targetfile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(biNew);
        biNew.flush();
        ios.flush();
        ios.close();
    }

	/**
	 * @brief 其他变换
	 * @details （必填）
	 * @param string
	 * @param string2
	 * @author 彭堃
	 * @throws Exception 
	 * @date 2016年10月10日下午8:23:23
	 */
	public void huiduChange(String file, String targetfile) throws Exception {
		// TODO Auto-generated method stub
		URL http;
        if(file.trim().startsWith("https")){
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else if(file.trim().startsWith("http")){
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else{
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        
        int max=0,min=256;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++){
            	int rgb=bi.getRGB(i,j);
            	
            	int red=(rgb&(255<<16))>>16;
            	int green=(rgb&(255<<8))>>8;
            	int blue=rgb&(255);
            	
            	int huidu=(red+green+blue)/3;
            	
            	if(huidu>max)max=huidu;
            	
            	if(huidu<min)min=huidu;
            }
        }
//        //反转
//        for (int i = 0; i < bi.getWidth(); i++) {
//            for (int j = 0; j < bi.getHeight(); j++){
//            	int rgb=bi.getRGB(i,j);
//            	
//            	int red=(rgb&(255<<16))>>16;
//            	int green=(rgb&(255<<8))>>8;
//            	int blue=rgb&(255);
//            	
//            	int huidu=(red+green+blue)/3;
//            	
//            	huidu=max-(huidu-min);
//            	
//            	int newRGB=new Color(huidu, huidu, huidu).getRGB();
//                bi.setRGB(i, j, newRGB);
//            }
//        }
        
        //s=clog(1+r)
//        int c=1;
//        for (int i = 0; i < bi.getWidth(); i++) {
//            for (int j = 0; j < bi.getHeight(); j++){
//            	int rgb=bi.getRGB(i,j);
//            	
//            	int red=(rgb&(255<<16))>>16;
//            	int green=(rgb&(255<<8))>>8;
//            	int blue=rgb&(255);
//            	
//            	int huidu=(red+green+blue)/3;
//            	
//            	double h=(c*   
//            			
//             			(
//             					
//             					(
//             					Math.log(1+200.0*(huidu-min+1)/(max-min+1))/Math.log(201)
//             					)
//             			*(max-min+1)+min));
//            	huidu=(int)h;
//            	
//            	System.out.println(h+","+huidu);
//            	
//            	if(huidu>255)huidu=255;
//            	
//            	int newRGB=new Color(huidu, huidu, huidu).getRGB();
//                bi.setRGB(i, j, newRGB);
//            }
//        }
        
////      //比特平面分层
//        int ceng=6;
//        for (int i = 0; i < bi.getWidth(); i++) {
//	          for (int j = 0; j < bi.getHeight(); j++){
//	          	int rgb=bi.getRGB(i,j);
//	          	
//	          	int red=(rgb&(255<<16))>>16;
//	          	int green=(rgb&(255<<8))>>8;
//	          	int blue=rgb&(255);
//	          	
//	          	int huidu=(red+green+blue)/3;
//	          	
//	          	huidu=huidu&((1<<ceng)-1);//去掉高位
//	          	if(huidu>=1<<(ceng-1)){
//	          		huidu=255;
//	          	}else{
//	          		huidu=0;
//	          	}
//	          	
//	          	int newRGB=new Color(huidu, huidu, huidu).getRGB();
//	              bi.setRGB(i, j, newRGB);
//	          }
//      	}
////      //比特平面分层叠加-7-8
//        for (int i = 0; i < bi.getWidth(); i++) {
//	          for (int j = 0; j < bi.getHeight(); j++){
//	          	int rgb=bi.getRGB(i,j);
//	          	
//	          	int red=(rgb&(255<<16))>>16;
//	          	int green=(rgb&(255<<8))>>8;
//	          	int blue=rgb&(255);
//	          	
//	          	int huidu=(red+green+blue)/3;
//	          	//huidu&11000000
//	          	huidu=huidu&11100000;//去掉低位精度
//	          	
//	          	int newRGB=new Color(huidu, huidu, huidu).getRGB();
//	              bi.setRGB(i, j, newRGB);
//	          }
//      	}
        //直方图变换
        double[] p=new double[256];
        int m_n=bi.getWidth()*bi.getHeight();
        //计算nk/mn
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++){
            	int rgb=bi.getRGB(i,j);
            	
            	int red=(rgb&(255<<16))>>16;
            	int green=(rgb&(255<<8))>>8;
            	int blue=rgb&(255);
            	
            	int huidu=(red+green+blue)/3;
            	p[huidu]++;
            }
        }
//        //计算s,T(r)
        for (int i = 0; i < p.length; i++) {
        	if(i>0){
        		p[i]=p[i]/m_n+p[i-1]/255;
        	}else{
        		p[i]=p[i]/m_n;
        	}
        	p[i]=p[i]*255;
        }
        //直方图均衡变换
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++){
            	int rgb=bi.getRGB(i,j);
            	
            	int red=(rgb&(255<<16))>>16;
            	int green=(rgb&(255<<8))>>8;
            	int blue=rgb&(255);
            	
            	int huidu=(red+green+blue)/3;
            	huidu=(int)(p[huidu]+0.5);
	          	int newRGB=new Color(huidu, huidu, huidu).getRGB();
	            bi.setRGB(i, j, newRGB);
            }
        }
       
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = it.next();
        File f = new File(targetfile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
	}
	
	/**
	 * 通用的操作图片
	 *  
	 * @param harg 滤波参数
	 * added by Buke at 2017年7月16日
	 * @throws Exception 
	 */
	public void operate(String file, String targetfile,ImageHandler handler) throws IOException {
		// TODO Auto-generated method stub
		URL http;
        if(file.trim().startsWith("https")){
            http = new URL(file);
            HttpsURLConnection conn = (HttpsURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else if(file.trim().startsWith("http")){
            http = new URL(file);
            HttpURLConnection conn = (HttpURLConnection) http.openConnection();
            conn.setRequestMethod("GET"); 
        }else{
            http = new File(file).toURI().toURL();
        }
        BufferedImage bi = ImageIO.read(http.openStream());
        
        int[][] image=new int[bi.getHeight()][bi.getWidth()];
        
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++){
            	int rgb=bi.getRGB(i,j);//代表坐标，不是第x行y列
            	
            	int red=(rgb&(255<<16))>>16;
            	int green=(rgb&(255<<8))>>8;
            	int blue=rgb&(255);
            	
            	int huidu=(red+green+blue)/3;
            	image[j][i]=huidu;
            }
        }
        
        image=handler.process(image);
        
        bi = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);
        
      //找出最大值和最小值，转化成0-255之间
        int max=-1,min=Integer.MAX_VALUE;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++){
            	if(image[i][j]>max){
            		max=image[i][j];
            	}
            	
            	if(image[i][j]<min){
            		min=image[i][j];
            	}
            }
        }
       
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++){
            	int huidu=ImageUtils.getValidHuidu(image[i][j],min,max);
            	
            	int newRGB=new Color(huidu, huidu, huidu).getRGB();
            	
            	bi.setRGB(j, i, newRGB);
            }
        }
        
       
        Iterator<ImageWriter> it = ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter writer = it.next();
        File f = new File(targetfile);
        ImageOutputStream ios = ImageIO.createImageOutputStream(f);
        writer.setOutput(ios);
        writer.write(bi);
        bi.flush();
        ios.flush();
        ios.close();
	}
}