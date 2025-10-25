import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceEvaluator {
    public void evaluatePerformance(String[] datasetPaths) throws IOException {
        List<String[]> csvRows = new ArrayList<>();
        csvRows.add(new String[]{"Dataset", "Algorithm", "Graph ID", "Total Cost", "Execution Time (ms)", "Operations Count"});

        for (String path : datasetPaths) {
            InputReader reader = new InputReader();
            List<Graph> graphs = reader.readGraphsFromJson("src/main/resources/" + path);

            PrimsAlgorithm prim = new PrimsAlgorithm();
            KruskalsAlgorithm kruskal = new KruskalsAlgorithm();

            for (int i = 0; i < graphs.size(); i++) {
                Graph graph = graphs.get(i);
                PrimsAlgorithm.Result primResult = prim.computeMST(graph);
                KruskalsAlgorithm.Result kruskalResult = kruskal.computeMST(graph);

                csvRows.add(new String[]{
                        path, "Prim", String.valueOf(i + 1),
                        String.valueOf(primResult.totalCost),
                        String.format("%.4f", primResult.executionTimeMs),
                        String.valueOf(primResult.operationsCount)
                });

                csvRows.add(new String[]{
                        path, "Kruskal", String.valueOf(i + 1),
                        String.valueOf(kruskalResult.totalCost),
                        String.format("%.4f", kruskalResult.executionTimeMs),
                        String.valueOf(kruskalResult.operationsCount)
                });
            }
        }

        writeToCsv(csvRows, "src/main/resources/performance_results.csv");
    }

    private void writeToCsv(List<String[]> rows, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : rows) {
                writer.append(String.join(",", row)).append("\n");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new PerformanceEvaluator().evaluatePerformance(new String[]{"small.json", "medium.json", "large.json", "disconnected.json"});
    }
}