package eureka.client.provider.sentinel.pojo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResourceRoleQps {
    private Long id;
    private String appId;
    private String api;
    private Long limitQps;
    private Long createAt;
}
