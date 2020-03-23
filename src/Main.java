import java.util.List;

public class Main {

    public static void main(String[] args) {
        AdjacencyListRepresentation<Integer> representation = new AdjacencyListRepresentation<>();
        representation.addVertex(5);
        representation.addVertex(100);
        representation.addVertex(56);

        representation.addEdge(5, 100, 3);
        representation.addEdge(56,5,8);

        representation.print();

        representation.removeVertex(5);
        representation.addEdge(56, 100, 3.6);
        representation.addVertex(3);
        representation.addEdge(3, 56, 7);

        representation.print();

        representation.removeEdge(56, 100);
        List<Edge<Integer>> edgeList = representation.getEdgeList();
        List<Integer> vertexList = representation.getVertexList();
        System.out.println(edgeList);
        System.out.println(vertexList);

        System.out.println(representation.getVertexNeighbours(3));
        System.out.println(representation.getEdgeWeight(3, 56));
        System.out.println(representation.hasEdge(56, 100));
        System.out.println(representation.hasEdge(3, 56));
        System.out.println(representation.hasVertex(5));
        System.out.println(representation.hasVertex(56));
        representation.setEdgeWeight(3, 56, 8);
        System.out.println(representation.getEdgeWeight(3, 56));

        Graph<Integer> graph = new Graph<>(representation);
        graph.print();
        System.out.println("--------------");
        System.out.println("Print inverted graph");
        Graph<Integer> invertedGraph = graph.getInvertedGraph();
        invertedGraph.print();

        System.out.println(graph.getStronglyConnectedComponentCount());
        graph.addEdge(56, 3, 101);
        System.out.println(graph.getStronglyConnectedComponentCount());

        Graph<Integer> graph2 = new Graph<>();
        graph2.addVertex(1);
        graph2.addVertex(2);
        graph2.addVertex(3);
        graph2.addVertex(4);
        graph2.addVertex(5);
        graph2.addVertex(6);
        graph2.addVertex(7);

        graph2.addEdge(1,2,5);
        graph2.addEdge(1,3,1);
        graph2.addEdge(2,4,2);
        graph2.addEdge(5,6,8);
        graph2.addEdge(3,4,7);
        graph2.addEdge(3,2,1);
        graph2.addEdge(2,7,1);
        graph2.addEdge(1,7,4);

        Dijkstra<Integer> dijkstra = new Dijkstra<>(graph2.getRepresentation());
        List<Path<Integer>> shortestPaths = dijkstra.getShortestPaths(1);
        System.out.println("Printing shortest paths:");
        for(Path<Integer> path : shortestPaths) {
            System.out.println(path);
        }

        System.out.printf("Graph is undirected: %b\n", graph2.isUndirected());
        System.out.printf("Graph is cyclic: %b\n", graph2.isCyclic());
        System.out.printf("Graph is connected: %b\n", graph2.isConnected());
        System.out.printf("Number of components: %d\n", graph2.getStronglyConnectedComponentCount());
        System.out.printf("Graph diameter: %d\n", graph2.graphDiameter());
        List<Integer> verticesAtDistance = graph2.getVerticesAtDistance(1, 1);
        System.out.printf("Vertices at distance %d from %d: %s\n", 1, 1, verticesAtDistance);
    }
}
