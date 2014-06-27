package com.ketonax.utilities;


import java.util.ArrayList;

public class LinkedList<T> {

	/*
	 * Any methods that modify the length of this linked list class must adjust
	 * the variable "length" appropriately. Otherwise the method getLength will
	 * return an incorrect value.
	 */

	private ListNode head, tail, current, previous;
	private int length;

	public LinkedList() {

		head = tail = current = previous = null;// Sets everything to null.
		length = 0;

	}

	public void add(T data) {// Adds new node to the tail end of the list.

		length++;// Increases size of the list.

		if (head == null) {

			current = head = tail = new ListNode(data, tail);

		} else {

			previous = tail;// Old tail becomes previous.

			ListNode newNode = new ListNode(data, null);
			tail.link = newNode;
			current = tail = newNode;// newNode becomes the new tail.

		}

	}

	public void add(T data, int targetIndex) throws LinkedListException {

		ListNode position = find(targetIndex), predecessor = find(targetIndex - 1);

		ListNode newNode = new ListNode();
		newNode.data = data;

		if (predecessor == null && position != head)
			throw new LinkedListException("Target index out of bounds.");
		else if (position == head) {// At head.

			if (head == null) {

				newNode.link = position;
				current = head = newNode;

			} else {

				newNode.link = position;
				current = head = newNode;

			}

		} else if (predecessor != null && position == null) {// At tail.

			tail.link = newNode;
			current = tail = newNode;

		} else {// Anywhere else on the list.

			current = newNode.link = position;// newNode link is point
			predecessor.link = newNode;// Node before position now links to
										// newNode.

		}
	}

	public void deleteHead() throws LinkedListException {

		length--;// Reduces size of the list.

		if (head != null)
			head = head.link;
		else
			throw new LinkedListException(
					"Using deleteHead with an empty list.");

	}

	public void deleteTail() throws LinkedListException {

		length--;// Reduces size of the list.

		ListNode position = head;

		if (head.link != null) {// Checking for list with only 1 node.

			while (position != null) {

				if (position.link == tail) {// Node position is at node before
											// tail

					position.link = null;
					tail = position;

				}

				position = position.link;

			}

		} else
			throw new LinkedListException("List has only 1 node (head).");
	}

	public void delete(int targetIndex) throws LinkedListException {

		length--;// Reduces size of the list.

		ListNode position, previousPosition;

		if (head != null) {

			if (targetIndex < 1 || targetIndex > length)
				throw new LinkedListException(
						"Target position is out of list bounds.");

			position = find(targetIndex);

			if (position == head)
				head = head.link;
			else {

				previousPosition = find(targetIndex - 1);

				if (position == tail) {

					previousPosition.link = position.link;
					tail = previousPosition;

				} else
					previousPosition.link = position.link;

			}

		} else
			throw new LinkedListException("Using delete on an empty list.");

	}

	public void clearList() {

		head = null;
		tail = null;
		current = null;
		previous = null;
		length = 0;

	}

	public void setDataAtPosition(int targetIndex, T data)
			throws LinkedListException {

		if (head != null) {

			if (targetIndex < 1 || targetIndex > length)
				throw new LinkedListException("Index out of bounds");
			else
				find(targetIndex).data = data;

		} else
			throw new LinkedListException(
					"Using setDataAtPosition on an empty list.");
	}

	public T getDataAtPosition(int targetIndex) throws LinkedListException {

		if (head != null) {

			if (targetIndex < 1 || targetIndex > length)
				throw new LinkedListException("Index out of bounds");
			else
				return find(targetIndex).data;

		} else
			throw new LinkedListException(
					"Using getDataAtPosition on an empty list.");

	}

	public int getLength() {

		return length;

	}

	public void showList() {

		ListNode position = head;

		while (position != null) {

			System.out.println(position.data);
			position = position.link;

		}
	}

	public boolean contains(T target) {

		return find(target) != null;

	}

	public boolean isEmpty() {

		return head == null;

	}

	public ArrayList<T> toArrayList() {

		ArrayList<T> listToReturn = new ArrayList<T>();
		ListNode position = head;

		while (position != null) {

			listToReturn.add(position.data);
			position = position.link;

		}

		return listToReturn;

	}

	// Beginning of internal iterator methods
	public void resetIteration() {

		current = head;
		previous = null;

	}

	public boolean hasNext() {

		return current != null;

	}

	public void goToNext() throws LinkedListException {

		if (current != null) {

			previous = current;
			current = current.link;

		} else if (head != null)
			throw new LinkedListException(
					"Iterated too many times, or unitialized iteration");
		else
			throw new LinkedListException("Iterating on an empty list");

	}

	public T getDataAtCurrent() throws LinkedListException {

		T result = null;

		if (current != null)
			result = current.data;
		else if (head != null)
			throw new LinkedListException(
					"Current is past all nodes or uninitialized.");
		else
			throw new LinkedListException(
					"Using getDataAtCurrent with an empty list.");

		return result;
	}

	public void insertNodeAfterCurrent(T data) throws LinkedListException {

		length++;// Increases size of the list.

		ListNode newNode = new ListNode();
		newNode.data = data;

		if (current != null) {

			newNode.link = current.link;
			current.link = newNode;

		} else if (head != null)
			throw new LinkedListException(
					"Current is past all nodes, or unitialized.");
		else
			throw new LinkedListException(
					"Using insertNodeAfterCurrent with an empty list");

	}

	public void setDataAtCurrent(T data) throws LinkedListException {

		if (current != null)
			current.data = data;
		else if (head != null)
			throw new LinkedListException(
					"Current is past all nodes or uninitialized.");
		else
			throw new LinkedListException("Editing an empty list.");

	}

	public void deleteCurrentNode() throws LinkedListException {

		length--;// Reduces size of the list.

		if (current != null && previous != null) {

			previous.link = current.link;
			current = current.link;

		} else if (current != null && previous == null) {

			head = current.link;
			current = head;

		} else if (current != null && current.link == null) {

			previous.link = current.link;
			current = current.link;
			tail = previous;

		} else
			throw new LinkedListException(
					"Trying to delete an empty list, or unitialized iteration.");

	}

	// End of internal iterator methods.

	private ListNode find(T target) {

		boolean found = false;

		ListNode position = head;

		while (position != null && found == false) {

			if (position.data == target)
				found = true;
			else
				position = position.link;
		}

		return position;

	}

	private ListNode find(int targetIndex) {

		ListNode position = head;
		boolean found = false;
		int count = 1;

		if (targetIndex < 1)
			position = null;
		else {

			while (position != null && found == false) {

				if (count == targetIndex)
					found = true;
				else
					position = position.link;

				count++;

			}
		}

		return position;

	}

	private class ListNode {

		T data;
		ListNode link;

		public ListNode() {

			data = null;
			link = null;

		}

		public ListNode(T newData, ListNode linkValue) {

			data = newData;
			link = linkValue;

		}

	}

}
