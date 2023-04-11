from typing import Tuple


# to write bits
class BitWriter(object):
    def __init__(self, writer):
        self.accumulator = 0
        self.bit_count = 0
        self.out = writer

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.flush()

    def __del__(self):
        try:
            self.flush()
        except ValueError:   # I/O operation on closed file.
            pass

    def _write_bit(self, bit):
        if self.bit_count == 8:
            self.flush()
        if bit > 0:
            self.accumulator |= 1 << (7 - self.bit_count)
        self.bit_count += 1

    def write_bits(self, bits, n):
        while n > 0:
            self._write_bit(bits & 1 << n - 1)
            n -= 1

    def flush(self):
        self.out.write(bytearray([self.accumulator]))
        self.accumulator = 0
        self.bit_count = 0


# to read bits
class BitReader(object):
    def __init__(self, reader):
        self.input = reader
        self.accumulator = 0
        self.bit_count = 0
        self.read = 0

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        pass

    def _read_bit(self) -> int:
        if not self.bit_count:
            a = self.input.read(1)
            if a:
                self.accumulator = ord(a)
            self.bit_count = 8
            self.read = len(a)
        rv = (self.accumulator & (1 << self.bit_count - 1)) >> self.bit_count - 1
        self.bit_count -= 1
        return rv

    def read_bits(self, n) -> int:
        v = 0
        while n > 0:
            v = (v << 1) | self._read_bit()
            n -= 1
        return v


# reads a byte by default
def read_bit_group(reader: BitReader, group_size: int = 8) -> int:
    return reader.read_bits(group_size)


# reads group of bytes by default
def read_bit_groups(reader: BitReader, groups_amount: int, group_size: int = 8) -> Tuple[int]:
    return tuple(read_bit_group(reader, group_size=group_size) for bit_group in range(groups_amount))


# to convert group of bytes to int value
def bytes_to_int(bytes_group) -> int:
    output = 0
    for i in range(0, len(bytes_group)):
        output += bytes_group[i] * (2 ** (8 * i))
    return output


# to process + (optional) copy and write, the header
def read_write_header(reader: BitReader, writer):
    # read/process the header
    header_start = read_bit_groups(reader, 12)
    width_bytes = read_bit_groups(reader, 2)
    height_bytes = read_bit_groups(reader, 2)
    header_the_rest = read_bit_groups(reader, 2)

    image_height = bytes_to_int(height_bytes)
    image_width = bytes_to_int(width_bytes)

    # copy the original header if writer was passed
    if writer is not None:
        writer.write_bits(bytes_to_int(bytes(header_start[::-1])), 12 * 8)
        writer.write_bits(bytes_to_int(bytes(width_bytes[::-1])), 2 * 8)
        writer.write_bits(bytes_to_int(bytes(height_bytes[::-1])), 2 * 8)
        writer.write_bits(bytes_to_int(bytes(header_the_rest[::-1])), 2 * 8)

    return image_width, image_height


# copies and writes the footer
def copy_write_footer(reader: BitReader, writer):
    while reader.read:
        writer.write_bits(reader.read_bits(8), 8)
