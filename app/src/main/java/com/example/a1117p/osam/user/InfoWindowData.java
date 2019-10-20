package com.example.a1117p.osam.user;


class InfoWindowData{
    String hostAddress,hostName,hostTel,hostPostalCode,hostIntro;
    long hostIdx;
    InfoWindowData(String hostAddress,String hostName,String hostTel, String hostPostalCode,String hostIntro,long hostIdx){
        this.hostAddress=hostAddress;
        this.hostName=hostName;
        this.hostTel=hostTel;
        this.hostPostalCode=hostPostalCode;
        this.hostIdx=hostIdx;
        this.hostIntro = hostIntro;
    }
}