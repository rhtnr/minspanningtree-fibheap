

import java.util.*; // For HashMap
import java.util.Map.Entry;

public class PrimFibHeap {
 
	adjacencyList gPrimResult;
	adjacencyList gPrim;
	int size;
	long genTime = 0; //holds the time req to calculate mst
	
	
	//initializes the structures req for calulcating mst in fib mode
	public PrimFibHeap(Graph g)
	{
		gPrim = new adjacencyList();
		this.size = g.size();
		for(int i = 0; i< size; i++)
		{
			gPrim.addNode(i);
		}
		for(int i = 0; i< size; i++)
		{
			LinkedHashMap<Integer, Double> adjE = g.getEdgesWithWeight(i);
			for(Entry<Integer, Double> entry : adjE.entrySet())
			{
				gPrim.addEdge(i, entry.getKey(), entry.getValue());
			}
		}
		
	}
	//finds the mst through fib scheme
	public void findMST() throws Exception
	{
		long start = System.currentTimeMillis();
		gPrimResult = this.findMST(gPrim);
		genTime = System.currentTimeMillis() - start;
	}
	
	//prints the mst 
	public void printMST() throws Exception
	{
		int[][] treeedges = new int[size][3];
		Double WeightSum = (double) 0;
		String result = "";
		int j = 0;
		for(int i = 0; i<size;i++)
		{
			Map<Integer, Double> edges = gPrimResult.edgesFrom(i);
			for(Entry <Integer, Double> entr: edges.entrySet())
			{
				if(notExists(treeedges, i, entr.getKey()) && notExists(treeedges, entr.getKey(), i))
				{
					WeightSum+=entr.getValue();
					result = result + i + " " + entr.getKey() + System.getProperty("line.separator");
					treeedges[j][0] = i;
					treeedges[j][1] = entr.getKey();
					j++;
				}
			}
		}
		double temp = WeightSum;
		System.out.println((int)temp);
		System.out.println(result);
	}
	
	
	//checks wther edge already in tree
    private boolean notExists(int[][] treeedges, int i, Integer key) 
    {
    //	System.out.println("For " + i + " " + key);
    	for(int i1=0;i1<treeedges.length; i1++)
    	{
    //		System.out.println("checking " + treeedges[i1][0] + " " + treeedges[i1][1]);
    		if(treeedges[i1][0] == i && treeedges[i1][1] == key)
    			return false;
    	}
		
		return true;
	}
    
    //the main priority queue logic with a fib heap to find mst
	public static adjacencyList findMST(adjacencyList graph) throws Exception 
	{
        mstFibHeap priorityQueue = new mstFibHeap();
        //Map to store the nodes. Map so that lookup time is O(1)
        Map<Integer, mstFibHeap.Vertex> entries = new HashMap<Integer, mstFibHeap.Vertex>();
        adjacencyList result = new adjacencyList();
        if (graph.isEmpty())
            return result;
        int startNode = graph.iterator().next();
        result.addNode(startNode); //root node is the statring point
        addAdjacentEdgesToHeap(startNode, graph, priorityQueue, result, entries);


        //For a min spanning tree we have No of vertices - 1 edges.
        //Therefore the loop is carried out for graph size -1 times
        
        for (int i = 0; i < graph.size() - 1; ++i) 
        {
            int toAdd = priorityQueue.deleteMin().getValue(); //get the least weight
            int endpoint = minimumWeightVertex(toAdd, graph, result); // find that edge having this minimum weight
            result.addNode(toAdd);
            result.addEdge(toAdd, endpoint, graph.edgeCost(toAdd, endpoint));
            addAdjacentEdgesToHeap(toAdd, graph, priorityQueue, result, entries); //add the adjacent edges of the newly added node to queue
        }
        return result;
    }

	
	
	//searches for the node that has the minimum cost specified in result
  
    private static int minimumWeightVertex(int node, adjacencyList graph,  adjacencyList result) throws Exception 
    {
    	Integer endpoint = null;
    	double minimumWeight = Double.POSITIVE_INFINITY;
        for (Map.Entry<Integer, Double> entry : graph.edgesFrom(node).entrySet()) 
        {
        	if (!result.containsNode(entry.getKey())) continue;
            if (entry.getValue() >= minimumWeight) continue;
            endpoint = entry.getKey();
            minimumWeight = entry.getValue();
        }
        return endpoint;
    }

    
    
    //add the adjacent nodes to heap
    private static void addAdjacentEdgesToHeap(int node, adjacencyList graph, mstFibHeap pq, adjacencyList result,  Map<Integer, mstFibHeap.Vertex> entries ) throws Exception 
    {
        for (Map.Entry<Integer, Double> edge1 : graph.edgesFrom((Integer) node).entrySet()) 
        {
        	if (result.containsNode(edge1.getKey())) continue; // Case 1
            if (!entries.containsKey(edge1.getKey())) 
            { // Case 2
                entries.put(edge1.getKey(), pq.insert(edge1.getKey(), edge1.getValue()));
            }
            else if (entries.get(edge1.getKey()).getPriority() > edge1.getValue()) 
            { // Case 3
                pq.decreaseKey(entries.get(edge1.getKey()), edge1.getValue());
            }
        }
    }
   
	public static class adjacencyList implements Iterable<Integer> {
		private final Map<Integer, Map<Integer, Double>> graph1 = new HashMap<Integer, Map<Integer, Double>>();
		
		//addnode to list
		public boolean addNode(int node) 
		{
			if (graph1.containsKey(node))
				return false;
			graph1.put(node, new HashMap<Integer, Double>());
			return true;
		}
		
		//to add edge
		public void addEdge(int one, int two, double length) 
		{
			if (!graph1.containsKey(one) || !graph1.containsKey(two))
				return;
			graph1.get(one).put(two, length);
			graph1.get(two).put(one, length);
		}
		
		//returns weight of edge
		public double edgeCost(int one, int two) throws Exception 
		{
			if (!graph1.containsKey(one) || !graph1.containsKey(two))
				throw new Exception();
			Double result = graph1.get(one).get(two);
			if (result == null)
				throw new Exception();
			return result;
		}
		//returns a map fo edges from a node
		public Map<Integer, Double> edgesFrom(int node) throws Exception 
		{
			Map<Integer, Double> edges = graph1.get(node);
			if (edges == null)
				throw new Exception();
			return Collections.unmodifiableMap(edges);
		}

		//checks wther it has a vertex
		public boolean containsNode(int node) {
			return graph1.containsKey(node);
		}

		
		public Iterator<Integer> iterator() {
			return graph1.keySet().iterator();
		}

		//returns size
		public int size() {
			return graph1.size();
		}

		//checks wther list is empty
		public boolean isEmpty() {
			return graph1.isEmpty();
		}
	}
};
