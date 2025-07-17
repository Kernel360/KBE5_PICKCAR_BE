package com.pickcar.reservation.application;

import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationScheduler {

    private final ReservationRepository reservationRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void setUpDelayed() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<Reservation> targets = reservationRepository.findAllByDueDate(yesterday);

        if (targets.isEmpty()) {
            log.info("지연 처리할 차량이 없습니다.");
            return;
        }

        log.info("12시가 되어 지연 처리를 진행합니다. targets : {} ", targets);
        for (Reservation reservation : targets) {
            reservation.changeToDelayed();
        }
    }
}
