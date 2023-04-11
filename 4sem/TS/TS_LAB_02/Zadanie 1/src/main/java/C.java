import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

class C {

  SimpleWeightedGraph<Integer, DefaultWeightedEdge> createGraph() {

    SimpleWeightedGraph<Integer, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    for (int i = 1; i <= 20; i++) {
      graph.addVertex(i);
    }

    for (int i = 1; i < 20; i++) {
      graph.setEdgeWeight(graph.addEdge(i, i + 1), 95);
    }

    graph.setEdgeWeight(graph.addEdge(1,10),80);
    graph.setEdgeWeight(graph.addEdge(5,15),70);
    graph.setEdgeWeight(graph.addEdge(1, 20), 95);

    return graph;
  }
}
