/****************************************/
/* OscilatorType.java					*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.types;

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
	
	Impulse			(ImpulseOscillator.class,		"IMP"),
	Pulse			(PulseOscillator.class,			"PLS"),
	Sawtooth			(SawtoothOscillator.class,		"SAW"),
	SawtoothDPW		(SawtoothOscillatorDPW.class,	"SW2"),
	Sine				(SineOscillator.class,			"SIN"),
	Square			(SquareOscillator.class,		"SQE"),
	Triangle			(TriangleOscillator.class,		"TRI");

	//--------------------------------------------------------------------------
	
	private final Class<? extends UnitOscillator> type;
	private final String symbol;

	//--------------------------------------------------------------------------

	private OscilatorType(Class<? extends UnitOscillator> type, String symbol)
	{
		this.type = type;
		this.symbol = symbol;
	}
			
	//--------------------------------------------------------------------------

	public String getSymbol()
	{
		return symbol;
	}
	
	//--------------------------------------------------------------------------
	
	public static OscilatorType get(int val)
	{
		int step = 127 / OscilatorType.values().length;
		int count = 0;
		
		for (OscilatorType type : OscilatorType.values())
		{
			if (val <= count)
				return type;
			
			count += step;			
		}
		
		return OscilatorType.values()[OscilatorType.values().length-1];
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