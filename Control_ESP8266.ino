#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>
#define FIREBASE_HOST "fireb-3b720.firebaseio.com"
#define FIREBASE_AUTH "8uES1nEaojUBpuhvGw7Moyyreq3unGMulaY05mk6"
#define WIFI_SSID "Park"
#define WIFI_PASSWORD "12345678"


void setup() {
  Serial.begin(9600);
  pinMode(D0,OUTPUT);
  pinMode(D1,OUTPUT);
  pinMode(D2,OUTPUT);
  pinMode(D3,OUTPUT);
  pinMode(D4,OUTPUT);
  pinMode(D5,OUTPUT);
  pinMode(D6,OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
    }

    
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setInt("SWITCH_1_STATUS", 0);  
  Firebase.setInt("SWITCH_2_STATUS", 0);  
  Firebase.setInt("SWITCH_3_STATUS", 0);  
  Firebase.setInt("TOGG_BTN_STATUS", 0);  
}


void loop() {
  if(Firebase.getInt("SWITCH_2_STATUS")==1){
     Switch2();
  }
    if(Firebase.getInt("SWITCH_3_STATUS")==1){
        digitalWrite(D2, HIGH);
        if(Firebase.getInt("TOGG_BTN_STATUS")==1){
          digitalWrite(D0, HIGH);
        }else
          digitalWrite(D0, LOW);
    }
   if(Firebase.getInt("SWITCH_3_STATUS")==0){
    if(Firebase.getInt("SWITCH_2_STATUS")==0){
        digitalWrite(D2, LOW);
    }
      if(Firebase.getInt("TOGG_BTN_STATUS")==1){
        digitalWrite(D0, HIGH);
        digitalWrite(D4, HIGH);
        delay(10000);
        digitalWrite(D0, LOW);
        delay(1000);
        digitalWrite(D0, HIGH);
        digitalWrite(D4, LOW);
        delay(10000);
        digitalWrite(D0, LOW);
        delay(1000);
      }else if(Firebase.getInt("TOGG_BTN_STATUS")==0){
        digitalWrite(D0, LOW);
      }
   }
}
void Switch2(){
    if(Firebase.getInt("SWITCH_2_STATUS")==1){
          digitalWrite(D2, HIGH);
    }else if(Firebase.getInt("SWITCH_2_STATUS")==0){
        digitalWrite(D2, LOW);
    }
  


}

