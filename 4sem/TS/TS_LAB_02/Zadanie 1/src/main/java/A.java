import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

class A {
  SimpleWeightedGraph<Integer, DefaultWeightedEdge> createGraph() {
    SimpleWeightedGraph<Integer, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    for (int i = 1; i <= 20; i++) {
      g.addVertex(i);
    }
    for (int i = 1; i < 20; i++) {
      g.setEdgeWeight(g.addEdge(i, i + 1), 95);
    }
    return g;
  }


}