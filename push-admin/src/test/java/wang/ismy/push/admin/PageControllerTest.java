package wang.ismy.push.admin;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PageControllerTest {

    private MockMvc mockMvc;

    @Test
    public void homePage() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new PageController()).build();

        mockMvc.perform(get("/")

        )
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/list.html"));
    }
}