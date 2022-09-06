package tech.outspace.papershare.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.outspace.papershare.model.dto.UserDto;
import tech.outspace.papershare.model.vo.EmailCheckVo;
import tech.outspace.papershare.model.vo.RegisterVo;
import tech.outspace.papershare.repo.objs.UserRepo;
import tech.outspace.papershare.utils.convert.JsonConvert;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthControlTest extends AbstractJUnit4SpringContextTests {

    private final ObjectMapper mapper = JsonConvert.instance();
    String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJQYXBlclNoYXJlIiwic2Vzc2lvbklkIjo0MDU3NDc0NzA1ODExNDU2MCwiZXhwIjoxNjUwNjUwOTMwLCJ1c2VySWQiOjQwNTc0NTM4NjM4OTUwNDAwfQ.Rmkl6do7MlAoyEutfUY9ZdrqqUhdQrwAJ7vSe0OhZYZR5iKjjgd7lXECvs5xTNhS5MczHJoLfqGcVBRl8nIcTA";
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private UserRepo userRepo;
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private Result<UserDto> parseResponseData(String body) throws JsonProcessingException {
        return mapper.readValue(body, new TypeReference<Result<UserDto>>() {
        });
    }

    private String parseJwtHeader(MockHttpServletResponse response) {
        String header = response.getHeader(HttpFormat.authHeader);
        try {
            header = header.split("")[1];
        } catch (Exception ex) {
            throw new RuntimeException();
        }
        return header;
    }

    private MockHttpServletResponse performMock(RequestBuilder request) throws Exception {
        return mvc
                .perform(request)
                .andDo(MockMvcResultHandlers.print()).
                andReturn()
                .getResponse();
    }

    private RequestBuilder buildRequest(String path, String json) {
        return post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json);
    }

    private String doRequest(String path, String json) throws Exception {
        RequestBuilder request = buildRequest(path, json);
        System.out.println("-------------- HTTP Response -----------------");
        MockHttpServletResponse response = performMock(request);
        Assertions.assertEquals(EResult.SUCCESS.getCode().intValue(), response.getStatus());
        return response.getContentAsString();
    }

    @Test
    void emailCodeSend() throws Exception {
        EmailCheckVo vo = new EmailCheckVo("papershare2022@163.com");
        String sender = mapper.writeValueAsString(vo);
        doRequest("/auth/email/code", sender);
        System.out.println("-------------- Email Send Successful -----------------");
    }

    @Test
    void emailDupCheck() throws Exception {
        EmailCheckVo vo = new EmailCheckVo("papershare2022@163.com");
        String sender = mapper.writeValueAsString(vo);
        doRequest("/auth/email/duplicate", sender);
        System.out.println("-------------- Email Check Successful -----------------");
    }

    @Test
    void register() throws Exception {
        RegisterVo vo = new RegisterVo("papershare2022@163.com", "papershare", "48161639", "717210");
        String sender = mapper.writeValueAsString(vo);
        String receive = doRequest("/auth/register", sender);
        Result<UserDto> result = parseResponseData(receive);
        System.out.println("-------------- Json Data -----------------");
        System.out.println(result);
        System.out.println("-------------- Register Successful -----------------");
    }

    @Test
    void login() {
    }

    @Test
    void logout() throws Exception {
        RequestBuilder request = post("/auth/logout").contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8").header("Authorization", "Bearer " + jwt);
        MockHttpServletResponse response = performMock(request);
        Assertions.assertEquals(EResult.SUCCESS.getCode().intValue(), response.getStatus());
    }
}