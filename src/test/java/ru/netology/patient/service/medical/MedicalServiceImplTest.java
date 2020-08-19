package ru.netology.patient.service.medical;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class MedicalServiceImplTest {


    @Test
    void checkBloodPressureTest_1()  {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(2000, 1, 1),
                new HealthInfo(new BigDecimal("36.6"),new BloodPressure(120, 80)
                ));
        String id1 = "1";
        String message = String.format("Warning, patient with id: %s, need help! Reason: Blood pressure", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id1, new BloodPressure(160, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }

    @Test
    void checkBloodPressureTest_2() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream old=System.out;
        System.setOut(new PrintStream(output));

        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(2000, 1, 1),
                new HealthInfo(new BigDecimal("36.65"),new BloodPressure(120, 80)
                        ));
        String id1 = "1";
        String message = String.format("Warning, patient with id: %s, need help! Reason: Blood pressure", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doCallRealMethod().when(sendAlertService).send(message);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkBloodPressure(id1, new BloodPressure(160, 80));

        Assert.assertEquals(output.toString(), message);
        System.setOut(old);
        output.close();
    }

    @Test
    void checkTemperatureTest_1()  {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(2000, 1, 1),
                new HealthInfo(new BigDecimal("36.6"),new BloodPressure(120, 80)
                ));
        String id1 = "1";
        String message = String.format("Warning, patient with id: %s, need help! Reason: Temperature", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkTemperature(id1, new BigDecimal("37.0"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendAlertService).send(argumentCaptor.capture());
        Assertions.assertEquals(message, argumentCaptor.getValue());
    }

    @Test
    void checkTemperatureTest_2() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream old=System.out;
        System.setOut(new PrintStream(output));

        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(2000, 1, 1),
                new HealthInfo(new BigDecimal("36.6"),new BloodPressure(120, 80)
                ));
        String id1 = "1";
        String message = String.format("Warning, patient with id: %s, need help! Reason: Temperature", patientInfo.getId());

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);
        Mockito.doCallRealMethod().when(sendAlertService).send(message);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);
        medicalService.checkTemperature(id1, new BigDecimal("37.0")) ;

        Assert.assertEquals(output.toString(), message);
        System.setOut(old);
        output.close();
    }

    @Test
    void checkHealthTest()  {
        PatientInfo patientInfo = new PatientInfo("1", "Ivan", "Ivanov",
                LocalDate.of(2000, 1, 1),
                new HealthInfo(new BigDecimal("36.6"),new BloodPressure(120, 80)
                ));
        String id1 = "1";

        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepository.getById(id1)).thenReturn(patientInfo);

        SendAlertService sendAlertService = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository, sendAlertService);

        medicalService.checkBloodPressure(id1, new BloodPressure(120, 80));
        Mockito.verify(sendAlertService, Mockito.never()).send(anyString());

        medicalService.checkTemperature(id1, new BigDecimal("36.6"));
        Mockito.verify(sendAlertService, Mockito.never()).send(anyString());
    }



}
