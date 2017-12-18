/****************************************/
/* SampleAndHoldOscillator.java			*/
/* Created on: 17-12-2017				*/
/* Copyright Cherry Tree Studio 2017		*/
/* Released under EUPL v1.1				*/
/****************************************/

package eu.cherrytree.synth.oscillator;

import com.jsyn.unitgen.UnitOscillator;

import java.util.Random;



/**
 *
 * @author Leszek Szczepański <leszek.gamedev@gmail.com>
 */
public class SampleAndHoldOscillator extends UnitOscillator
{
	//--------------------------------------------------------------------------
	
	private Random random = new Random();
	private double randomValue = 0.0f;
	
	private boolean change = false;
	
	//--------------------------------------------------------------------------
	
	@Override
	public void generate(int start, int limit)
	{
        double[] frequencies = frequency.getValues();
        double[] amplitudes = amplitude.getValues();
        double[] outputs = output.getValues();

        double currentPhase = phase.getValue();

        for (int i = start; i < limit; i++)
		{
            double phaseIncrement = convertFrequencyToPhaseIncrement(frequencies[i]);
            currentPhase = incrementWrapPhase(currentPhase, phaseIncrement);

            double ampl = amplitudes[i];
			
			boolean phs = currentPhase < 0.0;
			
			if (phs != change)
			{
				change = phs;
				randomValue = random.nextDouble();
			}
			
            outputs[i] = (phs ? -ampl : ampl) * randomValue;
        }

		phase.setValue(currentPhase);
	}
	
	//--------------------------------------------------------------------------
}
