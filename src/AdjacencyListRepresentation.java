import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AdjacencyListRepresentation<V> implements GraphRepresentation<V> {

    private static class NeighbourVertex {
        int vertexIndex;
        double weight;

        NeighbourVertex(int vertexIndex, double weight) {
            this.vertexIndex = vertexIndex;
            this.weight = weight;
        }
    }

    private ArrayList<ArrayList<NeighbourVertex>> adjacencyList;
    private ArrayList<V> vertices;
    private HashMap<V, Integer> vertexIndexes;

    public AdjacencyListRepresentation() {
        this.adjacencyList = new ArrayList<>();
        this.vertices = new ArrayList<>();
        this.vertexIndexes = new HashMap<>();
    }

    @Override
    public void addVertex(V vertex) {
        if (this.hasVertex(vertex)) {
            throw new IllegalArgumentException("Vertex already exists!");
        }
        this.vertices.add(vertex);
        this.vertexIndexes.put(vertex, this.vertices.size() - 1);
        this.adjacencyList.add(new ArrayList<>());
    }

    @Override
    public void removeVertex(V vertex) {
        if (!this.hasVertex(vertex)) {
            return;
        }

        int index = this.vertexIndexes.get(vertex);

        this.adjacencyList.remove(index);
        this.vertices.remove(index);

        for(int i = index; i < this.vertices.size(); i++) {
            this.vertexIndexes.put(this.vertices.get(i), i);
        }
        this.vertexIndexes.remove(vertex);

        for(ArrayList<NeighbourVertex> vertexNeighbours : this.adjacencyList) {
            for(int i = 0; i < vertexNeighbours.size(); i++) {
                NeighbourVertex currentNeighbour = vertexNeighbours.get(i);
                if (currentNeighbour.vertexIndex == index) {
                    vertexNeighbours.remove(i);
                    i--;
                } else if (currentNeighbour.vertexIndex > index){
                    currentNeighbour.vertexIndex -= 1;
                }
            }
        }
    }

    @Override
    public void addEdge(V vertexFrom, V vertexTo, double weight) {
        if (!this.hasVertex(vertexFrom) || !this.hasVertex(vertexTo)) {
            throw new IllegalArgumentException("Not both vertices exist!");
        }

        if(this.hasEdge(vertexFrom, vertexTo)) {
            throw new IllegalArgumentException("Edge already exists!");
        }

        int indexFrom = this.vertexIndexes.get(vertexFrom);
        int indexTo = this.vertexIndexes.get(vertexTo);

        NeighbourVertex newNeighbour = new NeighbourVertex(indexTo, weight);
        this.adjacencyList.get(indexFrom).add(newNeighbour);
    }

    @Override
    public void removeEdge(V vertexFrom, V vertexTo) {
        if (!this.hasVertex(vertexFrom) || !this.hasVertex(vertexTo) || !this.hasEdge(vertexFrom, vertexTo)) {
            return;
        }

        int indexFrom = this.vertexIndexes.get(vertexFrom);
        int indexTo = this.vertexIndexes.get(vertexTo);

        ArrayList<NeighbourVertex> neighbourList = this.adjacencyList.get(indexFrom);
        int i = 0;
        NeighbourVertex currentNeighbour = neighbourList.get(0);

        while(currentNeighbour.vertexIndex != indexTo) {
            i++;
            currentNeighbour = neighbourList.get(i);
        }

        neighbourList.remove(i);
    }

    @Override
    public List<V> getVertexList() {
        return (List<V>)this.vertices.clone();
    }

    @Override
    public List<V> getVertexNeighbours(V vertex) {
        if(!this.hasVertex(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist!");
        }
        int vertexIndex = this.vertexIndexes.get(vertex);
        ArrayList<NeighbourVertex> neighbours = this.adjacencyList.get(vertexIndex);
        return neighbours.stream()
                .map(neighbourVertex -> neighbourVertex.vertexIndex)
                .map(idx -> this.vertices.get(idx))
                .collect(Collectors.toList());
    }

    @Override
    public List<Edge<V>> getEdgeList() {
        List<Edge<V>> edges = new ArrayList<>();
        for(int i = 0; i < this.adjacencyList.size(); i++) {
            V vertexFrom = this.vertices.get(i);
            for(NeighbourVertex neighbour : this.adjacencyList.get(i)) {
                V vertexTo = this.vertices.get(neighbour.vertexIndex);
                edges.add(new Edge<V>(vertexFrom, vertexTo, neighbour.weight));
            }
        }
        return edges;
    }

    @Override
    public boolean hasVertex(V vertex) {
        return this.vertexIndexes.get(vertex) != null;
    }

    @Override
    public boolean hasEdge(V vertexFrom, V vertexTo) {
        return this.getEdge(vertexFrom, vertexTo) != null;
    }

    @Override
    public double getEdgeWeight(V vertexFrom, V vertexTo) {
        NeighbourVertex neighbour = this.getEdge(vertexFrom, vertexTo);
        if(neighbour == null) {
            throw new IllegalArgumentException("Edge does not exist!");
        }
        return neighbour.weight;
    }

    @Override
    public void setEdgeWeight(V vertexFrom, V vertexTo, double weight) {
        NeighbourVertex neighbour = this.getEdge(vertexFrom, vertexTo);
        if(neighbour == null) {
            throw new IllegalArgumentException("Edge does not exist!");
        }
        neighbour.weight = weight;
    }

    @Override
    public int vertexCount() {
        return this.vertices.size();
    }

    @Override
    public void print() {
        System.out.println("Printing graph...");
        for (int i = 0; i < this.adjacencyList.size(); i++) {
            V vertexFrom = this.vertices.get(i);
            System.out.print(vertexFrom + " -> ");
            for(NeighbourVertex neighbour : this.adjacencyList.get(i)) {
                V vertexTo = this.vertices.get(neighbour.vertexIndex);
                double weight = neighbour.weight;
                System.out.print("(vertex: " + vertexTo + ", weight: " + weight + "); ");
            }
            System.out.println();
        }
        System.out.println("End of graph");
    }

    private NeighbourVertex getEdge(V vertexFrom, V vertexTo) {
        if(!this.hasVertex(vertexFrom) || !this.hasVertex(vertexTo)) {
            throw new IllegalArgumentException("Not both vertices exist!");
        }
        int indexFrom = this.vertexIndexes.get(vertexFrom);
        int indexTo = this.vertexIndexes.get(vertexTo);

        ArrayList<NeighbourVertex> neighbours = this.adjacencyList.get(indexFrom);
        for(NeighbourVertex neighbour : neighbours) {
            if(neighbour.vertexIndex == indexTo) {
                return neighbour;
            }
        }
        return null;
    }
}
