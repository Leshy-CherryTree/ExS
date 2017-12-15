/****************************************/
/* FilterType.java						*/
/* Created on: 09-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public enum FilterType
{
	//--------------------------------------------------------------------------
	
	HighPass		("HPF"),
	BandPass		("BPF"),
	LowPass			("LPF");
	
	//--------------------------------------------------------------------------
	
	private final String symbol;
	
	//--------------------------------------------------------------------------

	private FilterType(String symbol)
	{
		this.symbol = symbol;
	}
	
	//--------------------------------------------------------------------------

	public String getSymbol()
	{
		return symbol;
	}
	
	//--------------------------------------------------------------------------
}
