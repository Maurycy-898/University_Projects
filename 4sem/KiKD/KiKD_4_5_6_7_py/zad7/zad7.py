import random
import numpy as np

G_matrix2 = np.matrix([[1, 1, 0, 1, 0, 0, 0],
                       [0, 1, 1, 0, 1, 0, 0],
                       [0, 0, 1, 1, 0, 1, 0],
                       [0, 0, 0, 1, 1, 0, 1]])

G_matrix = np.matrix([[1, 0, 0, 0, 0, 1, 1, 1],
                      [0, 1, 0, 0, 1, 0, 1, 1],
                      [0, 0, 1, 0, 1, 1, 0, 1],
                      [0, 0, 0, 1, 1, 1, 1, 0]])

H_matrix = np.matrix([[0, 1, 1, 1, 1, 0, 0, 0],
                      [1, 0, 1, 1, 0, 1, 0, 0],
                      [1, 1, 0, 1, 0, 0, 1, 0],
                      [1, 1, 1, 0, 0, 0, 0, 1]])

H_matrix2 = np.matrix([[0, 0, 1, 0, 1, 1, 1, 0],
                       [0, 1, 0, 1, 1, 1, 0, 0],
                       [1, 0, 1, 1, 1, 0, 0, 0],
                       [1, 1, 1, 1, 1, 1, 1, 1]])


def encode(file_in: str, file_out: str):
    with open(file_in, "rb") as f:
        bitstring_in = ''.join([bin(byte)[2:].zfill(8) for byte in f.read()])
    print(bitstring_in)

    bitstring_enc = ''.join([hamming_enc(bitstring_in[i: i + 4]) for i in range(0, len(bitstring_in), 4)])

    with open(file_out, "wb") as f:
        byte_arr = bytes([int(bitstring_enc[i: i + 8], 2) for i in range(0, len(bitstring_enc), 8)])
        f.write(byte_arr)


def hamming_enc(chunk_of_4: str) -> str:
    bit_vector: np.ndarray = np.matrix(list(map(int, chunk_of_4)))
    encoded: np.ndarray = (bit_vector * G_matrix) % 2
    return ''.join(map(str, encoded.tolist()[0]))


def add_noise(file_in: str, file_out: str, prob: float):
    with open(file_in, "rb") as f:
        bitstring_in = ''.join([bin(byte)[2:].zfill(8) for byte in f.read()])
    print(bitstring_in)
    bitlist = list(bitstring_in)
    for i, bit in enumerate(bitlist):
        if random.random() < prob:
            bitlist[i] = '0' if bit == '1' else '1'

    bitstring_out = ''.join(bitlist)
    print(bitstring_out)
    with open(file_out, "wb") as f:
        byte_arr = bytes([int(bitstring_out[i: i + 8], 2) for i in range(0, len(bitstring_out), 8)])
        f.write(byte_arr)


def decode(file_in: str, file_out: str):
    with open(file_in, "rb") as f:
        bitstring_in = ''.join([bin(byte)[2:].zfill(8) for byte in f.read()])
    decoded = [hamming_dec(bitstring_in[i: i + 8]) for i in range(0, len(bitstring_in), 8)]
    two_errors = len([el for el in decoded if el[1]])
    bitstring_dec = ''.join(map(lambda x: x[0], decoded))

    print(bitstring_dec)
    with open(file_out, "wb") as f:
        byte_arr = bytes([int(bitstring_dec[i: i + 8], 2) for i in range(0, len(bitstring_dec), 8)])
        f.write(byte_arr)

    print(f"\nAmount of double errors: {two_errors}")


def hamming_dec(enc_chunk: str):
    encoded = np.matrix(list(map(int, enc_chunk))).transpose()
    error: np.ndarray = H_matrix * encoded % 2
    ones = len([el for el in error if el == 1])
    err = int(''.join(map(str, error.transpose().tolist()[0]))[:3], 2)
    if ones == 3:
        i = np.where(error == 0)[0]
        encoded[i] = 1 - encoded[i]
    elif ones in (2, 4):
        return ''.join(map(str, encoded.transpose().tolist()[0][:4])), True
    return ''.join(map(str, encoded.transpose().tolist()[0][:4])), False


# def hamming_dec2(enc_chunk: str):
#     coded: np.ndarray = np.matrix(list(map(int, enc_chunk))).transpose()
#     error: np.ndarray = H_matrix * coded % 2
#     ones = len([el for el in error if el == 1])
#
#     parity_err = len([el for el in coded if el == 1]) % 2
#     err = int(''.join(map(str, error.transpose().tolist()[0]))[:3], 2)
#     parity = error.transpose().tolist()[0][3]
#     # print(parity_err, ", ", parity)
#     print(err)
#
#     if err > 0 and parity == 1:
#         coded[err] = 1 - coded[err]
#         return ''.join(map(str, coded.transpose().tolist()[0][:4])), False
#     elif err > 0 and parity == 0:
#         coded[err] = 1 - coded[err]
#         return ''.join(map(str, coded.transpose().tolist()[0][:4])), True
#     return ''.join(map(str, coded.transpose().tolist()[0][:4])), False


def check_diff(file1: str, file2: str) -> int:
    with open(file1, "rb") as f1, open(file2, "rb") as f2:
        differences = 0
        byte_f1 = f1.read(1)
        byte_f2 = f2.read(1)

        while byte_f1 and byte_f2:
            bitstring_f1 = bin(byte_f1[0])[2:].zfill(8)
            bitstring_f2 = bin(byte_f2[0])[2:].zfill(8)

            if bitstring_f1[:4] != bitstring_f2[:4]:
                differences += 1
            if bitstring_f1[4:] != bitstring_f2[4:]:
                differences += 1

            byte_f1 = f1.read(1)
            byte_f2 = f2.read(1)

    return differences


def test():
    print()
    encode("files/sample.txt", "files/encoded")
    add_noise("files/encoded", "files/enc_noised", 0.05)
    decode("files/enc_noised", "files/decoded.txt")

    print("Amount of differences from original: ", check_diff("files/sample.txt", "files/decoded.txt"))


if __name__ == '__main__':
    # print(hamming_enc("0101"))
    test()
