/****************************************/
/* DistortionFilter.java					*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.filters;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.UnitFilter;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class DistortionFilter extends UnitFilter 
{
	//--------------------------------------------------------------------------
	
	private static final double top = 1.0f;
	
	//--------------------------------------------------------------------------
	
	public UnitInputPort level = new UnitInputPort("Level", 0.75f);
	public UnitInputPort gain = new UnitInputPort("Gain", 4.0f);
	public UnitInputPort strength = new UnitInputPort("Strength", 2.0f);
		
	//--------------------------------------------------------------------------

	public DistortionFilter()
	{
		addPort(level);
		addPort(gain);
		addPort(strength);
	}
			
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		double[] inputs = input.getValues();
		double[] outputs = output.getValues();
		double[] levels = level.getValues();
		double[] gains = gain.getValues();
		double[] strengths = strength.getValues();
							
		for (int i = start; i < limit; i++)
		{
			double val = inputs[i] * strengths[i];
			
			if (Math.abs(val) > top)
				val = top / val;
			else
				val = inputs[i];
					
			outputs[i] = Math.max(Math.min(val * gains[i], top), -top) * levels[i];
		}
	}
	
	//--------------------------------------------------------------------------
}
