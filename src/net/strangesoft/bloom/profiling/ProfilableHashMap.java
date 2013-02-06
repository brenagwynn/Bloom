package net.strangesoft.bloom.profiling;

import java.util.HashMap;

public class ProfilableHashMap<K, V> extends HashMap<K, V> implements
		Profilable {

	private static final long serialVersionUID = -8595396750529840401L;

	@SuppressWarnings("unchecked")
	@Override
	public boolean test(Object t) {
		return containsValue((V) t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putValue(Object key, Object t) {
		put((K) key, (V) t);

	}

}
