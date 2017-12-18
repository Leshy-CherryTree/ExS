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
import com.jsyn.unitgen.Multiply;

import eu.cherrytree.synth.types.FilterType;
import eu.cherrytree.synth.types.LFOType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class FilterModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private FilterStateVariable filter;
	
	private Multiply multiply;
	private LFOModule lfo;
	
	private FilterType type = FilterType.LowPass;
	
	private float frequency = 440.0f;
		
	//--------------------------------------------------------------------------

	public FilterModule(Synthesizer synth)
	{
		super(synth);
		
		filter = new FilterStateVariable();
		synth.add(filter);
		
		multiply = new Multiply();
		synth.add(multiply);
		
		lfo = new LFOModule(synth);
		
		multiply.inputA.set(1.0f);		
		multiply.inputB.set(frequency);
		
		filter.frequency.connect(multiply.output);
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		lfo.rebuild();
		
		filter.input.disconnectAll();
		filter.highPass.disconnectAll();
		filter.bandPass.disconnectAll();
		filter.lowPass.disconnectAll();
		
		multiply.inputA.disconnectAll();
		
		if (lfo.getType() != LFOType.Off)
			lfo.getOutput().connect(multiply.inputA);
		else
			multiply.inputA.set(1.0f);	
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfo;
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
		
		if (lfo.getType() == LFOType.Off)
			multiply.inputA.set(1.0f);
		
		multiply.inputB.set(frequency);
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
