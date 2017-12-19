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
	private LFOUnitGeneratorModule distortionLFO;
	private float distortionGain = 0.4f;
	
	private BitCrusherFilter bitCrusher = new BitCrusherFilter();
	private LFOUnitGeneratorModule bitCrusherLFO;
	private float bitCrusherBits = 32.0f;
	
	private boolean distortionEnabled = false;
	private boolean bitCrusherEnabled = false;
	
	//--------------------------------------------------------------------------

	public EffectModule(Synthesizer synth)
	{
		super(synth);
		
		distortionLFO = new LFOUnitGeneratorModule(synth, distortion.gain, distortionGain, 0.5f);
		bitCrusherLFO = new LFOUnitGeneratorModule(synth, bitCrusher.bits, bitCrusherBits, 0.5f);
		
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
		
		distortionLFO.rebuild();
		bitCrusherLFO.rebuild();
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionGain(float gain)
	{
		distortionGain = gain;
		
		distortionLFO.setValue(distortionGain);
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionStrength(float strength)
	{
		distortion.strength.set(strength);
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionLevel(float level)
	{
		distortion.strength.set(level);
	}
	
	//--------------------------------------------------------------------------
	
	public float getDistortionGain()
	{
		return distortionGain;
	}
	
	//--------------------------------------------------------------------------
	
	public float getDistortionStrength()
	{
		return (float) distortion.strength.get();
	}
	
	//--------------------------------------------------------------------------
	
	public float getDistortionLevel()
	{
		return (float) distortion.level.get();
	}
	
	//--------------------------------------------------------------------------

	public LFOUnitGeneratorModule getDistortionLFO()
	{
		return distortionLFO;
	}
	
	//--------------------------------------------------------------------------
	
	public void setBitCrusherResolution(float resolution)
	{				
		bitCrusher.resolution.set(resolution);
	}
		
	//--------------------------------------------------------------------------
	
	public void setBitCrusherBits(int bits)
	{
		bitCrusherBits = bits;
		
		bitCrusher.bits.set((double) bits);
	}
		
	//--------------------------------------------------------------------------
	
	public void setBitCrusherLevel(float level)
	{
		bitCrusher.level.set(level);
	}
		
	//--------------------------------------------------------------------------
	
	public float getBitCrusherResolution()
	{
		return (float) bitCrusher.resolution.get();
	}
	
	//--------------------------------------------------------------------------

	public int getBitCrusherBits()
	{
		return (int) bitCrusherBits;
	}
	
	//--------------------------------------------------------------------------
	
	public float getBitCrusherLevel()
	{
		return (float) bitCrusher.level.get();
	}
	
	//--------------------------------------------------------------------------
	
	public LFOUnitGeneratorModule getBitCrusherLFO()
	{
		return bitCrusherLFO;
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
