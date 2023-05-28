
CC = g++
CFLAGS = -g -Wall -std=c++2a

TARGET = program
SRC_DIR = ./src
OUT_DIR = ./out

TARGET_OBJ = $(OUT_DIR)/$(TARGET).o
SRCS = $(wildcard $(SRC_DIR)/*.cpp)
OBJS = $(patsubst $(SRC_DIR)/%.cpp, $(OUT_DIR)/%.o, $(SRCS))

TARGET_EXEC = $(OUT_DIR)/$(TARGET)

$(TARGET_EXEC): $(OBJS) $(TARGET_OBJ)
	$(CC) $(CFLAGS) $(OBJS) $(TARGET_OBJ) -o $@

$(OUT_DIR)/%.o: $(SRC_DIR)/%.cpp
	$(CC) $(CFLAGS) -c $^ -o $@ 

$(TARGET_OBJ): $(TARGET).cpp
	$(CC) $(CFLAGS) -c $^ -o $@

clean:
	rm -f $(OBJS) $(TARGET_OBJ)
