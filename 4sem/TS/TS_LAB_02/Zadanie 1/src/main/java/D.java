import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Random;

public class D {

  private final Random generator = new Random();

  public SimpleWeightedGraph<Integer, DefaultWeightedEdge> createGraph() {

    SimpleWeightedGraph<Integer, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

    for (int i = 1; i <= 20; i++) {
      g.addVertex(i);
    }

    for (int i = 1; i < 20; i++) {
      g.setEdgeWeight(g.addEdge(i, i + 1), 95);
    }

    g.setEdgeWeight(g.addEdge(1, 20), 95);
    g.setEdgeWeight(g.addEdge(1, 10), 80);
    g.setEdgeWeight(g.addEdge(5, 15), 70);

    for (int i = 0; i < 4; i++) {
      int i1 = 0;
      int i2 = 0;

      while (i1 == i2 || g.containsEdge(i1, i2) || g.containsEdge(i2, i1)) {
        i1 = randInt(1, 20);
        i2 = randInt(1, 20);
      }

      g.setEdgeWeight(g.addEdge(i1, i2), 70);
    }

    return g;

  }

  private int randInt(int min, int max) {
    return generator.nextInt((max - min) + 1) + min;
  }
}