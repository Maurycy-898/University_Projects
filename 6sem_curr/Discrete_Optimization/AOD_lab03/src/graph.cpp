// Graph implementation
#include "graph.hpp"

// =============== GRAPH CLASS =============================
Graph::Graph(uint64_t V, uint64_t E, graph_T T) {
  this->V = V, this->E = E, this->type = T; this->W = 0;
  this->adj = new edgeVect_t[V + 1];
}

Graph::~Graph() { 
  delete[] this->adj; 
}

void Graph::addEdge(uint64_t u, uint64_t v, uint64_t w) {
  if (this->type == DIRECTED) {
    adj[u].push_back({w, v});
  } else { // UNDIRECTED
    adj[u].push_back({w, v});
    adj[v].push_back({w, u});
  }
  if (w > this->W) this->W = w;
}

// ------ SHORTEST PATH METHODS ------
// DIJKSTRA (STL priority queue - binary heap)
intVect_t Graph::dijkstraPQ(uint64_t s) {
  minHeap_t minPQ{ }; // priority queue
  intVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    minPQ.pop();

    for (auto adjEdge : adj[v]) {
      uint64_t w = adjEdge.first; // weight
      uint64_t u = adjEdge.second; // adjacent

      if (dists[u] > dists[v] + w) {
        dists[u] = dists[v] + w;
        minPQ.push({dists[u], u});
      }
    }
  } 
  // return list of distances
  return dists;
}

uint64_t Graph::dijkstraPQ(uint64_t s, uint64_t d) {
  minHeap_t minPQ{ }; // priority queue
  intVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    if (v == d) return dists[v];
    
    minPQ.pop();
    for (auto edge : adj[v]) {
      uint64_t w = edge.first; // weight
      uint64_t u = edge.second; // adjacent

      if (dists[u] > dists[v] + w) {
        dists[u] = dists[v] + w;
        minPQ.push({dists[u], u});
      }
    }
  }
  // when no path found to destination 
  return NO_PATH;
}

// DIAL ALGORITHM
intVect_t Graph::dialAlgorithm(uint64_t s) {
  // const unsigned long long maxBuckets = W*V + 1;
  const uint64_t maxBuckets = W*V + 1;
  const uint64_t bCycleSize = W + 1;

  intVect_t dists(V + 1, INF_DIST);
  bucketSet_t buckets[bCycleSize];

  dists[s] = 0;
  buckets[0].insert(s);

  uint64_t bucketIdx = 0;
  uint64_t bCycleIdx = 0;
  while (true) {
    while (bucketIdx < maxBuckets && buckets[bCycleIdx].empty()) {
      bucketIdx++;
      bCycleIdx = (bCycleIdx + 1) % (bCycleSize);
    } if (bucketIdx >= maxBuckets) break;

    uint64_t v = *(buckets[bCycleIdx].begin());
    buckets[bCycleIdx].erase(v);

    for (auto adjEdge : adj[v]) {
      uint64_t w = adjEdge.first;  // weight 
      uint64_t u = adjEdge.second; // adjacent
      
      uint64_t altDist = dists[v] + w;
      uint64_t currentDist = dists[u]; 

      if (altDist < currentDist) {
        if (currentDist != INF_DIST) {
          buckets[currentDist % bCycleSize].erase(u);
        }
        dists[u] = altDist;
        buckets[altDist % bCycleSize].insert(u);
      }
    }
  }
  // return list of distances
  return dists;
}

uint64_t Graph::dialAlgorithm(uint64_t s, uint64_t d) {
  const uint64_t maxBuckets = W*V + 1;
  const uint64_t bCycleSize = W   + 1;

  intVect_t dists(V + 1, INF_DIST);
  bucketSet_t buckets[bCycleSize];

  dists[s] = 0;
  buckets[0].insert(s);

  uint64_t bucketIdx = 0;
  uint64_t bCycleIdx = 0;
  while (true) {
    while (bucketIdx < maxBuckets && buckets[bCycleIdx].empty()) {
      bucketIdx++;
      bCycleIdx = (bCycleIdx + 1) % (bCycleSize);
    } if (bucketIdx >= maxBuckets) break;

    uint64_t v = *(buckets[bCycleIdx].begin());
    buckets[bCycleIdx].erase(v);
    if (v == d) return dists[v];
    
    for (auto adjEdge : adj[v]) {
      uint64_t w = adjEdge.first;  // weight 
      uint64_t u = adjEdge.second; // adjacent
      
      uint64_t altDist = dists[v] + w;
      uint64_t currentDist = dists[u]; 

      if (altDist < currentDist) {
        if (currentDist != INF_DIST) {
          buckets[currentDist % bCycleSize].erase(u);
        }
        dists[u] = altDist;
        buckets[altDist % bCycleSize].insert(u);
      }
    }
  }
  return NO_PATH;
}

// RADIX HEAP
intVect_t Graph::radixHeapAlg(uint64_t s) {
  const uint64_t heapSize = ceil(log2(W*V)) + 1;
  radixHeap minPQ(V, heapSize); // priority queue
  intVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    minPQ.pop();

    for (auto vtx : adj[v]) {
      uint64_t w = vtx.first; // weight
      uint64_t u = vtx.second; // adjacent

      if (dists[u] > dists[v] + w) {
        if (dists[u] != INF_DIST) {
          minPQ.erase({dists[u], u});
        }
        dists[u] = dists[v] + w;
        minPQ.push({dists[u], u});
      }
    }
  } 
  // return list of distances
  return dists;
}

uint64_t Graph::radixHeapAlg(uint64_t s, uint64_t d) {
  const uint64_t heapSize = ceil(log2(W*V)) + 1;
  radixHeap minPQ(V, heapSize); // priority queue
  intVect_t dists(V + 1, INF_DIST); // distances
  
  dists[s] = 0;
  minPQ.push({0, s});
  
  while (!minPQ.empty()) {
    uint64_t v = minPQ.top().second;
    if (v == d) return dists[v];
    
    minPQ.pop();
    for (auto vtx : adj[v]) {
      uint64_t w = vtx.first; // weight
      uint64_t u = vtx.second; // adjacent

      if (dists[u] > dists[v] + w) {
        if (dists[u] != INF_DIST) {
          minPQ.erase({dists[u], u});
        }
        dists[u] = dists[v] + w;
        minPQ.push({dists[u], u});
      }
    }
  } 
  // when no path found to destination 
  return NO_PATH;
}


// =============== RADIX HEAP CLASS =============================
// ------ RADIX BUCKET ------
radixBucket::radixBucket() { /* no range */ }

radixBucket::radixBucket(range_t range) {
  this->range = range;
}

radixBucket::radixBucket(uint64_t from, uint64_t to) {
  this->range = {from, to};
}

void radixBucket::addVtx(uint64_t vtx, uint64_t weight) {
  this->vtxs.push_back({weight, vtx});
}

void radixBucket::addVtx(edge_t vtx) {
  this->vtxs.push_back(vtx);
}

void radixBucket::erase(edge_t vtx) {
  for (auto it = this->vtxs.begin(); it != this->vtxs.end(); ++it) 
    if (it->second == vtx.second) it = this->vtxs.erase(it);
}

uint64_t radixBucket::min() {
  uint64_t min = INF_DIST;
  for (auto vtx : this->vtxs) {
    if (vtx.first < min) min = vtx.first;
  }
  return min;
}

edge_t radixBucket::first() {
  return this->vtxs.front();
}

void radixBucket::pop_first() {
  if (!this->empty())
    this->vtxs.pop_front();
}

bool radixBucket::empty() {
  return this->vtxs.empty();
}

void radixBucket::clear() {
  this->vtxs.clear();
}

bool radixBucket::inRange(uint64_t w) {
  if (this->range.first <= w) {
    if (this->range.second >= w) 
      return true;
  } return false;
}

// ------ RADIX HEAP ------
radixHeap::radixHeap(uint64_t V, uint64_t size) {
  this->bucketsSize = size;
  this->buckets.resize(size);
  this->bucketMap.resize(V + 1);
  this->initBuckets();
}

void radixHeap::push(edge_t vtx) {
  uint64_t bucketIdx = this->findBucket(vtx);
  this->bucketMap[vtx.second] = bucketIdx;
  this->buckets[bucketIdx].addVtx(vtx);
  this->heapSize++;
}

void radixHeap::erase(edge_t vtx) {
  uint64_t bucketIdx = this->bucketMap[vtx.second];
  this->buckets[bucketIdx].erase(vtx);
  this->bucketMap[vtx.second] = CHECKED;
  if (this->heapSize > 0) this->heapSize--;
}

edge_t radixHeap::top() {
  return this->buckets.front().first();
}

void radixHeap::pop() {
  edge_t v = this->top();
  this->erase(v);
}

bool radixHeap::empty() {
  if (this->buckets.front().empty()) this->reArrange();
  return this->buckets.front().empty();
}

void radixHeap::reArrange() {
  if (!this->buckets.front().empty()) return;

  uint64_t firstNotEmptyIdx = 0;
  while (this->buckets[firstNotEmptyIdx].empty() && firstNotEmptyIdx < this->bucketsSize) { 
    firstNotEmptyIdx++;
  }  if (firstNotEmptyIdx == this->bucketsSize) return;
  
  this->updateRanges(this->buckets[firstNotEmptyIdx].min(), firstNotEmptyIdx);
  this->buckets[firstNotEmptyIdx].range = NO_RANGE;
  while (!this->buckets[firstNotEmptyIdx].empty()) {
    edge_t vtx = this->buckets[firstNotEmptyIdx].first();
    this->push(vtx);
    this->buckets[firstNotEmptyIdx].pop_first();
  }
}

void radixHeap::updateRanges(uint64_t minDist, uint64_t updateRange) {
  this->buckets[0] = radixBucket(minDist, minDist);
  uint64_t fromOffset = 1, toOffset = 1;
  uint64_t from = minDist+fromOffset, to = minDist+toOffset;
  for (uint64_t i = 1; i < updateRange; ++i) {
    this->buckets[i] = radixBucket(from, to);
    fromOffset = toOffset+1; toOffset = 2*toOffset + 1;
    from = minDist+fromOffset; to = minDist+toOffset;
  }
}

void radixHeap::initBuckets() {
  this->buckets[0] = radixBucket(0, 0);
  uint64_t from = 1, to = 1;
  for (uint64_t i = 1; i < this->bucketsSize; ++i) {
    this->buckets[i] = radixBucket(from, to);
    from = to + 1; to = 2*to + 1;
  }
}

uint64_t radixHeap::findBucket(edge_t vtx) {
  uint64_t weight = vtx.first;
  for (uint64_t i = 0; i < this->bucketsSize; ++i) {
    if (this->buckets[i].inRange(weight)) { 
      return i;
    }
  } return ERR_IDX;
}
