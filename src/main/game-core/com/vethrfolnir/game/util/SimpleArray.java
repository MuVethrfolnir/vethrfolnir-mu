/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vethrfolnir.game.util;

/**
 * Fast collection similar to Array that grows on demand as elements are accessed.
 * It does not preserve order of elements.
 * 
 * Inspired by Artemis array.
 * 
 * @author libGDX
 */

public class SimpleArray<E> {
	private E[] data;
	private int size = 0;

	/**
	 * Empty array with an initial capacity of 10.
	 */
	public SimpleArray() {
		this(10);
	}

	/**
	 * Empty array with the specified initial capacity.
	 * 
	 * @param capacity the initial capacity of array.
	 */
	@SuppressWarnings("unchecked")
	public SimpleArray(int capacity) {
		data = (E[])new Object[capacity];
	}

	/**
	 * Removes the element at the specified position in this array. Order of elements
	 * is not preserved.
	 * 
	 * @param index
	 * @return element that was removed from the array.
	 */
	public E remove(int index) {
		E e = data[index]; // make copy of element to remove so it can be returned
		data[index] = data[--size]; // overwrite item to remove with last element
		data[size] = null; // null last element, so gc can do its work
		return e;
	}
	
	
	/**
	 * Removes and return the last object in the array.
	 * 
	 * @return the last object in the array, null if empty.
	 */
	public E removeLast() {
		if(size > 0) {
			E e = data[--size];
			data[size] = null;
			return e;
		}
		
		return null;
	}

	/**
	 * Removes the first occurrence of the specified element from this array, if
	 * it is present. If the array does not contain the element, it is unchanged.
	 * It does not preserve order of elements.
	 * 
	 * @param e
	 * @return true if the element was removed.
	 */
	public boolean remove(E e) {
		for (int i = 0; i < size; i++) {
			E e2 = data[i];

			if (e == e2) {
				data[i] = data[--size]; // overwrite item to remove with last element
				data[size] = null; // null last element, so gc can do its work
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Check if array contains this element. The operator == is used to check for equality.
	 */
	public boolean contains(E e) {
		for(int i = 0; size > i; i++) {
			if(e == data[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the element at the specified position in array.
	 */
	public E get(int index) {
		return data[index];
	}

	/**
	 * @return the number of elements in this array.
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @return the number of elements the array can hold without growing.
	 */
	public int getCapacity() {
		return data.length;
	}
	
	/**
	 * @param index
	 * @return whether or not the index is within the bounds of the collection
	 */
	public boolean isIndexWithinBounds(int index) {
		return index < getCapacity();
	}

	/**
	 * @return true if this list contains no elements
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Adds the specified element to the end of this array. if needed also
	 * increases the capacity of the array.
	 */
	public void add(E e) {
		// is size greater than capacity increase capacity
		if (size == data.length) {
			grow();
		}

		data[size++] = e;
	}

	/**
	 * Set element at specified index in the array.
	 */
	public void set(int index, E e) {
		if(index >= data.length) {
			grow(index*2);
		}
		size = index+1;
		data[index] = e;
	}
	
	public void forceSet(int index, E e) {
		data[index] = e;
	}
	
	/**
	 * Removes all of the elements from this array. The array will be empty after
	 * this call returns.
	 */
	public void clear() {
		// null all elements so gc can clean up
		for (int i = 0; i < size; i++) {
			data[i] = null;
		}

		size = 0;
	}

	private void grow() {
		int newCapacity = (data.length * 3) / 2 + 1;
		grow(newCapacity);
	}
	
	@SuppressWarnings("unchecked")
	private void grow(int newCapacity) {
		E[] oldData = data;
		data = (E[])new Object[newCapacity];
		System.arraycopy(oldData, 0, data, 0, oldData.length);
	}

}
