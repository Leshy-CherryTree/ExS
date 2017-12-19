/****************************************/
/* FrequencyScale.java						*/
/* Created on: 15-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.operators;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitGenerator;

/**
 *
 * @author leszek.szczepanski
 */
public class FrequencyScale extends UnitGenerator
{
	//--------------------------------------------------------------------------
	
	public UnitInputPort frequency = new UnitInputPort("Frequency");
	public UnitInputPort scale = new UnitInputPort("Scale");
	public UnitOutputPort output = new UnitOutputPort("Output");
	
	//--------------------------------------------------------------------------

	public FrequencyScale()
	{
		addPort(frequency);
		addPort(scale);
		addPort(output);
	}
			
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		double[] frequencies = frequency.getValues();
		double[] scales = scale.getValues();
		double[] outputs = output.getValues();
		
		for (int i = start; i < limit; i++)
		{
			double freq_val = frequencies[i];
			double scl_val = scales[i];
			
			if (scl_val > 0.0f)
				outputs[i] = freq_val * (scl_val + 1.0f);
			else if (scl_val < 0.0f)
				outputs[i] = freq_val / (-scl_val + 1.0f);
			else
				outputs[i] = freq_val;						
		}
	}
	
	//--------------------------------------------------------------------------
}
