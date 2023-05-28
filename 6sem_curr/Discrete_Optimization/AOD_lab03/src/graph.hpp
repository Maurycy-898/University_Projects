#ifndef __GRAPH_HPP__
#pragma once

#include <algorithm>
#include <queue>
#include <list>
#include <vector>
#include <unordered_set>
#include <string>
#include <cmath>
#include <climits>
#include <iostream>


// CONST VALUES
const uint64_t NO_PATH = INT_MAX;
const uint64_t INF_DIST = INT_MAX;
enum graph_T { DIRECTED, UNDIRECTED };

// HELPFUL DEFINED TYPES
typedef std::pair<uint64_t, uint64_t> edge_t;
typedef std::unordered_set<int> bucketSet_t;

typedef std::list<edge_t> edgeList_t;
typedef std::vector<edge_t> edgeVect_t;

typedef std::list<uint64_t> intList_t;
typedef std::vector<uint64_t> intVect_t;

typedef std::priority_queue <edge_t, edgeVect_t,
        std::greater<edge_t>> minHeap_t; // min binaary heap


// GRAPH IMPL
class Graph {
  private:
    graph_T type;
    edgeVect_t *adj;

  public:
    uint64_t V, E, W; // vertices, edges

    // creating new graph:
    void addEdge(uint64_t u, uint64_t v, uint64_t w);
    Graph(uint64_t V, uint64_t E = 0, graph_T T = DIRECTED);
    ~Graph(); 

    // shortest path algorithms:
    intVect_t dijkstraPQ(uint64_t s);
    intVect_t dialAlgorithm(uint64_t s);
    intVect_t radixHeapAlg(uint64_t s);
    
    // only to given destination
    uint64_t dijkstraPQ(uint64_t s, uint64_t d);
    uint64_t dialAlgorithm(uint64_t s, uint64_t d);
    uint64_t radixHeapAlg(uint64_t s, uint64_t d);
};


// RADIX HEAAP IMPL
typedef std::pair<uint64_t, uint64_t> range_t;
typedef std::pair<uint64_t, uint64_t> intPair_t;
const range_t NO_RANGE{-1ULL, -1ULL};

class radixBucket {
  public:
    range_t range;
    edgeList_t vtxs;
    
    radixBucket();
    radixBucket(range_t range);
    radixBucket(uint64_t from, uint64_t to);

    void addVtx(uint64_t vtx, uint64_t weight);
    void addVtx(edge_t vtx);

    void erase(edge_t vtx);
    uint64_t min();
    edge_t first();
    
    void pop_first();
    void clear();
    bool empty();
    bool inRange(uint64_t w);
    // void printBucket() {
    //   std::cout << "rng:[" << range.first << "," << range.second << "], elems:{";
    //   for (auto e : vtxs) {
    //     std::cout << e.second << "(" << e.first << "),";
    //   }
    //   std::cout << "}" << std::endl;
    // }
};

typedef std::vector<radixBucket> bucketVect_t;

class radixHeap {
  uint64_t heapSize;
  uint64_t bucketsSize;
  bucketVect_t buckets;
  intVect_t bucketMap;

  public:
    radixHeap(uint64_t V, uint64_t W);

    void push(edge_t vtx);
    void erase(edge_t vtx);
    edge_t top();

    void pop();
    bool empty();
  
  private:
    const uint64_t CHECKED = -1;
    const uint64_t ERR_IDX = -2;

    void reArrange();
    void updateRanges(uint64_t minDist, uint64_t updateRange) ;
    void initBuckets();
    uint64_t findBucket(edge_t vtx);
};

#endif /* __GRAPH_HPP__ */
