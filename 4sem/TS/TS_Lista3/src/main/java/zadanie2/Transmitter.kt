package zadanie2

class Transmitter(val id: String, val color: String, val basePosition: Int, var transmissionBuffer: Int, var isTransmitting: Boolean = false) {
    var rightSignalHead = basePosition
    var rightSignalTail = basePosition
    var leftSignalHead = basePosition
    var leftSignalTail = basePosition


}