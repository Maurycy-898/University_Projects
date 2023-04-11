import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.util.ArrayList;
import java.util.Random;

public class main {

    public static void main(String[] args) {

    Random r = new Random();
    final ArrayList<DefaultWeightedEdge> edgesToRemove = new ArrayList<>();

    int counter = 0;
    int passed = 0;
    double result;
    int triesAmount = 100000;
    double totalSum = 0;

      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));
      System.out.println(r.nextInt(2));


    //A

    while (counter < triesAmount) {

      SimpleWeightedGraph<Integer, DefaultWeightedEdge> aGraph = new A().createGraph();

      aGraph.edgeSet().forEach(edge -> {

        if (r.nextInt(100) > aGraph.getEdgeWeight(edge)) {
          edgesToRemove.add(edge);
        }
      });

      edgesToRemove.forEach(aGraph::removeEdge);      //removes bad edges
      if (isCohesive(aGraph)) {
        passed++;
      }
      counter++;
      result = (double) passed / counter;
      totalSum += result;
    }

    System.out.format("1.1 average result = %.2f (with %d tries)\n", totalSum / triesAmount, triesAmount);


    //B
    edgesToRemove.clear();
    counter = 0;
    passed = 0;
    totalSum = 0;

    while (counter < triesAmount) {

      SimpleWeightedGraph<Integer, DefaultWeightedEdge> bGraph = new B().createGraph();

      bGraph.edgeSet().forEach(edge -> {

        if (r.nextInt(100) > bGraph.getEdgeWeight(edge)) {
          edgesToRemove.add(edge);
        }
      });

      edgesToRemove.forEach(bGraph::removeEdge);      //removes bad edges

      if (isCohesive(bGraph)) {
        passed++;
      }
      counter++;
      result = (double) passed / counter;
      totalSum += result;
    }
    System.out.format("1.2 average result = %.2f (with %d tries)\n", totalSum / triesAmount, triesAmount);





    //C
    edgesToRemove.clear();
    counter = 0;
    passed = 0;
    totalSum = 0;

    while (counter < triesAmount) {

      SimpleWeightedGraph<Integer, DefaultWeightedEdge> cGraph = new C().createGraph();

      cGraph.edgeSet().forEach(edge -> {

        if (r.nextInt(100) > cGraph.getEdgeWeight(edge)) {
          edgesToRemove.add(edge);
        }
      });

      edgesToRemove.forEach(cGraph::removeEdge);      //removes bad edges

      if (isCohesive(cGraph)) {
        passed++;
      }
      counter++;
      result = (double) passed / counter;
      totalSum += result;
    }
    System.out.format("1.3 average result = %.2f (with %d tries)\n", totalSum / triesAmount, triesAmount );




    //D
    edgesToRemove.clear();
    counter = 0;
    passed = 0;
    totalSum = 0;

    while (counter < triesAmount) {

      SimpleWeightedGraph<Integer, DefaultWeightedEdge> dGraph = new D().createGraph();
      dGraph.edgeSet().forEach(edge -> {

        if (r.nextInt(100) > dGraph.getEdgeWeight(edge)) {
          edgesToRemove.add(edge);
        }
      });

      edgesToRemove.forEach(dGraph::removeEdge);      //removes bad edges

      if (isCohesive(dGraph)) {
        passed++;
      }
      counter++;
      result = (double) passed / counter;
      totalSum += result;
    }
    System.out.format("1.4 average result = %.2f (with %d tries)\n", totalSum / triesAmount, triesAmount );

  }


  @SuppressWarnings("unchecked")
  private static boolean isCohesive(SimpleWeightedGraph graph) {
    return new ConnectivityInspector<>(graph).isGraphConnected();
  }


}
