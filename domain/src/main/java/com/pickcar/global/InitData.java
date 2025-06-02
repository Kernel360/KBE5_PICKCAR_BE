package com.pickcar.global;

import com.pickcar.company.domain.Company;
import com.pickcar.company.domain.ContractStatus;
import com.pickcar.company.infrastructure.CompanyRepository;
import com.pickcar.vehicle.domain.FuelType;
import com.pickcar.vehicle.domain.Vehicle;
import com.pickcar.vehicle.domain.VehicleInfo;
import com.pickcar.vehicle.domain.VehicleStatus;
import com.pickcar.vehicle.infrastructure.VehicleRepository;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    //FIXME: Service를 호출하는것이 바람직함
    private final CompanyRepository companyRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public void run(String... args) throws Exception {

        companyRepository.saveAll(
                IntStream.iterate(1, i -> i + 1)
                        .limit(5)
                        .mapToObj(i -> new Company(
                                "company" + i,
                                "address" + i,
                                "phoneNumber" + i,
                                "dummy" + i + "@kernel.com",
                                "더미 회사입니다",
                                "0000000000" + i,
                                ContractStatus.ACTIVE
                        ))
                        .toList()
        );

        vehicleRepository.saveAll(
                IntStream.iterate(1, i -> i + 1)
                        .limit(10)
                        .mapToObj(i -> new Vehicle(
                                new VehicleInfo("model" + i, "color" + i, "가나다" + i,
                                        "200", "Brand", FuelType.DIESEL),
                                VehicleStatus.OPERABLE,
                                true,
                                false,
                                true
                        ))
                        .toList());
    }
}
