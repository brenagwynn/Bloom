package net.strangesoft.bloom.filter;

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
An interface which describes the Bloom filter
@see net.strangesoft.bloom.filter.FullBloomFilter
 */
public interface BloomFilter<T> {
	/**
	 * Adds an element to the filter
	 * 
	 * @param element
	 *            An element to add
	 */
	public void put(T element);

	/**
	 * Checks if the element exists in the filter
	 * 
	 * @return <code>true</code> is element exists, otherwise <code>false</code>
	 * @param element
	 *            An element to check
	 */
	public boolean checkExists(T element);

	/**
	 * Returns the quality of the filter, which is calculated as 1 - probability
	 * of false positive
	 * 
	 * @return Quality of the filter. The closer to 1, the better.
	 */
	public double getQuality();
}
