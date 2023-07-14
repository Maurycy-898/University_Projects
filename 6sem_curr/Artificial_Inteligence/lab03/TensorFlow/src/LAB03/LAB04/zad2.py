import numpy as np
from sklearn.cluster import DBSCAN
from sklearn.metrics import accuracy_score

# Wczytanie danych z MNIST
# Poniższy kod zakłada, że dane zostały już wcześniej pobrane i zapisane na dysku
# oraz odpowiednio zdekodowane w numpy arrays
X_train = np.load('mnist_X_train.npy')
y_train = np.load('mnist_y_train.npy')
X_test = np.load('mnist_X_test.npy')
y_test = np.load('mnist_y_test.npy')

# Inicjalizacja i dopasowanie algorytmu DBSCAN
epsilon = 1.5
min_samples = 5
dbscan = DBSCAN(eps=epsilon, min_samples=min_samples)
dbscan.fit(X_train)

# Otrzymane etykiety klastrów
cluster_labels = dbscan.labels_

# Obliczenie dokładności klasyfikacji
predicted_labels = np.zeros_like(cluster_labels)
for cluster in set(cluster_labels):
    mask = cluster_labels == cluster
    majority_label = np.argmax(np.bincount(y_train[mask]))
    predicted_labels[mask] = majority_label

accuracy = accuracy_score(y_train, predicted_labels)
print("Dokładność klasyfikacji: {:.2f}%".format(accuracy * 100))

# Obliczenie procentu szumu
noise_points = cluster_labels == -1
noise_percentage = np.mean(noise_points) * 100
print("Procent szumu: {:.2f}%".format(noise_percentage))

# Obliczenie procentu błędnych klasyfikacji w wyznaczonych klastrach
incorrect_cluster_points = (predicted_labels != y_train) & (~noise_points)
incorrect_cluster_percentage = np.mean(incorrect_cluster_points) * 100
print("Procent błędnych klasyfikacji w klastrach: {:.2f}%".format(incorrect_cluster_percentage))