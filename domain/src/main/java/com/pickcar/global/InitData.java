package com.pickcar.global;

import com.pickcar.auth.domain.User;
import com.pickcar.auth.domain.UserInfo;
import com.pickcar.auth.domain.UserRole;
import com.pickcar.auth.domain.UserStatus;
import com.pickcar.auth.infrastructure.UserRepository;
import com.pickcar.company.domain.Company;
import com.pickcar.company.domain.ContractStatus;
import com.pickcar.company.infrastructure.CompanyRepository;
import com.pickcar.drivehistory.domain.DriveHistory;
import com.pickcar.drivehistory.infrastructure.DriveHistoryRepository;
import com.pickcar.reservation.domain.Reservation;
import com.pickcar.reservation.domain.ReservationStatus;
import com.pickcar.reservation.infrastructure.ReservationRepository;
import com.pickcar.vehicle.domain.FuelType;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile({"local", "test", "docker"})
public class InitData implements CommandLineRunner {

    //FIXME: Service를 호출하는것이 바람직함
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ReservationRepository reservationRepository;
    private final DriveHistoryRepository driveHistoryRepository;
    private final PasswordEncoder  passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findByInfoEmail("user1@kernel.com").isEmpty()) {
            initDummyCompanies();
            initDummyUsers();
            initDummyVehicles();
            initDummyReservations();
            initDummyDriveHistories();
        }
    }

    private void initDummyCompanies() {
        companyRepository.saveAll(
                IntStream.iterate(1, i -> i + 1)
                        .limit(5)
                        .mapToObj(i -> new Company(
                                "company" + i,
                                "address" + i,
                                "phoneNumber" + i,
                                "company" + i + "@kernel.com",
                                "더미 회사입니다",
                                "0000000000" + i,
                                ContractStatus.ACTIVE
                        ))
                        .toList()
        );
    }

    private void initDummyUsers() {
        userRepository.saveAll(
                LongStream.iterate(1L, i -> i + 1)
                        .limit(5L)
                        .mapToObj(i -> new User(
                                i,
                                new UserInfo("user" + i + "@kernel.com", passwordEncoder.encode("1234"), "user" + i, "0101234567" + i),
                                UserRole.EMPLOYEE,
                                UserStatus.ACTIVE
                        ))
                        .toList());
    }

    private void initDummyVehicles() {
        vehicleRepository.saveAll(
                IntStream.iterate(1, i -> i + 1)
                        .limit(10)
                        .mapToObj(i -> new Vehicle(
                                new VehicleInfo("model" + i, "color" + i, "가나다" + i,
                                        "200", "Brand", FuelType.DIESEL),
                                VehicleStatus.OPERABLE,
                                true,
                                false
                        ))
                        .toList());
    }

    private void initDummyReservations() {
        reservationRepository.saveAll(
                LongStream.iterate(1L, i -> i + 1)
                        .limit(5)
                        .mapToObj(i -> new Reservation(
                                i,
                                i,
                                LocalDateTime.now(),
                                LocalDate.now().plusDays(50),
                                null,
                                ReservationStatus.RESERVED
                        )).toList());
    }

    private void initDummyDriveHistories() {
        driveHistoryRepository.saveAll(
                LongStream.iterate(1L, i -> i + 1)
                        .limit(5)
                        .mapToObj(i -> new DriveHistory(
                                i,
                                LocalDateTime.now().minusHours(2),
                                LocalDateTime.now().minusMinutes(30),
                                20.0D,
                                LocalTime.of(1, 30)
                        )).toList()
        );
    }
}
