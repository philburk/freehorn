package org.frogpeak.horn;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Interesting things about Prime numbers.
 * @author Phil Burk (C) 2003
 */
public class Primes
{
	/** Primes below 100 */
	public static final int[] primes =
		{
			1,
			2,
			3,
			5,
			7,
			11,
			13,
			17,
			19,
			23,
			29,
			31,
			37,
			41,
			43,
			47,
			53,
			59,
			61,
			67,
			71,
			73,
			79,
			83,
			89,
			97 };

	public static int highestPrimeFactor(int n)
	{
		if (n > 100)
			throw new RuntimeException("Input too large, " + n + " > 100 ");
		for (int i = primes.length - 1; i > 0; i--)
		{
			int p = primes[i];
			if ((p <= n) && ((n % primes[i]) == 0))
			{
				return p;
			}
		}
		return 1;
	}
	/** Compare prime complexity of two numbers and return -1 if pc(a)<pc(b),
	 * 0 if equal, and 1 if pc(a)>pc(b)
	 * @param a
	 * @param b
	 * @return
	 */
	public static int comparePrimeComplexity(int a, int b)
	{
		int result = 0;
		if (a == b)
			return 0;
		int highestA = 1;
		int highestB = 1;
		while (highestA == highestB)
		{
			a = a / highestA;
			b = b / highestB;
			highestA = highestPrimeFactor(a);
			highestB = highestPrimeFactor(b);
		}
		return (highestA > highestB) ? 1 : -1;
	}

	public static int[] createIntegersSortedByPrimeComplexity(int max)
	{
		// Create array of ordered Integer objects.
		Integer[] ints = new Integer[max];
		for (int i = 0; i < max; i++)
		{
			ints[i] = new Integer(i + 1);
		}
		// Define comparator for easy sorting.
		Comparator comp = new Comparator()
		{

			public int compare(Object o1, Object o2)
			{
				int a = ((Integer) o1).intValue();
				int b = ((Integer) o2).intValue();

				return comparePrimeComplexity(a, b);
			}
		};
		
		Arrays.sort( ints, comp );
		// Convert from Integer objects to more useful ints.
		int[] pis = new int[max];
		for (int i = 0; i < max; i++)
		{
			pis[i] = ints[i].intValue();
		}
		return pis;
	}
	
	public static void main(String[] args)
	{
		for (int i = 1; i < 10; i++)
		{
			System.out.println(
				"Highest factor of " + i + " = " + highestPrimeFactor(i));
		}
		int[] pis = createIntegersSortedByPrimeComplexity(24);
		for (int i = 0; i < pis.length; i++)
		{
			// if( (i & 7) == 0 ) System.out.println();
			System.out.print( pis[i] + ", ");
		}
	}
}
