package net.strangesoft.bloom.filter;

import net.strangesoft.bloom.hash.IntHashFunction;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;

/**
 * An implementation of Bloom filter using hash functions that return int
 * values.
 * 
 * @see net.strangesoft.bloom.hash.IntHashFunction
 * @see net.strangesoft.bloom.hash.IntHashFunctionFactory
 */

public class FullBloomFilter<T> implements BloomFilter<T> {

	protected byte[] flags;

	protected int size;
	protected int bitsize;
	protected int hashCount;

	protected long storageSize;

	protected IntHashFunctionFactory factory;

	protected IntHashFunction hashes[];
	
	protected int[] seeds;

	protected int[] hash(byte[] data) {
		int[] result = new int[hashCount];

		for (int i = 0; i < hashCount; i++) {
			if (i == 0)
				result[i] = hashes[i].hash(data, 87914052 ^ seeds[i]);
			else
				result[i] = hashes[i].hash(data, result[i - 1] ^ seeds[i]);						
		}

		return result;
	}

	protected void setupSizes(int hashCount, int size) {
		if (size >= 8)
			this.size = size >> 3;
		else
			this.size = 1;

		this.bitsize = this.size << 3;
		this.hashCount = hashCount;
		flags = new byte[this.size];
		seeds = new int[hashCount];
		for (int i = 0; i<hashCount; i++)
			seeds[i] = (int)Math.round(Math.random()*1000000)+1;
	}

	/**
	 * Returns a new FullBloomFilter object.
	 * 
	 * @param hashCount
	 *            Number of hash functions.
	 * @param size
	 *            Bloom filter size <b>in bits</b>. If this parameter is < 8,
	 *            then the filter will have the size of 8 bits. Otherwise the
	 *            size will be (size / 8) * 8 bits.
	 * @param hf
	 *            An object extending the IntHashFunctionFactory
	 *            (Murmur2Factory, for instance) which supplies the hash
	 *            functions.
	 * @return New FullBloomFilter object with specified parameters.
	 * @see IntHashFunction
	 * @see Murmur2
	 * @see IntHashFunctionFactory
	 * @see Murmur2Factory
	 */
	public FullBloomFilter(int hashCount, int size, IntHashFunctionFactory hf) {
		setupSizes(hashCount, size);
		this.factory = hf;
		hashes = new IntHashFunction[hashCount];
		for (int i = 0; i < hashCount; i++)
			hashes[i] = factory.getHashFunction(i);
	}

	/**
	 * Returns a new FullBloomFilter object based on given expected number of
	 * elements and false positive probability.
	 * 
	 * @param expectedNoE
	 *            Expected number of elements in filter
	 * @param falsePositiveProb
	 *            Allowed false positive probability. Must be between 0 and 1.
	 * @param hf
	 *            An object extending the IntHashFunctionFactory
	 *            (Murmur2Factory, for instance) which supplies the hash
	 *            functions.
	 * @return New FullBloomFilter with hash count calculated as
	 *         -ln(falsePositiveProb)/ln(2) and size calculated as -expectedNoE
	 *         * ln(falsePositiveProb) / ln(2)^2
	 * @see IntHashFunction
	 * @see Murmur2
	 * @see IntHashFunctionFactory
	 * @see Murmur2Factory
	 */
	public FullBloomFilter(int expectedNoE, double falsePositiveProb,
			IntHashFunctionFactory hf) {
		this((int) Math.ceil(-(Math.log(falsePositiveProb) / Math.log(2))),
				(int) Math.ceil(-expectedNoE * Math.log(falsePositiveProb)
						/ (Math.log(2) * Math.log(2))), hf);

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

	/**
	 * Converts hash value into corresponding bit number
	 * 
	 * @param hash
	 *            Int hash value
	 * @return Index of corresponding bit
	 */
	protected int getIndex(int hash) {
		long hl = (long) hash;
		hl += (long) Integer.MAX_VALUE;
		hl %= bitsize;
		return (int) hl;
	}

	/**
	 * Gets actual flags array index by bit number
	 * 
	 * @param bigIdx
	 *            Bit number
	 * @return Corresponding index in flags array
	 */
	protected int getBitIndex(int bigIdx) {
		return bigIdx >> 3;
	}

	/**
	 * Gets bit by bit number
	 * 
	 * @param bigIdx
	 *            Bit number
	 * @return Bit mask
	 */
	protected byte getBitMask(int bigIdx) {
		return (byte) (1 << (bigIdx % 8));
	}

	/**
	 * Converts hashes[index] value into index and mask and sets an appropriate
	 * bit in flags
	 * 
	 * @param hashes
	 *            Hash values array
	 * @param index
	 *            Index of current hash in a loop
	 */
	protected void doProcessAddElement(int[] hashes, int index) {
		int idx = getIndex(hashes[index]);

		int cidx = getBitIndex(idx);
		byte mask = getBitMask(idx);
		flags[cidx] |= mask;
	}

	/**
	 * Converts hashes[index] value into index and mask and tests if an
	 * appropriate bit is set
	 * 
	 * @param hashes
	 *            Hash values array
	 * @param index
	 *            Index of current hash in a loop
	 * @return <code>true</code> if corresponding bit is set, otherwise
	 *         <code>false</code>
	 */
	protected boolean doProcessCheckElement(int[] hashes, int index) {
		int idx = getIndex(hashes[index]);

		int cidx = getBitIndex(idx);
		byte mask = getBitMask(idx);

		return ((flags[cidx] & mask) != 0);
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
				doProcessAddElement(h, i);

			}
			storageSize++;
		}
	}

	@Override
	public boolean checkExists(T element) {
		boolean result = true;
		int[] h = hash(element.toString().getBytes());
		for (int i = 0; i < hashCount; i++) {

			synchronized (this) {
				result &= doProcessCheckElement(h, i);
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
