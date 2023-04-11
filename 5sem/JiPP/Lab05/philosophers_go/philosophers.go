package main

import (
    "fmt"
    "math/rand"
    "sync"
    "time"
)

var philosophersGroup sync.WaitGroup

type fork struct {
    sync.Mutex
}

type philosopher struct {
    philosopherID        int
    eatenDishes          int
    dishesToEat          int
    isLeftHanded         bool
    leftFork, rightFork *fork
}

func (p philosopher) start() {
    defer philosophersGroup.Done()
    for j := 0; j < p.dishesToEat; j++ {
        if p.isLeftHanded {
            doAction("Thinking", p.philosopherID)

            p.leftFork.Lock()
            doAction("Picked up left fork", p.philosopherID)


            p.rightFork.Lock()
            doAction("Picked up right fork", p.philosopherID)

            doAction("is Eating", p.philosopherID)
            p.eatenDishes ++

            p.rightFork.Unlock()
            doAction("Put down right fork", p.philosopherID)

            p.leftFork.Unlock()
            doAction("Put down left fork", p.philosopherID)

            doAction(fmt.Sprintf("Finished. %d dishes eaten, back to thinking", p.eatenDishes), p.philosopherID)

        } else {  // is right-handed
            doAction("Thinking", p.philosopherID)

            p.rightFork.Lock()
            doAction("Picked up right fork", p.philosopherID)
            p.leftFork.Lock()

            doAction("Picked up left fork", p.philosopherID)

            doAction("is Eating", p.philosopherID)
            p.eatenDishes ++

            p.leftFork.Unlock()
            doAction("Put down left fork", p.philosopherID)

            p.rightFork.Unlock()
            doAction("Put down right fork", p.philosopherID)

            doAction(fmt.Sprintf("Finished, %d dishes eaten, back to thinking", p.eatenDishes), p.philosopherID)
        }
    }
}

func doAction(action string, id int) {
    fmt.Printf("Philosopher%d: %s\n", id, action)
    time.Sleep(time.Duration(rand.Intn(500)) * time.Millisecond)
}

func main() {
    var philosophersNO int
    var dishesToEat int

    fmt.Println("Enter number of philosophers: ")
    _, _ = fmt.Scanf("%d\n", &philosophersNO)

    fmt.Println("Enter number of dishes: ")
    _, _ = fmt.Scanf("%d\n", &dishesToEat)

    forks := make([]*fork, philosophersNO)
    for i := 0; i < philosophersNO; i++ {
        forks[i] = new(fork)
    }

    philosophers := make([]*philosopher, philosophersNO)
    for i := 0; i < philosophersNO; i++ {
        philosophers[i] = &philosopher{
            philosopherID: i+1,
            eatenDishes:   0,
            dishesToEat:   dishesToEat,
            isLeftHanded:  i % 2 == 0,
            leftFork:      forks[i],
            rightFork:     forks[(i+1) %philosophersNO],
        }
        philosophersGroup.Add(1)
        go philosophers[i].start()
    }
    philosophersGroup.Wait()
}
