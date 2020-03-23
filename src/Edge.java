import java.util.Objects;

public class Edge<V> {
    V vertexFrom;
    V vertexTo;
    double weight;

    public Edge(V vertexFrom, V vertexTo, double weight) {
        this.vertexFrom = vertexFrom;
        this.vertexTo = vertexTo;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + vertexFrom + ", " + vertexTo + ", " + weight + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<?> edge = (Edge<?>) o;
        return Math.abs(edge.weight - weight) < 0.001 &&
                vertexFrom.equals(edge.vertexFrom) &&
                vertexTo.equals(edge.vertexTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertexFrom, vertexTo, weight);
    }
}
