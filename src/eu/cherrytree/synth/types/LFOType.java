/****************************************/
/* LFOType.java							*/
/* Created on: 17-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.types;

import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.SquareOscillator;
import com.jsyn.unitgen.TriangleOscillator;
import com.jsyn.unitgen.UnitOscillator;

import eu.cherrytree.synth.oscillator.SampleAndHoldOscillator;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public enum LFOType
{
	//--------------------------------------------------------------------------
	
	Off				(null,							"OFF"),
	Sawtooth			(SawtoothOscillator.class,		"SAW"),
	Sine				(SineOscillator.class,			"SIN"),
	Square			(SquareOscillator.class,		"SQE"),
	Triangle			(TriangleOscillator.class,		"TRI"),
	SampleAndHold	(SampleAndHoldOscillator.class,	"SAH");

	//--------------------------------------------------------------------------
	
	private final Class<? extends UnitOscillator> type;
	private final String symbol;

	//--------------------------------------------------------------------------

	private LFOType(Class<? extends UnitOscillator> type, String symbol)
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
	
	public static LFOType get(int val)
	{
		int step = 127 / LFOType.values().length;
		int count = 0;
		
		for (LFOType type : LFOType.values())
		{
			if (val <= count)
				return type;
			
			count += step;			
		}
		
		return LFOType.values()[LFOType.values().length-1];
	}
	
	//--------------------------------------------------------------------------
	
	public UnitOscillator create()
	{
		switch (this)
		{
			case Sawtooth:		return new SawtoothOscillator();
			case Sine:			return new SineOscillator();
			case Square:			return new SquareOscillator();
			case Triangle:		return new TriangleOscillator();
			case SampleAndHold:	return new SampleAndHoldOscillator();
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
