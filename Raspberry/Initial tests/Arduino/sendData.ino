/*
This program uses an array of read NMEA data recorded from BG instruments on a sailboat
It loops though the data and sends it as it would be send by the instruments

There are issues with the Arduino for larger arrays, the data coming though the port is
no readable anymore by the Pi correctly, we would need a numerical scope to check the electical
output and see what is causing the issues with larger arrays of data
*/

char* data[] = {
"$IIVTG,110,T,113,M,7.19,N,13.31,K,D*0D\r\n",
"$IIDPT,8.60,,*7C\r\n",
"$IIGGA,140244.00,4151.43,N,08734.75,W,2,12,0.80,,M,,M,,*6A\r\n",
"$IIGLL,4151.43,N,08734.75,W,140244.00,A,D*62\r\n",
"$IIHDG,113,,,003,W*30\r\n",
"$IIHDM,113,M*3F\r\n",
"$IIHDT,110,T*3C\r\n",
"$IIMTW,24.20,C*27\r\n",
"$IIMWD,357,T,001,M,7.05,N,3.63,M*40\r\n",
"$IIMWV,247,T,7.05,N,A*16\r\n",
"$IIRMB,A,0.02,L,,NOODB,4149.00,N,08731.00,W,3.71,131,6.80,V,D*02\r\n",
"$IIRMC,140244.00,A,4151.43,N,08734.75,W,7.30,109,,003,W,D*13\r\n",
"$IIVHW,110,T,113,M,6.97,N,12.92,K*66\r\n",
"$IIVLW,2862.43,N,2862.43,N*4D\r\n",
"$IIVTG,109,T,112,M,7.23,N,13.39,K,D*05\r\n",
"$IIDPT,8.60,,*7C\r\n",
"$IIGGA,140246.00,4151.43,N,08734.74,W,2,12,0.80,,M,,M,,*69\r\n",
"$IIGLL,4151.43,N,08734.74,W,140246.00,A,D*61\r\n",
"$IIHDG,113,,,003,W*30\r\n",
"$IIHDM,113,M*3F\r\n",
"$IIHDT,110,T*3C\r\n",
"$IIMTW,24.29,C*2E\r\n",
"$IIMWD,358,T,001,M,6.84,N,3.52,M*45\r\n",
"$IIMWV,306,R,8.30,N,A*1D\r\n",
"$IIRMB,A,0.01,L,,NOODB,4149.00,N,08731.00,W,3.71,131,6.39,V,D*03\r\n",
"$IIRMC,140248.00,A,4151.43,N,08734.74,W,6.99,108,,003,W,D*1D\r\n",
"$IIVHW,110,T,114,M,6.95,N,12.88,K*68\r\n",
"$IIVLW,2862.43,N,2862.43,N*4D\r\n",
"$IIVTG,108,T,111,M,6.99,N,12.96,K,D*03\r\n",
"$IIDPT,8.60,,*7C\r\n",
"$IIGGA,140250.00,4151.43,N,08734.73,W,2,12,0.80,,M,,M,,*69\r\n",
"$IIGLL,4151.43,N,08734.73,W,140250.00,A,D*61\r\n",
"$IIHDG,115,,,003,W*36\r\n",
"$IIHDM,115,M*39\r\n",
"$IIHDT,111,T*3D\r\n",
"$IIMTW,24.29,C*2E\r\n",
"$IIMWD,359,T,002,M,6.49,N,3.33,M*41\r\n",
"$IIMWV,247,T,6.49,N,A*1F\r\n",
"$IIRMB,A,0.01,L,,NOODB,4149.00,N,08731.00,W,3.70,131,5.90,V,D*02\r\n",
"$IIRMC,140250.00,A,4151.43,N,08734.73,W,7.19,110,,003,W,D*13\r\n",
"$IIVHW,112,T,115,M,6.97,N,12.92,K*62\r\n",
"$IIVLW,2862.44,N,2862.44,N*4D\r\n"
};

void setup(){
  Serial.begin(4800);
}

int cnt = 0;

void loop(){
  for (unsigned int j=0; j<14; j++) {
    Serial.write(data[cnt]);
    cnt = cnt + 1;
  }
  Serial.flush();
  delay(1000);
  
  if (cnt >= 40){
    cnt = 0;
  }
}

