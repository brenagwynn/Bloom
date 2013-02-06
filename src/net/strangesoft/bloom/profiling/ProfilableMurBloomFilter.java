package net.strangesoft.bloom.profiling;

import net.strangesoft.bloom.filter.Murmur2BloomFilter;

public class ProfilableMurBloomFilter<T> extends Murmur2BloomFilter<T> implements Profilable {

	public ProfilableMurBloomFilter(int hashCount, int size) {
		super(hashCount, size);
	}
		
	public ProfilableMurBloomFilter(int expectedNoE, double falsePositiveProb) {
		super(expectedNoE, falsePositiveProb);
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
