

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Random;


public class Graph {

	private int size; // Number of vertices
	private ArrayList<LinkedList<Edge>> neighbors; // Adjacency lists
	private int edgeCount;
	private Random generator;
	private static final int COST = 1000;
	private LinkedList<LinkedList<Integer>> AllPossibleEdges;
	
	//just to debug no of edges that were being added tht is already there in graph (For debugging only)
	public int Collisions=0;
	
	
	// to generate all possible edges to make random edge gen function better
	public void genAllPossibleRemainingEdges()
	{
		System.out.println("Optimizing Random Edge Generator Function....");
		AllPossibleEdges = new LinkedList<LinkedList<Integer>>();
		for(int Vertex = 0; Vertex<this.size; Vertex++)
		{
			LinkedList<Integer> V = new LinkedList<Integer>();
			V.add(Vertex);
			for(int Vertex2 = 0; Vertex2<this.size; Vertex2++)
			{
				if(Vertex2!=Vertex)
				{
					V.add(Vertex2);
				}
			}
			AllPossibleEdges.add(V);
		}
//		printRemPossibleEdges();
		System.out.println("Optimized Random Edge Generator Function");
	}
	
	//removes from allpossible edges once the edge is added to graph
	public void remFromAllPossibleEdges(int v1, int v2)
	{
		LinkedList<Integer> neighbours = AllPossibleEdges.get(v1);
		neighbours.remove(new Integer(v2));
		neighbours = AllPossibleEdges.get(v2);
		neighbours.remove(new Integer(v1));
	}


	// Adds an edge to the graph (both ways)
	public void addEdge(int v1, int v2, double weight) throws InterruptedException 
	{
		Edge edge1 = new Edge(v1, v2, weight);
	//	System.out.println("Inserting=> " + v1 + " " + v2 + " (" + weight + ")");
		if(!hasEdge(edge1))
		{
			neighbors(v1).add(edge1);
			Edge edge2 = new Edge(v2, v1, weight);
			neighbors(v2).add(edge2);
			//EXP BEGIN
			remFromAllPossibleEdges(v1, v2);
			//EXP END
			edgeCount++;
//			System.out.println("Total Edges:" + edgeCount);
			return;
		}
		Collisions++;
//		System.out.println("Edge already present (OR) Edge not allowed!!" + v1 + " " + v2);
		
	}
	
	//checks wther edge already ther in graph
	private boolean hasEdge(Edge edge) 
	{
		LinkedList<Edge> neighbours = neighbors(edge.v1);
		if(edge.v1 == edge.v2)
			return true;
		for(Edge e:neighbours)
		{
			if((e.v1 == edge.v1) && (e.v2 == edge.v2))
				return true;
			if((e.v1 == edge.v2) && (e.v2 == edge.v1))
				return true;
		}
		return false;
	}

	// Constructs a graph with s vertices and no edges
	public Graph(int s) 
	{
		this.size = s;
		generator = new Random();
		genAllPossibleRemainingEdges();
		neighbors = new ArrayList<LinkedList<Edge>>(size);
		for (int i = 0; i < size; i++)
			neighbors.add(new LinkedList<Edge>());
	}
	
	
	//init graph of size s and edges 
	public Graph(int s, int no_of_edges, double[][]edges) throws InterruptedException
	{
		this(s);
		for(int i = 0; i<no_of_edges; i++)
		{
			this.addEdge((int)edges[i][0], (int) edges[i][1], edges[i][2]);
		}
	}
	
	
	//constructor to init graph of size s and density d
	public Graph(int s, int density) throws InterruptedException 
	{
		this(s);
		int size = s;
		int i;
		int current_density = 0;
		
		//TIME CALC

			for(i = 0;  this.getDensity()<density ;i++)
			{
		//		int randomNotConnectedEdge = this.randomNotConnectedEdge();
		//		this.addEdge(this.random(size), this.random(size), (double)this.random(COST)+1); //WORKING METHOD
				
				//EXPERIMENT RANDOM BEGIN

				
				int V1 = this.getRandomVertexA();
				int V2 = this.getRandomVertexB(V1);
				this.addEdge(V1, V2, (double)this.random(COST)+1); 

				//EXPERIMENT RANDOM END
				
				
		//		System.out.println("Random Edge Not Connected to A:" + randomNotConnectedEdgeToA(this.random(size)));
		//		System.out.println(this.randomNotConnectedEdge());
		//		System.out.println("Achieved Density % = " + this.getDensity());
			}
	/*	System.out.println("Runs = " + i);
		System.out.println("Edge Count:" +this.getedgeCount()); 
		System.out.println("Achieved Density % = " + this.getDensity()); 
		*/
			if(this.isConnected_bydfs() == false)
			{
				System.out.println("Failed to generate Connected Graph in random mode. Please run again");
				System.exit(0);
			}
			this.AllPossibleEdges = null;
	}
	

	// Gets the neighbor list of a vertex
	public LinkedList<Edge> neighbors(int v) {
		return neighbors.get(v);
	}

	// Gets the size of the graph
	public int size() {
		return size;
	}

	


	// Depth-first search - fills in the provided array
	public void dfs(int s, boolean[] visited) {
		visited[s] = true;
		for (Edge edge : neighbors(s))
			if (!visited[edge.v2])
				dfs(edge.v2, visited);
	}
	
	//DFS traverse
	public boolean isConnected_bydfs()
	{
		boolean visited[] = new boolean[this.size()];
		dfs(0,visited);
		int nonvisted_count = 0;
		for(boolean i:visited)
		{
			if(i!=true)
			{
			//	System.out.println("Cannot visit " + labels.get(index++));
				nonvisted_count++;
			}
		}
//		System.out.println("Size = " + size);
//		System.out.println("Visited Count = " + visited_count);
//		System.out.println("Non visited Count = " + nonvisted_count);
//		System.out.println("Sum = " + (nonvisted_count + visited_count));
		if(nonvisted_count==0) return true;
		return false;
	}



	//returns random integer btwn 0 and size
	public int random(int n)
	{
		return generator.nextInt(n);
	}
	
	//returns edge count
	public int getedgeCount()
	{
		return this.edgeCount;
	}
	
	//returns density
	public int getDensity()
	{	
		return this.getedgeCount()*100/(size*(size-1)/2)	;
	}
	
	//get a random vertex
	public int getRandomVertexA()
	{
		int RandomA = this.random(this.size);
		while(AllPossibleEdges.get(RandomA).size() == 1)
			RandomA = this.random(this.size);
	//	System.out.println("Returning A = " + RandomA + " Size=" + AllPossibleEdges.get(RandomA).size());
		return RandomA;
	}
	
	//get a random vertex B connected to A
	public int getRandomVertexB(int A)
	{
		LinkedList<Integer> neighbours = AllPossibleEdges.get(A);
//		System.out.println("Finding neighbour for " + A);
//		printRemPossibleEdges();
		int randomIndex = 0;
		while(randomIndex==0)
			randomIndex = this.random(neighbours.size());
		return neighbours.get(randomIndex);
	}
	
	public ArrayList<LinkedList<Edge>> getAdjacencyList()
	{
		return neighbors;
	}
	
	
	//return adjacent edjes with the weight also
	public LinkedHashMap<Integer, Double> getEdgesWithWeight(int A)
	{
		LinkedHashMap<Integer, Double> Neighbours = new LinkedHashMap<Integer, Double>();
		LinkedList<Edge> NeighbourEdges =  this.neighbors(A);
		for(Edge e: NeighbourEdges)
		{
			Neighbours.put(e.v2, e.weight);
		}
		return Neighbours;
	}
	
}