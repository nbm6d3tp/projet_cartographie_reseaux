package fr.internetcartographier.util.shortestpath;

import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.util.path.Path;

import java.util.*;

/**
 * Breadth-First Search (BFS) algorithm for finding the minimum path between two
 * nodes in an Internet graph.
 * The class provides a static method, `getMinPath`, which calculates the
 * minimum path using BFS.
 * The resulting path includes a list of nodes and the total distance of the
 * path.
 */
public class BreadthFirstSearch {

    /**
     * Calculates the minimum path between the source and destination nodes using
     * Breadth-First Search.
     *
     * @param source      The IP address of the source node.
     * @param destination The IP address of the destination node.
     * @param graph       The Internet graph.
     * @return A `Path` object representing the minimum path and its
     * total distance.
     */
    public static Path<String> getMinPath(String source, String destination, InternetGraph graph) {
        if (source.equals(destination)) {
            return new Path<>(List.of(source), 0);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();
        Map<String, Boolean> spSet = new HashMap<>();

        bfs(source, destination, graph, predecessors, distances, spSet);

        LinkedList<String> path = new LinkedList<>();
        String crawl = destination;

        path.add(crawl);

        if (predecessors.get(crawl).isEmpty()) {
            path.add(source);
        } else {
            while (!predecessors.get(crawl).isEmpty()) {
                path.add(predecessors.get(crawl));
                crawl = predecessors.get(crawl);
            }
        }

        Collections.reverse(path);

        return new Path<>(path, distances.get(destination));
    }

    /**
     * Helper method for the Breadth-First Search algorithm.
     * Performs BFS traversal to find the minimum path.
     *
     * @param source       The source node's IP address.
     * @param destination  The destination node's IP address.
     * @param graph        The Internet graph.
     * @param predecessors Map to store predecessors of nodes.
     * @param distances    Map to store distances from the source node.
     * @param spSet        Map to keep track of visited nodes.
     */
    private static void bfs(String source, String destination, InternetGraph graph, Map<String, String> predecessors,
                            Map<String, Double> distances, Map<String, Boolean> spSet) {
        LinkedList<String> queue = new LinkedList<>();

        for (String ipAddress : graph.getAdjacencyList().keySet()) {
            distances.put(ipAddress, Double.MAX_VALUE);
            predecessors.put(ipAddress, "");
            spSet.put(ipAddress, false);
        }

        distances.replace(source, 0.0);
        spSet.replace(source, true);
        queue.add(source);

        while (!queue.isEmpty()) {
            String u = queue.remove();

            for (Node nodeNeighbor : graph.getNeighbors(u)) {
                String neighbor = nodeNeighbor.getStringIpAddress();

                if (!spSet.get(neighbor)) {
                    spSet.replace(neighbor, true);
                    distances.replace(neighbor, distances.get(u) + 1);
                    predecessors.replace(neighbor, u);
                    queue.add(neighbor);

                    if (neighbor.equals(destination)) {
                        return;
                    }
                }
            }
        }
    }

}
