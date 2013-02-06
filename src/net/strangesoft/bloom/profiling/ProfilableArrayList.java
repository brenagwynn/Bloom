package net.strangesoft.bloom.profiling;

import java.util.ArrayList;

public class ProfilableArrayList<E> extends ArrayList<E> implements Profilable {

	private static final long serialVersionUID = -9046659986489214188L;

	@Override
	public boolean test(Object t) {
		return contains(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void putValue(Object key, Object t) {
		add((E) t);
	}

}
