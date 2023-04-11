#include <iostream>
#include <fstream>
#include <math.h>

/**
 * @brief Program to calculate entropy and conditional entropy
 * To run please pass file path as an argument 
 * 
 */

using namespace std;
int main(int argc, char* argv[]) {
  int count_single_symbols[256] = {}; // how many times a character has appeared in the file
  int count_consequentive_symbols[256][256] = {}; // how many times a character has appeared after another character
  unsigned int total_count = 0; // how many characters are in the file

  ifstream myfile;
  string filePath = argv[1];
  myfile.open(filePath.c_str());
  
  // gathering data
  unsigned char current = 0;
  unsigned char previous = 0;
  while (!myfile.eof()) {
    count_single_symbols[(int) current] ++;
    count_consequentive_symbols[(int) previous][(int) current] ++;

    previous = current;
    current = myfile.get();

    total_count ++;
  }

  // calcualting enropy
  double entropy_output = 0.0;
  for (int i = 0; i < 256; i++) {
    if (count_single_symbols[i] > 0) {
      entropy_output += (-1.0) * log2((double)count_single_symbols[i] / total_count) * ((double)count_single_symbols[i] / total_count);
    }
  }
  
  // calcualting conditional enropy
  double conditional_entropy_output = 0.0;
  for (int i = 0; i < 256; i++) {
    for (int j = 0; j < 256; j++) {
      if (count_single_symbols[i] > 0 && count_consequentive_symbols[i][j] > 0) {
        conditional_entropy_output += (-1.0) * ((double)count_consequentive_symbols[i][j] / total_count) * (log2((double)count_consequentive_symbols[i][j] / (double)count_single_symbols[i]));
      }
    }
  }
  
  // printing results
  cout << "Entropy             : " << entropy_output << endl;
  cout << "Conditional Entropy : " << conditional_entropy_output << endl;
  cout << "Difference          : " << entropy_output - conditional_entropy_output << endl;
  
  return 0;
}
