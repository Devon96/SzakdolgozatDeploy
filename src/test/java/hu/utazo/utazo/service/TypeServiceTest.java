package hu.utazo.utazo.service;

import hu.utazo.utazo.model.Type;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class TypeServiceTest {

    @Autowired
    TypeService typeService;

    @Test
    @DisplayName("Típus hozzáadása")
    public void testAddType(){

        Assert.assertEquals(typeService.findAll().size(), 0);

        Type type = new Type();
        type.setName("TESTTYPE");
        typeService.add(type);
        type.setName("testtype");
        Assert.assertEquals(typeService.findAll(), Collections.singletonList(type));

    }
}
