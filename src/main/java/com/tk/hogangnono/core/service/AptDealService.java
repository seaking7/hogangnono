package com.tk.hogangnono.core.service;

import com.tk.hogangnono.core.dto.AptDealDto;
import com.tk.hogangnono.core.entity.Apt;
import com.tk.hogangnono.core.entity.AptDeal;
import com.tk.hogangnono.core.repository.AptDealRepository;
import com.tk.hogangnono.core.repository.AptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AptDealDto 에 있는 값을 Apt, AptDeal 엔티티로 저장한다.
 */
@Service
@AllArgsConstructor
public class AptDealService {
    private final AptRepository aptRepository;
    private final AptDealRepository aptDealRepository;

    @Transactional
    public void upsert(AptDealDto dto){
        Apt apt = getArtOrNew(dto);
        saveAptDeal(dto, apt);
    }

    private Apt getArtOrNew(AptDealDto dto){
        Apt apt = aptRepository.findAptByAptNameAndJibun(dto.getAptName(), dto.getJibun())
                .orElseGet(() -> Apt.from(dto));
        return aptRepository.save(apt);
    }

    private void saveAptDeal(AptDealDto dto, Apt apt){
        AptDeal aptDeal = aptDealRepository.findAptDealByAptAndExclusiveAreaAndDealDateAndDealAmountAndFloor(
                        apt, dto.getExclusiveArea(), dto.getDealDate(), dto.getDealAmount(), dto.getFloor())
                .orElseGet(() -> AptDeal.of(dto, apt));
        aptDeal.setDealCanceled(dto.isDealCanceled());
        aptDeal.setDealCanceledDate(dto.getDealCanceledDate());
        aptDealRepository.save(aptDeal);
    }
}
