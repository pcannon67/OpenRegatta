#This scripts opens the RaspberryPi default serial port and reads data from it's buffer
#The device is set to read data at 4800bps 8bit data 1 bit stop no parity, which is 
#equivalent as the NMEA input usually found on marine devices.
#
#The program reads the data and outputs it to the console so the user executing this 
#script is able to monitor the data that is coming though the serial port

#!/usr/bin/python
import serial
ser = serial.Serial('/dev/ttyAMA0', 4800, timeout=1)
ser.open()

try:
        while 1:
                response = ser.readline()
                print response
except KeyboardInterrupt:
        ser.close()
