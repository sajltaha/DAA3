import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DatasetGenerator {
    private final Random random = new Random();

    public void generateDatasets() throws IOException {
        generateAndSave("small.json", 4, 6, "sparse");
        generateAndSave("medium.json", 10, 15, "medium");
        generateAndSave("large.json", 20, 30, "dense");
        generateDisconnected("disconnected.json", 10);
    }

    private void generateAndSave(String fileName, int minVertices, int maxVertices, String density) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode graphsArray = mapper.createArrayNode();

        for (int i = 0; i < 3; i++) {
            Graph graph = generateRandomGraph(minVertices + random.nextInt(maxVertices - minVertices + 1), density);
            ObjectNode graphNode = mapper.createObjectNode();
            graphNode.put("id", i + 1);

            ArrayNode nodesArray = mapper.createArrayNode();
            for (String node : graph.getNodes()) {
                nodesArray.add(node);
            }
            graphNode.set("nodes", nodesArray);

            ArrayNode edgesArray = mapper.createArrayNode();
            Set<Edge> uniqueEdges = new HashSet<>(graph.getEdges());
            for (Edge edge : uniqueEdges) {
                if (edge.getFrom().compareTo(edge.getTo()) > 0) continue;
                ObjectNode edgeNode = mapper.createObjectNode();
                edgeNode.put("from", edge.getFrom());
                edgeNode.put("to", edge.getTo());
                edgeNode.put("weight", edge.getWeight());
                edgesArray.add(edgeNode);
            }
            graphNode.set("edges", edgesArray);

            graphsArray.add(graphNode);
        }

        root.set("graphs", graphsArray);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/" + fileName), root);
    }

    private Graph generateRandomGraph(int numVertices, String density) {
        Graph graph = new Graph();
        List<String> nodes = generateNodeLabels(numVertices);

        for (String node : nodes) {
            graph.addNode(node);
        }

        List<Edge> mstEdges = generateRandomMST(nodes);
        for (Edge edge : mstEdges) {
            graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        }

        int extraEdges = calculateExtraEdges(numVertices, density);
        for (int i = 0; i < extraEdges; i++) {
            String from = nodes.get(random.nextInt(numVertices));
            String to = nodes.get(random.nextInt(numVertices));
            if (!from.equals(to) && !hasEdge(graph, from, to)) {
                graph.addEdge(from, to, 1 + random.nextInt(20));
            }
        }

        return graph;
    }

    private List<String> generateNodeLabels(int num) {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            labels.add(Character.toString((char) ('A' + i % 26)) + (i / 26 > 0 ? (i / 26) : ""));
        }
        return labels;
    }

    private List<Edge> generateRandomMST(List<String> nodes) {
        List<Edge> mst = new ArrayList<>();
        for (int i = 0; i < nodes.size() - 1; i++) {
            mst.add(new Edge(nodes.get(i), nodes.get(i + 1), 1 + random.nextInt(20)));
        }
        for (int i = 0; i < nodes.size() / 2; i++) {
            int idx1 = random.nextInt(nodes.size());
            int idx2 = random.nextInt(nodes.size());
            if (Math.abs(idx1 - idx2) > 1) {
                mst.add(new Edge(nodes.get(idx1), nodes.get(idx2), 1 + random.nextInt(20)));
            }
        }
        return mst;
    }

    private boolean hasEdge(Graph graph, String from, String to) {
        for (Edge edge : graph.adjacencyList.getOrDefault(from, new ArrayList<>())) {
            if (edge.getTo().equals(to)) return true;
        }
        return false;
    }

    private int calculateExtraEdges(int vertices, String density) {
        return switch (density) {
            case "sparse" -> vertices;
            case "medium" -> (int) (vertices * Math.log(vertices));
            case "dense" -> (vertices * (vertices - 1) / 4);
            default -> 0;
        };
    }

    private void generateDisconnected(String fileName, int numVertices) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        ArrayNode graphsArray = mapper.createArrayNode();

        Graph graph = new Graph();
        int half = numVertices / 2;
        List<String> nodes1 = generateNodeLabels(half);
        List<String> nodes2 = generateNodeLabels(numVertices - half);
        nodes2.replaceAll(s -> "X" + s);

        Graph component1 = generateRandomGraph(half, "medium");
        Graph component2 = generateRandomGraph(numVertices - half, "medium");

        for (String node : component1.getNodes()) graph.addNode(node);
        for (String node : component2.getNodes()) graph.addNode(node);
        for (Edge edge : component1.getEdges()) graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());
        for (Edge edge : component2.getEdges()) graph.addEdge(edge.getFrom(), edge.getTo(), edge.getWeight());

        ObjectNode graphNode = mapper.createObjectNode();
        graphNode.put("id", 1);
        ArrayNode nodesArray = mapper.createArrayNode();
        for (String node : graph.getNodes()) nodesArray.add(node);
        graphNode.set("nodes", nodesArray);

        ArrayNode edgesArray = mapper.createArrayNode();
        Set<Edge> uniqueEdges = new HashSet<>(graph.getEdges());
        for (Edge edge : uniqueEdges) {
            if (edge.getFrom().compareTo(edge.getTo()) > 0) continue;
            ObjectNode edgeNode = mapper.createObjectNode();
            edgeNode.put("from", edge.getFrom());
            edgeNode.put("to", edge.getTo());
            edgeNode.put("weight", edge.getWeight());
            edgesArray.add(edgeNode);
        }
        graphNode.set("edges", edgesArray);

        graphsArray.add(graphNode);
        root.set("graphs", graphsArray);
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File("src/main/resources/" + fileName), root);
    }

    public static void main(String[] args) throws IOException {
        new DatasetGenerator().generateDatasets();
        System.out.println("Datasets generated.");
    }
}