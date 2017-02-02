package com.stxnext.intranet2.model;

/**
 * Created by Tomasz Konieczny on 2015-06-11.
 */
public enum Office {

    POZNAN("ul. Morawskiego 12/5", "60-239", "Poznań", "+48 61 610 01 92", "+48 61 610 03 18", 52.3946831, 16.8940677),
    WROCLAW("ul. Aleksandra Hercena 3-5", "50-453", "Wrocław", "+48 71 707 11 13", "", 51.102992, 17.041462),
    PILA("al. Piastów 3", "64-920", "Piła", "+48 67 342 32 16", "", 53.148584, 16.738079),
    LODZ("ul. Targowa 35", "90-043", "Łódź", "+48 42 203 10 16", "", 51.7615984, 19.4724402);

    private final String street;
    private final String postcode;
    private final String city;
    private final String tel;
    private final String fax;
    private final double lat;
    private final double lon;

    Office(String street, String postcode, String city, String tel, String fax, double lat, double lon) {
        this.street = street;
        this.postcode = postcode;
        this.city = city;
        this.tel = tel;
        this.fax = fax;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getTel() {
        return tel;
    }

    public String getFax() {
        return fax;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getPostcode() {
        return postcode;
    }
}
