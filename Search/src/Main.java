import java.util.*;
import java.io.*;

public class Main {
	static Map<String, double[]> townCoords = new HashMap<>();
	static Map<String, List<String>> adjTowns = new HashMap<>();
	
	static File file1 = new File("Adjacencies.txt");
	static File file2 = new File("coordinates (1).csv");
	
	public static void main(String[] args) {
		userInput();
	}
	

	public static void townData() {
		Scanner adj;
		try {
			adj = new Scanner(file1);
			while(adj.hasNextLine()) {
				    String line = adj.nextLine().trim();
				    String[] parts = line.split("\\s+"); 

				    String town1 = parts[0];
				    String town2 = parts[1];

				   
				    adjTowns.putIfAbsent(town1, new ArrayList<>());
				    adjTowns.get(town1).add(town2);

				  
				    adjTowns.putIfAbsent(town2, new ArrayList<>());
				    adjTowns.get(town2).add(town1);
				    
				}
			adj.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		
		Scanner towns;
		try {
			towns = new Scanner(file2);
			while(towns.hasNextLine()) {
				String line = towns.nextLine();
				String[] parts = line.split(",");
				
				String town = parts[0];
				double lat = Double.parseDouble(parts[1]);
				double longit = Double.parseDouble(parts[2]);
				
				townCoords.put(town, new double[] {lat,longit});
			}
		towns.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		

	}
	
	public static void userInput() {
		townData();
		 		
		Scanner scanner = new Scanner(System.in);
		String start = null;
		String end = null;
		
		boolean exit = false;
		do {
		
		System.out.println("Choose a search method"+"\n"+
			"1.Breadth-first search"+"\n"+
			"2.Depth-first search"+"\n"+
			"3.ID-DFS search"+"\n"+
			"4.Best-first search"+"\n"+
			"5.A* search"+"\n"+
			"6.Exit");
			
		int option = Integer.parseInt(scanner.nextLine());
		
		
		
		switch(option){
		case 1:
			 System.out.println("Where would you like to start?");
			 start = scanner.nextLine();
			
			
			 System.out.println("Where would you like to end?");
			 end = scanner.nextLine();
			
			 if (!adjTowns.containsKey(start) || !adjTowns.containsKey(end)) {
		            System.out.println("Town not in data");
		            scanner.close();
		            return;
		        }
			BreadthFS(start,end);
			
			break;
		case 2:
			 System.out.println("Where would you like to start?");
			 start = scanner.nextLine();
			
			
			 System.out.println("Where would you like to end?");
			 end = scanner.nextLine();
			
			 if (!adjTowns.containsKey(start) || !adjTowns.containsKey(end)) {
		            System.out.println("Town not in data");
		            scanner.close();
		            return;
		        }
			DFS(start,end);
		
			break;
		case 3:
			System.out.println("Where would you like to start?");
			 start = scanner.nextLine();
			
			
			 System.out.println("Where would you like to end?");
			 end = scanner.nextLine();
			
			 if (!adjTowns.containsKey(start) || !adjTowns.containsKey(end)) {
		            System.out.println("Town not in data");
		            scanner.close();
		            return;
		        }
			IDDFS(start,end);
			
			break;
		case 4:
			 System.out.println("Where would you like to start?");
			 start = scanner.nextLine();
			
			
			 System.out.println("Where would you like to end?");
			 end = scanner.nextLine();
			
			 if (!adjTowns.containsKey(start) || !adjTowns.containsKey(end)) {
		            System.out.println("Town not in data");
		            scanner.close();
		            return;
		        }
			BestFS(start,end);
			
			break;
		case 5:
			 System.out.println("Where would you like to start?");
			 start = scanner.nextLine();
			
			
			 System.out.println("Where would you like to end?");
			 end = scanner.nextLine();
			
			 if (!adjTowns.containsKey(start) || !adjTowns.containsKey(end)) {
		            System.out.println("Town not in data");
		            scanner.close();
		            return;
		        }
			ASearch(start,end);
			
			break;
		case 6:
			exit = true;
			System.exit(0);
			break;
		default:
			System.out.println("Not a valid option");
			
	}
	}while(!exit);
		
	
	
		System.out.println("Town not in data");
		
		scanner.close();
		
	}
	
	
	private static double getDistance(double lat1, double lon1, double lat2, double lon2) {
		 return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
	    }
	
	
	public static void BreadthFS(String start,String end) {
		 	double startTime = System.nanoTime();
			
		    Queue<String> queue = new LinkedList<>();
	        Set<String> visited = new HashSet<>();
	        HashMap<String, String> cameFrom = new HashMap<>();
	        HashMap<String, Double> distances = new HashMap<>();

	        queue.add(start);
	        visited.add(start);
	        distances.put(start, 0.0);

	        while (!queue.isEmpty()) {
	            String current = queue.poll();

	            if (current.equals(end)) {
	            	
	            	double endTime = System.nanoTime(); // Stop the timer
	                double duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds

	                
	                List<String> path = new ArrayList<>();
	                String step = end;
	                while (step != null) {
	                    path.add(step);
	                    step = cameFrom.get(step);
	                }
	                Collections.reverse(path);
	                System.out.println("Total Distance: " + distances.get(end) + " km");
	                System.out.println("Path: " + path);
	                System.out.printf("Time Taken:%.3f ms%n ",duration);
	                return;
	            }

	            for (String neighbor : adjTowns.getOrDefault(current, new ArrayList<>())) {
	                if (!visited.contains(neighbor)) {
	                    visited.add(neighbor);
	                    queue.add(neighbor);
	                    cameFrom.put(neighbor, current);

	                   
	                    double[] currCoords = townCoords.get(current);
	                    double[] neighborCoords = townCoords.get(neighbor);
	                    double dist = getDistance(currCoords[0], currCoords[1], neighborCoords[0], neighborCoords[1]);

	                    
	                    distances.put(neighbor, distances.get(current) + dist);
	                }
	            }
	        }

	      
	    }
	
	
	public static void DFS(String start, String end) {
		 	double startTime = System.nanoTime();

		    Stack<String> stack = new Stack<>();
		    Set<String> visited = new HashSet<>();
		    HashMap<String, String> cameFrom = new HashMap<>();
		    HashMap<String, Double> distances = new HashMap<>();

		    stack.push(start);
		    visited.add(start);
		    distances.put(start, 0.0);

		    while (!stack.isEmpty()) {
		        String current = stack.pop();

		        if (current.equals(end)) {
		            double endTime = System.nanoTime(); 
		            double duration = (endTime - startTime)  / 1_000_000; 

		            List<String> path = new ArrayList<>();
		            String step = end;
		            while (step != null) {
		                path.add(step);
		                step = cameFrom.get(step);
		            }
		            Collections.reverse(path);

		            System.out.println("Total Distance: " + distances.get(end) + " km");
		            System.out.println("Path: " + path);
		            System.out.printf("Time Taken:%.3f ms%n ",duration);
		            return;
		        }

		        for (String neighbor : adjTowns.getOrDefault(current, new ArrayList<>())) {
		            if (!visited.contains(neighbor)) {
		                visited.add(neighbor);
		                stack.push(neighbor);
		                cameFrom.put(neighbor, current);

		                double[] currCoords = townCoords.get(current);
		                double[] neighborCoords = townCoords.get(neighbor);
		                double dist = getDistance(currCoords[0], currCoords[1], neighborCoords[0], neighborCoords[1]);

		                distances.put(neighbor, distances.get(current) + dist);
		            }
		        }
		    }

	
	}
	
	
	public static void IDDFS(String start, String end) {
	    long startTime = System.nanoTime();
	    int depth = 0;
	    while (true) {
	        Set<String> visited = new HashSet<>();
	        Map<String, String> cameFrom = new HashMap<>();
	        Map<String, Double> distances = new HashMap<>();
	        distances.put(start, 0.0);

	        if (DLS(start, end, depth, visited, cameFrom, distances)) {
	            long endTime = System.nanoTime(); 
	            double elapsedTime = (endTime - startTime) / 1_000_000; 
	            System.out.printf("Time taken: %.3f ms%n", elapsedTime);
	            return;
	        }
	        depth++; 
	    }
	}
	
	
	public static boolean DLS(String current, String end, int depth, Set<String> visited, Map<String, String> cameFrom, Map<String, Double> distances) {
	    if (depth < 0) return false; 

	    visited.add(current);

	    
	    if (current.equals(end)) {
	        List<String> path = new ArrayList<>();
	        String step = end;
	        while (step != null) {
	            path.add(step);
	            step = cameFrom.get(step);
	        }
	        Collections.reverse(path);
	        System.out.println("Total Distance: " + distances.get(end) + " km");
	        System.out.println("Path: " + path);
	        return true;
	    }

	    for (String neighbor : adjTowns.getOrDefault(current, new ArrayList<>())) {
	        if (!visited.contains(neighbor)) {
	            cameFrom.put(neighbor, current);

	        
	            double[] currCoords = townCoords.get(current);
	            double[] neighborCoords = townCoords.get(neighbor);
	            double dist = getDistance(currCoords[0], currCoords[1], neighborCoords[0], neighborCoords[1]);

	            distances.put(neighbor, distances.get(current) + dist);

	            
	            if (DLS(neighbor, end, depth - 1, visited, cameFrom, distances)) {
	                return true; 
	            }
	        }
	    }
	    return false; 
	}
	
	
	private static double heuristic(String town, String end) {
	    double[] townCoordsStart = townCoords.get(town);
	    double[] townCoordsEnd = townCoords.get(end);
	    
	    if (townCoordsStart == null || townCoordsEnd == null) return Double.MAX_VALUE;

	    double dx = townCoordsEnd[0] - townCoordsStart[0];
	    double dy = townCoordsEnd[1] - townCoordsStart[1];

	    return Math.sqrt(dx * dx + dy * dy); 
	}
	
	
	public static void BestFS(String start, String end) {
	    double startTime = System.nanoTime(); 

	    PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(town -> heuristic(town, end)));
	    Set<String> visited = new HashSet<>();
	    Map<String, String> cameFrom = new HashMap<>();
	    Map<String, Double> distances = new HashMap<>();

	    priorityQueue.add(start);
	    distances.put(start, 0.0);

	    while (!priorityQueue.isEmpty()) {
	        String current = priorityQueue.poll();
	        
	        if (current.equals(end)) {
	            double endTime = System.nanoTime(); 
	            double elapsedTime = (endTime - startTime) / 1_000_000; 
	            
	            List<String> path = new ArrayList<>();
	            String step = end;
	            while (step != null) {
	                path.add(step);
	                step = cameFrom.get(step);
	            }
	            Collections.reverse(path);

	            System.out.println("Total Distance: " + distances.get(end) + " km");
	            System.out.println("Path: " + path);
	            System.out.printf("Time taken: %.3f ms%n", elapsedTime);
	            return;
	        }

	        visited.add(current);

	        for (String neighbor : adjTowns.getOrDefault(current, new ArrayList<>())) {
	            if (!visited.contains(neighbor)) {
	                double[] currCoords = townCoords.get(current);
	                double[] neighborCoords = townCoords.get(neighbor);
	                double dist = getDistance(currCoords[0], currCoords[1], neighborCoords[0], neighborCoords[1]);

	                double newDist = distances.get(current) + dist;

	                if (!distances.containsKey(neighbor) || newDist < distances.get(neighbor)) {
	                    distances.put(neighbor, newDist);
	                    cameFrom.put(neighbor, current);
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }
	    }

	}
	
	
	
	
	public static void ASearch(String start, String end) {
	    double startTime = System.nanoTime(); 
	    
	    Set<String> visited = new HashSet<>();
	    Map<String, String> cameFrom = new HashMap<>();
	    Map<String, Double> gScore = new HashMap<>();
	    Map<String, Double> fScore = new HashMap<>();

	    PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(town -> fScore.getOrDefault(town, Double.MAX_VALUE)));
	    
	    gScore.put(start, 0.0);
	    fScore.put(start, heuristic(start, end));
	    priorityQueue.add(start);

	    while (!priorityQueue.isEmpty()) {
	        String current = priorityQueue.poll();

	        if (current.equals(end)) {
	            double endTime = System.nanoTime(); // End timer
	            double elapsedTime = (endTime - startTime) / 1_000_000;
	            
	            List<String> path = new ArrayList<>();
	            String step = end;
	            while (step != null) {
	                path.add(step);
	                step = cameFrom.get(step);
	            }
	            Collections.reverse(path);

	            System.out.println("Total Distance: " + gScore.get(end) + " km");
	            System.out.println("Path: " + path);
	            System.out.printf("Time taken: %.3f ms%n", elapsedTime);
	            return;
	        }

	        visited.add(current);

	        for (String neighbor : adjTowns.getOrDefault(current, new ArrayList<>())) {
	            if (visited.contains(neighbor)) continue;

	            double[] currCoords = townCoords.get(current);
	            double[] neighborCoords = townCoords.get(neighbor);
	            double dist = getDistance(currCoords[0], currCoords[1], neighborCoords[0], neighborCoords[1]);

	            double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + dist;

	            if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
	                cameFrom.put(neighbor, current);
	                gScore.put(neighbor, tentativeGScore);
	                fScore.put(neighbor, tentativeGScore + heuristic(neighbor, end));

	                if (!priorityQueue.contains(neighbor)) {
	                    priorityQueue.add(neighbor);
	                }
	            }
	        }
	    }

	}
}






