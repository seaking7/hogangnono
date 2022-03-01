package com.tk.hogangnono.job.apt;

import com.tk.hogangnono.BatchTestConfig;
import com.tk.hogangnono.adapter.ApartmentApiResource;
import com.tk.hogangnono.core.repository.LawdRepository;
import com.tk.hogangnono.core.service.AptDealService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptDealInsertJobConfig.class, BatchTestConfig.class})
class AptDealInsertJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @MockBean
    private AptDealService aptDealService;

    @MockBean
    private LawdRepository lawdRepository;

    @MockBean
    private ApartmentApiResource apartmentApiResource;


    @Test
    public void success() throws Exception{

        //given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("27710", "41135"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(
                new ClassPathResource("test-api-response.xml"));

        //when
        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("2022-01"))));

        //then
        Assertions.assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(aptDealService, times(4)).upsert(any());
    }

    @Test
    public void fail_whenYearMonthNotExist() throws Exception{
        //Given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("27710"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(
                new ClassPathResource("test-api-response.xml"));

        //When
        Assertions.assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob());

        //Then
        verify(aptDealService, never()).upsert(any());

    }


}