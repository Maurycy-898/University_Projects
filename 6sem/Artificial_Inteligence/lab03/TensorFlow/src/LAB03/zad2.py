import cv2
import os
import zad1
import numpy as np

if __name__ == '__main__':
    x_values = []
    y_values = []
    model = zad1.tf_train_model()

    dir = '../images/original'
    for filename in os.listdir(dir):
        file = os.path.join(dir, filename)
        test_image = cv2.imread(file, cv2.IMREAD_GRAYSCALE)
        img_resized = cv2.resize(test_image, (28, 28), interpolation=cv2.INTER_LINEAR)
        x_values.append(img_resized)
        y_values.append(int(filename[0]))
        cv2.imwrite(os.path.join('../images/generated', filename), img_resized)

    x_custom_test = np.array(x_values)
    x_custom_test = x_custom_test / 255.0
    y_custom_test = np.array(y_values)
    print("\nCUSTOM TESTS SECTION:")
    model.evaluate(x_custom_test, y_custom_test)
    predictions = model.predict(x_custom_test)

    idx = 0  # print results
    for prediction in predictions:
        print(prediction)
        print(f"prediction:{np.argmax(prediction)}, expected:{y_values[idx]}\n")
        idx += 1
