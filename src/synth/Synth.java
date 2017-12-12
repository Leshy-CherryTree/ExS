/****************************************/
/* Synth.java							*/
/* Created on: 08-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package synth;


import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.FilterStateVariable;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.MixerMono;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.unitgen.UnitOscillator;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;



/**
 * Play a tone using a JSyn oscillator.
 * 
 * @author Phil Burk (C) 2010 Mobileer Inc
 */
public class Synth
{

	Synthesizer synth;
	
	UnitOscillator osc1;
	UnitOscillator osc2;
	
	MixerMono mixer;
	
	LineOut lineOut;
	
	FilterStateVariable filter;

	private void testSynth()
	{

		// Create a context for the synthesizer.
		synth = JSyn.createSynthesizer();

		// Start synthesizer using default stereo output at 44100 Hz.
		synth.start();

		// Add a tone generator.
		synth.add(osc1 = new SineOscillator());
		synth.add(osc2 = new SineOscillator());
		
		synth.add(mixer = new MixerMono(2));
		
		// Add a stereo audio output unit.
		synth.add(lineOut = new LineOut());
		
		synth.add(filter = new FilterStateVariable());

//		// Connect the oscillator to both channels of the output.
//		osc1.output.connect(osc2.amplitude);
		
//		//osc1.output.connect(lineOut.input);
//		osc2.output.connect(lineOut.input);

		osc1.output.connect(0, mixer.input, 0);
		osc2.output.connect(0, mixer.input, 1);
		
		mixer.output.connect(filter.input);
		filter.bandPass.connect(lineOut.input);
		
		filter.frequency.set(500);
		filter.amplitude.set(0.7);
		filter.resonance.set(0.1);

		// Set the frequency and amplitude for the sine wave.
//		osc1.frequency.set(145.0);
		osc1.amplitude.set(0.0);
		
//		osc2.frequency.set(345.0);
		osc2.amplitude.set(0.0);
		
		lineOut.start();
				
		testMIDI();

//		System.out.println("You should now be hearing a sine wave. ---------");
//
//		// Sleep while the sound is generated in the background.
//		try
//		{
//			double time = synth.getCurrentTime();
//			System.out.println("time = " + time);
//			// Sleep for a few seconds.
//			synth.sleepUntil(time + 2.0);
//		}
//		catch (InterruptedException e)
//		{
//			e.printStackTrace();
//		}
//
//		System.out.println("Stop playing. -------------------");
//		// Stop everything.
//		synth.stop();
	}
	
	private class REC implements Receiver
	{
		float notes[] = {
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

		@Override
		public void send(MidiMessage message, long timeStamp)
		{
			if (message instanceof ShortMessage)
			{
				ShortMessage msg = (ShortMessage) message;

				float str = msg.getData2() / 128.0f;
				
				switch (msg.getCommand())
				{
					case ShortMessage.NOTE_ON:
						System.out.println("NOTE_ON");
						
						osc1.amplitude.set(str);
						osc2.amplitude.set(str);
						
						osc1.frequency.set(notes[msg.getData1()] / 2.0f);
						osc2.frequency.set(notes[msg.getData1()]);
						
						break;
						
					case ShortMessage.NOTE_OFF:
						System.out.println("NOTE_OFF");
						
						osc1.amplitude.set(0.0f);
						osc2.amplitude.set(0.0f);
						
						break;
						
					case ShortMessage.CONTROL_CHANGE:
						System.out.println("CONTROL_CHANGE");
						break;
						
					default:
						break;
				}

				System.out.println("A: " + msg.getData1());
				System.out.println("B: " + msg.getData2());
			}
		}

		@Override
		public void close()
		{
			
		}
		
	}
	
	private void testMIDI()
	{
		REC reciever = new REC();
		
		Info[] infos = MidiSystem.getMidiDeviceInfo();
			
		for (Info info : infos)
		{
			try
			{								
				MidiDevice inputDevice = MidiSystem.getMidiDevice(info);

				if (inputDevice.getMaxTransmitters() != 0)
				{					
					inputDevice.open();						
					inputDevice.getTransmitter().setReceiver(reciever);

					System.out.println("Opened: " + info.getName() + " - " + info.getDescription());					
				}																		
			}
			catch (MidiUnavailableException ex)
			{
				System.out.println("Cannot open: " + info.getName() + " - " + info.getDescription());
			}
		}
	}

	public static void main(String[] args)
	{
		new Synth().testSynth();
	}
}
