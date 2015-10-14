// Copy paste this Java Template and save it as "OutForAWalk.java"
import java.util.*;
import java.io.*;

// write your matric number here: A0124123Y
// write your name here: Adrian Pheh
// write list of collaborators here:
// year 2015 hash code: JESg5svjYpIsmHmIjabX (do NOT delete this line)

class OutForAWalk {
	private static int V; // number of vertices in the graph (number of rooms in the building)
	private static Vector<Vector<IntegerPair>> AdjList; // the weighted graph (the building), effort rating of each corridor is stored here too
	private static int[][] cache;
	private static int[] visited;
	private static IntegerPair[] parent;

	public OutForAWalk() {
		V = 0;
		AdjList = new Vector<Vector<IntegerPair>>();
	}

	public static class Prim {

		private static Vector<Vector<IntegerPair>> AdjList;
		private static Vector<Boolean> taken;
		private static PriorityQueue<IntegerTriple> pq;

		private static void process(int vtx) {
			//System.out.println(">> At vertex " + vtx);
			taken.set(vtx, true);
			for (int j = 0; j < AdjList.get(vtx).size(); j++) {
				IntegerTriple v = new IntegerTriple(AdjList.get(vtx).get(j).first(), 
						vtx, AdjList.get(vtx).get(j).second());
				if (!taken.get(v.third())) {
					pq.offer(new IntegerTriple(v.first(), v.second(), v.third()));  // we sort by weight then by adjacent vertex
					//System.out.println(">> Inserting (" + v.first() + ", " + v.second() + ") to priority queue");
				}
			}
		}

		//
		@SuppressWarnings("unchecked")
		public static void PrimWeight(Vector<Vector<IntegerPair>> adjList, int source) throws Exception {

			AdjList = adjList;
			
			taken = new Vector<Boolean>(); 
			taken.addAll(Collections.nCopies(V, false));

			pq = new PriorityQueue<IntegerTriple>();

			// take any vertex of the graph, for simplicity, vertex 0, to be included in the MST
			process(source);
			int currMax = 0;

			while (!pq.isEmpty()) { // we will do this until all V vertices are taken (or E = V-1 edges are taken)
				IntegerTriple front = pq.poll();
				if (!taken.get(front.third())) { // we have not connected this vertex yet
					currMax = Math.max(currMax, front.first());
					cache[source][front.third()] = currMax;
					process(front.third());
				}
			}
		}
		
		// Return greatest minimum edge from <source> to <destination>
		@SuppressWarnings("unchecked")
		public static int PrimMST(Vector<Vector<IntegerPair>> adjList, int source, int destination) throws Exception {

			AdjList = adjList;
			
			taken = new Vector<Boolean>(); 
			taken.addAll(Collections.nCopies(V, false));

			pq = new PriorityQueue<IntegerTriple>();

			// take any vertex of the graph, for simplicity, vertex 0, to be included in the MST
			process(source);
			int maxWeight = 0;

			while (!pq.isEmpty()) { // we will do this until all V vertices are taken (or E = V-1 edges are taken)
				IntegerTriple front = pq.poll();
				if (front.third() == destination) {
					maxWeight = Math.max(maxWeight, front.first());
					break;
				} else if (!taken.get(front.third())) { // we have not connected this vertex yet
					maxWeight = Math.max(maxWeight, front.first()); // add the weight of this edge
					process(front.third());
				}
			}		
			return maxWeight;
		}
		
		// Return MST using Prim's
		@SuppressWarnings("unchecked")
		public static Vector<Vector<IntegerPair>> getMST(Vector<Vector<IntegerPair>> adjList) throws Exception {

			AdjList = adjList;
			
			taken = new Vector<Boolean>(); 
			taken.addAll(Collections.nCopies(V, false));
			pq = new PriorityQueue<IntegerTriple>();
			Vector<Vector<IntegerPair>> mst = new Vector<Vector<IntegerPair>>(V);
			for (int i = 0; i < V; i++) {
				mst.add(new Vector<IntegerPair>());
			}

			// take any vertex of the graph, for simplicity, vertex 0, to be included in the MST
			process(0);

			while (!pq.isEmpty()) { // we will do this until all V vertices are taken (or E = V-1 edges are taken)
				IntegerTriple front = pq.poll();
				if (!taken.get(front.third())) { // we have not connected this vertex yet
					mst.get(front.second()).add(new IntegerPair(front.first(), front.third()));
					mst.get(front.third()).add(new IntegerPair(front.first(), front.second()));
					process(front.third());
				}
			}
			return mst;
		}
	}

	void PreProcess() throws Exception {
		
		// For subtask D
		// Given that queries are restricted to only [0..9], run Prim's to get the MST
		Vector<Vector<IntegerPair>> mst = Prim.getMST(AdjList);
		
		int source = 10;
		
		// For cases when V is smaller than 10 vertices
		if (V < 10) {
			source = V;
		}
		
		// Initialize the cache, space O(10V)
		cache = new int[10][V];
		
		for (int i = 0; i < source; i++) {			
			// Run modified Prim's
			Prim.PrimWeight(mst, i);
		}
	}

	int Query(int source, int destination) throws Exception {
		int ans = 0;
		
		// Part C answer
		// ans = Prim.PrimMST(AdjList, source, destination);
		
		// Part D answer
		ans = cache[source][destination];
		
		return ans;
	}

	// You can add extra function if needed
	// --------------------------------------------
	public void display(Vector<Vector<IntegerPair>> adjList) {
		for (int i = 0; i < adjList.size(); i++) {
			for (int j = 0; j < adjList.get(i).size(); j++) {
				System.out.println("Vertex " + i + 
						" to " + adjList.get(i).get(j).second() + 
						" with weight " + adjList.get(i).get(j).first());
			}
		}
		System.out.println('\n');
	}

	// --------------------------------------------

	void run() throws Exception {
		// do not alter this method
		IntegerScanner sc = new IntegerScanner(System.in);
		PrintWriter pr = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

		int TC = sc.nextInt(); // there will be several test cases
		while (TC-- > 0) {
			V = sc.nextInt();

			// clear the graph and read in a new graph as Adjacency List
			AdjList = new Vector<Vector<IntegerPair>>();
			for (int i = 0; i < V; i++) {
				AdjList.add(new Vector<IntegerPair>());

				int k = sc.nextInt();
				while (k-- > 0) {
					int j = sc.nextInt(), w = sc.nextInt();
					AdjList.get(i).add(new IntegerPair(w, j)); // edge (corridor) weight (effort rating) is stored here
				}
			}

			PreProcess(); // you may want to use this function or leave it empty if you do not need it

			int Q = sc.nextInt();
			while (Q-- > 0)
				pr.println(Query(sc.nextInt(), sc.nextInt()));
			pr.println(); // separate the answer between two different graphs
		}

		pr.close();
	}

	public static void main(String[] args) throws Exception {
		// do not alter this method
		OutForAWalk ps4 = new OutForAWalk();
		ps4.run();
	}
}



class IntegerScanner { // coded by Ian Leow, using any other I/O method is not recommended
	BufferedInputStream bis;
	IntegerScanner(InputStream is) {
		bis = new BufferedInputStream(is, 1000000);
	}

	public int nextInt() {
		int result = 0;
		try {
			int cur = bis.read();
			if (cur == -1)
				return -1;

			while ((cur < 48 || cur > 57) && cur != 45) {
				cur = bis.read();
			}

			boolean negate = false;
			if (cur == 45) {
				negate = true;
				cur = bis.read();
			}

			while (cur >= 48 && cur <= 57) {
				result = result * 10 + (cur - 48);
				cur = bis.read();
			}

			if (negate) {
				return -result;
			}
			return result;
		}
		catch (IOException ioe) {
			return -1;
		}
	}
}



class IntegerPair implements Comparable < IntegerPair > {
	Integer _first, _second;

	public IntegerPair(Integer f, Integer s) {
		_first = f;
		_second = s;
	}

	public int compareTo(IntegerPair o) {
		if (!this.first().equals(o.first()))
			return this.first() - o.first();
		else
			return this.second() - o.second();
	}

	Integer first() { return _first; }
	Integer second() { return _second; }
}



class IntegerTriple implements Comparable < IntegerTriple > {
	Integer _first, _second, _third;

	public IntegerTriple(Integer f, Integer s, Integer t) {
		_first = f;
		_second = s;
		_third = t;
	}

	public int compareTo(IntegerTriple o) {
		if (!this.first().equals(o.first()))
			return this.first() - o.first();
		else if (!this.second().equals(o.second()))
			return this.second() - o.second();
		else
			return this.third() - o.third();
	}

	Integer first() { return _first; }
	Integer second() { return _second; }
	Integer third() { return _third; }
}
