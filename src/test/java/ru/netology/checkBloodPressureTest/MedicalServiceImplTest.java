package ru.netology.checkBloodPressureTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    public void medicalServiceImplTests() {
        PatientInfoFileRepository med = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(med.getById(Mockito.anyString())).thenReturn(
                new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService send = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(med, send);
        medicalService.checkBloodPressure("1", new BloodPressure(160, 91));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(send).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 1, need help", argumentCaptor.getValue());
    }

    @Test
    public void checkTemperatureTests() {
        PatientInfoFileRepository med = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(med.getById(Mockito.anyString())).thenReturn(
                new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService send = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(med, send);
        medicalService.checkTemperature("1", new BigDecimal("35"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(send).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 1, need help", argumentCaptor.getValue());
    }

    @Test
    public void checkNormalTests() {
        PatientInfoFileRepository med = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(med.getById(Mockito.anyString())).thenReturn(
                new PatientInfo("1", "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
        );
        SendAlertService send = Mockito.mock(SendAlertService.class);
        MedicalService medicalService = new MedicalServiceImpl(med, send);
        medicalService.checkTemperature("1", new BigDecimal("36.65"));
        medicalService.checkBloodPressure("1", new BloodPressure(120, 80));

        Mockito.verify(send, Mockito.atLeast(0)).send(Mockito.anyString());
        Mockito.verify(send, Mockito.atMost(1)).send(Mockito.anyString());
    }
}
