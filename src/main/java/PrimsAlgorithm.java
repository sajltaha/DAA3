import java.util.*;

public class PrimsAlgorithm {
    public static class Result {
        public List<Edge> mstEdges;
        public int totalCost;
        public int operationsCount;
        public double executionTimeMs;

        public Result(List<Edge> mstEdges, int totalCost, int operationsCount, double executionTimeMs) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.operationsCount = operationsCount;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public Result computeMST(Graph graph) {
        long startTime = System.nanoTime();
        int opsCount = 0;

        if (!graph.isConnected()) {
            throw new IllegalArgumentException("Graph is disconnected; no MST exists.");
        }

        List<String> nodes = graph.getNodes();
        if (nodes.isEmpty()) {
            return new Result(new ArrayList<>(), 0, 0, 0.0);
        }

        String startNode = nodes.get(0);
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(); // Min-heap for edges
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        visited.add(startNode);
        opsCount++;

        for (Edge edge : graph.adjacencyList.get(startNode)) {
            pq.add(edge);
            opsCount += 2;
        }

        while (!pq.isEmpty() && visited.size() < nodes.size()) {
            Edge minEdge = pq.poll();
            opsCount += 2;

            String toNode = minEdge.getTo();
            if (visited.contains(toNode)) {
                continue;
            }

            visited.add(toNode);
            mstEdges.add(minEdge);
            totalCost += minEdge.getWeight();
            opsCount += 3;

            for (Edge nextEdge : graph.adjacencyList.get(toNode)) {
                String nextTo = nextEdge.getTo();
                if (!visited.contains(nextTo)) {
                    pq.add(nextEdge);
                    opsCount += 3;
                }
            }
        }

        double execTimeMs = (System.nanoTime() - startTime) / 1_000_000.0;
        return new Result(mstEdges, totalCost, opsCount, execTimeMs);
    }
}