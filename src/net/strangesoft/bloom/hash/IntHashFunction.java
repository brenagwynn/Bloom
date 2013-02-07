package net.strangesoft.bloom.hash;

/**
This abstract class must be extended and hash method must contain an implementation of concrete hash function.
@see net.strangesoft.bloom.hash.murmur2.Murmur2
 */

public abstract class IntHashFunction {
	/**
	 * Returns the integer (32-bit) hash value of the supplied data.
	 *
	 * @param  data Data that needs to be hashed in form of byte array.
	 * @param  seed Seed of the hash function. 	 
	 * @return Integer hash value 
	 */
	 public abstract int hash(byte[] data, int seed);		
}
