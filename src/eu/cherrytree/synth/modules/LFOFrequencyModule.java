/****************************************/
/* LFOOscillatorFrequencyModule.java		*/
/* Created on: 19-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;

import eu.cherrytree.synth.operators.FrequencyScale;
import eu.cherrytree.synth.types.LFOType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class LFOFrequencyModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private final FrequencyScale scale;
	private final LFOModule lfo;
	
	private double value;
	
	//--------------------------------------------------------------------------
	
	public LFOFrequencyModule(Synthesizer synth, float value)
	{
		super(synth);
		
		this.value = value;
		
		scale = new FrequencyScale();
		synth.add(scale);
		
		lfo = new LFOModule(synth);
		
		scale.scale.set(0.0);		
		scale.frequency.set(value);
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfo;
	}
	//--------------------------------------------------------------------------
	
	public void rebuild(UnitInputPort port)
	{
		lfo.rebuild();
		
		scale.output.disconnectAll();
		scale.scale.disconnectAll();
		
		if (lfo.getType() != LFOType.Off)
			lfo.getOutput().connect(scale.scale);
		else
			scale.scale.set(0.0);
		
		scale.frequency.set(value);
		
		port.connect(scale.output);
	}
	
	//--------------------------------------------------------------------------
	
	public void setValue(double value)
	{
		this.value = value;
		
		if (lfo.getType() == LFOType.Off)
			scale.scale.set(0.0);
		
		scale.frequency.set(value);
	}
	
	//--------------------------------------------------------------------------
}
