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
import eu.cherrytree.synth.modules.FilterType;
import eu.cherrytree.synth.modules.OscilatorType;
import java.util.Collections;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Synth implements Receiver
{
	//--------------------------------------------------------------------------
	
	private static final int OSC1Amplitude		= 16;
	private static final int OSC1Detune			= 17;
	private static final int OSC1Phase			= 18;
	private static final int OSC1Type			= 24;
	private static final int OSC2Amplitude		= 20;
	private static final int OSC2Detune			= 21;
	private static final int OSC2Phase			= 22;
	private static final int OSC2Type			= 25;
	private static final int OSCMixMode			= 26;
	private static final int FilterFrequency	= 28;
	private static final int FilterResonance		= 29;
	private static final int FilterPassType		= 30;
	private static final int FilterLFORate		= 46;
	private static final int FilterLFOAmplitude	= 47;
	private static final int FilterLFOType		= 48;
	private static final int AmpAmplitude		= 62;
	private static final int AmpLFORate			= 50;
	private static final int AmpLFOAmplitude		= 51;
	private static final int AmpLFOType			= 52;
	
	//--------------------------------------------------------------------------
	
	private Synthesizer synthesizer;
	private LineOut lineOut;
	private AmpModule amp;
	
	private Voice[] voices;

	private HashMap<Integer, Voice> voice_note_map = new HashMap<>();
	private ArrayList<Voice> used_voices = new ArrayList<>();
	private ArrayList<Voice> free_voices = new ArrayList<>();
	
	//--------------------------------------------------------------------------
	
	public Synth(int voiceCount)
	{
		synthesizer = JSyn.createSynthesizer();
		synthesizer.start();
		
		lineOut = new LineOut();
		
		voices = new Voice[voiceCount];
		
		for (int i = 0 ; i < voiceCount ; i++)
		{
			voices[i] = new Voice(synthesizer);
			voices[i].rebuild();
		}
		
		amp = new AmpModule(synthesizer, voiceCount);		
		amp.setVoices(voices);
		
		amp.getOutput().connect(lineOut.input);
		
		synthesizer.add(lineOut);
		
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
		lineOut.stop();
				
		lineOut.input.disconnectAll();
		
		for (Voice v : voices)
			v.rebuild();
		
		amp.setVoices(voices);
		
		amp.getOutput().connect(lineOut.input);	
		
		lineOut.start();
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
					
					break;
				}
				
				case ShortMessage.NOTE_OFF:
				{
					int note = msg.getData1();
					
					Voice voice = voice_note_map.remove(note);
					voice.getOscilator().endNote();
					
					used_voices.remove(voice);
					free_voices.add(voice);
					
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
			case FilterFrequency:
			{								
				float frequency = value / 127.0f;
				frequency *= 8000.0f;
				
				System.out.println("Setting filter frequency: " + frequency);
				
				for (Voice v : voices)
					v.getFilter().setFrequency(frequency);
				
				break;
			}
			
			case FilterResonance:
			{
				float resonance = value / 127.0f;
				
				System.out.println("Setting filter resonance: " + resonance);
				
				for (Voice v : voices)
					v.getFilter().setResonance(resonance);
				
				break;
			}
			
			case FilterPassType:
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
					System.out.println("Setting filter type: " + type);
					
					for (Voice v : voices)
						v.getFilter().setType(type);

					rebuild();
				}
								
				break;
			}
			
			case AmpAmplitude:
			{								
				float amplitude = value / 127.0f;
				amp.setAmplitude(amplitude);
				
				System.out.println("Setting amp amplitude: " + amplitude);
				
				break;
			}
			
			case OSC1Type:
			{
				OscilatorType type = OscilatorType.get(value);
				
				if (type != voices[0].getOscilator().getOsc1Type())
				{
					System.out.println("Setting osc1 type: " + type);
					
					for (Voice v : voices)
						v.getOscilator().setOsc1Type(type);
					
					rebuild();
				}
				
				break;
			}
			
			case OSC2Type:
			{
				OscilatorType type = OscilatorType.get(value);
				
				if (type != voices[0].getOscilator().getOsc2Type())
				{
					System.out.println("Setting osc2 type: " + type);
					
					for (Voice v : voices)
						v.getOscilator().setOsc2Type(type);
					
					rebuild();
				}
				
				break;
			}
			
			case OSC1Amplitude:
			{
				float amplitude = value / 127.0f;
				
				System.out.println("Setting osc1 amplitude: " + amplitude);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Amplitude(amplitude);
				
				break;
			}
			
			case OSC1Detune:
			{
				float detune = value / 127.0f;
				
				if (detune > 0.51f)
					detune *= 2.0f;
				else if (detune < 0.49f)
					detune += 0.5f;
				else
					detune = 1.0f;
								
				System.out.println("Setting osc1 detune: " + detune);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Detune(detune);
				
				break;
			}
			
			case OSC1Phase:
			{
				float phase = (value / 63.5f) - 1.0f;
				
				System.out.println("Setting osc1 phase: " + phase);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Phase(phase);
				
				break;
			}
			
			case OSC2Amplitude:
			{
				float amplitude = value / 127.0f;
				
				System.out.println("Setting osc2 amplitude: " + amplitude);
				
				for (Voice v : voices)
					v.getOscilator().setOsc2Amplitude(amplitude);
				
				break;
			}			
			
			case OSC2Detune:
			{
				float detune = value / 127.0f;

				if (detune > 0.51f)
					detune *= 2.0f;
				else if (detune < 0.49f)
					detune += 0.5f;
				else
					detune = 1.0f;
								
				System.out.println("Setting osc2 detune: " + detune);
				
				for (Voice v : voices)
					v.getOscilator().setOsc2Detune(detune);
				
				break;
			}
			
			case OSC2Phase:
			{
				float phase = (value / 63.5f) - 1.0f;
				
				System.out.println("Setting osc1 phase: " + phase);
				
				for (Voice v : voices)
					v.getOscilator().setOsc1Phase(phase);
				
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
