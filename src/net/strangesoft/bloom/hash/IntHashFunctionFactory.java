package net.strangesoft.bloom.hash;

/**
This abstract class must be extended and getHashFunction method must be implemented so it supplies IntHashFunction instances according to the index.
@see net.strangesoft.bloom.hash.murmur2.Murmur2Factory
 */
public abstract class IntHashFunctionFactory {
	/**
	 * Returns the instance of IntHashFunction
	 *
	 * @param  index  	 
	 * @return New hash function (IntHashFunction) 
	 */
	public abstract IntHashFunction getHashFunction(int index);
}
