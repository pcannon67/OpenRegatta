/*
This programs opens the default serial port of a RaspberryPi and reads
data as it comes line by line in order to read NMEA data transmitted by
the sensors of the boat

It also then broadcasts NMEA received as is by UDP packets on port 1703
*/

#include <stdio.h> // standard input / output functions
#include <string> // string function definitions
#include <unistd.h> // UNIX standard function definitions
#include <fcntl.h> // File control definitions
#include <errno.h> // Error number definitions
#include <termios.h> //serial port configuration

//Open the serial port, returns a file descriptor if success
int open_port(void)
{
  int fd; // file description for the serial port

  fd = open("/dev/ttyAMA0", O_RDWR | O_NOCTTY | O_NDELAY);

  if(fd == -1) // if open is unsucessful
    {
      //perror("open_port: Unable to open /dev/ttyS0 - ");
      printf("open_port: Unable to open /dev/ttyS0. \n");
    }
  else
    {
      fcntl(fd, F_SETFL, 0);
      printf("port is open.\n");
    }

  return(fd);
} //open_port

//Configure the serial port
int configure_port(int fd)
{
  struct termios port_settings;      // structure to store the port settings in

  cfsetispeed(&port_settings, B4800);    // set baud rates
  cfsetospeed(&port_settings, B4800);

  port_settings.c_cflag &= ~PARENB;    // set no parity, 1 stop bits, 8 data bits
  port_settings.c_cflag &= ~CSTOPB;    // 8N1
  port_settings.c_cflag &= ~CSIZE;
  port_settings.c_cflag |= CS8;

  tcsetattr(fd, TCSANOW, &port_settings);    // apply the settings to the port
  return(fd);

} //configure_port

//Read incoming data on the serial port
int read(int fd)
{
  char* buff = new char[2];

  //read a bunch of lines
  for(;;)
  {
    //read a single line:
    std::string data="";
    int bytes=0;
    do
    {
      bytes= read(fd,buff,1);
      if(buff[0]=='\r')
      {
        read(fd,buff,1);//read the following char which should be \n
        break;
      }
      else
      {
        data.append(1,buff[0]);
      }
    }while(bytes>0);
       
	printf(data.c_str());
  }
}

int main(void)
{
  int fd = open_port();
  configure_port(fd);
  read(fd);
  close(fd);
  return fd;
}