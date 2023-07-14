import tensorflow as tf
import tensorflow_decision_forests as tfdf
import numpy as np
import cv2
import os


def tf_train_model():
    mnist = tf.keras.datasets.mnist
    (x_train, y_train), (x_test, y_test) = mnist.load_data()
    assert x_train.shape == (60000, 28, 28)
    assert x_test.shape == (10000, 28, 28)
    assert y_train.shape == (60000,)
    assert y_test.shape == (10000,)

    x_train, x_test = x_train / 255.0, x_test / 255.0

    model = tf.keras.models.Sequential([
        tf.keras.layers.Flatten(input_shape=(28, 28)),
        tf.keras.layers.Dense(128, activation='relu'),
        tf.keras.layers.Dropout(0.2),
        tf.keras.layers.Dense(10, activation='softmax')
    ])

    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=['accuracy'])
    model.fit(x_train, y_train, epochs=5)
    model.evaluate(x_test, y_test)
    return model


def df_train_model():
    mnist = tf.keras.datasets.mnist
    (x_train, y_train), (x_test, y_test) = mnist.load_data()
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

if __name__ == '__main__':
    tf_train_model()
    # df_train_model()
