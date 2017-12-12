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
import java.util.Collections;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class Synth implements Receiver
{
	//--------------------------------------------------------------------------
	
	private Synthesizer synthesizer;
	private LineOut lineOut;
	private AmpModule amp;
	
	private Voice[] voices;

	HashMap<Integer, Voice> voice_note_map = new HashMap<>();
	ArrayList<Voice> used_voices = new ArrayList<>();
	ArrayList<Voice> free_voices = new ArrayList<>();
	
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
					System.out.println("CONTROL_CHANGE");
					break;
					
				default:
					break;
			}
			
//			System.out.println("A: " + msg.getData1());
//			System.out.println("B: " + msg.getData2());
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
