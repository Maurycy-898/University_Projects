#include "src/graph_utils.hpp"

int main(int argc, char **argv) {

  std::vector<uint64_t> v;
  std::cout << v.max_size();
  uint64_t start_node{1};

  // Graph* g = graphFromFile("data/samples/sample1.txt", UNDIRECTED);
  Graph* g = graphFromFile("data/samples/USA-road-t.NY.gr");
  try {
    intVect_t res = g->dialAlgorithm(start_node);
    std::cout << "alg done" << std::endl;
    for (int dist : res) {
      std::cout << dist << ", " << std::endl;
    } std::cout << std::endl;
  } catch(...) {
    std::cout << "exception" << std::endl;
  }
  
  
  
  // res = g->dialAlgorithm(0);
  // for (int dist : res) {
  //   std::cout << dist << ", ";
  // } std::cout << std::endl;

  // res = g->radixHeapAlg(0);
  // for (int dist : res) {
  //   std::cout << dist << ", ";
  // } std::cout << std::endl;

  // for (int i = 1; i <= g->V; i++) {
  //   std::cout << g->dijkstraPQ(0, i) << ", ";
  // } std::cout << std::endl;

  // for (int i = 1; i <= g->V; i++) {
  //   std::cout << g->dialAlgorithm(0, i) << ", ";
  // } std::cout << std::endl;

  // for (int i = 1; i <= g->V; i++) {
  //   std::cout << g->radixHeapAlg(0, i) << ", ";
  // } std::cout << std::endl;
  
  delete g;
  
  return 0;
}
