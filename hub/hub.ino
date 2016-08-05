#include <SPI.h>
#include <SD.h>

const int chipSelect = 4;
int bluetoothTx = 0;
int bluetoothRx = 1;
int ledPin = 13;
int micPin = A1;
int micOutputPin = 3;
int signalPin = 12;
int micValue = 0;
int count = 0;
int startPin = 4;
int pressed = 0;

void setup() {
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
  pinMode(signalPin, OUTPUT);
  pinMode(startPin, INPUT);
}

void loop() {
  readStartButton();
  if(pressed == 1) {
    digitalWrite(signalPin, LOW);
    count ++;
    readMic();
  
    String envInput = "";
    
    int sensorValue = digitalRead(micOutputPin);
//    Serial.println(sensorValue);
    envInput += String(sensorValue);
      
    File dataFile = SD.open("record.wav", FILE_WRITE);
  
    if (dataFile) {
      dataFile.println(envInput);
      dataFile.close();
//      Serial.println(envInput);
    }
    else {
      Serial.println("error opening datalog.wav");
    }
  } else {
    count = 0;
    digitalWrite(signalPin, HIGH);
  }
}

void readMic() {
  micValue = analogRead(micPin);
  if(micValue>700) {
    digitalWrite(ledPin, HIGH);
  } else {
    digitalWrite(ledPin, LOW);
  }
}

void transmitBluetooth(int input) {
  
}

void readStartButton() {
  pressed = digitalRead(startPin);
}

