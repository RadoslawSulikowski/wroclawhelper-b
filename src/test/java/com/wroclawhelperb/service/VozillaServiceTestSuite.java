package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class VozillaServiceTestSuite {

    @Autowired
    private VozillaService vozillaService;

    @Test
    public void testGetVozillaCarList() {
        List<VozillaCarDto> list = vozillaService.getVozillaCarList();
        list.forEach(System.out::println);
    }


}
