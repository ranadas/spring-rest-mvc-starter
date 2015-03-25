package com.rdas.controller;

import com.rdas.config.AppConfig;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by rdas on 25/03/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class TestIndexController {

    private static final Logger log = Logger.getLogger(TestIndexController.class);
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testAssertAllNotNull() {
        log.debug("Test One");
    }

    //@Test
    public void getHome() throws Exception {
        this.mockMvc.perform(get("/")).
                andDo(print()).
                andExpect(status().isOk());
        // .andExpect(forwardedUrl("/WEB-INF/views/home.jsp"));
    }

    /**/
    //@Formatter:off

//    @Test
//    public void getHTML() throws Exception
//    {
//        /*
//            This following code will do 'GET' to the web apps
//            and check that the return view is "helloworld"
//            and also that it has a attribute "user" to "JohnathanMarkSmith"
//
//         */
//        this.mockMvc.perform(get("/ask/something")
//                .accept(MediaType.TEXT_HTML))
//                .andExpect(status().isOk())
//                .andExpect(view().name("helloworld"))
//                .andExpect(MockMvcResultMatchers.model().attribute("user", "rdas"));
//
//
//    }
//
//    @Test
//    public void getJSON() throws Exception
//    {
//        /*
//            This following code will do 'GET' to the web apps
//            and also that it has a attribute "user" to "rana"
//
//         */
//        this.mockMvc.perform(get("/json/apiget")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.user").value("Rana Das"));
//    }
//
}
