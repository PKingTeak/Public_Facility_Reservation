
public class Reservation {
    private long id;
    private User user;
    private Facility facility;
    private String date;
    private ReservationStatus status;


    public enum ReservationStatus 
    {
        RESERVED,
        CANCELLED
    }
    

    public Reservation(User _user, Facility _facility, String _date) {
        this.user = _user;
        this.facility = _facility;
        this.date = _date;
        status = ReservationStatus.RESERVED;
    }

    public long getReservationId() {
        return this.id;
    }

    public String getReservationUserName() {
        return this.user.getName();
    }

    public String getReservationFacilityName() {
        return this.facility.getFacilityName();
    }

    public String getReservationDate() {
        return this.date;
    }



    public void Reserve()
    {
        this.status = ReservationStatus.RESERVED;
    }

    public void cancel()
    {
        if(this.status == ReservationStatus.CANCELLED)
        {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }
        
        this.status = ReservationStatus.CANCELLED;
    } 
}