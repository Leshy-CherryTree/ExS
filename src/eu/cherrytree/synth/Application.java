/****************************************/
/* Application.java						*/
/* Created on: 12-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import java.io.IOException;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Application
{		
	//--------------------------------------------------------------------------
	
	public static void main(String[] args) throws IOException
	{
		Synth synth = new Synth(new Screen(), 1);
	}
	
	//--------------------------------------------------------------------------
}