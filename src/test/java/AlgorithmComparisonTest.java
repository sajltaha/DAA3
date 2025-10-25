import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmComparisonTest {
    private PrimsAlgorithm prim;
    private KruskalsAlgorithm kruskal;
    private List<Graph> sampleGraphs;

    @BeforeEach
    void setUp() throws IOException {
        prim = new PrimsAlgorithm();
        kruskal = new KruskalsAlgorithm();
        InputReader reader = new InputReader();
        sampleGraphs = reader.readGraphsFromJson("src/main/resources/input.json");
    }

    @Test
    void testCostEquality() {
        for (Graph graph : sampleGraphs) {
            PrimsAlgorithm.Result primResult = prim.computeMST(graph);
            KruskalsAlgorithm.Result kruskalResult = kruskal.computeMST(graph);
            assertEquals(primResult.totalCost, kruskalResult.totalCost, "MST costs should be identical");
        }
    }
}