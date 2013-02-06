package net.strangesoft.bloom.hash;

public interface IntHashFunction {
	/**
	 * Returns the integer (32-bit) hash value of the supplied data.
	 *
	 * @param  data Data that needs to be hashed in form of byte array. 	 
	 * @return Integer hash value 
	 */
	 public int hash(byte[] data);
}
