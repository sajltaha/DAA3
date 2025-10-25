import java.util.*;

public class Graph {
    public final Map<String, List<Edge>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        addNode(from);
        addNode(to);
        adjacencyList.get(from).add(new Edge(from, to, weight));
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }

    public List<String> getNodes() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    public List<Edge> getEdges() {
        Set<Edge> edges = new HashSet<>();
        for (List<Edge> edgeList : adjacencyList.values()) {
            edges.addAll(edgeList);
        }
        return new ArrayList<>(edges);
    }

    public String toDotFormat() {
        StringBuilder dot = new StringBuilder("graph G {\n");
        for (String node : adjacencyList.keySet()) {
            dot.append("  ").append(node).append(";\n");
        }
        for (Edge edge : getEdges()) {
            dot.append("  ").append(edge.getFrom()).append(" -- ").append(edge.getTo())
                    .append(" [label=").append(edge.getWeight()).append("];\n");
        }
        dot.append("}");
        return dot.toString();
    }

    public boolean isConnected() {
        if (adjacencyList.isEmpty()) return true;
        Set<String> visited = new HashSet<>();
        dfs(getNodes().get(0), visited);
        return visited.size() == adjacencyList.size();
    }

    private void dfs(String node, Set<String> visited) {
        visited.add(node);
        for (Edge edge : adjacencyList.get(node)) {
            String neighbor = edge.getTo();
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited);
            }
        }
    }

    @Override
    public String toString() {
        return "Graph{nodes=" + adjacencyList.keySet() + ", edges=" + getEdges() + "}";
    }
}