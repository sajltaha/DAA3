import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            InputReader reader = new InputReader();
            List<Graph> graphs = reader.readGraphsFromJson("src/main/resources/input.json");

            PrimsAlgorithm prim = new PrimsAlgorithm();
            KruskalsAlgorithm kruskal = new KruskalsAlgorithm();

            List<OutputWriter.CombinedResult> results = new ArrayList<>();
            for (int i = 0; i < graphs.size(); i++) {
                Graph graph = graphs.get(i);
                PrimsAlgorithm.Result primResult = prim.computeMST(graph);
                KruskalsAlgorithm.Result kruskalResult = kruskal.computeMST(graph);

                int vertices = graph.getNodes().size();
                int edges = graph.getEdges().size() / 2;
                results.add(new OutputWriter.CombinedResult(i + 1, vertices, edges, primResult, kruskalResult));
            }

            OutputWriter writer = new OutputWriter();
            writer.writeResultsToJson(results, "src/main/resources/output.json");

            if (args.length > 0 && args[0].equalsIgnoreCase("eval")) {
                PerformanceEvaluator evaluator = new PerformanceEvaluator();
                evaluator.evaluatePerformance(new String[]{"small.json", "medium.json", "large.json", "disconnected.json"});
                System.out.println("Performance evaluation complete. Results in performance_results.csv");
            } else {
                System.out.println("Processing complete. Results written to output.json");
            }
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Error computing MST: " + e.getMessage());
        }
    }
}