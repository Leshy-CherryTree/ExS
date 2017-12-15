/****************************************/
/* BypassFilter.java						*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.filters;

import com.jsyn.unitgen.UnitFilter;

/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class BypassFilter extends UnitFilter
{
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		System.arraycopy(input.getValues(), start, output.getValues(), start, limit - start);
	}
	
	//--------------------------------------------------------------------------

}
