package ru.otus.hw;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@UtilityClass
public class MvcMockHelper {
    public static MockHttpServletRequestBuilder builder(String url, HttpMethod method) {
        if (method == HttpMethod.GET) {
            return get(url);
        }
        if (method == HttpMethod.POST) {
            return post(url);
        }
        if (method == HttpMethod.PATCH) {
            return patch(url);
        }
        if (method == HttpMethod.DELETE) {
            return delete(url);
        }
         throw new IllegalArgumentException("Method '" + method + "' is not supported");
    }
}
