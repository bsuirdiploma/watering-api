package by.trusevich.house.watering.service

import by.trusevich.house.core.util.lazyLogger
import jssc.SerialPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ComPortService(
    @Value("\${house.default.jssc.baudRate}") private val baudRate: Int,
    @Value("\${house.default.jssc.dataBits}") private val dataBits: Int,
    @Value("\${house.default.jssc.stopBits}") private val stopBits: Int,
    @Value("\${house.default.jssc.parity}") private val parity: Int,
    @Value("\${house.default.jssc.flowControl}") private val flowControl: Int
) {

    private val log by lazyLogger()

    fun connectToPort(portName: String) = SerialPort(portName).apply {
        openPort()
        setParams(baudRate, dataBits, stopBits, parity)
        flowControlMode = flowControl
    }

    fun disconnectFromPort(serialPort: SerialPort) = serialPort.closePort()

    fun sendMessage(portName: String, message: String): Unit = connectToPort(portName).let { port ->
        log.info("Sending message $message to $portName")

        port.writeString(message)
        disconnectFromPort(port)
    }
}