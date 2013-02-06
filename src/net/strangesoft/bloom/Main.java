package net.strangesoft.bloom;

import net.strangesoft.bloom.filter.BloomFilter;
import net.strangesoft.bloom.profiling.Profilable;
import net.strangesoft.bloom.profiling.ProfilableArrayList;
import net.strangesoft.bloom.profiling.ProfilableHashMap;
import net.strangesoft.bloom.profiling.ProfilableMurBloomFilter;

public class Main {

	@SuppressWarnings({ "rawtypes" })
	private static void doProfile(Profilable c, int tries, int ssize,
			String name) {
		String s;
		StringBuilder sb = new StringBuilder(4);
		sb.setLength(4);
		int t = 0;
		int g;
		System.out.print(name);

		long startTime = System.nanoTime();
		for (int i = 0; i < tries; i++) {
			g = (int) (0 + Math.random() * (ssize + 5));
			sb.setCharAt(0, (char) ("A".codePointAt(0) + g));
			g = (int) (0 + Math.random() * (ssize + 5));
			sb.setCharAt(1, (char) ("A".codePointAt(0) + g));
			g = (int) (0 + Math.random() * (ssize + 5));
			sb.setCharAt(2, (char) ("A".codePointAt(0) + g));
			g = (int) (0 + Math.random() * (ssize + 5));
			sb.setCharAt(3, (char) ("A".codePointAt(0) + g));
			s = sb.toString();

			if (c.test(s))
				t++;

			if (i % (tries / 10) == 0)
				System.out.print(".");
		}
		long estimatedTime = (System.nanoTime() - startTime) / 1000l / 1000l;
		System.out.print(" " + estimatedTime + " ms");
		if (c instanceof BloomFilter)
			System.out.println(", filter quality: "
					+ ((BloomFilter) c).getQuality());
		else
			System.out.println("");
		System.out.println("Positives: " + t);

		return;
	}

	public static void main(String[] args) {
		ProfilableMurBloomFilter<String> f = new ProfilableMurBloomFilter<String>(5, 80000000);		
		long cnt = 0;
		StringBuilder sb = new StringBuilder(4);
		int m = 40;
		sb.setLength(4);
		ProfilableArrayList<String> al = new ProfilableArrayList<String>();
		ProfilableHashMap<Long, String> hm = new ProfilableHashMap<Long, String>();

		long startTime = System.nanoTime();
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();
						cnt++;
						f.put(s);
					}
		long estimatedTime = (System.nanoTime() - startTime) / 1000l / 1000l;
		System.out.println("Bloom filter fill: " + estimatedTime + " ms");

		cnt = 0;
		startTime = System.nanoTime();
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();
						cnt++;
						al.add(s);
					}
		estimatedTime = (System.nanoTime() - startTime) / 1000l / 1000l;
		System.out.println("ArrayList fill: " + estimatedTime + " ms");

		cnt = 0;
		startTime = System.nanoTime();
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				for (int k = 0; k < m; k++)
					for (int l = 0; l < m; l++) {
						sb.setCharAt(0, (char) ("A".codePointAt(0) + i));
						sb.setCharAt(1, (char) ("A".codePointAt(0) + j));
						sb.setCharAt(2, (char) ("A".codePointAt(0) + k));
						sb.setCharAt(3, (char) ("A".codePointAt(0) + l));
						String s = sb.toString();
						cnt++;
						hm.put(cnt, s);
					}
		estimatedTime = (System.nanoTime() - startTime) / 1000l / 1000l;
		System.out.println("HashMap fill: " + estimatedTime + " ms");

		System.out.println("Generated " + cnt + " strings");
		System.out.println("Running tests...");

		doProfile(f, 1000, 40, "Bloom");
		doProfile(al, 1000, 40, "ArrayList");
		doProfile(hm, 1000, 40, "HashMap");

	}

}
