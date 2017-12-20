/****************************************/
/* FilterMixer.java						*/
/* Created on: 20-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.operators;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class FilterMixer extends UnitGenerator
{
	//--------------------------------------------------------------------------
	
	public UnitInputPort[] inputs;
	public UnitOutputPort output = new UnitOutputPort("Output");
		
	//--------------------------------------------------------------------------

	public FilterMixer(int inputCount)
	{
		inputs = new UnitInputPort[inputCount];
		
		for (int i = 0 ; i < inputCount ; i++)
		{
			inputs[i] = new UnitInputPort("Input" + i);
			addPort(inputs[i]);
		}
		
		addPort(output);
	}
			
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		double[] outputs = output.getValues();
		
		for (int i = start; i < limit; i++)
		{
			double val = 0.0f;
			
			for (int j = 0 ; j < inputs.length ; j++)
				val = Math.max(val, inputs[j].getValues()[i]);
			
			outputs[i] = val;
		}
	}

	//--------------------------------------------------------------------------
}
