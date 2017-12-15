/****************************************/
/* EffectModule.java						*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.ports.UnitOutputPort;

import eu.cherrytree.synth.filters.BitCrusherFilter;
import eu.cherrytree.synth.filters.BypassFilter;
import eu.cherrytree.synth.filters.DistortionFilter;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class EffectModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private BypassFilter bypass = new BypassFilter();
	
	private DistortionFilter distortion = new DistortionFilter();
	private LFOModule distortionLFO;
	
	private BitCrusherFilter bitCrusher = new BitCrusherFilter();
	private LFOModule bitCrusherLFO;
	
	private boolean distortionEnabled = false;
	private boolean bitCrusherEnabled = false;
	
	//--------------------------------------------------------------------------

	public EffectModule(Synthesizer synth)
	{
		super(synth);
		
		distortionLFO = new LFOModule(synth);
		bitCrusherLFO = new LFOModule(synth);
		
		synth.add(bypass);
		synth.add(distortion);
		synth.add(bitCrusher);
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionEnabled(boolean enabled)
	{
		distortionEnabled = enabled;				
	}

	//--------------------------------------------------------------------------
	
	public void setBitCrusherEnabled(boolean enabled)
	{
		bitCrusherEnabled = enabled;
	}
	
	//--------------------------------------------------------------------------

	public boolean isDistortionEnabled()
	{
		return distortionEnabled;
	}
	
	//--------------------------------------------------------------------------

	public boolean isBitCrusherEnabled()
	{
		return bitCrusherEnabled;
	}
			
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		bypass.input.disconnectAll();
		bypass.output.disconnectAll();
		
		distortion.input.disconnectAll();
		distortion.output.disconnectAll();
		
		bitCrusher.input.disconnectAll();
		bitCrusher.output.disconnectAll();
		
		if (distortionEnabled)
			bypass.output.connect(distortion.input);
		
		if (bitCrusherEnabled)
		{
			if (distortionEnabled)
				distortion.output.connect(bitCrusher.input);
			else
				bypass.output.connect(bitCrusher.input);
		}
	}
	
	//--------------------------------------------------------------------------
	
	public BitCrusherFilter getBitCrusher()
	{
		return bitCrusher;
	}		
	
	//--------------------------------------------------------------------------

	public DistortionFilter getDistortion()
	{
		return distortion;
	}
	
	//--------------------------------------------------------------------------
	
	public UnitInputPort getInput()
	{
		return bypass.input;
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		if (bitCrusherEnabled)
			return bitCrusher.output;
		else if (distortionEnabled)
			return distortion.output;
		
		return bypass.output;
	}
	
	//--------------------------------------------------------------------------
}
