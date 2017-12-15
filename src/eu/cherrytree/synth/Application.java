/****************************************/
/* Application.java						*/
/* Created on: 12-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Application
{		
	public static void main(String[] args)
	{
		try
		{
			DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
			
			Terminal terminal = defaultTerminalFactory.createTerminal();
			
			TextGraphics textGraphics = terminal.newTextGraphics();
			
			textGraphics.putString(0, 0, "WELCOME TO ExS-01");
			
			Synth synth = new Synth(2);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}