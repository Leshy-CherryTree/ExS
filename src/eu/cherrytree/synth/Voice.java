/****************************************/
/* Voice.java							*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;

import eu.cherrytree.synth.modules.FilterModule;
import eu.cherrytree.synth.modules.OscillatorModule;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Voice
{
	//--------------------------------------------------------------------------
	
	private OscillatorModule oscilator;
	private FilterModule filter;
	
	//--------------------------------------------------------------------------
	
	public Voice(Synthesizer synth)
	{
		oscilator = new OscillatorModule(synth);
		filter = new FilterModule(synth);
	}
	
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		oscilator.rebuild();
		filter.rebuild();
		
		oscilator.getOutput().connect(filter.getInput());
	}
	
	//--------------------------------------------------------------------------
	
	public OscillatorModule getOscilator()
	{
		return oscilator;
	}
	
	//--------------------------------------------------------------------------

	public FilterModule getFilter()
	{
		return filter;
	}
				
	//--------------------------------------------------------------------------
	
	public UnitOutputPort getOutput()
	{
		return filter.getOutput();
	}
	
	//--------------------------------------------------------------------------
}
