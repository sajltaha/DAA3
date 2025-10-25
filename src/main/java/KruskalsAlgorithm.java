import java.util.*;

public class KruskalsAlgorithm {
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

    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;

        public UnionFind(Set<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public String find(String node) {
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node)));
            }
            return parent.get(node);
        }

        public void union(String node1, String node2) {
            String root1 = find(node1);
            String root2 = find(node2);
            if (root1.equals(root2)) return;

            // Union by rank
            if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
            }
        }
    }

    public Result computeMST(Graph graph) {
        long startTime = System.nanoTime();
        int opsCount = 0;

        if (!graph.isConnected()) {
            throw new IllegalArgumentException("Graph is disconnected; no MST exists.");
        }

        List<Edge> allEdges = new ArrayList<>(graph.getEdges());
        Collections.sort(allEdges);
        opsCount += allEdges.size();

        UnionFind uf = new UnionFind(new HashSet<>(graph.getNodes()));
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge edge : allEdges) {
            String root1 = uf.find(edge.getFrom());
            String root2 = uf.find(edge.getTo());
            opsCount += 2;
            if (!root1.equals(root2)) {
                uf.union(edge.getFrom(), edge.getTo());
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                opsCount += 3;
            }
        }

        double execTimeMs = (System.nanoTime() - startTime) / 1_000_000.0;
        return new Result(mstEdges, totalCost, opsCount, execTimeMs);
    }
}