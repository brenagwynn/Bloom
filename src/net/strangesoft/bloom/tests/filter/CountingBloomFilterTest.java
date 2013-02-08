package net.strangesoft.bloom.tests.filter;

import static org.junit.Assert.*;

import net.strangesoft.bloom.filter.CountingBloomFilter;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;
import net.strangesoft.bloom.hash.murmur2.Murmur2Factory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CountingBloomFilterTest {

	private static CountingBloomFilter<String> gp;
	private static CountingBloomFilter<String> bgp;

	private static final IntHashFunctionFactory hf = new Murmur2Factory();

	@BeforeClass
	public static void prepareTest() {
		gp = new CountingBloomFilter<String>(3000000, 2e-6, hf,
				new CountingBloomFilter.OverflowCallback() {
					@Override
					public void onOverflow() {
						System.out.println("Overflow in gp");
					}
				});
		bgp = new CountingBloomFilter<String>(25600, 1e-2, hf,
				new CountingBloomFilter.OverflowCallback() {
					@Override
					public void onOverflow() {
						System.out.println("Overflow in bgp");
					}
				});
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
						bgp.put(s);
					}

	}

	@Test
	public void testRemoval() {
		if (gp.checkExists("QWERTYASDFG"))
			fail("QWERTYASDFG should NOT be in filter now");

		gp.put("QWERTYASDFG");
		if (!gp.checkExists("QWERTYASDFG"))
			fail("QWERTYASDFG should be in filter now");

		gp.remove("QWERTYASDFG");
		if (gp.checkExists("QWERTYASDFG"))
			fail("QWERTYASDFG should not be in filter now");

		long ss = gp.getStorageSize();
		gp.remove("NOT EXISTS");
		if (ss != gp.getStorageSize())
			fail("Storage size should not have changed");

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
							fail(s + " must be in filter");
						gp.remove(s);
						if (gp.checkExists(s))
							fail(s + " should NOT be in filter anymore");
					}

		if (gp.getStorageSize() != 0)
			fail("Storage size must be 0 now, not " + gp.getStorageSize());

		boolean result = false;
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();
						bgp.remove(s);
						if (bgp.checkExists(s))
							result = true;
					}
		if (!result)
			fail("Some false NEGATIVES should exists in low quality filter");

	}
	
	@AfterClass
	public static void finishTest() {
		gp = null;
		bgp = null;
	}
}
