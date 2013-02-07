package net.strangesoft.bloom.profiling;

import net.strangesoft.bloom.filter.FullBloomFilter;
import net.strangesoft.bloom.hash.IntHashFunctionFactory;

public class ProfilableFullBloomFilter<T> extends FullBloomFilter<T> implements Profilable {

	public ProfilableFullBloomFilter(int hashCount, int size, IntHashFunctionFactory hf) {
		super(hashCount, size, hf);
	}
		
	public ProfilableFullBloomFilter(int expectedNoE, double falsePositiveProb, IntHashFunctionFactory hf) {
		super(expectedNoE, falsePositiveProb, hf);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean test(Object t) {
		return checkExists((T) t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putValue(Object key, Object t) {
		put((T) t);
	}	

}
