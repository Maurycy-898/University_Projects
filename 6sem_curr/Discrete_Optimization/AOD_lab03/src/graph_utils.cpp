// Graph Utils implementation
#include "graph_utils.hpp"

Graph* graphFromFile(std::string filename, graph_T T) {
  Graph* graph = NULL;
  int nodes, edges;
  bool paramsGiven = false;

  std::fstream file(filename);
  if (file.is_open()) {
    std::string dump;
    char lineType = 'c';
    while (file >> lineType) {
      switch (lineType) {
        case 'p': { // read params
          if (paramsGiven) { std::getline(file, dump); break; }
          file >> dump; // ski problem type (is always shortest path)
          file >> nodes; // read nodes
          file >> edges; // read edges
          graph = new Graph(nodes, edges, T); // init graph
          std::getline(file, dump); // dump rest of the line
          paramsGiven = true; break;
        }
        case 'a': { // read edges
          int u, v, w; // from, to, weight
          file >> u >> v >> w; // read data
          graph->addEdge(u, v, w); // add edge
          std::getline(file, dump);// dump rest of the line
          break;
        }
        default: // skip line
          std::getline(file, dump);
          break;
        }
    }
    file.close();
  }

  return graph;
}

time_t executionTime(std::string filename, Graph* graph, AlgType algtType, int source, int target) {
  // time_t start, end;

  switch (algtType) {
    case DIJKSTRA: {

    }
    case DIJKSTRA_SINGLE: {

    }
    case DIALS: {

    }
    case DIALS_SINGLE: {

    }
    case RADIX: {

    }
    case RADIX_SINGLE: {

    }
  }

  return 0LL;
}
