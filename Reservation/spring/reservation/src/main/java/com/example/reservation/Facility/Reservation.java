
package com.example.reservation.Facility;


import java.time.LocalDate;
import java.time.LocalTime;


public class Reservation {

    private long id; // 예약번호
    private User user;
    private Facility facility;
    private LocalDate  date; // 시간대 별로 나눌예정
    private ReservationStatus status;
    private TimeSlot resTimeSlot = new TimeSlot();

    // 번호는 자동으로 순차적으로 발급 되는식으로 구현

    public enum ReservationStatus {
        RESERVED,
        CANCELLED
    }

    public class TimeSlot {
        private LocalTime startTime;
        private LocalTime endTime;

        public void setTimeSlot(LocalTime _starTime, LocalTime _endTime) {
            if (!_starTime.isBefore(_endTime)) {
                throw new IllegalStateException("예약 시간이 잘못되었습니다");
            }
            startTime = _starTime;
            endTime = _endTime;
        }

        public boolean overlap(TimeSlot other) {

            if (startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime)) {
                return true;
            }
            return false;
        }
        
        public boolean overlap(LocalTime otherstartTime, LocalTime otherEndTime) {

            if (startTime.isBefore(otherEndTime) && endTime.isAfter(otherstartTime)) {
                return true;
            }
            return false;
        }

        public boolean isEqual(LocalTime _startTime, LocalTime _endTime)
        {
             return startTime.equals(_startTime) && endTime.equals(_endTime);
        }
    }

    public Reservation(long _id, User _user, Facility _facility, LocalDate _date, LocalTime _starTime,
            LocalTime _endTime) {
        this.id = _id;
        this.user = _user;
        this.facility = _facility;
        this.date = _date;
        this.resTimeSlot.setTimeSlot(_starTime, _endTime);
        status = ReservationStatus.RESERVED;
    }

   

    public long getReservationId() {
        return this.id;
    }

    public long getReseravtoinUserId() {
        return user.getId();
    }

    public String getReservationUserName() {
        return this.user.getName();
    }

    public String getReservationFacilityName() {
        return this.facility.getFacilityName();
    }

    public long getReservationFacilityId() {
        return facility.getFacilityId();
    }

    public LocalDate  getReservationDate() {
        return this.date;
    }

    public TimeSlot getReservationTimeSlot() {
        return resTimeSlot;
    }

    public void Reserve() {
        this.status = ReservationStatus.RESERVED;
    }

    public void cancel() {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 예약입니다.");
        }

        this.status = ReservationStatus.CANCELLED;
    }

    public ReservationStatus getStatus()
    {
        return this.status;
    }
}