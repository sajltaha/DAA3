import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrimsAlgorithmTest {
    private PrimsAlgorithm prim;
    private List<Graph> sampleGraphs;

    @BeforeEach
    void setUp() throws IOException {
        prim = new PrimsAlgorithm();
        InputReader reader = new InputReader();
        sampleGraphs = reader.readGraphsFromJson("src/main/resources/input.json");
    }

    @Test
    void testCorrectness() {
        for (Graph graph : sampleGraphs) {
            PrimsAlgorithm.Result result = prim.computeMST(graph);
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
        assertThrows(IllegalArgumentException.class, () -> prim.computeMST(disconnected));
    }

    @Test
    void testPerformanceConsistency() {
        for (Graph graph : sampleGraphs) {
            PrimsAlgorithm.Result result1 = prim.computeMST(graph);
            PrimsAlgorithm.Result result2 = prim.computeMST(graph);
            assertEquals(result1.totalCost, result2.totalCost, "Results should be reproducible");
            assertTrue(result1.executionTimeMs >= 0, "Execution time should be non-negative");
            assertTrue(result1.operationsCount > 0, "Operations count should be positive");
        }
    }
}