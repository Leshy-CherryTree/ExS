/****************************************/
/* EqualizerModule.java					*/
/* Created on: 20-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.FilterBandPass;
import com.jsyn.unitgen.FilterBiquadCommon;
import com.jsyn.unitgen.FilterHighPass;
import com.jsyn.unitgen.FilterLowPass;
import eu.cherrytree.synth.operators.FilterMixer;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class EqualizerModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	public enum EqualizerBand
	{
		LowBand,
		MidBand_01,
		MidBand_02,
		MidBand_03,
		MidBand_04,
		MidBand_05,
		MidBand_06,
		HighBand;
	};
	
	//--------------------------------------------------------------------------
	
	private static final float defaultQ = 3.78f;
	
	//--------------------------------------------------------------------------
	
	private FilterMixer mixer = new FilterMixer(EqualizerBand.values().length);
	private FilterBiquadCommon[] filters = new FilterBiquadCommon[EqualizerBand.values().length];
		
	private float q = defaultQ;
	
	//--------------------------------------------------------------------------

	public EqualizerModule(Synthesizer synthesizer)
	{
		super(synthesizer);
		
		filters[EqualizerBand.LowBand.ordinal()] = new FilterLowPass();
		filters[EqualizerBand.HighBand.ordinal()] = new FilterHighPass();
		
		for (int i = EqualizerBand.LowBand.ordinal() + 1 ; i < EqualizerBand.HighBand.ordinal() ; i++)
			filters[i] = new FilterBandPass();
		
		for (FilterBiquadCommon filter : filters)
		{
			filter.Q.set(defaultQ);
			filter.amplitude.set(0.75f);
			
			synthesizer.add(filter);
		}
		
		filters[EqualizerBand.LowBand.ordinal()].frequency.set(55.0);
		filters[EqualizerBand.MidBand_01.ordinal()].frequency.set(110.0);
		filters[EqualizerBand.MidBand_02.ordinal()].frequency.set(220.0);
		filters[EqualizerBand.MidBand_03.ordinal()].frequency.set(440.0);
		filters[EqualizerBand.MidBand_04.ordinal()].frequency.set(880.0);
		filters[EqualizerBand.MidBand_05.ordinal()].frequency.set(1760.0);
		filters[EqualizerBand.MidBand_06.ordinal()].frequency.set(3520.0);
		filters[EqualizerBand.HighBand.ordinal()].frequency.set(7040.0);
		
		synthesizer.add(mixer);
	}

	//--------------------------------------------------------------------------
	
	public void setQ(float q)
	{
		this.q = q;
		
		for (FilterBiquadCommon filter : filters)
			filter.Q.set(q);		
	}
	
	//--------------------------------------------------------------------------

	public float getQ()
	{
		return q;
	}
			
	//--------------------------------------------------------------------------
	
	public float getAmplitude(EqualizerBand band)
	{
		return (float) filters[band.ordinal()].amplitude.get();
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(EqualizerBand band, float amplitude)
	{
		filters[band.ordinal()].amplitude.set(amplitude);
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild(UnitOutputPort port)
	{
		for (FilterBiquadCommon filter : filters)
		{
			filter.input.disconnectAll();
			filter.output.disconnectAll();
		}
		
		for (int i = 0 ; i < filters.length ; i++)
		{
			port.connect(filters[i].input);
			filters[i].output.connect(mixer.inputs[i]);
		}
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		return mixer.output;
	}
	
	//--------------------------------------------------------------------------
}
