/****************************************/
/* LFOOscillatorFrequencyModule.java			*/
/* Created on: 19-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.unitgen.TunableFilter;
import com.jsyn.unitgen.UnitOscillator;
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
	
	//--------------------------------------------------------------------------
	
	public LFOFrequencyModule(Synthesizer synth, UnitOscillator oscillator, float value)
	{
		super(synth);
		
		scale = new FrequencyScale();
		synth.add(scale);
		
		lfo = new LFOModule(synth);
		
		scale.scale.set(1.0f);		
		scale.frequency.set(value);
		
		
	}
	
	//--------------------------------------------------------------------------
	
	public LFOFrequencyModule(Synthesizer synth, TunableFilter filter, float value)
	{
		super(synth);
		
		scale = new FrequencyScale();
		synth.add(scale);
		
		lfo = new LFOModule(synth);
		
		scale.scale.set(1.0f);		
		scale.frequency.set(value);
		
		filter.frequency.connect(scale.output);
	}
	
	//--------------------------------------------------------------------------

	public LFOModule getLFO()
	{
		return lfo;
	}
	
	//--------------------------------------------------------------------------

	public void rebuild()
	{
		lfo.rebuild();
		
		scale.scale.disconnectAll();
		
		if (lfo.getType() != LFOType.Off)
			lfo.getOutput().connect(scale.scale);
		else
			scale.scale.set(1.0);
	}
	
	//--------------------------------------------------------------------------
	
	public void setValue(double value)
	{
		if (lfo.getType() == LFOType.Off)
			scale.scale.set(1.0);
		
		scale.frequency.set(value);
	}
	
	//--------------------------------------------------------------------------
}
