#include <iostream>
#include <cmath>
#include <random>
#include <ctime>


const double LEARNING_RATE = 0.005;
// ReLU: 0.005
// Sigma: 1

double** getX(int size) {
    std::default_random_engine generator;
    std::uniform_real_distribution<double> distribution(-1.0, 1.0);

    double **x = new double* [size];
    for (int i = 0; i < size; i++) {
        x[i] = new double [2];
        double tmp1 = distribution(generator);
        double tmp2 = distribution(generator);

        x[i][0] = tmp1;
        x[i][1] = tmp2;

        // double l1 = std::abs(tmp1) + std::abs(tmp2);
        // x[i][0] = tmp1 / l1;
        // x[i][1] = tmp2 / l1;

        // double l2 = sqrt(tmp1 * tmp1 + tmp2 * tmp2);
        // x[i][0] = tmp1 / l2;
        // x[i][1] = tmp2 / l2;

        if (x[i][0] == 0 || x[i][1] == 0) {
            i--;
        }
    }
    return x;
}

double** getY(int size, double **x) {
    double **y = new double* [size];
    for (int i = 0; i < size; i++) {
        y[i] = new double [1];
        y[i][0] = (x[i][0] * x[i][1] > 0);
    }
    return y;
}

double ReLU(double x) {
    return std::max(0.0, x);
}

double ReLU_prim(double x) {
    return x > 0;
}

double sigma(double x) {
    return 1 / (1 + std::exp(-x));
}

double sigma_prim(double x) {
    double y = sigma(x);
    return y * (1 - y);
}

double*** train(double **x_train, double **y_train, int train_size, int layer_cnt, int *sizes, int epochs, double (*activ)(double), double (*activ_prim)(double)) {
    std::default_random_engine generator;
    std::uniform_real_distribution<double> distribution(-0.5, 0.5);

    double ***weights = new double** [layer_cnt];
    for (int i = 0; i < layer_cnt; i++) {
        weights[i] = new double* [sizes[i]];
        for (int j = 0; j < sizes[i]; j++) {
            weights[i][j] = new double [sizes[i + 1]];
        }
    }
    
    double **delta = new double* [layer_cnt];
    for (int i = 0; i < layer_cnt; i++) {
        delta[i] = new double [sizes[i + 1]];
    }

    double **V = new double* [layer_cnt + 1];
    double **h = new double* [layer_cnt + 1];
    for (int i = 0; i < layer_cnt + 1; i++) {
        V[i] = new double [sizes[i]];
        h[i] = new double [sizes[i]];
    }

    for (int i = 0; i < layer_cnt; i++) {
        for (int j = 0; j < sizes[i]; j++) {
            for (int k = 0; k < sizes[i + 1]; k++) {
                weights[i][j][k] = distribution(generator);
            }
        }
    }

    while (epochs--) {
        for (int m = 0; m < train_size; m++) {
            for (int j = 0; j < sizes[0]; j++) {
                V[0][j] = x_train[m][j];
            }
            for (int i = 0; i < layer_cnt; i++) {
                for (int k = 0; k < sizes[i + 1]; k++) {
                    h[i + 1][k] = 0;
                    for (int j = 0; j < sizes[i]; j++) {
                        h[i + 1][k] += weights[i][j][k] * V[i][j];
                    }
                    V[i + 1][k] = activ(h[i + 1][k]);
                }
            }
            for (int j = 0; j < sizes[layer_cnt]; j++) {
                delta[layer_cnt - 1][j] = activ_prim(h[layer_cnt][j]) * (y_train[m][j] - V[layer_cnt][j]);
            }
            for (int i = layer_cnt - 1; i > 0; i--) {
                for (int j = 0; j < sizes[i]; j++) {
                    delta[i - 1][j] = 0;
                    for (int k = 0; k < sizes[i + 1]; k++) {
                        delta[i - 1][j] += weights[i][j][k] * delta[i][k];
                    }
                    delta[i - 1][j] *= activ_prim(h[i][j]);
                }
            }
            for (int i = 0; i < layer_cnt; i++) {
                for (int j = 0; j < sizes[i]; j++) {
                    for (int k = 0; k < sizes[i + 1]; k++) {
                        weights[i][j][k] += LEARNING_RATE * delta[i][k] * V[i][j];
                    }
                }
            }
        }
    }
    for (int i = 0; i < layer_cnt + 1; i++) {
        if (i < layer_cnt) {
            delete[] delta[i];
        }
        delete[] V[i];
        delete[] h[i];
    }
    delete[] delta;
    delete[] V;
    delete[] h;

    return weights;
}

double** test(double **x_test, int test_size, int layer_cnt, int* sizes, double*** weights, double (*activ)(double)) {
    double **results = new double* [test_size];
    double **V = new double* [layer_cnt + 1];
    double **h = new double* [layer_cnt + 1];
    for (int i = 0; i < layer_cnt + 1; i++) {
        V[i] = new double [sizes[i]];
    }
    for (int m = 0; m < test_size; m++) {
        for (int j = 0; j < sizes[0]; j++) {
            V[0][j] = x_test[m][j];
        }
        for (int i = 0; i < layer_cnt; i++) {
            for (int k = 0; k < sizes[i + 1]; k++) {
                V[i + 1][k] = 0;
                for (int j = 0; j < sizes[i]; j++) {
                    V[i + 1][k] += weights[i][j][k] * V[i][j];
                }
                V[i + 1][k] = activ(V[i + 1][k]);
            }
        }
        results[m] = new double [sizes[layer_cnt]];
        for (int j = 0; j < sizes[layer_cnt]; j++) {
            results[m][j] = V[layer_cnt][j];
        }
    }
    for (int i = 0; i < layer_cnt + 1; i++) {
        delete[] V[i];
    }
    delete[] V;
    return results;
}

int main() {
    srand(time(NULL));
    int train_size = 1000, test_size = 1000;
    double **x_train, **x_test;
    double **y_train, **y_test;
    x_train = getX(train_size);
    y_train = getY(train_size, x_train);
    int sizes[3];
    sizes[0] = 2;
    sizes[1] = 4;
    sizes[2] = 1;
    double ***weights = train(x_train, y_train, train_size, 2, sizes, 10000, &ReLU, &ReLU_prim);
    // double ***weights = train(x_train, y_train, train_size, 2, sizes, 10000, &sigma, &sigma_prim);
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < sizes[i]; j++) {
            for (int k = 0; k < sizes[i + 1]; k++) {
                //cout << weights[i][j][k] << " ";
            }
        }
    }
    delete[] x_train;
    delete[] y_train;
    
    x_test = getX(test_size);
    y_test = getY(test_size, x_test);
    double** results = test(x_test, test_size, 2, sizes, weights, &ReLU);
    // double** results = test(x_test, test_size, 2, sizes, weights, &sigma);
    int successCounter = 0;
    for (int i = 0; i < test_size; i++) {
        int res = (results[i][0] < 0.5) ? 0 : 1;
        if (res == y_test[i][0]) successCounter++;

        std::cout << x_test[i][0] << " " << x_test[i][1] << " " << y_test[i][0] << " " << results[i][0] << std::endl;
    }
    double successRate = ((double) successCounter / (double) test_size) * 100.0;
    std::cout << "Success rate: " << successRate << "%" << std::endl;


    delete[] x_test;
    delete[] y_test;
    
    return 0;
}
