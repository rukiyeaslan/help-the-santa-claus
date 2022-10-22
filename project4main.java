import java.io.File;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Scanner;



public class project4main {


		//static long startTime = System.nanoTime();
		//static String where = "here";

	          static List<Edge>[] createGraph(int nodes) {
			    List<Edge>[] graph = new List[nodes];
			    for (int i = 0; i < nodes; i++)
			      graph[i] = new ArrayList<>();
			    return graph;
			  }

			  public static void addEdge(List<Edge>[] graph, int s, int t, int cap) {
			    graph[s].add(new Edge(t, graph[t].size(), cap));
			    graph[t].add(new Edge(s, graph[s].size()-1 , 0));   //negative direction
			  }

			  //finding augmenting path
			  static boolean bfs(List<Edge>[] graph, int src, int dest, int[] level) {
				  Arrays.fill(level, -1);
					LinkedList<Integer> q = new LinkedList<>();
					level[src] =0;
									
				    q.add(src);
				    while(!q.isEmpty()) {
				    	int f = q.poll();
				    	
				    	for(int i=0; i< graph[f].size();i++) {
				    		Edge e = graph[f].get(i);
				    		if (e.rev == dest) return level[dest] >= 0;
				    		
				    		if(e.cap>0 && level[e.t]<0) {
				    			level[e.t] = level[f] +1;			    			
				    			q.add(e.t);
				    		}
				    	}			    	
					}
				    return level[dest] >= 0;
			  }
	 
			  //iterating thr level list and greating residual graph.
			  static int sendFlow(List<Edge>[] graph, int[] iter, int[] level, int dest, int u, int f) {
				  if (u == dest)
				      return f;
				    for (; iter[u] < graph[u].size(); iter[u]++) {
				      Edge edge = graph[u].get(iter[u]);
				        
				      //if (iter[u] == graph[u].size()) break;
				      if (level[edge.t] == level[u] + 1 && 0< edge.cap) {
				        int df = sendFlow(graph, iter, level, dest, edge.t, Math.min(f, edge.cap));
				        if (df > 0) {
				          edge.cap -= df;
				          graph[edge.t].get(edge.rev).cap += df;
				          return df;
				        }
				      }
				    }
				    return 0;
			  }

			  public static int maxFlow(List<Edge>[] graph, int src, int dest) {
			    int flow = 0;
			    int[] level = new int[graph.length];
			    while (bfs(graph, src, dest, level)) {   //while there is augmenting path, proceed
			      int[] iter = new int[graph.length]; 
			    
			      while ( true) {
			        int df = sendFlow(graph, iter, level, dest, src, Integer.MAX_VALUE);  //find flow from src to dest
			        if (df == 0) {
			          
			          break;}
			        flow += df;
			      }
			    }
			    return flow;
			  }

			  //taking input
			  public static void main(String[] args) throws FileNotFoundException{
				  
				  Scanner in = new Scanner(new File(args[0]));
					PrintStream out = new PrintStream(new File(args[1]));
					
					HashMap<String, ArrayList<Integer>> pocketMap = new HashMap<>();
					HashMap<Integer, Integer> vehicleMap = new HashMap<>();
					int totalGifts=0;
					int index =1;
					HashMap<Integer, Integer> greenTrains = new HashMap<>();
					int greenTrain = in.nextInt();
					for(int i=1; i<greenTrain+1; i++) {
						int capacity = in.nextInt();
					
						
						
						vehicleMap.put(index,capacity);
						greenTrains.put(index,capacity);
						index++;
					}
					HashMap<Integer, Integer> redTrains = new HashMap<>();
					int redTrain = in.nextInt();
					for(int i=1; i<redTrain+1; i++) {
						int capacity = in.nextInt();
					
						
						
						vehicleMap.put(index,capacity);
						redTrains.put(index,capacity);
						index++;
						}
					
					HashMap<Integer, Integer> greenReins = new HashMap<>();
					int greenRein = in.nextInt();
					for(int i=1; i<greenRein+1; i++) {
						int capacity = in.nextInt();
				
						
						vehicleMap.put(index,capacity);
						greenReins.put(index,capacity);
						index++;
						}
					
					
					int redRein = in.nextInt();
					HashMap<Integer, Integer> redReins = new HashMap<>();
					for(int i=1; i<redRein+1; i++) {
						int capacity = in.nextInt();
			
					
						vehicleMap.put(index,capacity);
						redReins.put(index,capacity);
						index++;
						
						}
					
					int numOfBags = in.nextInt();
					in.nextLine();
					String line =in.nextLine();
					String[] lst = line.split(" ");
					
					//creating graph with number of bags + vehicles +2
					List<Edge>[] graph = createGraph(greenTrain + redTrain + redRein + greenRein +numOfBags +2);
					for(int i=0; i<2*numOfBags; i+=2) {

						String name = lst[i];
						String cap = lst[i+1];
						int capacity = Integer.parseInt(cap);
						totalGifts+=capacity;
						
						String extract = name.replaceAll("[^a-zA-Z]+", "");
						
						if(!name.contains("a") && pocketMap.keySet().contains(name)) {
							ArrayList<Integer> mapp = pocketMap.get(name);
							int capp = mapp.get(1);
							mapp.set(1,  capp +capacity);
							pocketMap.put(name, mapp);
							index++;
							continue;
						}
						
						else if(name.contains("a") && pocketMap.keySet().contains(name)) {
							name = name + index;
							ArrayList<Integer> mappyy = new ArrayList<Integer>();
							mappyy.add(index);
							mappyy.add(capacity);
							pocketMap.put(name,mappyy);
							index++;
							continue;
						}
						ArrayList<Integer> mappy = new ArrayList<Integer>();
						mappy.add(index);
						mappy.add(capacity);
						
						pocketMap.put(name,mappy);
						index++;
						}
				
					 
					for(Map.Entry<String, ArrayList<Integer>> i: pocketMap.entrySet()) {
						addEdge(graph, 0, i.getValue().get(0), i.getValue().get(1));
						String x = i.getKey();
						int capacity = i.getValue().get(1);
						int index1 = i.getValue().get(0);
						String name = x.replaceAll("[^a-zA-Z]+", "");
						//System.out.println("name: " + i.getKey() + " index: "+ i.getValue().get(0) +  " capacity: " + i.getValue().get(1));
						if(name.equals("a")) {
							for(int j : vehicleMap.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("b")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
							
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("c")) {
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
							
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("d")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
							
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("e")) {
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
							
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("ab")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j, 1);
							}
							
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("ac")) {
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, 1);
							}
							
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("ad")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j,1);
							}
							
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("ae")) {
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
							
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("bd")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("be")) {
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("cd")) {
							
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						else if(name.equals("ce")) {
							
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, capacity);
							}
						}
						
						else if(name.equals("abd")) {
							for(int j : greenTrains.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("abe")) {
							for(int j : greenReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						
						else if(name.equals("acd")) {
							for(int j : redTrains.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
						else if(name.equals("ace")) {
							for(int j : redReins.keySet()) {
								addEdge(graph, index1, j, 1);
							}
						}
					} 
					
					int destIndex = greenTrain + redTrain + redRein + greenRein +numOfBags +1;
					
					for(Map.Entry<Integer, Integer> i: vehicleMap.entrySet()) {
						addEdge(graph, i.getKey(), destIndex, i.getValue());

					}

			
				System.out.println(totalGifts - maxFlow(graph, 0, destIndex));


		}
	
}
