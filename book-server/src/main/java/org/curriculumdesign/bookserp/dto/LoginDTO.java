package org.curriculumdesign.bookserp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lesl
 */
@Data
public class LoginDTO {

    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
}
