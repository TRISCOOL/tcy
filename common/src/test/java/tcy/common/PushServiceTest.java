package tcy.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tcy.common.service.PushService;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class PushServiceTest {

    @Autowired
    private PushService pushService;

    @Test
    public void test(){
        pushService.pushBirthday(1L,"曲悠"," ");
    }


}
