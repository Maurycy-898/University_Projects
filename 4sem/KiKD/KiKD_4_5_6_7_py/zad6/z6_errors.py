import os
import sys

from z6_utils import *
from sys import exit
from math import log10

COLORS = ["blue", "green", "red"]


class ErrorsCalculator(object):
    def __init__(self):
        self._mse = dict()
        self._snr = dict()
        self._cnt = dict()
        for c in COLORS:
            self._mse[c] = 0
            self._snr[c] = 0
            self._cnt[c] = 0

    def register_val(self, original_value: int, quantized_value: int, color: str):
        self._mse[color] += (original_value - quantized_value) ** 2
        self._snr[color] += original_value ** 2
        self._cnt[color] += 1

    def calc_mse(self, color: str = ""):
        if color in COLORS:
            return self._mse[color] / self._cnt[color]
        else:
            # calc as a whole pixel
            bottom = 0
            top = 0
            for color in COLORS:
                bottom += self._cnt[color]
                top += self._mse[color]
            return top / bottom

    def calc_snr(self, color: str = ""):
        top = 0
        bottom = 0
        if color in COLORS:
            # calc for given color
            # (1/N) / (1/N) = 1
            top = self._snr[color]
            bottom = self._mse[color]
        else:
            # calc as a whole pixel
            for color in COLORS:
                top += self._snr[color]
                bottom += self._mse[color]
        if bottom > 0:
            return top / bottom
        else:
            return float("inf")


def main():
    # program usage: ./z6_errors.py <first img> <second img>
    first_image = sys.argv[1]
    second_image = sys.argv[2]
    errors_calc = ErrorsCalculator()

    with open(first_image, "rb+") as f1:
        with BitReader(f1) as reader1:
            with open(second_image, "rb+") as f2:
                with BitReader(f2) as reader2:
                    # read files' headers
                    first_image_width, first_image_height = read_write_header(reader1, None)
                    image_width, image_height = read_write_header(reader2, None)

                    # check compatibility
                    if (image_width != first_image_width) or (image_height != first_image_height):
                        exit("images do not have the same sizes")

                    # iterate over all pixels in both files
                    for _ in range(image_width * image_height):
                        pixel1 = read_bit_groups(reader1, 3)  # first file pixel
                        pixel2 = read_bit_groups(reader2, 3)  # second file pixel
                        i = 0  # register for every colour
                        for c in COLORS:
                            errors_calc.register_val(pixel1[i], pixel2[i], c)
                            i += 1
    # print the errors that were calculated
    print("\nMSE = ", errors_calc.calc_mse())
    for c in COLORS:
        print("MSE(" + c + ") = ", errors_calc.calc_mse(c))

    print("\nSNR = ", errors_calc.calc_snr(), "(" + str(10 * log10(errors_calc.calc_snr())) + " dB)")
    for c in COLORS:
        print("SNR(" + c + ") = ", errors_calc.calc_snr(c), "(" + str(10 * log10(errors_calc.calc_snr(c))) + " dB)")

    # to clear the unnecessary files
    os.remove(second_image)
    os.remove("result_enc")


if __name__ == "__main__":
    main()
