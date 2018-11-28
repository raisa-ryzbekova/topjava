package ru.javawebinar.topjava.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ResourceControllerTest extends AbstractControllerTest {

    @Test
    void testCcs() throws Exception {
        this.mockMvc.perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("text/css")));
    }
}