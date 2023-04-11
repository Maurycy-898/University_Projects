from sys import argv
from math import log, inf
from collections import defaultdict


class Pixel:
    def __init__(self, red, green, blue):
        self.r = red
        self.g = green
        self.b = blue

    def __add__(self, other):
        return Pixel(
            self.r + other.r, self.g + other.g, self.b + other.b
        )

    def __sub__(self, other):
        return Pixel(
            self.r - other.r, self.g - other.g, self.b - other.b
        )

    def __floordiv__(self, number):
        return Pixel(self.r // number, self.g // number, self.b // number)

    def __mod__(self, number):
        return Pixel(self.r % number, self.g % number, self.b % number)


def new_standard_scheme(n, w, nw):
    n = [n.r, n.g, n.b]
    w = [w.r, w.g, w.b]
    nw = [nw.r, nw.g, nw.b]

    rgb_result = []
    for color in range(3):
        if nw[color] >= max(w[color], n[color]):
            rgb_result.append(max(w[color], n[color]))
        elif nw[color] <= min(w[color], n[color]):
            rgb_result.append(min(w[color], n[color]))
        else:
            rgb_result.append(w[color] + n[color] - nw[color])

    return Pixel(rgb_result[0], rgb_result[1], rgb_result[2])


scheme_funs = [
    lambda n, w, nw: w,
    lambda n, w, nw: n,
    lambda n, w, nw: nw,
    lambda n, w, nw: n + w - nw,
    lambda n, w, nw: n + (w - nw) // 2,
    lambda n, w, nw: w + (n - nw) // 2,
    lambda n, w, nw: (n + w) // 2,
    lambda n, w, nw: new_standard_scheme(n, w, nw),
]


def jpeg_ls(image_pixels, scheme_fun):
    result = []
    for i, row in enumerate(image_pixels):
        encoded_row = []
        for j, pixel in enumerate(row):
            if i == 0:
                n = Pixel(0, 0, 0)
            else:
                n = image_pixels[i - 1][j]

            if j == 0:
                w = Pixel(0, 0, 0)
            else:
                w = image_pixels[i][j - 1]

            if i == 0 or j == 0:
                nw = Pixel(0, 0, 0)
            else:
                nw = image_pixels[i - 1][j - 1]

            encoded_row.append((pixel - scheme_fun(n, w, nw)) % 256)
        result.append(encoded_row)
    return result


def calc_entropy(image_pixels, color):
    freq_table = defaultdict(int)
    count = 0

    for row in image_pixels:
        for pixel in row:
            if color == "r":
                freq_table[pixel.r] += 1
                count += 1
            elif color == "g":
                freq_table[pixel.g] += 1
                count += 1
            elif color == "b":
                freq_table[pixel.b] += 1
                count += 1
            else:
                freq_table[pixel.r] += 1
                freq_table[pixel.g] += 1
                freq_table[pixel.b] += 1
                count += 3

    entropy = 0
    for i in freq_table:
        entropy += freq_table[i] / count * -log(freq_table[i] / count, 2)
    return entropy


def parse_image_pixels(byte_map, width, height):
    image_pixels = []
    row = []
    for i in range(width * height):
        row.append(Pixel(blue=byte_map[i * 3], green=byte_map[i * 3 + 1], red=byte_map[i * 3 + 2]))

        if width == len(row):
            image_pixels.insert(0, row)
            row = []

    return image_pixels


def main():
    if len(argv) == 2:
        with open(argv[1], "rb") as f:
            tga = f.read()
            width = tga[13] * 256 + tga[12]
            height = tga[15] * 256 + tga[14]
            image_pixels = parse_image_pixels(tga[18: len(tga) - 26], width, height)

            print("Input file entropy")
            print("Red: ", calc_entropy(image_pixels, "r"))
            print("Green: ", calc_entropy(image_pixels, "g"))
            print("Blue: ", calc_entropy(image_pixels, "b"))
            print("RGB: ", calc_entropy(image_pixels, "rgb"))
            print("\n")

            best = inf
            best_r = inf
            best_g = inf
            best_b = inf

            best_scheme = 0
            best_red_scheme = 0
            best_green_scheme = 0
            best_blue_scheme = 0

            for idx, scheme in enumerate(scheme_funs):
                encoded = jpeg_ls(image_pixels, scheme)

                entropies = [
                    calc_entropy(encoded, "r"),
                    calc_entropy(encoded, "g"),
                    calc_entropy(encoded, "b"),
                    calc_entropy(encoded, "rgb"),
                ]

                if entropies[0] < best_r:
                    best_r = entropies[0]
                    best_red_scheme = idx + 1
                if entropies[1] < best_g:
                    best_g = entropies[1]
                    best_green_scheme = idx + 1
                if entropies[2] < best_b:
                    best_b = entropies[2]
                    best_blue_scheme = idx + 1
                if entropies[3] < best:
                    best = entropies[3]
                    best_scheme = idx + 1

                print("Scheme ", idx + 1)
                print("Red: ", entropies[0])
                print("Green: ", entropies[1])
                print("Blue: ", entropies[2])
                print("RGB: ", entropies[3])
                print("\n")

            print("Best red: ", best_red_scheme)
            print("Best green: ", best_green_scheme)
            print("Best blue: ", best_blue_scheme)
            print("Best RGB: ", best_scheme)


if __name__ == "__main__":
    main()
