package com.hanaone.tplt.util;

import java.util.Random;

public class ColorUtils {
	public static int randomColor(){
		Random r = new Random();
		int temp = r.nextInt(360);

		
		float H = (float)temp;
		float S = 0.25f;
		float V = 0.5f;
		
		float C = V * S;
		float X = C * (1 - (float)Math.abs(((int)H/60)%2 - 1));
		float m = V - C;

		float R = 0.0f;
		float G = 0.0f;
		float B = 0.0f;
		if(H >=0.0f && H < 60.0f){
			R = C;
			G = X;
			B = 0;
		} else if(H >= 60.0f && H < 120.0f){
			R = X;
			G = C;
			B = 0;
		} else if(H >= 120.0f && H < 180.0f){
			R = 0;
			G = C;
			B = X;
		} else if(H >= 180.0f && H < 240.0f){
			R = 0;
			G = X;
			B = C;
		} else if(H >= 240.0f && H < 300.0f){
			R = X;
			G = 0;
			B = C;
		} else if(H >= 300.0f && H < 360.0f){
			R = C;
			G = 0;
			B = X;
		}

		R = (R + m) * 255.0f;
		G = (G + m) * 255.0f;
		B = (B + m) * 255.0f;	
		
		
		int A = 0xff;
		return A << 24 | (int)R << 16 | (int)G << 8 | (int)B;
	}
	
	
}
