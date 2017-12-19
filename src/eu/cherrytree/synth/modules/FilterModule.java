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

import eu.cherrytree.synth.types.FilterType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class FilterModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private FilterStateVariable filter;	
	private LFOUnitGeneratorModule lfoModule;
	
	private FilterType type = FilterType.LowPass;
	
	private float frequency = 440.0f;
		
	//--------------------------------------------------------------------------

	public FilterModule(Synthesizer synth)
	{
		super(synth);
		
		filter = new FilterStateVariable();
		synth.add(filter);
		
		lfoModule = new LFOUnitGeneratorModule(synth, filter.frequency, frequency, 1.0f);
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		filter.input.disconnectAll();
		filter.highPass.disconnectAll();
		filter.bandPass.disconnectAll();
		filter.lowPass.disconnectAll();
		
		lfoModule.rebuild();
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfoModule.getLFO();
	}
			
	//--------------------------------------------------------------------------

	public void setType(FilterType type)
	{
		this.type = type;
	}
	
	//--------------------------------------------------------------------------

	public FilterType getType()
	{
		return type;
	}
			
	//--------------------------------------------------------------------------
	
	public float getFrequency()
	{
		return frequency;
	}
	
	//--------------------------------------------------------------------------
	
	public float getResonance()
	{
		return (float) filter.resonance.get();
	}
	
	//--------------------------------------------------------------------------
	
	public void setFrequency(float frequency)
	{
		this.frequency = frequency;
		
		lfoModule.setValue(frequency);
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
