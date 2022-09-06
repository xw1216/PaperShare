package tech.outspace.papershare.model.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import tech.outspace.papershare.utils.convert.JsonConvert;

class EmailCheckVoTest {

    @Test
    public void printJson() throws JsonProcessingException {
        EmailCheckVo vo = new EmailCheckVo("xw1216@outlook.com");
        String json = JsonConvert.toJson(vo);
        System.out.println(json);
    }

}