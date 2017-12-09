/****************************************/
/* FilterModule.java						*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.UnitFilter;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class FilterModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private FilterStateVariable filter;
	private LFOModule lfo;
	
	private FilterType type;
		
	//--------------------------------------------------------------------------

	public FilterModule(Synthesizer synth)
	{
		super(synth);
		
		filter = new FilterStateVariable();
		synth.add(filter);
	}
	
	//--------------------------------------------------------------------------

	public void setType(FilterType type)
	{
		this.type = type;
	}
	
	//--------------------------------------------------------------------------
	
	public void setFrequency(float frequency)
	{
		filter.frequency.set(frequency);
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(float amplitude)
	{
		filter.amplitude.set(amplitude);
	}
	
	//--------------------------------------------------------------------------
	
	public void setResonance(float resonance)
	{
		filter.resonance.set(resonance);
	}
			
	//--------------------------------------------------------------------------
	
	public UnitInputPort getInput()
	{
		return filter.input;
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		switch (type)
		{
			case HighPass:	return filter.highPass;
			case BandPass:	return filter.bandPass;
			case LowPass:	return filter.lowPass;
		}
		
		return null;
	}
	
	//--------------------------------------------------------------------------
}
