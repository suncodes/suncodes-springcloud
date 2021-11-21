package feign.suncodes;

import feign.Param;
import feign.RequestLine;

public interface FeignDeptAction {
    @RequestLine("GET /dept/list")
    public String list();

    @RequestLine("GET /dept/get/{id}")
    public String getById(@Param("id") Long id);
}
