package net.strangesoft.bloom.hash.murmur2;

/*
Copyright (C) 2013 Igor Bezugliy

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

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
