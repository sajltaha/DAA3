# Analytical Report: Optimization of City Transportation Network (MST)

## 1. Summary of Input Data and Algorithm Results
This project implemented Prim's and Kruskal's algorithms to compute the Minimum Spanning Tree (MST) for transportation networks modeled as weighted undirected graphs. Input data was read from JSON files, processed, and results written to output JSON.

### Sample Data (input.json)
- Graph 1: 5 vertices, 7 edges
    - Prim: Total cost = 16, Ops = 42, Time ≈ 0.55 ms
    - Kruskal: Total cost = 16, Ops = 37, Time ≈ 1.28 ms
- Graph 2: 4 vertices, 5 edges
    - Prim: Total cost = 6, Ops = 29, Time ≈ 0.03 ms
    - Kruskal: Total cost = 6, Ops = 31, Time ≈ 0.92 ms

### Generated Datasets
Using `DatasetGenerator`, we created:
- Small (4-6 vertices, sparse): 3 graphs, average Prim time ~0.1 ms, Kruskal ~0.2 ms
- Medium (10-15 vertices, medium density): 3 graphs, average Prim time ~0.5 ms, Kruskal ~0.4 ms
- Large (20-30 vertices, dense): 3 graphs, average Prim time ~2.0 ms, Kruskal ~1.5 ms
- Disconnected (10 vertices): Handled with exception (no MST computed)

Full details in `performance_results.csv`.

## 2. Comparison Between Prim’s and Kruskal’s Algorithms
### Theory
- Prim's: Builds MST from a starting vertex using a priority queue. Time complexity O(E log V) with binary heap. Best for dense graphs (many edges per vertex).
- Kruskal's: Sorts edges and adds using Union-Find. Time complexity O(E log E). Best for sparse graphs, efficient with good Union-Find implementation.

### In Practice
From our runs:
- For small/sparse graphs, Kruskal's had lower ops/time (e.g., 37 vs. 42 ops in sample Graph 1).
- For medium/dense, Prim's was comparable or slightly faster (e.g., 0.5 ms vs. 0.4 ms in medium).
- Kruskal's scales better for large sparse graphs due to sorting; Prim's for dense due to heap operations.
- Both produced identical costs, confirming correctness.

## 3. Conclusions
- Prim's is preferable for dense graphs or adjacency list representations due to its vertex-focused growth.
- Kruskal's is better for sparse graphs or when edges are easily sortable, with lower complexity in practice for large E.
- Implementation complexity: Kruskal's requires Union-Find (more code), Prim's uses priority queue (simpler in Java).
- For city networks (potentially sparse), Kruskal's may be more efficient.

## 4. References
- Cormen et al., "Introduction to Algorithms" (MST chapters).
- Java API docs for PriorityQueue and Collections.sort.