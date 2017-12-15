/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cherrytree.synth.operators;

import com.jsyn.ports.UnitInputPort;
import com.jsyn.unitgen.UnitBinaryOperator;

/**
 *
 * @author leszek.szczepanski
 */
public class Modulate extends UnitBinaryOperator
{
	//--------------------------------------------------------------------------
	
	public UnitInputPort ratio = new UnitInputPort("Ratio", 0.5f);
	
	//--------------------------------------------------------------------------

	public Modulate()
	{
		addPort(ratio);
	}
			
	//--------------------------------------------------------------------------

	@Override
	public void generate(int start, int limit)
	{
		double[] inputs_a = inputA.getValues();
		double[] inputs_b = inputB.getValues();
		double[] ratios = ratio.getValues();
		double[] outputs = output.getValues();
		
		for (int i = start; i < limit; i++)
			outputs[i] = inputs_a[i] * ratios[i] + inputs_b[i] * (1.0 - ratios[i]);
	}
	
	//--------------------------------------------------------------------------
}
