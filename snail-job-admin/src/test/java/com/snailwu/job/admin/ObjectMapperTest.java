package com.snailwu.job.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Date;

/**
 * @author 吴庆龙
 * @date 2020/8/14 10:27 上午
 */
public class ObjectMapperTest {
    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
                .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .failOnUnknownProperties(false)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .build();

        Person person = new Person();
        person.setNickName("Mike");
        person.setBirthday(new Date());
        System.out.println(objectMapper.writeValueAsString(person));

//        String json = "{\"nick_name\":\"Mike\",\"birthday\":\"2020-08-14 10:29:41\"}";
//        Person person1 = objectMapper.readValue(json, Person.class);
//        System.out.println(person1.getNickName());
//        System.out.println(DateFormatUtils.format(person1.getBirthday(), "yyyy-MM-dd HH:mm:ss"));

    }

    static class Person {
        private String nickName;
        private Date birthday;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }
}
