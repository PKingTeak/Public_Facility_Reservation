public class Facility {
    private long id;
    private String name;
    private String location;
    private int capacity;


    Facility(long _id, String _name, String _location, int _capacity ) {
        this.id = _id;
        this.name = _name;
        this.location = _location;
        this.capacity = _capacity;

    }

    public String getFacilityName() {
        return this.name;
    }

    public String getFacilityLocation() {
        return this.location;
    }

    public int getFacilityCapacity() {
        return this.capacity;
    }

    public long getFacilityId() {
        return this.id;
    }


}