import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MstTestUtils {
    public static boolean hasCycle(List<Edge> mstEdges, List<String> nodes) {
        Graph mstGraph = buildGraphFromEdges(mstEdges, nodes);
        for (String node : nodes) {
            if (hasCycleDfs(mstGraph, node, null, new HashSet<>())) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCycleDfs(Graph graph, String node, String parent, Set<String> visited) {
        visited.add(node);
        for (Edge edge : graph.adjacencyList.getOrDefault(node, List.of())) {
            String neighbor = edge.getTo();
            if (neighbor.equals(parent)) continue;
            if (visited.contains(neighbor) || hasCycleDfs(graph, neighbor, node, visited)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isConnected(List<Edge> mstEdges, List<String> nodes) {
        Graph mstGraph = buildGraphFromEdges(mstEdges, nodes);
        if (nodes.isEmpty()) return true;
        Set<String> visited = new HashSet<>();
        dfsConnect(mstGraph, nodes.get(0), visited);
        return visited.size() == nodes.size();
    }

    private static void dfsConnect(Graph graph, String node, Set<String> visited) {
        visited.add(node);
        for (Edge edge : graph.adjacencyList.getOrDefault(node, List.of())) {
            String neighbor = edge.getTo();
            if (!visited.contains(neighbor)) {
                dfsConnect(graph, neighbor, visited);
            }
        }
    }

    private static Graph buildGraphFromEdges(List<Edge> edges, List<String> nodes) {
        Graph graph = new Graph();
        for (String node : nodes) {
            graph.addNode(node);
        }
        for (Edge edge : edges) {
            graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }
        return graph;
    }
}