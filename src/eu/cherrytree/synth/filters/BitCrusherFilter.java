/****************************************/
/* BitCrusherFilter.java					*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.filters;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.UnitFilter;

/**
 *
 * @author leszek.szczepanski
 */
public class BitCrusherFilter extends UnitFilter 
{
	//--------------------------------------------------------------------------
	
	private static class FloatRepresentation
	{		
		private final int exponentSize;
		private final int mantissaSize;

		public FloatRepresentation(int mantissaSize, int exponentSize)
		{
			this.exponentSize = exponentSize;
			this.mantissaSize = mantissaSize;
		}
				
		public double convert(double doubleValue)
		{
			float multiply = 1.0f;
			double abs_val = Math.abs(doubleValue);
			
			while (abs_val * multiply < 1.0)
				multiply *= 10.0f;
			
			long v = Double.doubleToLongBits(doubleValue * multiply);
			long ln = 0;
			
			for (int i = 63 ; i >= 0 ; i--)
			{
				byte val = (byte) ((v >> i) & 1);

				ln = ln << 1;

				if (i == 63 || i == 62 || (i < 52 + exponentSize && i > 52 - mantissaSize))
					ln = ln ^ val;															
			}

			return Double.longBitsToDouble(ln) / multiply;
		}			
	}
	
	//--------------------------------------------------------------------------
	
	private static final FloatRepresentation[] reps = 
	{
		new FloatRepresentation(1,1),		// 2 bit
		new FloatRepresentation(1,2),		// 3 bit
		new FloatRepresentation(1,3),		// 4 bit
		new FloatRepresentation(1,4),		// 5 bit
		new FloatRepresentation(2,3),		// 6 bit
		new FloatRepresentation(2,4),		// 7 bit
		new FloatRepresentation(2,5),		// 8 bit
		new FloatRepresentation(2,6),		// 9 bit
		new FloatRepresentation(3,6),		// 10 bit
		new FloatRepresentation(3,7),		// 11 bit
		new FloatRepresentation(3,8),		// 12 bit
		new FloatRepresentation(3,9),		// 13 bit
		new FloatRepresentation(4,9),		// 14 bit
		new FloatRepresentation(4,10),		// 15 bit
		new FloatRepresentation(4,11),		// 16 bit
		new FloatRepresentation(4,12),		// 17 bit
		new FloatRepresentation(5,12),		// 18 bit
		new FloatRepresentation(5,13),		// 19 bit
		new FloatRepresentation(5,14),		// 20 bit
		new FloatRepresentation(5,15),		// 21 bit
		new FloatRepresentation(6,15),		// 22 bit
		new FloatRepresentation(6,16),		// 23 bit
		new FloatRepresentation(6,17),		// 24 bit
		new FloatRepresentation(6,18),		// 25 bit
		new FloatRepresentation(7,18),		// 26 bit
		new FloatRepresentation(7,19),		// 27 bit
		new FloatRepresentation(7,20),		// 28 bit
		new FloatRepresentation(7,21),		// 29 bit
		new FloatRepresentation(8,21),		// 30 bit
		new FloatRepresentation(8,22),		// 31 bit
		new FloatRepresentation(8,23)		// 32 bit		
	};
	
	//--------------------------------------------------------------------------
	
	public UnitInputPort level = new UnitInputPort("Level", 0.75f);
	public UnitInputPort resolution = new UnitInputPort("Resolution", 1.0f);
	public UnitInputPort bits = new UnitInputPort("Bits", 32.0f);
	
	private int resolutionCounter = 0;
	private double lastVal = 0.0f;
	
	//--------------------------------------------------------------------------

	public BitCrusherFilter()
	{
		addPort(input);
		addPort(resolution);
		addPort(bits);
	}
			
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		double[] inputs = input.getValues();
		double[] outputs = output.getValues();
		double[] levels = level.getValues();
		double[] bit_vals = bits.getValues();
		
		for (int i = start; i < limit; i++)
		{
			if (resolutionCounter > 0)
			{		
				resolutionCounter--;
			}
			else
			{
				resolutionCounter = (int) Math.max(0.0f, this.resolution.getValue());									
				lastVal = inputs[i];
			}
			
			int idx = (int) Math.max(2.0, Math.min(bit_vals[i], 32.0));			
			outputs[i] = reps[idx - 2].convert(lastVal) * levels[i];
		}
	}
	
	//--------------------------------------------------------------------------
}
