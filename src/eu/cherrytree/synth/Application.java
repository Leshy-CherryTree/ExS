/****************************************/
/* Application.java						*/
/* Created on: 12-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Application
{		
	public static void main(String[] args)
	{
//		double d_pi = -Math.PI * 0.01f;
//		float f_pi = (float) -Math.PI * 0.01f;;
//
//		System.out.println("f: " + f_pi);
//		System.out.println("d: " + d_pi);
//		
////		int floatBits = Float.floatToIntBits(f_pi);
////		String floatBitsStr = Integer.toBinaryString(floatBits );
////		System.out.println(floatBitsStr);
//		
//		for (int i = 31 ; i >= 0 ; i--)
//		{
//			int v = Float.floatToIntBits(f_pi);
//			byte val = (byte) ((v >> i) & 1);
//			
//			if (i == 30 || i == 22)
//				System.out.print(" ");
//			
//			System.out.print(val);
//		}
//		
//		System.out.println();
//		
//		long ln = 0;
//		
////		long doubleBits = Double.doubleToLongBits(d_pi);
////		String doubleBitsStr = Long.toBinaryString(doubleBits);
////		System.out.println(doubleBitsStr);
//						
//		int m_l = 2;
//		int e_l = 9;
//
//		for (int i = 63 ; i >= 0 ; i--)
//		{
//			long v = Double.doubleToLongBits(d_pi);
//			byte val = (byte) ((v >> i) & 1);
//			
//			System.out.print(val);
//			
//			ln = ln << 1;
//			
//			if (i == 63 || i == 62 || (i < 52 + e_l && i > 52 - e_l))
//				ln = ln ^ val;															
//		}
//		
//		System.out.println();
//		
//		System.out.println(Long.toBinaryString(ln));
//		double n = Double.longBitsToDouble(ln);
//		System.out.println("d: " + n);
//		
//		
//		
		Synth synth = new Synth(2);		
	}
}