import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputReader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Graph> readGraphsFromJson(String filePath) throws IOException {
        List<Graph> graphs = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(new File(filePath));
        JsonNode graphsNode = rootNode.get("graphs");

        if (graphsNode != null && graphsNode.isArray()) {
            for (JsonNode graphNode : graphsNode) {
                Graph graph = new Graph();
                JsonNode nodesNode = graphNode.get("nodes");
                if (nodesNode != null && nodesNode.isArray()) {
                    for (JsonNode node : nodesNode) {
                        graph.addNode(node.asText());
                    }
                }
                JsonNode edgesNode = graphNode.get("edges");
                if (edgesNode != null && edgesNode.isArray()) {
                    for (JsonNode edgeNode : edgesNode) {
                        String from = edgeNode.get("from").asText();
                        String to = edgeNode.get("to").asText();
                        int weight = edgeNode.get("weight").asInt();
                        graph.addEdge(from, to, weight);
                    }
                }
                graphs.add(graph);
            }
        }
        return graphs;
    }
}