import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read input data
            InputReader reader = new InputReader();
            List<Graph> graphs = reader.readGraphsFromJson("src/main/resources/input.json");

            // Initialize algorithms
            PrimsAlgorithm prim = new PrimsAlgorithm();
            KruskalsAlgorithm kruskal = new KruskalsAlgorithm();

            // Process each graph and collect results
            List<OutputWriter.CombinedResult> results = new ArrayList<>();
            for (int i = 0; i < graphs.size(); i++) {
                Graph graph = graphs.get(i);
                PrimsAlgorithm.Result primResult = prim.computeMST(graph);
                KruskalsAlgorithm.Result kruskalResult = kruskal.computeMST(graph);

                // Calculate input stats
                int vertices = graph.getNodes().size();
                int edges = graph.getEdges().size() / 2; // Undirected, edges are duplicated

                // Create combined result
                OutputWriter.CombinedResult combinedResult = new OutputWriter.CombinedResult(
                        i + 1, vertices, edges, primResult, kruskalResult
                );
                results.add(combinedResult);
            }

            // Write output to JSON
            OutputWriter writer = new OutputWriter();
            writer.writeResultsToJson(results, "src/main/resources/result.json");

            System.out.println("Processing complete. Results written to result.json");
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error computing MST: " + e.getMessage());
        }
    }
}