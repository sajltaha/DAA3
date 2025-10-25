import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class OutputWriter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static class CombinedResult {
        public int graphId;
        public int vertices;
        public int edges;
        public PrimsAlgorithm.Result primResult;
        public KruskalsAlgorithm.Result kruskalResult;

        public CombinedResult(int graphId, int vertices, int edges, PrimsAlgorithm.Result primResult, KruskalsAlgorithm.Result kruskalResult) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
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

            ObjectNode kruskalNode = createAlgorithmNode(result.kruskalResult.mstEdges, result.kruskalResult.totalCost,
                    result.kruskalResult.operationsCount, result.kruskalResult.executionTimeMs);
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