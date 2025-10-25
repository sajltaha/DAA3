import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList;

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
        adjacencyList.get(to).add(new Edge(to, from, weight)); // Undirected graph
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

    // Simple visualization method for bonus (DOT format for graphviz)
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

    @Override
    public String toString() {
        return "Graph{nodes=" + adjacencyList.keySet() + ", edges=" + getEdges() + "}";
    }
}