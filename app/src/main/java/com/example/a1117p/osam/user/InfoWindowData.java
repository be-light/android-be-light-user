package com.example.a1117p.osam.user;


class InfoWindowData {
    String hostAddress, hostName, hostTel, hostPostalCode, hostIntro, term;
    long hostIdx;

    InfoWindowData(String hostAddress, String hostName, String hostTel, String hostPostalCode, String hostIntro, String term, long hostIdx) {
        this.hostAddress = hostAddress;
        this.hostName = hostName;
        this.hostTel = hostTel;
        this.hostPostalCode = hostPostalCode;
        this.hostIdx = hostIdx;
        this.term = term;
        this.hostIntro = hostIntro;
    }
}