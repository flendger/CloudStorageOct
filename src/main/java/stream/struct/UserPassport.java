package stream.struct;

public class UserPassport {

    String passportId;
    byte[] photo;
    String address;

    public UserPassport(String passportId, byte[] photo, String address) {
        this.passportId = passportId;
        this.photo = photo;
        this.address = address;
    }

    public boolean validateId() {
        return passportId.startsWith("user");
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
