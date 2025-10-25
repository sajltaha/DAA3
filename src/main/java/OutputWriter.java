import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OutputWriter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void writeResultsToJson(List<Result> results, String filePath) throws IOException {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode resultsArray = objectMapper.createArrayNode();

        for (int i = 0; i < results.size(); i++) {
            Result result = results.get(i);
            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("graph_id", i + 1);

            ObjectNode inputStats = objectMapper.createObjectNode();
            inputStats.put("vertices", result.vertices);
            inputStats.put("edges", result.edges);
            resultNode.set("input_stats", inputStats);

            ObjectNode primNode = createAlgorithmNode(result.primEdges, result.primCost, result.primOps, result.primTime);
            resultNode.set("prim", primNode);

            ObjectNode kruskalNode = createAlgorithmNode(result.kruskalEdges, result.kruskalCost, result.kruskalOps, result.kruskalTime);
            resultNode.set("kruskal", kruskalNode);

            resultsArray.add(resultNode);
        }

        rootNode.set("results", resultsArray);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), rootNode);
    }

    private ObjectNode createAlgorithmNode(List<Edge> edges, int totalCost, int opsCount, double execTime) {
        ObjectNode algNode = objectMapper.createObjectNode();
        ArrayNode edgesArray = objectMapper.createArrayNode();
        for (Edge edge : edges) {
            ObjectNode edgeNode = objectMapper.createObjectNode();
            edgeNode.put("from", edge.getFrom());
            edgeNode.put("to", edge.getTo());
            edgeNode.put("weight", edge.getWeight());
            edgesArray.add(edgeNode);
        }
        algNode.set("mst_edges", edgesArray);
        algNode.put("total_cost", totalCost);
        algNode.put("operations_count", opsCount);
        algNode.put("execution_time_ms", execTime);
        return algNode;
    }

    // Temporary placeholder class for results (will be refined later)
    public static class Result {
        public int vertices;
        public int edges;
        public List<Edge> primEdges;
        public int primCost;
        public int primOps;
        public double primTime;
        public List<Edge> kruskalEdges;
        public int kruskalCost;
        public int kruskalOps;
        public double kruskalTime;
    }
}