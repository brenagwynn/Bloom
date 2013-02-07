package net.strangesoft.bloom.hash.murmur2;

import net.strangesoft.bloom.hash.IntHashFunction;

/**
This is a straightforward implementation of Murmur2 hash algorithm. See {@link http://dmy999.com/article/50/murmurhash-2-java-port}
 */
public class Murmur2 extends IntHashFunction {		
	/**
	 * Returns the Murmur2 (32-bit) hash value of the supplied data.
	 *
	 * @param  data Data that needs to be hashed in form of byte array.
	 * @param seed Seed for hash function 	 
	 * @return Integer hash value 
	 */
	@Override
	public int hash(byte[] data, int seed) {		
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
