package net.strangesoft.bloom.filter;

import net.strangesoft.bloom.hash.Murmur2;

public class Murmur2BloomFilter<T> implements BloomFilter<T> {

	private byte[] flags;

	private int size;
	private int bitsize;
	private int hashCount;

	private long storageSize;

	private int[] hash(byte[] data) {
		int[] result = new int[hashCount];

		Murmur2 hA = new Murmur2(0);
		int hashA = hA.hash(data);
		Murmur2 hB = new Murmur2(hashA);
		int hashB = hB.hash(data);

		for (int i = 0; i < hashCount; i++) {
			result[i] = hashA + i * hashB;
		}

		return result;
	}

	private void setupSizes(int hashCount, int size) {
		if (size >= 8)
			this.size = size >> 3;
		else
			this.size = 1;

		this.bitsize = this.size << 3;
		this.hashCount = hashCount;
		flags = new byte[this.size];
	}

	/**
	 * Returns a new Murmur2BloomFilter object.
	 * 
	 * @param hashCount
	 *            Number of hash functions. Hashes are calculated as a linear
	 *            combinations of 2 Murmur2 hash functions. Wise people say
	 *            that's ok: see {@link http
	 *            ://www.eecs.harvard.edu/~kirsch/pubs/bbbf/esa06.pdf}
	 * @param size
	 *            Bloom filter size <b>in bits</b>. If this parameter is < 8,
	 *            then the filter will have the size of 8 bits. Otherwise the
	 *            size will be (size / 8) * 8 bits.
	 * @return New Murmur2BloomFilter object with specified parameters.
	 * @see Murmur2
	 */
	public Murmur2BloomFilter(int hashCount, int size) {
		setupSizes(hashCount, size);
	}

	/**
	 * Returns a new Murmur2BloomFilter object based on given expected number of
	 * elements and false positive probability.
	 * 
	 * @param expectedNoE
	 *            Expected number of elements in filter
	 * @param falsePositiveProb
	 *            Allowed false positive probability. Must be between 0 and 1.
	 * @return New Murmur2BloomFilter with hash count calculated as
	 *         -ln(falsePositiveProb)/ln(2) and size calculated as -expectedNoE
	 *         * ln(falsePositiveProb) / ln(2)^2
	 * @see Murmur2
	 */
	public Murmur2BloomFilter(int expectedNoE, double falsePositiveProb) {
		int hCount = (int) Math.ceil(-(Math.log(falsePositiveProb) / Math
				.log(2)));
		int lSize = (int) Math.ceil(-expectedNoE * Math.log(falsePositiveProb)
				/ (Math.log(2) * Math.log(2)));
		setupSizes(hCount, lSize);
	}

	/**
	 * Returns a size of a filter in <b>bytes</b>
	 * 
	 * @return Size of the filter in bytes.
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * Returns a size of a filter in <b>bits</b>
	 * 
	 * @return Size of the filter in bits
	 */
	public Integer getBitSize() {
		return this.bitsize;
	}

	/**
	 * Returns a number of added elements
	 * 
	 * @return Number of elements in the internal storage
	 */
	public Long getStorageSize() {
		return storageSize;
	}

	private int getIndex(int hash) {
		long hl = (long) hash;
		hl += (long) Integer.MAX_VALUE;
		hl %= bitsize;
		return (int) hl;
	}

	/**
	 * Adds an element to the filter. Hashes are calculated based on the
	 * element.toString() value.
	 * 
	 * @param element
	 *            An element to add
	 */
	@Override
	public void put(T element) {
		int[] h = hash(element.toString().getBytes());
		synchronized (this) {
			for (int i = 0; i < hashCount; i++) {
				int idx = getIndex(h[i]);

				int cidx = idx >> 3;
				byte mask = (byte) (1 << (idx % 8));
				flags[cidx] |= mask;

			}
			storageSize++;
		}

	}

	@Override
	public boolean checkExists(T element) {
		boolean result = true;
		int[] h = hash(element.toString().getBytes());
		for (int i = 0; i < hashCount; i++) {
			int idx = getIndex(h[i]);

			int cidx = idx >> 3;
			byte mask = (byte) (1 << (idx % 8));
			synchronized (this) {
				result &= ((flags[cidx] & mask) != 0);
			}
		}
		return result;
	}

	@Override
	public double getQuality() {
		double x = Math.pow(1.0 - 1.0 / (double) bitsize,
				(double) (hashCount * storageSize));
		double q = Math.pow(1 - x, hashCount);

		return 1 - q; // 1 - probability of false positive
	}
}
