package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.enums.MemberLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "会员DTO")
public class MemberDTO {

    @Schema(description = "会员ID")
    private Long id;

    @NotBlank(message = "会员姓名不能为空")
    @Schema(description = "会员姓名", required = true)
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", required = true)
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "会员等级")
    private MemberLevel level;

    @Schema(description = "当前积分")
    private Integer points;

    @Schema(description = "累计消费金额")
    private BigDecimal totalConsumption;

    @Schema(description = "储值卡余额")
    private BigDecimal storedValueBalance;

    @Schema(description = "生日")
    private LocalDate birthday;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "登录次数")
    private Integer loginCount;

    @Schema(description = "是否启用")
    private Boolean active;
}
