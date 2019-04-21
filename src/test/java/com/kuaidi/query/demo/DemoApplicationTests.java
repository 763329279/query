package com.kuaidi.query.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    ResultActions get(String url) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders
            .get(url)).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void contextLoads() throws Exception {
        ResultActions actions = get("/v1/api/query?type=wjkwl&postId=414226368235791356");
        actions.andExpect(MockMvcResultMatchers.jsonPath("code").value(200));
    }

}
