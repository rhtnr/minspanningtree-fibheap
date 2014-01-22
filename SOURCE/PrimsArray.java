

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

public class PrimsArray {
	
	ArrayList<TreeEdge> MSTree;
	Graph g;
	public double TreeWeight;
	public long genTime;
	
	
	//A inner class to represent edge neighbours
	public static class Edges
	{
		int vertex;
		LinkedHashMap<Integer, Double> adjacentVertices;
		
		public Edges(int a, LinkedHashMap<Integer, Double> neigh)
		{
			this.vertex = a;
			this.adjacentVertices = neigh;
		}
	}
	
	//constructr to init graph
	public PrimsArray(Graph g)
	{
		this.g = g;
		if(g.size() == 0)
		{
			System.out.println("Graph Empty!!");
			return;
		}
		MSTree = new ArrayList<TreeEdge>();
		
	}
	
	
	//to represent Tree edges
	public static class TreeEdge
	{
		int v1;
		int v2;
		double weight;
		public TreeEdge(int v1, int v2, double weight)
		{
			this.v1=v1;
			this.v2=v2;
			this.weight=weight;
		}
		public void printTreeEdge(TreeEdge edge)
		{
			System.out.println(edge.v1 + " " + edge.v2);
		}
		public void printTreeEdgeWithWeight(TreeEdge edge) {
			System.out.println(edge.v1 + " " + edge.v2 + " " + edge.weight);	
		}
	}
	
	//to print genereated mst
	public void printMST()
	{
		System.out.println((int)TreeWeight);
		for(TreeEdge e:MSTree)
		{
			e.printTreeEdge(e);
		}
	}
	public void printMSTwithWeight()
	{
		System.out.println(TreeWeight);
		for(TreeEdge e:MSTree)
		{
			e.printTreeEdgeWithWeight(e);
		}
	}
	
	//it finds mst with the graph provided to class
	public void findMST() throws InterruptedException
	{
		int root = 0;
		int count = 0;
		int currentVertex = root;
		long start = System.currentTimeMillis();
		HashSet<Integer> VerticeAlreadyInQueue = new HashSet<Integer>();
		ArrayList<Edges> adjacentVerticesToTree = new ArrayList<Edges>();
		Edges temp = new Edges(currentVertex, g.getEdgesWithWeight(currentVertex));
		adjacentVerticesToTree.add(temp);
		VerticeAlreadyInQueue.add(currentVertex);
		Entry<Integer, Double> min;
		HashSet<Integer> VerticesInTree = new HashSet<Integer>();
		VerticesInTree.add(0);
		int minCorrespondingVertexIndex = -1;
		while(count!=g.size() - 1)
		{
			min = null;
			int MinVertex;
			double MinWeight;
			int minCorrespondingVertex = -1;
//			Entry<Integer, Double> minEntry;
			for(int index = 0 ; index < adjacentVerticesToTree.size(); index++)
			{
//				System.out.print("Current Vertex updated to " + currentVertex + " => ");
				currentVertex = adjacentVerticesToTree.get(index).vertex;
				for(Entry<Integer, Double> entry:adjacentVerticesToTree.get(index).adjacentVertices.entrySet())
				{
//					System.out.print(entry.getKey() + " : " + entry.getValue() + " , ");
					if(min == null || min.getValue() > entry.getValue())
					{
						min = entry;
						minCorrespondingVertex = currentVertex;
						minCorrespondingVertexIndex = index;
					}
				}
		//		System.out.println();
			}
//			System.out.println("Found Min AT " + minCorrespondingVertex + " " + min.getKey() + " " + min.getValue());
			MinVertex = min.getKey();
			MinWeight = min.getValue();
		
//			System.out.println("Adding " + minCorrespondingVertex + " " + MinVertex);
			if(count == 0 || 
					(VerticesInTree.contains(minCorrespondingVertex) && !VerticesInTree.contains(MinVertex)) || 
						((!VerticesInTree.contains(minCorrespondingVertex) && VerticesInTree.contains(MinVertex))))
			{
				if(!VerticeAlreadyInQueue.contains(MinVertex))
				{
					temp = new Edges(MinVertex, g.getEdgesWithWeight(MinVertex));
					adjacentVerticesToTree.add(temp);
					VerticeAlreadyInQueue.add(MinVertex);
				}
				removeEdgeByIndex(adjacentVerticesToTree, minCorrespondingVertexIndex, MinVertex);
				removeEdge(adjacentVerticesToTree, MinVertex, minCorrespondingVertex);
			/*	if(adjacentVerticesToTree.get(minCorrespondingVertex).isEmpty())
					adjacentVerticesToTree.remove(minCorrespondingVertex);
				if(adjacentVerticesToTree.get(MinVertex).isEmpty())
					adjacentVerticesToTree.remove(MinVertex); */
				TreeEdge temp2 = new TreeEdge(minCorrespondingVertex, MinVertex, MinWeight);
				VerticesInTree.add(minCorrespondingVertex);
				VerticesInTree.add(MinVertex);
				MSTree.add(temp2);
				count+=1;
				TreeWeight+=MinWeight;
	//			System.out.println("Count = " + count);
			}
			else
			{
		//		System.out.println("Did not add!!");
			//	Thread.sleep(3000);
				removeEdgeByIndex(adjacentVerticesToTree, minCorrespondingVertexIndex, MinVertex);
			/*	if(adjacentVerticesToTree.get(minCorrespondingVertex).isEmpty())
					adjacentVerticesToTree.remove(minCorrespondingVertex); */
			}
		}
		genTime = System.currentTimeMillis() - start;
 	}
	
	
	//removes the edges already added to tree
	private void removeEdgeByIndex(ArrayList<Edges> adjacentVerticesToTree,
			int minCorrespondingVertexIndex, int minVertex) {
		adjacentVerticesToTree.get(minCorrespondingVertexIndex).adjacentVertices.remove(minVertex);
		if(adjacentVerticesToTree.get(minCorrespondingVertexIndex).adjacentVertices.size() == 0)
			adjacentVerticesToTree.remove(minCorrespondingVertexIndex);
		
	}
	private void removeEdge(ArrayList<Edges> adjacentVerticesToTree,
			int minCorrespondingVertex, int minVertex) {
		for(int i = 0; i< adjacentVerticesToTree.size(); i++)
		{
			if(adjacentVerticesToTree.get(i).vertex == minCorrespondingVertex)
			{
				adjacentVerticesToTree.get(i).adjacentVertices.remove(minVertex);
				if(adjacentVerticesToTree.get(i).adjacentVertices.size() == 0)
					adjacentVerticesToTree.remove(i);
				return;
			}
		}
		
	}

}
