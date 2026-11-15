package com.example.reservation.Facilitys.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.reservation.Facilitys.NameMasker;
import com.example.reservation.Facilitys.Reservation;

/**
 * 예약 조회 API 응답 전용 객체.
 * 엔티티를 그대로 내보내지 않고, 필요한 값만 담으며 예약자명은 가려서 내보낸다.
 * 필드 이름은 기존 화면(Input.html)이 쓰던 JSON 이름과 같게 맞췄다.
 */
public record ReservationResponse(
        long reservationId,
        long reservationFacilityId,
        String reservationFacilityName,
        String reservationUserName,
        LocalDate reservationDate,
        LocalTime startTime,
        LocalTime endTime,
        Reservation.ReservationStatus status) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getReservationId(),
                reservation.getReservationFacilityId(),
                reservation.getReservationFacilityName(),
                NameMasker.mask(reservation.getReservationUserName()),
                reservation.getReservationDate(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getStatus());
    }
}
