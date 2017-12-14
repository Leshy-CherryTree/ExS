/****************************************/
/* OscillatorModule.java					*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.modules;

import com.jsyn.Synthesizer;
import com.jsyn.ports.UnitOutputPort;

import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.UnitOscillator;




/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class OscillatorModule extends SynthModule
{
	//--------------------------------------------------------------------------
	
	float notes[] =
	{
		16.35f,
		17.32f,
		18.35f,
		19.45f,
		20.60f,
		21.83f,
		23.12f,
		24.50f,
		25.96f,
		27.50f,
		29.14f,
		30.87f,
		32.70f,
		34.65f,
		36.71f,
		38.89f,
		41.20f,
		43.65f,
		46.25f,
		49.00f,
		51.91f,
		55.00f,
		58.27f,
		61.74f,
		65.41f,
		69.30f,
		73.42f,
		77.78f,
		82.41f,
		87.31f,
		92.50f,
		98.00f,
		103.83f,
		110.00f,
		116.54f,
		123.47f,
		130.81f,
		138.59f,
		146.83f,
		155.56f,
		164.81f,
		174.61f,
		185.00f,
		196.00f,
		207.65f,
		220.00f,
		233.08f,
		246.94f,
		261.63f,
		277.18f,
		293.66f,
		311.13f,
		329.63f,
		349.23f,
		369.99f,
		392.00f,
		415.30f,
		440.00f,
		466.16f,
		493.88f,
		523.25f,
		554.37f,
		587.33f,
		622.25f,
		659.25f,
		698.46f,
		739.99f,
		783.99f,
		830.61f,
		880.00f,
		932.33f,
		987.77f,
		1046.5f,
		1108.73f,
		1174.66f,
		1244.51f,
		1318.51f,
		1396.91f,
		1479.98f,
		1567.98f,
		1661.22f,
		1760.00f,
		1864.66f,
		1975.53f,
		2093.00f,
		2217.46f,
		2349.32f,
		2489.02f,
		2637.02f,
		2793.83f,
		2959.96f,
		3135.96f,
		3322.44f,
		3520.00f,
		3729.31f,
		3951.07f,
		4186.01f,
		4434.92f,
		4698.63f,
		4978.03f,
		5274.04f,
		5587.65f,
		5919.91f,
		6271.93f,
		6644.88f,
		7040.00f,
		7458.62f,
		7902.13f
	};
	
	//--------------------------------------------------------------------------
	
	private UnitOscillator osc1;
	private UnitOscillator osc2;
	
	private MixerMono mixer;
	
	private OscilatorType osc1Type = OscilatorType.Sine;
	private OscilatorType osc2Type = OscilatorType.Sine;
	private MixMode mode = MixMode.Mix;

	private int note = -1;
	private float strength = -1.0f;
	
	private float osc1Amplitude = 1.0f;
	private float osc2Amplitude = 1.0f;
	
	private float osc1Detune = 1.0f;
	private float osc2Detune = 1.0f;
	
	//--------------------------------------------------------------------------
	
	public OscillatorModule(Synthesizer synth)
	{
		super(synth);
	}
	
	//--------------------------------------------------------------------------
	
	public void setNote(int note, float strength)
	{
		this.note = note;
		this.strength = strength;
		
		updateNote();
	}
	
	//--------------------------------------------------------------------------
	
	public void endNote()
	{
		note = -1;		

		updateNote();
	}
			
	//--------------------------------------------------------------------------

	public int getNote()
	{
		return note;
	}
	
	//--------------------------------------------------------------------------

	public void setOsc1Type(OscilatorType osc1Type)
	{
		this.osc1Type = osc1Type;
	}
	
	//--------------------------------------------------------------------------

	public void setOsc2Type(OscilatorType osc2Type)
	{
		this.osc2Type = osc2Type;
	}	
	
	//--------------------------------------------------------------------------

	public OscilatorType getOsc1Type()
	{
		return osc1Type;
	}
	
	//--------------------------------------------------------------------------

	public OscilatorType getOsc2Type()
	{
		return osc2Type;
	}		
	
	//--------------------------------------------------------------------------
	
	public void updateNote()		
	{		
		if(note > 0)
		{
			osc1.frequency.set(notes[note] * osc1Detune);
			osc2.frequency.set(notes[note] * osc2Detune);

			if (mode == MixMode.Mix)
			{
				osc1.amplitude.set(strength * osc1Amplitude);
				osc2.amplitude.set(strength * osc2Amplitude);
			}
			else
			{
				osc1.amplitude.set(strength * osc1Amplitude);
				osc2.amplitude.set(osc2Amplitude);
			}
		}
		else
		{					
			osc1.amplitude.set(0.0f);
			osc2.amplitude.set(0.0f);
		}
	}
		
	//--------------------------------------------------------------------------

	public void setOsc1Detune(float detune)
	{
		this.osc1Detune = detune;
		
		updateNote();
	}
		
	//--------------------------------------------------------------------------

	public void setOsc2Detune(float detune)
	{
		this.osc2Detune = detune;
		
		updateNote();
	}
	
	//--------------------------------------------------------------------------
	
	public void setOsc1Phase(float phase)
	{
		osc1.phase.set(phase);
	}
	
	//--------------------------------------------------------------------------
	
	public void setOsc2Phase(float phase)
	{
		osc2.phase.set(phase);
	}
	
	//--------------------------------------------------------------------------
	
	public void setRatio(float ratio)
	{
		
	}
	
	//--------------------------------------------------------------------------

	public void setMixMode(MixMode mode)
	{
		this.mode = mode;
	}
			
	//--------------------------------------------------------------------------

	public MixMode getMixMode()
	{
		return mode;
	}
			
	//--------------------------------------------------------------------------
	
	public void rebuild()
	{
		if (osc1 != null)
		{
			osc1.output.disconnectAll();			
			getSynthesizer().remove(osc1);
		}
		
		if (osc2 != null)
		{
			osc2.output.disconnectAll();
			getSynthesizer().remove(osc2);
		}
		
		if (mixer != null)
		{
			mixer.input.disconnectAll();
			mixer.output.disconnectAll();
			
			getSynthesizer().remove(mixer);
		}
		
		osc1 = osc1Type.create();
		osc2 = osc2Type.create();
		
		osc1.amplitude.set(0.0f);
		osc2.amplitude.set(0.0f);
		
		getSynthesizer().add(osc1);
		getSynthesizer().add(osc2);
		
		if (mode == MixMode.Mix)
		{
			mixer = new MixerMono(2);
			getSynthesizer().add(mixer);
			
			osc1.output.connect(0, mixer.input, 0);
			osc2.output.connect(0, mixer.input, 1);
		}
		else
		{
			mixer = null;
			
			osc1.output.connect(osc2.amplitude);
		}
	}
	
	//--------------------------------------------------------------------------


	public UnitOutputPort getOutput()
	{
		if (mode == MixMode.Mix)
			return mixer.output;
		else
			return osc2.output;
	}
	
	//--------------------------------------------------------------------------
}
