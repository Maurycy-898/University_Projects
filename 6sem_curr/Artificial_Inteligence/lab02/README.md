Project: 
    extended Tic Tac Toe, game client + engine (alpha beta)

Authors: 
    Maciej Gębala: game client (base), board.h(pp)
    Maurycy Sosnowski (261705): game engine, added engine in game client, Makefile

Technologies used: 
    C/C++

Libraries: 
    C: stdio.h, stdbool.h, stdlib.h, string.h, unistd.h, arpa/inet.h
    C++: iostream, vector

Usage:
    make: compile project 
    make clean: remove *.o files

    final program: client(.exe)
    note: RUN ON LINUX (UBUNTU), wywołanie:
    ./client <server_ip> <server_port> <player_id> <engine_depth>

    1. server_ip: adres ip serwera,
    2. server_port: numer portu serwera
    3. player_id: numer gracza (1 lub 2),
    4. engine_depth: głębokość przeszukiwania (od 1 do 10).
    