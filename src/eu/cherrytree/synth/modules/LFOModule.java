/****************************************/
/* LFOModule.java						*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;
import com.jsyn.unitgen.UnitOscillator;

import eu.cherrytree.synth.types.LFOType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class LFOModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private UnitOscillator osc;
	
	private float amplitude = 1.0f;
	private float rate = 10.0f;
	
	private LFOType type = LFOType.Off;
	
	//--------------------------------------------------------------------------
	
	public LFOModule(Synthesizer synth)
	{
		super(synth);
	}
	
	//--------------------------------------------------------------------------
	
	public void setType(LFOType type)
	{
		this.type = type;
	}
	
	//--------------------------------------------------------------------------

	public LFOType getType()
	{
		return type;
	}
			
	//--------------------------------------------------------------------------
	
	public void setRate(float rate)
	{
		if (type != LFOType.Off)
			osc.frequency.set(rate);
		
		this.rate = rate;
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmplitude(float amplitude)
	{
		if (type != LFOType.Off)
			osc.amplitude.set(amplitude);
		
		this.amplitude = amplitude;
	}
			
	//--------------------------------------------------------------------------

	public float getRate()
	{
		return rate;
	}

	//--------------------------------------------------------------------------
	
	public float getAmplitude()
	{
		return amplitude;
	}		
	
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		if (osc != null)
		{
			osc.output.disconnectAll();
			getSynthesizer().remove(osc);
		}
		
		if (type != LFOType.Off)
		{
			osc = type.create();		
			
			osc.frequency.set(rate);
			osc.amplitude.set(amplitude);
			
			getSynthesizer().add(osc);
		}
		else
		{
			osc = null;
		}
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		if (type != LFOType.Off)
			return osc.output;
		
		return null;
	}
	
	//--------------------------------------------------------------------------
}
