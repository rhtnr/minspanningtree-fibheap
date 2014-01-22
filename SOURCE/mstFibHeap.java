

import java.util.*; 

public final class mstFibHeap {
	
	public static final class Vertex {
		
		
		private Vertex parent; // Parent in the tree, if any.
		private Vertex child; // Child node, if any.

		private Vertex next; // Next and previous elements in the list
		private Vertex prev;

		

		private int element; // Element being stored here
		private double Priority; // Its priority
		
		private boolean marked = false; // Whether this node is marked
		
		private int deg = 0; // Number of children
		
		//Returns Node
		public int getValue() 
		{
			return element;
		}

		//sets Node
		public void setValue(int value) 
		{
			element = value;
		}

		//gets priority
		public double getPriority() 
		{
			return Priority;
		}


		//Creates a node with a val and priority
		private Vertex(int elem, double priority) 
		{
			next = prev = this;
			element = elem;
			Priority = priority;
		}
	}

	//points to the min element in heap
	private Vertex minimumInHeap = null;

	//size of heap
	private int Size_Of_Heap = 0;

	//to insert into heap
	public Vertex insert(int value, double priority) 
	{
		checkPriority(priority);
		Vertex result = new Vertex(value, priority);
		minimumInHeap = mergeHeap(minimumInHeap, result);
		Size_Of_Heap += 1;
		return result;
	}


	//to check if heap is empty
	public boolean isEmpty() 
	{
		return minimumInHeap == null;
	}

	//deletes and returns the min element of the heap
	public Vertex deleteMin() 
	{
		if (isEmpty())
			throw new NoSuchElementException("Empty Heap!!");
		Size_Of_Heap-=1;
		Vertex minElem = minimumInHeap;

		//If element is the only elemnt in root list
		if (minimumInHeap.next == minimumInHeap) 
		{ // Case one
			minimumInHeap = null;
		} 
		else 
		{ // Otherwise disconnect from the linked list
			minimumInHeap.prev.next = minimumInHeap.next;
			minimumInHeap.next.prev = minimumInHeap.prev;
			minimumInHeap = minimumInHeap.next;
		}

		//remove parent fields of min elements children
		if (minElem.child != null) 
		{
			Vertex curr = minElem.child;
			do 
			{
				curr.parent = null;
				curr = curr.next;
			} 
			while (curr != minElem.child);
		}

		
		minimumInHeap = mergeHeap(minimumInHeap, minElem.child);

		if (minimumInHeap == null)
			return minElem;

		List<Vertex> treeTable = new ArrayList<Vertex>();

		List<Vertex> toVisit = new ArrayList<Vertex>();

		for (Vertex curr = minimumInHeap; toVisit.isEmpty() || toVisit.get(0) != curr; curr = curr.next)
			toVisit.add(curr);
		for (Vertex currentNode : toVisit) 
		{

			while (true) 
			{
			
				while (currentNode.deg >= treeTable.size())
					treeTable.add(null);
				if (treeTable.get(currentNode.deg) == null) 
				{
					treeTable.set(currentNode.deg, currentNode);
					break;
				}
				Vertex other = treeTable.get(currentNode.deg);
				treeTable.set(currentNode.deg, null); 
				Vertex min = (other.Priority < currentNode.Priority) ? other : currentNode;
				Vertex max = (other.Priority < currentNode.Priority) ? currentNode : other;
				max.next.prev = max.prev;
				max.prev.next = max.next;
				max.next = max.prev = max;
				min.child = mergeHeap(min.child, max);
				max.parent = min;
				max.marked = false;
				++min.deg;
				currentNode = min;
			}
			if (currentNode.Priority <= minimumInHeap.Priority)
				minimumInHeap = currentNode;
		}
		return minElem;
	}

	//to decrease the priority
	public void decreaseKey(Vertex entry, double newPriority) 
	{
		checkPriority(newPriority);
		if (newPriority > entry.Priority)
			throw new IllegalArgumentException("new priority cannot exceed old");
		decreaseKeyUnchecked(entry, newPriority);
	}

//to check priority
	private void checkPriority(double priority) 
	{
		if (Double.isNaN(priority))
			throw new IllegalArgumentException(priority + " - cannot be a vertex weight or priority");
	}

	//to merge two heaps
	private static  Vertex mergeHeap(Vertex one, Vertex two) 
	{
		
		if (one == null && two == null) 
		{
			return null;
		} 
		else if (one != null && two == null) 
		{ 
			return one;
		} 
		else if (one == null && two != null) 
		{ 
			return two;
		} 
		else 
		{ 
			Vertex oneNext = one.next;
			one.next = two.next;
			one.next.prev = one;
			two.next = oneNext;
			two.next.prev = two;

			/* Return a pointer to whichever's smaller. */
			return one.Priority < two.Priority ? one : two;
		}
	}

//to decrease the weight of a vertex
	private void decreaseKeyUnchecked(Vertex entry, double priority) {
		entry.Priority = priority;
		if (entry.parent != null && entry.Priority <= entry.parent.Priority)
			cutNode(entry);
		if (entry.Priority <= minimumInHeap.Priority)
			minimumInHeap = entry;
	}

	private void cutNode(Vertex entry) {
		entry.marked = false;
		if (entry.parent == null)
			return;
		if (entry.next != entry) 
		{ // Has siblings
			entry.next.prev = entry.prev;
			entry.prev.next = entry.next;
		}
		if (entry.parent.child == entry) 
		{
			if (entry.next != entry) {
				entry.parent.child = entry.next;
			}
			else {
				entry.parent.child = null;
			}
		}
		--entry.parent.deg;
		entry.prev = entry.next = entry;
		minimumInHeap = mergeHeap(minimumInHeap, entry);
		if (entry.parent.marked)
			cutNode(entry.parent);
		else
			entry.parent.marked = true;
		entry.parent = null;
	}
}
