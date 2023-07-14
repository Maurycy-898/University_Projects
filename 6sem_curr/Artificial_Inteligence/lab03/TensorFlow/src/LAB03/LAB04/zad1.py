import tensorflow as tf
import tensorflow_decision_forests as tfdf
import numpy as np

# Load a dataset
mnist = tf.keras.datasets.mnist
(x_train, y_train),(x_test, y_test) = mnist.load_data()
x_train = np.reshape(x_train, (-1, 784))
x_test = np.reshape(x_test, (-1, 784))
x_train = x_train.astype('float32') / 255
x_test = x_test.astype('float32') / 255

# Train a Random Forest model.
model = tfdf.keras.RandomForestModel()
model.compile(metrics=["accuracy"])
model.fit(x_train, y_train)

# Summary of the model structure.
model.summary()

# Evaluate the model.
model.evaluate(x_test, y_test)

# Export the model to a SavedModel.
# model.save("project/model")
