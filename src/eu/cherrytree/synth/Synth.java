/****************************************/
/* Synth.java							*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.LineOut;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import eu.cherrytree.synth.modules.AmpModule;
import eu.cherrytree.synth.modules.EffectModule;
import eu.cherrytree.synth.types.FilterType;
import eu.cherrytree.synth.types.LFOType;
import eu.cherrytree.synth.types.MixMode;
import eu.cherrytree.synth.types.OscilatorType;
import java.util.Collections;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Synth implements Receiver
{
	//--------------------------------------------------------------------------
	
	// Fix the order of LFO controls.
	
	private static final int AmpShift						= 1;
	private static final int AmpLFOToggle					= 3;
	
	private static final int OSCMixModeAmpLFOType		= 16;
	private static final int OSCMixRatioAmpLFOAmplitude		= 17;	
	private static final int AmpLFORate						= 18;		// Add something here.
	
	private static final int OSC1Shift						= 4;
	private static final int OSC1LFOToggle					= 6;
	
	private static final int OSC1TypeLFORate					= 20;
	private static final int OSC1DetuneLFOAmplitude			= 21;
	private static final int OSC1PhaseLFOType				= 22;
	
	private static final int OSC2Shift						= 7;
	private static final int OSC2LFOToggle					= 9;
	
	private static final int OSC2TypeLFORate					= 24;
	private static final int OSC2DetuneLFOAmplitude			= 25;
	private static final int OSC2PhaseLFOType				= 26;
	
	private static final int FilterShift						= 10;
	private static final int FilterLFOToggle					= 12;
	
	private static final int FilterPassTypeLFOType			= 28;
	private static final int FilterFrequencyLFORate			= 29;
	private static final int FilterResonanceLFOAmplitude		= 30;
	

	private static final int DistortionEnable				= 13;
	private static final int DistortionLFOToggle				= 15;
	
	private static final int DistortionGainLFORate			= 46;
	private static final int DistortionStrengthLFOAmplitude	= 47;
	private static final int DistortionLevelLFOType			= 48;
	
	private static final int BitCrusherEnable				= 32;
	private static final int BitCrusherLFOToggle				= 34;
	
	private static final int BitCrusherResolutionLFORate		= 50;
	private static final int BitCrusherBitsLFOAmplitude		= 51;
	private static final int BitCrusherLevelLFOType			= 52;
	
	private static final int AmpAmplitude					= 62;
	
	//--------------------------------------------------------------------------
	
	private Synthesizer synthesizer;
			
	private Voice[] voices;	
	private AmpModule amp;
	private EffectModule effects;
	private LineOut lineOut;

	private boolean OSC1LFOFlag = false;
	private boolean OSC2LFOFlag = false;
	private boolean ampLFOFlag = false;
	private boolean filterLFOFlag = false;
	
	private HashMap<Integer, Voice> voice_note_map = new HashMap<>();
	private ArrayList<Integer> pressed_notes = new ArrayList<>();
	private ArrayList<Float> pressed_strengths = new ArrayList<>();
	private ArrayList<Voice> used_voices = new ArrayList<>();
	private ArrayList<Voice> free_voices = new ArrayList<>();
	
	private Screen screen;
	
	//--------------------------------------------------------------------------
	
	public Synth(Screen screen, int voiceCount)
	{
		this.screen = screen;
		
		synthesizer = JSyn.createSynthesizer();
		synthesizer.start();
		
		lineOut = new LineOut();
		
		voices = new Voice[voiceCount];
		
		for (int i = 0 ; i < voiceCount ; i++)
			voices[i] = new Voice(synthesizer);			
		
		amp = new AmpModule(synthesizer, voiceCount);		
		amp.rebuild(voices);
		
		effects = new EffectModule(synthesizer);
		
		synthesizer.add(lineOut);
		
		rebuild();
		
		lineOut.start();
	
		Collections.addAll(free_voices, voices);
		
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
			
		for (MidiDevice.Info info : infos)
		{
			try
			{								
				MidiDevice inputDevice = MidiSystem.getMidiDevice(info);

				if (inputDevice.getMaxTransmitters() != 0)
				{					
					inputDevice.open();						
					inputDevice.getTransmitter().setReceiver(this);

					System.out.println("Opened: " + info.getName() + " - " + info.getDescription());					
				}																		
			}
			catch (MidiUnavailableException ex)
			{
				System.out.println("Cannot open: " + info.getName() + " - " + info.getDescription());
			}
		}
	}
	
	//--------------------------------------------------------------------------	
	
	private void rebuild()
	{
		ArrayList<Integer> notes = new ArrayList<>();
		ArrayList<Float> strengths = new ArrayList<>();
		
		int used_len = used_voices.size();
		
		// Gathering information about current notes.
		for (Voice v : used_voices)
		{
			notes.add(v.getOscilator().getNote());
			strengths.add(v.getOscilator().getStrength());
		}
		
		lineOut.stop();
				
		lineOut.input.disconnectAll();
		
		for (Voice v : voices)
			v.rebuild();
		
		amp.rebuild(voices);
		
		effects.rebuild();
		
		amp.getOutput().connect(effects.getInput());	
		effects.getOutput().connect(lineOut.input);	
		
		// Restarting all notes.
		for (int i = 0 ; i < used_len ; i++)
		{
			int note = notes.get(i);
			float strength = strengths.get(i);
			Voice voice = used_voices.get(i);
			
			voice.getOscilator().setNote(note, strength);						
		}
		
		lineOut.start();

		// Setting values on the display
		screen.setOSCMixMode(voices[0].getOscilator().getMixMode());
		screen.setOSCMIXRatio(voices[0].getOscilator().getRatio());
		
		screen.setAmpLFOType(amp.getLFO().getType());
		screen.setAmpLFOAmplitude(amp.getLFO().getAmplitude());
		screen.setAmpLFORate(amp.getLFO().getRate());
		
		screen.setFilterLFOType(voices[0].getFilter().getLFO().getType());
		screen.setFilterLFOAmplitude(voices[0].getFilter().getLFO().getAmplitude());
		screen.setFilterLFORate(voices[0].getFilter().getLFO().getRate());
		
		screen.setOSC1Type(voices[0].getOscilator().getOsc1Type());
		screen.setOSC2Type(voices[0].getOscilator().getOsc2Type());
		screen.setOSC1Detune(voices[0].getOscilator().getOsc1Detune());
		screen.setOSC2Detune(voices[0].getOscilator().getOsc2Detune());
		screen.setOSC1Phase(voices[0].getOscilator().getOsc1Phase());
		screen.setOSC2Phase(voices[0].getOscilator().getOsc2Phase());
		
		screen.setAmpAmplitude(amp.getAmplitude());
		
		screen.setFilterFrequency(voices[0].getFilter().getFrequency());
		screen.setFilterResonance(voices[0].getFilter().getResonance());
		screen.setFilterType(voices[0].getFilter().getType());
		
		screen.setDistortionEnabled(effects.isDistortionEnabled());
		screen.setDistortionGain((float) effects.getDistortion().gain.get());
		screen.setDistortionStrength((float) effects.getDistortion().strength.get());
		screen.setDistortionLevel((float) effects.getDistortion().level.get());
		
		screen.setBitCrusherEnabled(effects.isBitCrusherEnabled());
		screen.setBitCrusherResolution((float) effects.getBitCrusher().resolution.get());
		screen.setBitCrusherBits((int) effects.getBitCrusher().bits.get());
		screen.setBitCrusherLevel((float) effects.getBitCrusher().level.get());
	}
	
	//--------------------------------------------------------------------------	
	
	@Override
	public void send(MidiMessage message, long timeStamp)
	{
		if (message instanceof ShortMessage)
		{
			ShortMessage msg = (ShortMessage) message;
			
			switch (msg.getCommand())
			{
				case ShortMessage.NOTE_ON:
				{					
					Voice voice;
					int note = msg.getData1();
					float strength = msg.getData2() / 128.0f;
					
					if (!free_voices.isEmpty())
					{
						voice = free_voices.remove(0);						
					}
					else
					{
						voice = used_voices.remove(0);
						voice_note_map.remove(voice.getOscilator().getNote());
					}
					
					voice.getOscilator().setNote(note, strength);
					voice_note_map.put(note, voice);
					used_voices.add(voice);
					
					pressed_notes.add(note);
					pressed_strengths.add(strength);
					
					break;
				}
				
				case ShortMessage.NOTE_OFF:
				{
					int note = msg.getData1();
					
					if (!pressed_notes.isEmpty())
					{
						int idx = pressed_notes.indexOf(note);
						pressed_notes.remove(idx);
						pressed_strengths.remove(idx);
					}
					
					if (voice_note_map.containsKey(note))
					{
						Voice voice = voice_note_map.remove(note);
						voice.getOscilator().endNote();

						used_voices.remove(voice);
						free_voices.add(voice);						
						voice_note_map.remove(note);
						
						if (!pressed_notes.isEmpty())
						{	
							int last_idx = -1;
						
							for (int i = pressed_notes.size() - 1 ; i >= 0 ; i--)
							{
								int played_note = pressed_notes.get(i);
																
								if (!voice_note_map.containsKey(played_note))
								{
									last_idx = i;
									break;
								}
							}
							
							if (last_idx >= 0)
							{
								int old_note = pressed_notes.get(last_idx);
								float old_strength = pressed_strengths.get(last_idx);

								voice.getOscilator().setNote(old_note, old_strength);
								voice_note_map.put(old_note, voice);
								used_voices.add(voice);	
								free_voices.remove(voice);
							}
						}
					}
																														
					break;
				}
				
				case ShortMessage.CONTROL_CHANGE:
					processControl(msg.getData1(), msg.getData2());
					break;
					
				default:
					break;
			}
			
//			System.out.println("A: " + msg.getData1());
//			System.out.println("B: " + msg.getData2());
		}
	}
	
	//--------------------------------------------------------------------------
	
	private void processControl(int id, int value)
	{
		switch (id)
		{
			// ------------------------------------------------
			// AMP/MIX CONTROL
			// ------------------------------------------------
			
			case AmpLFOToggle:
				ampLFOFlag = (value > 63);
				break;
			
			case AmpAmplitude:
			{		
				float amplitude = value / 127.0f;
				
				amp.setAmplitude(amplitude);				
				screen.setAmpAmplitude(amplitude);
				
				break;
			}
			
			case OSCMixModeAmpLFOType:
			{
				if (ampLFOFlag)
				{
					LFOType type = LFOType.get(value);
					
					if (type != amp.getLFO().getType())
					{
						amp.getLFO().setType(type);
						screen.setAmpLFOType(type);
						
						rebuild();
					}
				}
				else
				{
					MixMode mode = value > 63 ? MixMode.Mix : MixMode.Modulate;

					if (mode != voices[0].getOscilator().getMixMode())
					{
						screen.setOSCMixMode(mode);

						for (Voice v : voices)
							v.getOscilator().setMixMode(mode);

						rebuild();
					}	
				}				
				
				break;
			}
			
			case OSCMixRatioAmpLFOAmplitude:
			{
				if (ampLFOFlag)
				{
					float amplitude = value / 127.0f;
					
					amp.getLFO().setAmplitude(amplitude);
					screen.setAmpLFOAmplitude(amplitude);
				}
				else
				{
					float ratio = value / 127.0f;

					screen.setOSCMIXRatio(ratio);

					for (Voice v : voices)
						v.getOscilator().setRatio(ratio);					
				}
				
				break;
			}
			
			case AmpLFORate:
			{
				if (ampLFOFlag)
				{
					float rate = value / 127.0f;
					rate *= 19.0f;
					rate += 1.0f;
					
					amp.getLFO().setRate(rate);
					screen.setAmpLFORate(rate);
				}
				
				break;
			}


			// ------------------------------------------------
			// FILTER CONTROL
			// ------------------------------------------------
			
			case FilterLFOToggle:
				filterLFOFlag = (value > 63);
				break;
			
			case FilterPassTypeLFOType:
			{
				if (filterLFOFlag)
				{
					LFOType type = LFOType.get(value);
					
					if (type != voices[0].getFilter().getLFO().getType())
					{
						for (Voice v : voices)
							v.getFilter().getLFO().setType(type);
						
						screen.setFilterLFOType(type);
						
						rebuild();
					}					
				}
				else
				{
					FilterType type;

					if (value <= 43)
						type = FilterType.LowPass;
					else if (value <= 85)
						type = FilterType.BandPass;
					else
						type = FilterType.HighPass;

					if (type != voices[0].getFilter().getType())
					{
						screen.setFilterType(type);

						for (Voice v : voices)
							v.getFilter().setType(type);

						rebuild();
					}	
				}
												
				break;
			}
				
			case FilterResonanceLFOAmplitude:
			{
				if (filterLFOFlag)
				{
					float amplitude = value / 127.0f;
					
					for (Voice v : voices)
						v.getFilter().getLFO().setAmplitude(amplitude);
					
					screen.setFilterLFOAmplitude(amplitude);
				}
				else
				{
					float resonance = value / 127.0f;
				
					screen.setFilterResonance(resonance);

					for (Voice v : voices)
						v.getFilter().setResonance(resonance);
				}
				
				break;
			}
			
			case FilterFrequencyLFORate:
			{	
				if (filterLFOFlag)
				{
					float rate = value / 127.0f;
					rate *= 19.0f;
					rate += 1.0f;
					
					for (Voice v : voices)
						v.getFilter().getLFO().setRate(rate);
					
					screen.setFilterLFORate(rate);				
				}
				else
				{
					float frequency = value / 127.0f;
					frequency *= 8000.0f;

					screen.setFilterFrequency(frequency);

					for (Voice v : voices)
						v.getFilter().setFrequency(frequency);	
				}
				
				
				break;
			}
			
			// ------------------------------------------------
			// OSCILLATOR 1 CONTROL
			// ------------------------------------------------
			
			case OSC1LFOToggle:
				OSC1LFOFlag = (value > 63);
				break;
			
			case OSC1TypeLFORate:
			{
				OscilatorType type = OscilatorType.get(value);
				
				if (type != voices[0].getOscilator().getOsc1Type())
				{
					screen.setOSC1Type(type);
					
					for (Voice v : voices)
						v.getOscilator().setOsc1Type(type);
					
					rebuild();
				}
				
				break;
			}
			
			case OSC1DetuneLFOAmplitude:
			{
				// Make it 2 octaves.
				
				float detune = value / 127.0f;
				
				if (detune > 0.51f)
					detune *= 2.0f;
				else if (detune < 0.49f)
					detune += 0.5f;
				else
					detune = 1.0f;
								
				screen.setOSC1Detune(detune);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Detune(detune);
				
				break;
			}
			
			case OSC1PhaseLFOType:
			{
				float phase = (value / 63.5f) - 1.0f;
				
				screen.setOSC1Phase(phase);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Phase(phase);
				
				break;
			}	
			
			// ------------------------------------------------
			// OSCILLATOR 2 CONTROL
			// ------------------------------------------------
			
			case OSC2LFOToggle:
				OSC2LFOFlag = (value > 63);
				break;
			
			case OSC2TypeLFORate:
			{
				OscilatorType type = OscilatorType.get(value);
				
				if (type != voices[0].getOscilator().getOsc2Type())
				{
					screen.setOSC2Type(type);
					
					for (Voice v : voices)
						v.getOscilator().setOsc2Type(type);
					
					rebuild();
				}
				
				break;
			}
		
			case OSC2DetuneLFOAmplitude:
			{
				// Make it 2 octaves.
				
				float detune = value / 127.0f;

				if (detune > 0.51f)
					detune *= 2.0f;
				else if (detune < 0.49f)
					detune += 0.5f;
				else
					detune = 1.0f;
								
				screen.setOSC2Detune(detune);
				
				for (Voice v : voices)
					v.getOscilator().setOsc2Detune(detune);
				
				break;
			}
			
			case OSC2PhaseLFOType:
			{
				float phase = (value / 63.5f) - 1.0f;
				
				screen.setOSC2Phase(phase);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Phase(phase);
				
				break;
			}
			
			// ------------------------------------------------
			// DISTORTION CONTROL
			// ------------------------------------------------
			
			case DistortionEnable:
			{
				if (value > 63)
				{
					boolean enabled = !effects.isDistortionEnabled();
					
					screen.setDistortionEnabled(enabled);					
					effects.setDistortionEnabled(enabled);
					
					rebuild();
				}
				
				break;
			}
			
			case DistortionGainLFORate:
			{
				float gain = value / 6.35f;
				
				screen.setDistortionGain(gain);				
				effects.getDistortion().gain.set(gain);
				
				break;
			}
			
			case DistortionStrengthLFOAmplitude:
			{
				float strength = value / 15.825f;
				
				screen.setDistortionStrength(strength);				
				effects.getDistortion().strength.set(strength);
				
				break;
			}
			
			case DistortionLevelLFOType:
			{
				float level = value / 63.5f;
				
				screen.setDistortionLevel(level);				
				effects.getDistortion().level.set(level);
				
				break;
			}
			
			// ------------------------------------------------
			// BITCRUSHER CONTROL
			// ------------------------------------------------
			
			case BitCrusherEnable:
			{
				if (value > 63)
				{
					boolean enabled = !effects.isBitCrusherEnabled();
					
					screen.setBitCrusherEnabled(enabled);					
					effects.setBitCrusherEnabled(enabled);
					
					rebuild();
				}
				
				break;
			}
			
			case BitCrusherResolutionLFORate:
			{
				float resolution = value / 4.09f + 1.0f;
				
				screen.setBitCrusherResolution(resolution);				
				effects.getBitCrusher().resolution.set(resolution);
				
				break;
			}
			
			case BitCrusherBitsLFOAmplitude:
			{
				float bits = (1.0f - value / 127.0f) * 30.0f + 2.0f;
				
				screen.setBitCrusherBits((int) bits);				
				effects.getBitCrusher().bits.set(bits);
				
				break;
			}
			
			case BitCrusherLevelLFOType:
			{
				float level = value / 63.5f;
				
				screen.setBitCrusherLevel(level);				
				effects.getBitCrusher().level.set(level);
				
				break;
			}
			
			
		}
	}
	
	//--------------------------------------------------------------------------

	@Override
	public void close()
	{
		// Intentionally empty.
	}
	
	//--------------------------------------------------------------------------
}
