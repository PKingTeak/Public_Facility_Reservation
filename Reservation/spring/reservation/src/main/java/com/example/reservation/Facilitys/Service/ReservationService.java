
package com.example.reservation.Facilitys.Service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.reservation.Facilitys.Facility;
import com.example.reservation.Facilitys.Reservation;
import com.example.reservation.Facilitys.User;
import com.example.reservation.Facilitys.DTO.ReservationRequest;
import com.example.reservation.Facilitys.Exception.DataNotFoundException;
import com.example.reservation.Facilitys.Exception.DuplicateDataException;
import com.example.reservation.Facilitys.Exception.InvalidRequestException;
import com.example.reservation.Facilitys.Repository.ReservationRepository;

import java.time.LocalDate;

@Service
public class ReservationService {

    // Map<LocalDate, ArrayList<Reservation>> schedule = new HashMap<LocalDate,
    // ArrayList<Reservation>>(); // Date,Reservationinfo
    // static long totalScheduleNum = 1;
    // 이전에 사용하던 자료구조를 레퍼지토리로 변경

    // Repository
    private final ReservationRepository resRepository;

    // other tool
    private final UserService userService;
    private final FacilityService facilityService;

    public ReservationService(UserService _userService, FacilityService _FacilityService,
            ReservationRepository _Repository) {
        this.userService = _userService;
        this.facilityService = _FacilityService;
        this.resRepository = _Repository;

    }

    // #region 오버라이드 함수
    // 해당 기관 정보 조회 Spring 연동
    @Transactional
    public void addReservation(ReservationRequest _request, String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        Facility facility = facilityService.getFacilityInfoByIdWithLock(_request.getFacilityId());

        if (user == null || facility == null) {
            throw new DataNotFoundException("user 혹은 facility가 null입니다");
        }

        List<Reservation> list = resRepository.getReservationByFacilityIdAndDate(_request.getFacilityId(),
                _request.getDate());

        for (Reservation reservation : list) {
            if (reservation.getReservationTimeSlot().overlap(_request.getStartTime(), _request.getEndTime())
                    && reservation.getStatus() == Reservation.ReservationStatus.RESERVED) {
                throw new DuplicateDataException("해당 날짜는 중복된 예약입니다.");
            }
        }

        // 모든 조건에 만족하지 않을때
        Reservation res = new Reservation(user, facility, _request.getDate(), _request.getStartTime(),
                _request.getEndTime());
        resRepository.save(res);

    }

    // #endregion

    public Reservation getReservation(LocalDate _date, long _reservationId) {

        return resRepository.findById(_reservationId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 예약 아이디가 존재하지 않습니다."));
        // return null;
        // 값 없음
    }

    @Transactional
    public void cancelReservation(Long reservationId, String currentEmail) {
        long currentUserId = userService.getUserByEmail(currentEmail).getId();
        Reservation reservation = resRepository.findById(reservationId).orElseThrow(()->new DataNotFoundException("해당하는 예약 아이디가 존재하지 않습니다."));
        if (reservation.getReservationByUserId() != currentUserId) {
            throw new InvalidRequestException(
                    "본인이 아닌 예약은 취소가 불가능합니다.");
        }
        reservation.cancel();
    }

    public Boolean checkReservationUserId(Long _reservationId, Long _userId, LocalDate _date) // 신원 확인용
    {
        Reservation res = resRepository.findById(_reservationId)
                .orElseThrow(() -> new DataNotFoundException("해당하는 예약이 존재하지 않습니다."));

        if (res.getReservationByUserId() == _userId && res.getReservationDate().equals(_date)) {
            return true;
        }
        return false;
    }

    public List<Reservation> getReservationsByDate(LocalDate _date) {
        return resRepository.getReservationByDate(_date);
    }

    public int getDateTableSize(LocalDate _date) {

        return resRepository.getReservationByDate(_date).size();
    }

    public List<Reservation> getAllReservationByDate(LocalDate _date) {
        return resRepository.getReservationByDate(_date);
    }

    public List<Reservation> getReservationsByFacilityAndDate(long facilityId, LocalDate date) {
        return resRepository.getReservationByFacilityIdAndDate(facilityId, date);
    }

}
