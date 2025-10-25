import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KruskalsAlgorithmTest {
    private KruskalsAlgorithm kruskal;
    private List<Graph> sampleGraphs;

    @BeforeEach
    void setUp() throws IOException {
        kruskal = new KruskalsAlgorithm();
        InputReader reader = new InputReader();
        sampleGraphs = reader.readGraphsFromJson("src/main/resources/input.json");
    }

    @Test
    void testCorrectness() {
        for (Graph graph : sampleGraphs) {
            KruskalsAlgorithm.Result result = kruskal.computeMST(graph);
            assertEquals(graph.getNodes().size() - 1, result.mstEdges.size(), "MST should have V-1 edges");
            assertFalse(MstTestUtils.hasCycle(result.mstEdges, graph.getNodes()), "MST should be acyclic");
            assertTrue(MstTestUtils.isConnected(result.mstEdges, graph.getNodes()), "MST should connect all nodes");
        }
    }

    @Test
    void testDisconnectedGraph() {
        Graph disconnected = new Graph();
        disconnected.addNode("A");
        disconnected.addNode("B"); // No edges
        assertThrows(IllegalArgumentException.class, () -> kruskal.computeMST(disconnected));
    }

    @Test
    void testPerformanceConsistency() {
        for (Graph graph : sampleGraphs) {
            KruskalsAlgorithm.Result result1 = kruskal.computeMST(graph);
            KruskalsAlgorithm.Result result2 = kruskal.computeMST(graph);
            assertEquals(result1.totalCost, result2.totalCost, "Results should be reproducible");
            assertTrue(result1.executionTimeMs >= 0, "Execution time should be non-negative");
            assertTrue(result1.operationsCount > 0, "Operations count should be positive");
        }
    }
}