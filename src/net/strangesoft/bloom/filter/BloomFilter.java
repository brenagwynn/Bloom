package net.strangesoft.bloom.filter;

/**
An interface which describes the Bloom filter
@see net.strangesoft.bloom.filter.FullBloomFilter
 */
public interface BloomFilter<T> {
	/**
	 * Adds an element to the filter
	 * 
	 * @param element
	 *            An element to add
	 */
	public void put(T element);

	/**
	 * Checks if the element exists in the filter
	 * 
	 * @return <code>true</code> is element exists, otherwise <code>false</code>
	 * @param element
	 *            An element to check
	 */
	public boolean checkExists(T element);

	/**
	 * Returns the quality of the filter, which is calculated as 1 - probability
	 * of false positive
	 * 
	 * @return Quality of the filter. The closer to 1, the better.
	 */
	public double getQuality();
}
