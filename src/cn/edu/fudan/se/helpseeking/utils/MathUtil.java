package cn.edu.fudan.se.helpseeking.utils;

import java.util.List;

public class MathUtil
{
	public static double calculateEntropy(List<Integer> LeafOfEachNode, int countOfLeaf)
	{
		double result = 0.0;
		for(int value : LeafOfEachNode)
		{
			result = result - (value * 1.0 / countOfLeaf ) * Math.log(value * 1.0 /countOfLeaf);
		}
		return result;
	}
}
