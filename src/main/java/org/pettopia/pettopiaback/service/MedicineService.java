package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.Medicine;
import org.pettopia.pettopiaback.domain.Pet;
import org.pettopia.pettopiaback.dto.MedicineDTO;
import org.pettopia.pettopiaback.exception.NotFoundException;
import org.pettopia.pettopiaback.repository.MedicineRepository;
import org.pettopia.pettopiaback.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class MedicineService {

    private final PetRepository petRepository;

    private final MedicineRepository medicineRepository;


    public void makeMedicineList(Long petPk, MedicineDTO.RequestMedicineList requestMedicineList
    ) throws RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        List<MedicineDTO.RequestMedicine> medicineRequests = requestMedicineList.getList();

        for (MedicineDTO.RequestMedicine requestMedicine : medicineRequests) {
            Medicine medicine = Medicine.builder()
                    .name(requestMedicine.getName())
                    .cnt(requestMedicine.getCnt())
                    .pet(pet)
                    .build();

            medicineRepository.save(medicine);
        }
    }

    public MedicineDTO.ResponseMedicineList getMedicinesByPetPk(Long petPk) {
        List<Medicine> medicines = medicineRepository.findByPetPk(petPk);

        List<MedicineDTO.RequestMedicine> medicineDTOList = medicines.stream()
                .map(medicine -> new MedicineDTO.RequestMedicine(medicine.getName(), medicine.getCnt()))
                .collect(Collectors.toList());

        return new MedicineDTO.ResponseMedicineList(medicineDTOList.size(), medicineDTOList);
    }

    public void updateMedicineList(Long petPk, MedicineDTO.RequestMedicineList requestMedicineList
    ) throws RuntimeException {

        Pet pet = petRepository.findById(petPk)
                .orElseThrow(() -> new NotFoundException("해당하는 사용자가 없습니다."));

        List<Medicine> existingMedicines = pet.getMedicineList();
        List<MedicineDTO.RequestMedicine> requestMedicines = requestMedicineList.getList();

        // 기존 약 정보의 이름 리스트를 추출
        List<String> existingMedicineNames = existingMedicines.stream()
                .map(Medicine::getName)
                .collect(Collectors.toList());

        // 새로운 요청의 약 정보 이름 리스트를 추출
        List<String> requestMedicineNames = requestMedicines.stream()
                .map(MedicineDTO.RequestMedicine::getName)
                .collect(Collectors.toList());

        // 요청에 없는 기존 약 정보 삭제
        Iterator<Medicine> iterator = existingMedicines.iterator();
        while (iterator.hasNext()) {
            Medicine medicine = iterator.next();
            if (!requestMedicineNames.contains(medicine.getName())) {
                iterator.remove();
                medicineRepository.delete(medicine);
            }
        }

        // 요청 약 정보 추가 및 업데이트
        for (MedicineDTO.RequestMedicine requestMedicine : requestMedicines) {
            if (existingMedicineNames.contains(requestMedicine.getName())) {
                // 기존 약 정보 업데이트
                Medicine existingMedicine = existingMedicines.stream()
                        .filter(medicine -> medicine.getName().equals(requestMedicine.getName()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Unexpected error: Medicine not found"));

                existingMedicine.setCnt(requestMedicine.getCnt());
                medicineRepository.save(existingMedicine);

            } else {
                // 새로운 약 정보 추가
                Medicine newMedicine = Medicine.builder()
                        .name(requestMedicine.getName())
                        .cnt(requestMedicine.getCnt())
                        .pet(pet)
                        .build();

                medicineRepository.save(newMedicine);
            }
        }


    }



}
