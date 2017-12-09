/****************************************/
/* OscilatorType.java					*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.unitgen.ImpulseOscillator;
import com.jsyn.unitgen.PulseOscillator;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SawtoothOscillatorDPW;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public enum OscilatorType
{
	//--------------------------------------------------------------------------
	
	Impulse			(ImpulseOscillator.class),
	Pulse			(PulseOscillator.class),
	Sawtooth			(SawtoothOscillator.class),
	SawtoothDPW		(SawtoothOscillatorDPW.class),
	Sine				(SineOscillator.class),
	Square			(SquareOscillator.class),
	Triangle			(TriangleOscillator.class);

	//--------------------------------------------------------------------------
	
	private Class<? extends UnitOscillator> type;

	//--------------------------------------------------------------------------
	
	private OscilatorType(Class<? extends UnitOscillator> type)
	{
		this.type = type;
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOscillator create()
	{
		switch (this)
		{
			case Impulse:		return new ImpulseOscillator();
			case Pulse:			return new PulseOscillator();
			case Sawtooth:		return new SawtoothOscillator();
			case SawtoothDPW:	return new SawtoothOscillatorDPW();
			case Sine:			return new SineOscillator();
			case Square:			return new SquareOscillator();
			case Triangle:		return new TriangleOscillator();
		}
		
		return null;
	}
	
	//--------------------------------------------------------------------------
	
	public Class<? extends UnitOscillator> getType()
	{
		return type;
	}				
	
	//--------------------------------------------------------------------------
}