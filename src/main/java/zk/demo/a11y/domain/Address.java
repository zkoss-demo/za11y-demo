package zk.demo.a11y.domain;

public class Address {
    private String street;
    private String city;
    private String phone;
    private String zipCode;

    public Address(String street, String city, String phone, String zipCode) {
        this.street = street;
        this.city = city;
        this.phone = phone;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return street + ", " + zipCode + " " + city + " (" + phone + ")";
    }

    public String searchString() {
        return street + "|" + zipCode + "|" + city + "|" + phone;
    }
}
