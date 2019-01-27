package com.dcloud.app_dao;

import com.dcloud.bean.DFTItem;
import com.dcloud.process.CommonUtils;
import com.dcloud.process.FastFourierService;

public class TestErweiFFT {

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		int M = 256,N=256;
		DFTItem[][] x = new DFTItem[M][N];

		int count=0;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++){
				x[i][j] = new DFTItem(count++,0);
			}
		}
		
		FastFourierService fastFourierService=new FastFourierService();
		System.out.println("==================x序列============================");
		CommonUtils.showDFT(x);
		System.out.println("====================FFT序列==========================");
		long s=System.currentTimeMillis();
		DFTItem[][] _X = fastFourierService.calErweiFFT(x);
		System.out.println("FFT消耗"+(System.currentTimeMillis()-s)+"ms");
		CommonUtils.showDFT(_X);
		System.out.println("===================还原x序列===========================");
		
		DFTItem[][] preX=fastFourierService.calErweiIFFT(_X);
		CommonUtils.showDFT(preX);

	}
}