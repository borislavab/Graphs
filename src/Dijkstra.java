import java.util.*;

// not thread safe
public class Dijkstra<V> {
    private GraphRepresentation<V> graph;
    private V startVertex;
    private PriorityQueue<ReachableVertex<V>> reachableVertices;
    private Map<V, ReachableVertex<V>> reachedVertices;

    public Dijkstra(GraphRepresentation<V> graphRepresentation) {
        this.graph = graphRepresentation;
    }

    public List<Path<V>> getShortestPaths(V startVertex) {
        this.startVertex = startVertex;
        this.reachableVertices = new PriorityQueue<>();
        this.reachedVertices = new HashMap<>();
        this.reachableVertices.add(new ReachableVertex<>(startVertex, startVertex, 0.0));
        while(!this.reachableVertices.isEmpty()) {
            ReachableVertex<V> closest = this.reachableVertices.poll();
            V vertex = closest.vertex;
            if(this.reachedVertices.containsKey(vertex)) {
                continue;
            }
            this.reachedVertices.put(vertex, closest);
            List<V> neighbours = this.graph.getVertexNeighbours(vertex);
            for(V neighbour : neighbours) {
                if(this.reachedVertices.get(neighbour) != null) {
                    continue;
                }
                double price = closest.price + this.graph.getEdgeWeight(vertex, neighbour);
                this.reachableVertices.add(new ReachableVertex<>(neighbour, vertex, price));
            }
        }
        return constructPaths();
    }

    private List<Path<V>> constructPaths() {
        List<Path<V>> paths = new ArrayList<>();
        for(Map.Entry<V, ReachableVertex<V>> entry : reachedVertices.entrySet()) {
            Path<V> newPath = constructPathToVertex(entry.getKey());
            paths.add(newPath);
        }
        return paths;
    }

    private Path<V> constructPathToVertex(V endVertex) {
        if(endVertex == this.startVertex) {
            return new Path<>(this.startVertex);
        }

        List<Edge<V>> listOfEdges = new LinkedList<>();
        V currentVertex = endVertex;
        while(currentVertex != this.startVertex) {
            ReachableVertex<V> vertexEntry = this.reachedVertices.get(currentVertex);
            V parent = vertexEntry.parent;
            double weight = this.graph.getEdgeWeight(parent, currentVertex);
            Edge<V> newEdge = new Edge<>(parent, currentVertex, weight);
            listOfEdges.add(newEdge);
            currentVertex = parent;
        }

        Collections.reverse(listOfEdges);
        return new Path(listOfEdges);
    }

    private static class ReachableVertex<V> implements Comparable<ReachableVertex<V>> {
        V vertex;
        V parent;
        double price;

        ReachableVertex(V vertex, V parent, double price) {
            this.vertex = vertex;
            this.parent = parent;
            this.price = price;
        }

        @Override
        public int compareTo(ReachableVertex<V> other) {
            return Double.compare(price, other.price);
        }
    }
}
