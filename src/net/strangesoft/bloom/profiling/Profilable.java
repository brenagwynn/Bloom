package net.strangesoft.bloom.profiling;

public interface Profilable {
	// marker interface
	public void putValue(Object key, Object t);

	public boolean test(Object t);
}
