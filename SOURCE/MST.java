

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MST {
	
	public static void main(String args[]) throws Exception
	{
		int n;
		int d;
		File f;
		if(args.length == 0)
		{
			printHelp();return;
		}
		
		if (!args[0].equalsIgnoreCase("-r") && !args[0].equalsIgnoreCase("-s") && !args[0].equalsIgnoreCase("-f") && !args[0].equalsIgnoreCase("-p"))
		{
			printHelp();
			return;
		}
		//RANDOM MODE
		if(args[0].equalsIgnoreCase("-r"))
		{
			try
			{
				n = Integer.parseInt(args[1]);
				if(n!=5 && n!=6 && n!=10 && n!=1000 && n!=3000 && n!= 5000)
				{
					System.out.println("Invalid input!!");
					printHelp();return;
				}
				d = Integer.parseInt(args[2]);
				if(d!=10 && d!=20 && d!= 30 && d!= 40 && d!= 50 && d!= 60 && d!= 70 && d!= 80 && d!= 90 && d!= 100)
				{
					System.out.println("Invalid input!!");
					printHelp();return;
				}
				System.out.println("Generating graph in memory..");
				Graph g = new Graph(n,d);
				System.out.println("Graph Generated..");
				System.out.println("Size = " + g.size() + " Edge Count = " + g.getedgeCount());
				PrimFibHeap p2 = new PrimFibHeap(g);
				System.out.println("Running Fib Heap Scheme on the graph..");
				p2.findMST();
				System.out.println("Fib Heap Scheme on the graph generated and MST found");
				System.out.println("Fibonacci Scheme Output:");
				p2.printMST();
				PrimsArray p = new PrimsArray(g);
				System.out.println("Running Simple Scheme on the graph..");
				p.findMST();
				System.out.println("Simple Scheme on the graph generated and MST found"); 
				System.out.println("Simple Scheme Output:");
				p.printMST();
				System.out.println("Time taken for Simple Scheme with n=" + n + " d=" + d + " is " + p.genTime + " msec");
				System.out.println("Time taken for Fib Heap Scheme with n=" + n + " d=" + d + " is " + p2.genTime + " msec");
				
			}
			catch(Exception e)
			{
				System.out.println("Invalid Input!!");
				e.printStackTrace();
				printHelp();
				return;
			}
		}
		//SIMPLE SCHEME
		if(args[0].equalsIgnoreCase("-s"))
		{
			//for public Graph(int s, int no_of_edges, double[][]edges) throws InterruptedException
			f = new File(args[1]);
			if(f.exists()) 
			{
				String line = "";
				FileReader fileReader = new FileReader(args[1]);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				line = bufferedReader.readLine();
				String[] parts = line.split(" ");
				int size = Integer.parseInt(parts[0]);
				int no_of_edges = Integer.parseInt(parts[1]);
				double edges[][] = new double[no_of_edges+1][4];
				int j = 0;
				System.out.println("Read Size = " + size + " , Read No Of Edges = " + no_of_edges);
				int lineCount = 0;
				while((line = bufferedReader.readLine()) != null) 
				{
					if(lineCount==no_of_edges)
						break;
					parts = line.split(" ");
					if(parts.length!=3)
					{
						System.out.println("Invalid File Contents/Blank Lines Found. Please correct the input File");
						return;
					}
					edges[j][0] = Double.parseDouble(parts[0]);
					edges[j][1] = Double.parseDouble(parts[1]);
					edges[j][2] = Double.parseDouble(parts[2]);
					System.out.println("Read : " + edges[j][0] + " " + edges[j][1] + " " + edges[j][2]);
					j++;
					lineCount++;
				} 
				System.out.println("Generating Graph in memory..");
				Graph g = new Graph(size, no_of_edges, edges);
				System.out.println("Graph Generated..");
		//		g.print();
				PrimsArray p = new PrimsArray(g);
				p.findMST();
				System.out.println("MST Simple Array Scheme Output");
				System.out.println("******************************");
				p.printMST();
				System.out.println();
				System.out.println("Time taken for Simple Scheme (Graph generation Time Not Included): " + p.genTime + " ms");
				bufferedReader.close();
			}
			else
			{
				System.out.println("File does not exist or No permission to read!!");
			printHelp();return;
			}
		}
		//FIBONACCI SCHEME
		if(args[0].equalsIgnoreCase("-f"))
		{
			f = new File(args[1]);
			if(f.exists()) 
			{ 
				String line = "";
				FileReader fileReader = new FileReader(args[1]);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				line = bufferedReader.readLine();
				String[] parts = line.split(" ");
				int size = Integer.parseInt(parts[0]);
				int no_of_edges = Integer.parseInt(parts[1]);
				double edges[][] = new double[no_of_edges+1][4];
				int j = 0;
				int lineCount = 0;
				System.out.println("Read Size = " + size + " , Read No Of Edges = " + no_of_edges);
				while((line = bufferedReader.readLine()) != null) 
				{
					if(lineCount==no_of_edges)
						break;
					parts = line.split(" ");
					if(parts.length!=3)
					{
						System.out.println("Invalid File Contents/Blank Lines Found. Please correct the input File");
						return;
					}
					edges[j][0] = Double.parseDouble(parts[0]);
					edges[j][1] = Double.parseDouble(parts[1]);
					edges[j][2] = Double.parseDouble(parts[2]);
					System.out.println("Read : " + edges[j][0] + " " + edges[j][1] + " " + edges[j][2]);
					j++;
					lineCount++;
				} 
				System.out.println("Generating Graph in memory..");
				Graph g = new Graph(size, no_of_edges, edges);
				System.out.println("Graph Generated..");
		//		g.print();
				PrimFibHeap p = new PrimFibHeap(g);
				p.findMST();
				System.out.println();
				System.out.println("MST Fibonacci Scheme Output");
				System.out.println("****************************");
				p.printMST();
				System.out.println("Time taken for Fibinacci Heap Scheme (Graph generation Time Not Included): " + p.genTime + " ms");
				bufferedReader.close();
			}
			else
			{
				System.out.println("File does not exist or No permission to read!!");
			printHelp();return;
			}
		}
	
		
/*		for(int i = 50; i<=50; i+=10)
		{
			int size = 100;
			Graph g = new Graph(size,i);
			g.print();
			Thread.sleep(100);
			PrimsArray p = new PrimsArray(g);
			long start = System.currentTimeMillis();
			p.findMST();
			long p1time = System.currentTimeMillis() - start;
			p.printMSTwithWeight();
			System.out.println("Density = " + g.getDensity());
			System.out.println(g.getedgeCount() + " " + g.size());
			System.out.println("COllissions" + g.Collisions);
			Prim p2 = new Prim(g);
			start = System.currentTimeMillis();
			p2.findMST();
			long p2time = System.currentTimeMillis() - start;
			p2.printMST();
			System.out.println("P1 time: " + p1time + " P2 Time: " + p2time );
		}  
		{
			int size = 3;
			int no_of_edges = 2;
			double[][] edges = {{0,1,5}, {1,2,8}};
			Graph g = new Graph(size, no_of_edges, edges);
			g.print();
			PrimsArray p = new PrimsArray(g);
			p.findMST();
			p.printMSTwithWeight();
		} */
		
	}

	private static void printHelp() {
		System.out.println("USAGE 1: MST -r n d");
		System.out.println("USAGE 2: MST -s file-name");
		System.out.println("USAGE 3: MST -f file-name");
//		System.out.println("USAGE 3: MST -p");
		System.out.println();
		System.out.println("More Details");
		System.out.println("************");
		System.out.println("-r => random");
		System.out.println("-n => graph size");
		System.out.println("-d => density in percentage");
		System.out.println("-s => simple scheme");
		System.out.println("-f => fibonacci heap");
//		System.out.println("-p => performance measurment mode with randomly generated 10 undirected graphs with 10%-100% densities");
		
	}

}
