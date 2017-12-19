/****************************************/
/* LFOUnitGeneratorModule.java			*/
/* Created on: 19-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.MultiplyAdd;

import eu.cherrytree.synth.types.LFOType;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class LFOUnitGeneratorModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	private MultiplyAdd multiply;
	
	private LFOModule lfo;
	
	private float scale;

	//--------------------------------------------------------------------------
	
	public LFOUnitGeneratorModule(Synthesizer synth, UnitInputPort port, float value, float scale)
	{
		super(synth);
		
		this.scale = scale;
		
		multiply = new MultiplyAdd();
		synth.add(multiply);
		
		lfo = new LFOModule(synth);
		
		multiply.inputA.set(1.0f);		
		multiply.inputB.set(value * scale);
		multiply.inputC.set(value * scale);
		
		port.connect(multiply.output);
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
		
		multiply.inputA.disconnectAll();
		
		if (lfo.getType() != LFOType.Off)
			lfo.getOutput().connect(multiply.inputA);
		else
			multiply.inputA.set(1.0f);			
	}
	
	//--------------------------------------------------------------------------
	
	public void setValue(float value)
	{
		if (lfo.getType() == LFOType.Off)
			multiply.inputA.set(1.0f);
		
		multiply.inputB.set(value * scale);
		multiply.inputC.set(value * scale);
	}
	
	//--------------------------------------------------------------------------
}
