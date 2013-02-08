package net.strangesoft.bloom.hash;

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

/**
This abstract class must be extended and getHashFunction method must be implemented so it supplies IntHashFunction instances according to the index.
@see net.strangesoft.bloom.hash.murmur2.Murmur2Factory
 */
public abstract class IntHashFunctionFactory {
	/**
	 * Returns the instance of IntHashFunction
	 *
	 * @param  index  	 
	 * @return New hash function (IntHashFunction) 
	 */
	public abstract IntHashFunction getHashFunction(int index);
}
