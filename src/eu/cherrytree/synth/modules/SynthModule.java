/****************************************/
/* Module.java							*/
/* Created on: 09-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class SynthModule
{
	//--------------------------------------------------------------------------
	
	private Synthesizer synthesizer;
	
	//--------------------------------------------------------------------------

	public SynthModule(Synthesizer synthesizer)
	{
		this.synthesizer = synthesizer;
	}
	
	//--------------------------------------------------------------------------

	public Synthesizer getSynthesizer()
	{
		return synthesizer;
	}
	
	//--------------------------------------------------------------------------
}
