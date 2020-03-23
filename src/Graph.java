import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Graph<V> {
    private GraphRepresentation<V> graphRepresentation;

    public Graph() {
        this.graphRepresentation = new AdjacencyListRepresentation();
    }

    public Graph(GraphRepresentation<V> representation) {
        this.graphRepresentation = representation;
    }

    void addVertex(V vertex) {
        this.graphRepresentation.addVertex(vertex);
    }

    void removeVertex(V vertex) {
        this.graphRepresentation.removeVertex(vertex);
    }

    void addEdge(V vertexFrom, V vertexTo, double weight) {
        this.graphRepresentation.addEdge(vertexFrom, vertexTo, weight);
    }

    void removeEdge(V vertexFrom, V vertexTo) {
        this.graphRepresentation.removeEdge(vertexFrom, vertexTo);
    }

    List<V> getVertexList() {
        return this.graphRepresentation.getVertexList();
    }

    List<V> getVertexNeighbours(V vertex) {
        return this.graphRepresentation.getVertexNeighbours(vertex);
    }

    List<Edge<V>> getEdgeList() {
        return this.graphRepresentation.getEdgeList();
    }

    boolean hasVertex(V vertex) {
        return this.graphRepresentation.hasVertex(vertex);
    }

    boolean hasEdge(V vertexFrom, V vertexTo) {
        return this.graphRepresentation.hasEdge(vertexFrom, vertexTo);
    }

    double getEdgeWeight(V vertexFrom, V vertexTo) {
        return this.graphRepresentation.getEdgeWeight(vertexFrom, vertexTo);
    }

    void setEdgeWeight(V vertexFrom, V vertexTo, double weight) {
        this.graphRepresentation.setEdgeWeight(vertexFrom, vertexTo, weight);
    }

    int vertexCount() {
        return this.graphRepresentation.vertexCount();
    }

    void print() {
        this.graphRepresentation.print();
    }

    public ArrayList<V> getParents(V vertex) {
        ArrayList<V> parents = new ArrayList<>();
        List<V> vertices = this.getVertexList();
        for (V currentVertex : vertices) {
            if (this.hasEdge(currentVertex, vertex)) {
                parents.add(currentVertex);
            }
        }
        return parents;
    }

    public boolean isUndirected() {
        List<Edge<V>> edgeList = this.getEdgeList();
        Set<Edge<V>> distinctEdges = new HashSet<>(edgeList);
        for (Edge<V> edge : edgeList) {
            Edge<V> flippedEdge = new Edge<>(edge.vertexTo, edge.vertexFrom, edge.weight);
            if (!distinctEdges.contains(flippedEdge)) {
                return false;
            }
        }
        return true;
    }

    public List<V> getVerticesAtDistance(V vertex, int distance) {
        List<V> verticesAtDistance = new ArrayList<>();
        if (distance == 0) {
            verticesAtDistance.add(vertex);
            return verticesAtDistance;
        }

        Set<V> visited = new HashSet<>();
        LinkedList<V> queue = new LinkedList<>();
        queue.add(vertex);
        visited.add(vertex);
        int level = 0;
        int currentLevelCount = 1;
        int nextLevelCount = 0;

        while (!queue.isEmpty()) {
            V current = queue.remove();

            List<V> neighbours = this.getVertexNeighbours(current);
            for (V neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    if (level == distance - 1) {
                        verticesAtDistance.add(neighbour);
                    } else {
                        queue.add(neighbour);
                        visited.add(neighbour);
                        nextLevelCount++;
                    }
                }
            }

            currentLevelCount--;
            if (currentLevelCount == 0) {
                level++;
                currentLevelCount = nextLevelCount;
                nextLevelCount = 0;
            }

            if (level == distance) {
                return verticesAtDistance;
            }
        }
        return verticesAtDistance;
    }

    // strongly connected
    public boolean isConnected() {
        List<V> vertices = this.getVertexList();
        Set<V> allVerticesSet = new HashSet<>(vertices);
        for (V vertex : vertices) {
            Set<V> reachedVertices = BreadthFirstSearch(vertex);
            if (!reachedVertices.equals(allVerticesSet)) {
                return false;
            }
        }
        return true;
    }

    public Set<V> BreadthFirstSearch(V vertex) {
        Set<V> reached = new HashSet<>();
        LinkedList<V> queue = new LinkedList<>();
        queue.add(vertex);
        reached.add(vertex);
        while (!queue.isEmpty()) {
            V currentVertex = queue.remove();
            List<V> currentNeighbours = this.getVertexNeighbours(currentVertex);
            for (V neighbour : currentNeighbours) {
                if (!reached.contains(neighbour)) {
                    queue.add(neighbour);
                    reached.add(neighbour);
                }
            }
        }
        return reached;
    }

    public boolean isCyclic() {
        List<V> vertices = this.getVertexList();
        Set<V> visitedVertices = new HashSet<>();
        for (int i = 0; i < vertices.size(); i++) {
            if (!visitedVertices.contains(vertices.get(i))) {
                boolean hasCycle = hasCycleReachableFrom(vertices.get(i), visitedVertices, new HashSet<>());
                if (hasCycle) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasCycleReachableFrom(V vertex, Set<V> visited, Set<V> active) {
        visited.add(vertex);
        active.add(vertex);
        List<V> neighbours = this.getVertexNeighbours(vertex);
        for (V neighbour : neighbours) {
            if (!visited.contains(neighbour)) {
                boolean hasCycle = hasCycleReachableFrom(neighbour, visited, active);
                if (hasCycle) {
                    return true;
                }
            } else if (active.contains(neighbour)) {
                return true;
            }
        }
        active.remove(vertex);
        return false;
    }

    public double shortestPathLength(V startVertex, V endVertex) {
        Path<V> shortestPath = this.getShortestPath(startVertex, endVertex);
        if (shortestPath == null) {
            throw new IllegalArgumentException("There is no path between the vertices!");
        }
        return shortestPath.getLength();
    }

    // Dijkstra's algorithm, positive weights assumed
    public Path<V> getShortestPath(V startVertex, V endVertex) {
        Dijkstra dijkstra = new Dijkstra(this.graphRepresentation);
        List<Path<V>> paths = dijkstra.getShortestPaths(startVertex);
        for (Path<V> path : paths) {
            if (path.getEndVertex() == endVertex) {
                return path;
            }
        }
        return null;
    }

    // gets longest path in terms of number of edges in the path
    public int graphDiameter() {
        if (this.isCyclic()) {
            throw new IllegalStateException("Graph is cyclic! There are infinitely long paths");
        }
        List<V> vertices = this.getVertexList();
        int currentLongestPath = 0;
        for(V startVertex : vertices) {
            int longestPathFromVertex = getVertexRadius(startVertex);
            if(longestPathFromVertex > currentLongestPath) {
                currentLongestPath = longestPathFromVertex;
            }
        }
        return currentLongestPath;
    }

    public int getVertexRadius(V vertex) {
        int currentLongest = 0;
        List<V> neighbours = this.getVertexNeighbours(vertex);
        for(V neighbour : neighbours) {
            int neighbourRadius = getVertexRadius(neighbour);
            if(neighbourRadius + 1 > currentLongest) {
                currentLongest = neighbourRadius + 1;
            }
        }
        return currentLongest;
    }

    public Graph<V> getInvertedGraph() {
        Class<? extends GraphRepresentation> representationClass = this.graphRepresentation.getClass();
        GraphRepresentation<V> newRepresentation;
        try {
            newRepresentation = representationClass.getDeclaredConstructor(null).newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
        Graph<V> newGraph = new Graph<>(newRepresentation);
        List<V> vertices = this.getVertexList();
        for(V vertex : vertices) {
            newGraph.addVertex(vertex);
        }
        List<Edge<V>> edges = this.getEdgeList();
        for(Edge<V> edge : edges) {
            newGraph.addEdge(edge.vertexTo, edge.vertexFrom, edge.weight);
        }
        return newGraph;
    }

    // achieved using Kosaraju's algorithm
    public int getStronglyConnectedComponentCount() {
        Deque<V> orderStack = new LinkedList<>();
        Set<V> visited = new HashSet<>();
        List<V> vertices = this.getVertexList();
        for(V vertex : vertices) {
            if(!visited.contains(vertex)) {
                fillStackDFS(vertex, orderStack, visited);
            }
        }
        Graph<V> invertedGraph = this.getInvertedGraph();
        int componentCount = 0;
        visited.clear();
        while(!orderStack.isEmpty()) {
            V nextVertex = orderStack.pop();
            if(!visited.contains(nextVertex)) {
                invertedGraph.depthFirstSearch(nextVertex, visited);
                componentCount++;
            }
        }
        return componentCount;
    }

    public void fillStackDFS(V vertex, Deque<V> orderStack, Set<V> visited) {
        visited.add(vertex);
        List<V> neighbours = this.getVertexNeighbours(vertex);
        for(V neighbour : neighbours) {
            if(!visited.contains(neighbour)) {
                fillStackDFS(neighbour, orderStack, visited);
            }
        }
        orderStack.push(vertex);
    }

    public void depthFirstSearch(V vertex, Set<V> visited) {
        visited.add(vertex);
        List<V> neighbours = getVertexNeighbours(vertex);
        for(V neighbour : neighbours) {
            if(!visited.contains(neighbour)) {
                depthFirstSearch(neighbour, visited);
            }
        }
    }

    public GraphRepresentation<V> getRepresentation() {
        return this.graphRepresentation;
    }

}
