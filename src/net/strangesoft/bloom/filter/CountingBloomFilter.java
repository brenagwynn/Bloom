package net.strangesoft.bloom.filter;

/*
Copyright (C) 2013 Igor Bezugliy

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import net.strangesoft.bloom.hash.IntHashFunction;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;

/**
 * An implementation of Bloom filter using hash functions that return int
 * values with ability to remove elements.
 * 
 * @see net.strangesoft.bloom.hash.IntHashFunction
 * @see net.strangesoft.bloom.hash.IntHashFunctionFactory
 */
public class CountingBloomFilter<T> extends FullBloomFilter<T> {


/**
 * Implement this interface to capture the overflow events (when any internal counter fills to the maximum)
 */
	public interface OverflowCallback {
		/** This method should contain the overflow handling routine  */
		public void onOverflow();
	}

	private OverflowCallback handler;
	
	/**
	 * Returns a new CountingBloomFilter object.
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
	 * @param handler An instance of some class implementing OverflowCallback interface.
	 * @return New CountingBloomFilter object with specified parameters.
	 * @see IntHashFunction
	 * @see Murmur2
	 * @see IntHashFunctionFactory
	 * @see Murmur2Factory
	 * @see OverflowCallback 
	 */
	public CountingBloomFilter(int hashCount, int size,
			IntHashFunctionFactory hf, OverflowCallback handler) {
		super(hashCount, size, hf);
		this.handler = handler; 
	}

	/**
	 * Returns a new CountingBloomFilter object.
	 * 
	 * @param expectedNoE
	 *            Expected number of elements in filter
	 * @param falsePositiveProb
	 *            Allowed false positive probability. Must be between 0 and 1.
	 * @param hf
	 *            An object extending the IntHashFunctionFactory
	 *            (Murmur2Factory, for instance) which supplies the hash
	 *            functions.
	 * @param handler An instance of some class implementing OverflowCallback interface.
	 * @return New CountingBloomFilter with hash count calculated as
	 *         -ln(falsePositiveProb)/ln(2) and size calculated as -expectedNoE
	 *         * ln(falsePositiveProb) / ln(2)^2
	 * @see IntHashFunction
	 * @see Murmur2
	 * @see IntHashFunctionFactory
	 * @see Murmur2Factory
	 * @see OverflowCallback 
	 */
	public CountingBloomFilter(int expectedNoE, double falsePositiveProb,
			IntHashFunctionFactory hf, OverflowCallback handler) {
		super(expectedNoE, falsePositiveProb, hf);
		this.handler = handler;
	}

	@Override
	protected void doProcessAddElement(int[] hashes, int index) {
		int idx = getIndex(hashes[index]);
		if (flags[idx] < 127)
			flags[idx]++;
		else
			if (handler != null)
				handler.onOverflow();
	}

	protected void doProcessRemoveElement(int[] hashes, int index) {
		int idx = getIndex(hashes[index]);
		if (flags[idx] != -127)
			flags[idx]--;
	}

	@Override
	protected boolean doProcessCheckElement(int[] hashes, int index) {
		int idx = getIndex(hashes[index]);
		return (flags[idx] != -127);
	}	
	
	/**
	 * Removes an element from the filter. Hashes are calculated based on the
	 * element.toString() value.
	 * 
	 * @param element
	 *            An element to remove
	 */
	public void remove(T element) {
		if (checkExists(element)) {
			int[] h = hash(element.toString().getBytes());
			synchronized (this) {
				for (int i = 0; i < hashCount; i++) {
					doProcessRemoveElement(h, i);
				}
				storageSize--;
			}
		}

	}

	@Override
	protected void setupSizes(int hashCount, int size) {
		this.size = size >> 3;
		this.bitsize = size;
		this.hashCount = hashCount;
		flags = new byte[this.bitsize];
		seeds = new int[hashCount];
		for (int i = 0; i < hashCount; i++)
			seeds[i] = (int) Math.round(Math.random() * 1000000) + 1;
		for (int i = 0; i < this.bitsize; i++)
			flags[i] = -127;
	}

}
