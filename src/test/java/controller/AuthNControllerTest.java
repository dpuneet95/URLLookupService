package controller;

import com.urllookup.Application;
import com.urllookup.controller.AuthNController;
import com.urllookup.model.URL;
import com.urllookup.respository.URLRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static com.urllookup.Constants.FAILURE;
import static com.urllookup.Constants.SUCCESS;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthNController.class)
@ContextConfiguration(classes = Application.class)

public class AuthNControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private URLRepository urlRepository;

    @Test
    public void check_malware_url() throws Exception {
        given(urlRepository.findById(anyString())).willReturn(Optional.of(new URL("www.xyz.com")));

        MvcResult result = mvc.perform(get("/v1/urlinfo/www.xyz.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(FAILURE));
    }

    @Test
    public void check_non_malware_url() throws Exception {
        given(urlRepository.findById(anyString())).willReturn(Optional.empty());

        MvcResult result = mvc.perform(get("/v1/urlinfo/www.xyz.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains(SUCCESS));
    }

    @Test
    public void check_empty_url() throws Exception {
        given(urlRepository.findById(anyString())).willReturn(Optional.empty());

        mvc.perform(get("/v1/urlinfo/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void check_url_with_reserved_characters() throws Exception {

        mvc.perform(get("/v1/urlinfo/https://www/xyz.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
