import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        InputReader reader = new InputReader();
        List<Graph> graphs = reader.readGraphsFromJson("src/main/resources/input.json");

        PrimsAlgorithm prim = new PrimsAlgorithm();
        KruskalsAlgorithm kruskal = new KruskalsAlgorithm();
        List<OutputWriter.CombinedResult> results = new ArrayList<>();
        for (int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            PrimsAlgorithm.Result primResult = prim.computeMST(g);
            KruskalsAlgorithm.Result kruskalResult = kruskal.computeMST(g);
            OutputWriter.CombinedResult combined = new OutputWriter.CombinedResult(i + 1, g.getNodes().size(),
                    g.getEdges().size() / 2, primResult, kruskalResult);
            results.add(combined);
        }

        OutputWriter writer = new OutputWriter();
        writer.writeResultsToJson(results, "src/main/resources/test_output.json");
    }
}