import sys
from z6_utils import *


def pixels_add_mod(first, second) -> tuple:
    return tuple((first[i] + second[i]) % 256 for i in range(0, 3))


def encode(input_image: str, output_file: str, bit_depth: int):
    with open(input_image, "rb+") as fi:
        with BitReader(fi) as reader:
            with open(output_file, "wb+") as fo:
                with BitWriter(fo) as writer:
                    # copy the original header and read data
                    image_width, image_height = read_write_header(reader, writer)

                    # write information about k - bit-depth
                    writer.write_bits(bit_depth, 8)

                    prev_pixel = (0, 0, 0)
                    for _ in range(image_width * image_height):
                        curr_pixel = read_bit_groups(reader, 3)
                        # iterate over all colors
                        quant_diff = [0 for i in range(3)]
                        for c in range(0, 3):
                            # calculate the difference and quantize it
                            quant_diff[c] = ((curr_pixel[c] - prev_pixel[c]) % 256) >> (8 - bit_depth)
                            writer.write_bits(quant_diff[c], bit_depth)

                            # revert to original bit size
                            quant_diff[c] = quant_diff[c] << (8 - bit_depth)

                        # update prev pixel (with quant-diff)
                        prev_pixel = pixels_add_mod(prev_pixel, quant_diff)

                    # finally copy the original footer
                    copy_write_footer(reader, writer)


def decode(input_file: str, output_file: str):
    with open(input_file, "rb+") as fi:
        with BitReader(fi) as reader:
            with open(output_file, "wb+") as fo:
                with BitWriter(fo) as writer:
                    # copy the original header and read the dimensions
                    image_width, image_height = read_write_header(reader, writer)

                    # read information about k (bit-depth)
                    bit_depth = read_bit_group(reader)

                    prev_pixel = curr_pixel = (0, 0, 0)
                    for pixel in range(image_height * image_width):
                        # read the encrypted diff and retain original bit-size
                        dec_quant_diff = tuple(map(
                            lambda a: a << (8 - bit_depth),
                            read_bit_groups(reader, 3, group_size=bit_depth)
                        ))
                        # recreate and save the pixel
                        curr_pixel = pixels_add_mod(prev_pixel, dec_quant_diff)
                        for c in range(0, 3):
                            writer.write_bits(curr_pixel[c], 8)

                        prev_pixel = curr_pixel

                    # recover and write the original footer
                    copy_write_footer(reader, writer)


def main():
    # program usage: ./z6_main.py e/d (encode/decode) <input file> <output file> <bit-depth (k)>
    program_mode = sys.argv[1]
    bit_depth = sys.argv[2]
    input_file = sys.argv[3]
    output_file = sys.argv[4]

    if program_mode == "e":
        encode(input_file, output_file, int(bit_depth))
    else:
        decode(input_file, output_file)


def test_enc_dec(k: int):
    in_file = "example0.tga"
    enc_file = "result_enc"
    dec_file = "result_dec.tga"

    encode(in_file, enc_file, k)
    decode(enc_file, dec_file)


if __name__ == "__main__":
    # main()
    test_enc_dec(k=6)
