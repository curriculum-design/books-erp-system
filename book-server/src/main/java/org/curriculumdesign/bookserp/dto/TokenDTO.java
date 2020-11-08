package org.curriculumdesign.bookserp.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lesl
 */
@Data
@AllArgsConstructor
public class TokenDTO {

    @ApiModelProperty(value = "token", required = true, example = "112321321")
    private String token ;
}
