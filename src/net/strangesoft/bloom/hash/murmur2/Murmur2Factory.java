package net.strangesoft.bloom.hash.murmur2;

import net.strangesoft.bloom.hash.IntHashFunction;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;
/**
This is an extension of IntHashFunctionFactory which returns Murmur2 hash function instances (index is ignored).
@see net.strangesoft.bloom.hash.murmur2.Murmur2
 */

public class Murmur2Factory extends IntHashFunctionFactory {

	@Override
	public IntHashFunction getHashFunction(int index) {
		return new Murmur2();
	}

}
