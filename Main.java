import java.io.*;
import java.util.*;

class Graph {
    private final Map<String, List<Edge>> adjList = new HashMap<>();

    public void addEdge(String source, String destination, int weight) {
        adjList.computeIfAbsent(source, k -> new ArrayList<>()).add(new Edge(destination, weight));
        adjList.computeIfAbsent(destination, k -> new ArrayList<>()).add(new Edge(source, weight));
    }

    public Map<String, Integer> prim(String start) {
        Map<String, Integer> mst = new HashMap<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        Set<String> visited = new HashSet<>();

        visited.add(start);
        for (Edge edge : adjList.getOrDefault(start, new ArrayList<>())) {
            pq.add(edge);
        }

        while (!pq.isEmpty() && visited.size() < adjList.size()) {
            Edge edge = pq.poll();
            if (!visited.contains(edge.destination)) {
                visited.add(edge.destination);
                mst.put(edge.destination, edge.weight);

                for (Edge nextEdge : adjList.get(edge.destination)) {
                    if (!visited.contains(nextEdge.destination)) {
                        pq.add(nextEdge);
                    }
                }
            }
        }
        return mst;
    }
}

class Edge {
    String destination;
    int weight;

    Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

class PrimsAlgorithm {
    public static void main(String[] args) {
        Graph graph = new Graph();
        Random rand = new Random();
        String filePath = "src/resources/routes.csv";  // Adjust the path if needed
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();  // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.out.println("Skipping malformed row: " + line);
                    continue;
                }

                String source = parts[2].trim();  // "source airport"
                String destination = parts[4].trim();  // "destination airport"

                // Generate a random weight between 10 and 25 for each edge
                int weight = 10 + rand.nextInt(16);

                graph.addEdge(source, destination, weight);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        // Run Prim's algorithm from a specified starting point
        String startAirport = "JFK";  // Example starting point
        Map<String, Integer> mst = graph.prim(startAirport);

        // Print the Minimum Spanning Tree
        System.out.println("Minimum Spanning Tree from " + startAirport + ":");
        for (Map.Entry<String, Integer> entry : mst.entrySet()) {
            System.out.println("To " + entry.getKey() + ": Weight " + entry.getValue());
        }
    }
}
