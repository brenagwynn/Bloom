package net.strangesoft.bloom.hash;

/**
 * @author i.bezugliy
 * <p>
 * An implementation of Murmur2 hash algorithm.
 */
public class Murmur2 implements IntHashFunction {

	private int seed;

	/**
	 * Returns a new object implementing Murmur2 hash function with specified seed.
	 *
	 * @param  seed Seed for Murmur2 hash function	 
	 * @return New object implementing Murmur2 hash function with specified seed. 
	 */
	public Murmur2(int seed) {
		this.seed = seed;
	}
	
	/**
	 * Returns the Murmur2 (32-bit) hash value of the supplied data.
	 *
	 * @param  data Data that needs to be hashed in form of byte array. 	 
	 * @return Integer hash value 
	 */
	@Override
	public int hash(byte[] data) {
		int m = 0x5bd1e995;
		int r = 24;

		int len = data.length;
		int h = seed ^ len;

		int i = 0;
		while (len >= 4) {
			int k = data[i + 0] & 0xFF;
			k |= (data[i + 1] & 0xFF) << 8;
			k |= (data[i + 2] & 0xFF) << 16;
			k |= (data[i + 3] & 0xFF) << 24;

			k *= m;
			k ^= k >>> r;
			k *= m;

			h *= m;
			h ^= k;

			i += 4;
			len -= 4;
		}

		switch (len) {
		case 3:
			h ^= (data[i + 2] & 0xFF) << 16;
		case 2:
			h ^= (data[i + 1] & 0xFF) << 8;
		case 1:
			h ^= (data[i + 0] & 0xFF);
			h *= m;
		}

		h ^= h >>> 13;
		h *= m;
		h ^= h >>> 15;

		return h;
	}

}
