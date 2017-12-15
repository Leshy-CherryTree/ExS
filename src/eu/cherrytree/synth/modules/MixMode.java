/****************************************/
/* MixMode.java							*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public enum MixMode
{
	//--------------------------------------------------------------------------
	
	Mix			("MIX"),
	Modulate	("MOD");
	
	//--------------------------------------------------------------------------
	
	private final String symbol;
	
	//--------------------------------------------------------------------------

	private MixMode(String symbol)
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
