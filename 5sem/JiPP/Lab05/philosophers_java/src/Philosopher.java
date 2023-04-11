public class Philosopher implements Runnable {
    int philosopherID;
    int eatenDishesAmount;
    int dishesToEat;
    boolean isLeftHanded;

    final Object rightFork;
    final Object leftFork;



    public Philosopher(int philosopherID, int dishesToEat, boolean isLeftHanded, Object rightFork, Object leftFork) {
        this.philosopherID = philosopherID;
        this.isLeftHanded = isLeftHanded;
        this.rightFork = rightFork;
        this.leftFork = leftFork;
        this.dishesToEat = dishesToEat;

        this.eatenDishesAmount = 0;
    }

    void doAction(String action) throws InterruptedException {
        System.out.println(action);
        Thread.sleep(((int) (Math.random() * 500)));
    }

    @Override
    public void run() {
        try {
            while (this.eatenDishesAmount < this.dishesToEat) {
                if (this.isLeftHanded) {
                    doAction("Philosopher" + this.philosopherID + ": Thinking");
                    // trying to eat
                    synchronized (leftFork) {
                        doAction("Philosopher" + this.philosopherID + ": Picked up left fork");
                        synchronized (rightFork) {
                            doAction("Philosopher" + this.philosopherID + ": Picked up right fork");
                            // eating
                            doAction("Philosopher" + this.philosopherID + ": is Eating");
                            this.eatenDishesAmount++;

                            doAction("Philosopher" + this.philosopherID + ": Put down right fork");
                        }
                        doAction("Philosopher" + this.philosopherID + ": Put down left fork");
                    }  // Back to thinking
                    doAction("Philosopher" + this.philosopherID + ": Finished," + this.eatenDishesAmount + " dishes eaten, back to thinking");
                }
                else {  // is right-handed
                    doAction("Philosopher" + this.philosopherID + ": Thinking");
                    // trying to eat
                    synchronized (rightFork) {
                        doAction("Philosopher" + this.philosopherID + ": Picked up right fork");
                        synchronized (leftFork) {
                            doAction("Philosopher" + this.philosopherID + ": Picked up left fork");
                            // eating
                            doAction("Philosopher" + this.philosopherID + ": is Eating");
                            this.eatenDishesAmount++;

                            doAction("Philosopher" + this.philosopherID + ": Put down left fork");
                        }
                        doAction("Philosopher" + this.philosopherID + ": Put down right fork");
                    }  // Back to thinking
                    doAction("Philosopher" + this.philosopherID + ": Finished, " + this.eatenDishesAmount + " dishes eaten, back to thinking");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
