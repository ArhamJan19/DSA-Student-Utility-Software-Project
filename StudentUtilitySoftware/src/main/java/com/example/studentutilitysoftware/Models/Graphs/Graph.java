package com.example.studentutilitysoftware.Models.Graphs;

import java.util.*;

public class Graph {
    private final Map<String, Map<String, Integer>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyList.putIfAbsent(vertex, new HashMap<>());
    }

    public void addEdge(String source, String destination, int weight) {
        adjacencyList.get(source).put(destination, weight);
        adjacencyList.get(destination).put(source, weight);
    }

    public void renameVertex(String oldName, String newName) {
        if (!adjacencyList.containsKey(oldName)) return;

        // Update adjacency list with the new vertex name
        Map<String, Integer> edges = adjacencyList.remove(oldName);
        adjacencyList.put(newName, edges);

        // Update other vertices referencing this vertex
        for (String vertex : adjacencyList.keySet()) {
            Map<String, Integer> neighbors = adjacencyList.get(vertex);
            if (neighbors.containsKey(oldName)) {
                int weight = neighbors.remove(oldName);
                neighbors.put(newName, weight);
            }
        }
    }

    public void updateEdge(String source, String destination, int newWeight) {
        if (!adjacencyList.containsKey(source) || !adjacencyList.get(source).containsKey(destination)) {
            throw new IllegalArgumentException("Edge does not exist between the given vertices.");
        }
        adjacencyList.get(source).put(destination, newWeight);
        adjacencyList.get(destination).put(source, newWeight);
    }

    public void removeEdge(String source, String destination) {
        if (!adjacencyList.containsKey(source) || !adjacencyList.get(source).containsKey(destination)) {
            throw new IllegalArgumentException("Edge does not exist between the given vertices.");
        }
        adjacencyList.get(source).remove(destination);
        adjacencyList.get(destination).remove(source);
    }

    public Map<String, Integer> dijkstra(String source) {
        Map<String, Integer> distances = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));
        Set<String> visited = new HashSet<>();

        // Initialize distances
        for (String vertex : adjacencyList.keySet()) {
            distances.put(vertex, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            if (!visited.add(current)) continue;

            for (Map.Entry<String, Integer> neighbor : adjacencyList.get(current).entrySet()) {
                String next = neighbor.getKey();
                int weight = neighbor.getValue();

                if (distances.get(current) + weight < distances.get(next)) {
                    distances.put(next, distances.get(current) + weight);
                    pq.add(next);
                }
            }
        }
        return distances;
    }

    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public void clear() {
        adjacencyList.clear();
    }
}
