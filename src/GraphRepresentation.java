import java.util.List;

public interface GraphRepresentation<V> {
    void addVertex(V vertex);
    void removeVertex(V vertex);
    void addEdge(V vertexFrom, V vertexTo, double weight);
    void removeEdge(V vertexFrom, V vertexTo);
    List<V> getVertexList();
    List<V> getVertexNeighbours(V vertex);
    List<Edge<V>> getEdgeList();
    boolean hasVertex(V vertex);
    boolean hasEdge(V vertexFrom, V vertexTo);
    double getEdgeWeight(V vertexFrom, V vertexTo);
    void setEdgeWeight(V vertexFrom, V vertexTo, double weight);
    int vertexCount();
    void print();
}
