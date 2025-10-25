import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutputWriter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Combined result to hold data for both algorithms
    public static class CombinedResult {
        public int graphId;
        public int vertices;
        public int edges;
        public PrimsAlgorithm.Result primResult;
        // Placeholder for Kruskal's result (to be added in next branch)
        // public KruskalsAlgorithm.Result kruskalResult;

        public CombinedResult(int graphId, int vertices, int edges, PrimsAlgorithm.Result primResult) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.primResult = primResult;
        }
    }

    public void writeResultsToJson(List<CombinedResult> results, String filePath) throws IOException {
        ObjectNode rootNode = objectMapper.createObjectNode();
        ArrayNode resultsArray = objectMapper.createArrayNode();

        for (CombinedResult result : results) {
            ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("graph_id", result.graphId);

            ObjectNode inputStats = objectMapper.createObjectNode();
            inputStats.put("vertices", result.vertices);
            inputStats.put("edges", result.edges);
            resultNode.set("input_stats", inputStats);

            ObjectNode primNode = createAlgorithmNode(result.primResult.mstEdges, result.primResult.totalCost,
                    result.primResult.operationsCount, result.primResult.executionTimeMs);
            resultNode.set("prim", primNode);

            // Placeholder for Kruskal's node (to be implemented later)
            ObjectNode kruskalNode = createAlgorithmNode(new ArrayList<>(), 0, 0, 0.0); // Dummy values
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
}