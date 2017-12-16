/****************************************/
/* Screen.java							*/
/* Created on: 14-12-2017				*/
/* Copyright Cherry Tree Studio 2017	*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import eu.cherrytree.synth.modules.FilterType;

import eu.cherrytree.synth.modules.MixMode;
import eu.cherrytree.synth.modules.OscilatorType;

import java.io.IOException;
import java.util.Arrays;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Screen
{
	//--------------------------------------------------------------------------
	
	private static final int SPC = 9;
	
	private static final int MIX_C =	2;
	private static final int OC1_C =	MIX_C + SPC;
	private static final int OC2_C =	OC1_C + SPC;
	private static final int FLT_C =	OC2_C + SPC;
	private static final int AMP_C =	FLT_C + SPC;
	private static final int DST_C =	AMP_C + SPC;
	private static final int CRS_C =	DST_C + SPC;
	private static final int PHS_C =	CRS_C + SPC;
	private static final int REV_C =	PHS_C + SPC;
	
	private static final int VAL_1 =	4;
	private static final int VAL_2 =	5;
	private static final int VAL_3 =	6;
	
	//--------------------------------------------------------------------------
	
	private final Terminal terminal;
	private final TextGraphics textGraphics;
	
	//--------------------------------------------------------------------------

	public Screen() throws IOException
	{
		DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();			
		terminal = defaultTerminalFactory.createTerminal();
		textGraphics = terminal.newTextGraphics();
		
		terminal.setCursorVisible(false);
				
		char[] chars = new char[terminal.getTerminalSize().getColumns()-2];
		Arrays.fill(chars, '-');   
		
		String char_str = new String(chars);
		
		textGraphics.putString(1, 0, char_str);
		textGraphics.putString(1, 2, char_str);
		textGraphics.putString(1,terminal.getTerminalSize().getRows()-1, char_str);
		
		String col_str = new String(new char[]{'|'});
		
		for (int i = 0 ; i < terminal.getTerminalSize().getRows() ; i++)
		{
			textGraphics.putString(0, i, col_str);
			textGraphics.putString(terminal.getTerminalSize().getColumns()-1, i, col_str);
		}
		
		textGraphics.putString(4, 1, "ExS-01", SGR.BOLD);
		
		textGraphics.putString(MIX_C, 3, "MIX:", SGR.BOLD);
		textGraphics.putString(OC1_C, 3, "OSC1:", SGR.BOLD);
		textGraphics.putString(OC2_C, 3, "OSC2:", SGR.BOLD);
		textGraphics.putString(FLT_C, 3, "FLT:", SGR.BOLD);
		textGraphics.putString(AMP_C, 3, "AMP:", SGR.BOLD);
		
		textGraphics.putString(DST_C, 3, "DST:");
		textGraphics.putString(CRS_C, 3, "CRS:");
		textGraphics.putString(PHS_C, 3, "PHS:");
		textGraphics.putString(REV_C, 3, "REV:");
		
		terminal.flush();
	}
	
	//--------------------------------------------------------------------------
	
	private void flush()
	{
		try
		{
			terminal.flush();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	//--------------------------------------------------------------------------
	
	private void drawFloat(int x, int y, float value)
	{
		String val = String.format("%.2f", value);
		textGraphics.putString(x, y, val);
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSCMixMode(MixMode mode)
	{
		textGraphics.putString(MIX_C, VAL_1, mode.getSymbol());
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSCMIXRatio(float ratio)
	{
		drawFloat(MIX_C, VAL_2, ratio);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC1Type(OscilatorType type)
	{
		textGraphics.putString(OC1_C, VAL_1, type.getSymbol());
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC1Detune(float detune)
	{
		drawFloat(OC1_C, VAL_2, detune);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC1Phase(float phase)
	{
		drawFloat(OC1_C, VAL_3, phase);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC2Type(OscilatorType type)
	{
		textGraphics.putString(OC2_C, VAL_1, type.getSymbol());
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC2Detune(float detune)
	{
		drawFloat(OC2_C, VAL_2, detune);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOSC2Phase(float phase)
	{
		drawFloat(OC2_C, VAL_3, phase);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setFilterFrequency(float freq)
	{
		drawFloat(FLT_C, VAL_2, freq);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setFilterResonance(float resonance)
	{
		drawFloat(FLT_C, VAL_3, resonance);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setFilterType(FilterType type)
	{
		textGraphics.putString(FLT_C, VAL_1, type.getSymbol());
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setAmpAmplitude(float amplitude)
	{
		drawFloat(AMP_C, VAL_1, amplitude);
		
		flush();		
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionEnabled(boolean enabled)
	{
		if (enabled)
			textGraphics.putString(DST_C, 3, "DST:", SGR.BOLD);
		else
			textGraphics.putString(DST_C, 3, "DST:");
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionGain(float gain)
	{
		drawFloat(DST_C, VAL_1, gain);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionStrength(float strength)
	{
		drawFloat(DST_C, VAL_2, strength);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setDistortionLevel(float level)
	{
		drawFloat(DST_C, VAL_3, level);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setBitCrusherEnabled(boolean enabled)
	{
		if (enabled)
			textGraphics.putString(CRS_C, 3, "CRS:", SGR.BOLD);
		else
			textGraphics.putString(CRS_C, 3, "CRS:");		
	}
	
	//--------------------------------------------------------------------------
	
	public void setBitCrusherResolution(float resolution)
	{
		drawFloat(CRS_C, VAL_1, resolution);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setBitCrusherBits(int bits)
	{
		textGraphics.putString(CRS_C, VAL_2, String.format("%d",bits));
		
		flush();
	}
	
	//--------------------------------------------------------------------------
	
	public void setBitCrusherLevel(float level)
	{
		drawFloat(CRS_C, VAL_3, level);
		
		flush();
	}
	
	//--------------------------------------------------------------------------
}
