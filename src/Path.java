import java.util.ArrayList;
import java.util.List;

public class Path<V> {
    private List<Edge<V>> edges;

    public Path() {
        edges = new ArrayList<>();
    }

    public Path(V vertex) {
        edges = new ArrayList<>();
        edges.add(new Edge<>(vertex, vertex, 0.0));
    }

    public Path(List<Edge<V>> edges) {
        this.edges = edges;
    }

    public V getStartVertex() {
        if(edges == null || edges.isEmpty()) {
            throw new IllegalArgumentException("No path provided!");
        }
        return edges.get(0).vertexFrom;
    }

    public V getEndVertex() {
        if(edges == null || edges.isEmpty()) {
            throw new IllegalArgumentException("No path provided!");
        }
        return edges.get(edges.size() - 1).vertexTo;
    }

    public double getLength() {
        double length = 0.0;
        for(Edge<V> edge : edges) {
            length += edge.weight;
        }
        return length;
    }

    public List<Edge<V>> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder pathString = new StringBuilder();
        for(Edge<V> edge : this.edges) {
            pathString.append(String.format("%s ~ %f ~> ", edge.vertexFrom, edge.weight, edge.vertexTo));
        }
        Edge<V> lastEdge = this.edges.get(this.edges.size() - 1);
        pathString.append(String.format("%s.", lastEdge.vertexTo));
        return pathString.toString();
    }
}
