package net.strangesoft.bloom.tests.filter;

import static org.junit.Assert.*;

import net.strangesoft.bloom.filter.FullBloomFilter;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;
import net.strangesoft.bloom.hash.murmur2.Murmur2Factory;

import org.junit.BeforeClass;
import org.junit.Test;

public class FullBloomFilterTest {

	private static FullBloomFilter<String> gp;
	private static FullBloomFilter<String> sgp;
	private static FullBloomFilter<String> gpp;
	
	private static final IntHashFunctionFactory hf = new Murmur2Factory();

	@BeforeClass
	public static void prepareTest() {
		gp = new FullBloomFilter<String>(5, 80000000, hf);
		sgp = new FullBloomFilter<String>(5, 6000000, hf);
		gpp = new FullBloomFilter<String>(2560000, 0.00001, hf);
		StringBuilder sb = new StringBuilder(4);
		int m = 40;
		sb.setLength(4);

		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();						
						gp.put(s);
						sgp.put(s);
						gpp.put(s);
					}
				
	}

	@Test
	public void testGetSize() {
		FullBloomFilter<String> f = new FullBloomFilter<String>(5, 80000000, hf);
		if (f.getSize() != 10000000)
			fail("Size is " + f.getSize() + ", should be 10000000");

		f = new FullBloomFilter<String>(4, 40000021, hf);
		if (f.getSize() != 5000002)
			fail("Size is " + f.getSize() + ", should be 5000002");

		f = new FullBloomFilter<String>(4, 8, hf);
		if (f.getSize() != 1)
			fail("Size is " + f.getSize() + ", should be 1");

		f = new FullBloomFilter<String>(4, 1, hf);
		if (f.getSize() != 1)
			fail("Size is " + f.getSize() + ", should be 1");

		if (gp.getSize() != 10000000)
			fail("Size is " + gp.getSize() + ", should be 1");
		
		if (gp.getBitSize() != 80000000)
			fail("Bit size is " + gp.getBitSize() + ", should be 80000000");
	}

	@Test
	public void testCheckExists() {
		
		StringBuilder sb = new StringBuilder(4);
		int m = 40;
		sb.setLength(4);

		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();						
						if (!gp.checkExists(s))
							fail(s + " should be in filter");
					}
				
		if (gp.checkExists("~!@#"))
			fail("~!@# should NOT be in filter");
		
		gp.put("~!@#");		
		if (!gp.checkExists("~!@#"))
			fail("~!@# should now be in filter");		
		
		// the following 2 tests MIGHT (but usually should NOT) fail! This is probability theory, baby...
		boolean result = false;
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				for (int k = 0; k < 5; k++)
					for (int l = 0; l < 5; l++) {
						sb.setCharAt(0, (char) ("ß".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("ß".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("ß".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("ß".codePointAt(0) + l));
						String s = sb.toString();						
						result |= sgp.checkExists(s);						
					}				
		if (!result) 
			fail("There should be some false positives in bad quality filter. But not necessary...");
		
		result = false;
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 3; k++)
					for (int l = 0; l < 3; l++) {
						sb.setCharAt(0, (char) ("ß".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("ß".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("ß".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("ß".codePointAt(0) + l));
						String s = sb.toString();						
						result |= gp.checkExists(s);						
					}
		if (result) 
			fail("There SHOULD be NO false positives in high quality filter. But that's possible...");
	}
	
	public double round(double num, int cnt) {
		double result = num * Math.pow(10, cnt);
		result = Math.round(result);
		result = result / Math.pow(10, cnt);
		return result;
		}

	@Test
	public void testGetQuality() {
		if (gp.getQuality() <= 0.999) 
			fail("Filter quality should be > 0.999");
		
		if (sgp.getQuality() >= 0.5) 
			fail("Filter quality should be < 0.5");
		
		if (round(gpp.getQuality(),5) != (1-0.00001))
			fail("Filter quality should be = " + (1-0.00001) + ", not " +  gpp.getQuality());
	}
}
