#ifndef __GRAPH_UTILS_HPP__
#pragma once

#include <fstream>
#include <bits/stdc++.h>
#include <chrono>
#include "graph.hpp"

using std::time_t, std::time, std::string;
using namespace std::chrono;

enum AlgType { 
  DIJKSTRA, DIJKSTRA_SINGLE, 
  DIALS,    DIALS_SINGLE,
  RADIX,    RADIX_SINGLE,
  RADIX2,   RADIX2_SINGLE,
};

// Read sources to test on from <filename>.ss file
iList_t ssFromFile(string filename);

// Read source-destination pairs to test on from <filename>.p2p file
iPairList_t p2pFromFile(string filename);

// Read graph instance from <filename>.gr file
Graph* grFromFile(string filename, graph_T T = DIRECTED);

// Meaasure algorithm execution time in microseconds for given algorithm type
int64_t execTime(Graph* graph, AlgType algtType, uint64_t source, uint64_t target=0ULL);

// Test all shortest path algorithm on given problem instance file
void testAlgorithms(string filename, string resFilename);

#endif /* __GRAPH_UTILS_HPP__ */
