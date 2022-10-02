package com.joe.kuaishou.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author joe
 * @since 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AnchorInfo对象", description="")
public class AnchorInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "主播名字")
    private String anchorName;

    @ApiModelProperty(value = "主播id")
    private String anchorId;

    @ApiModelProperty(value = "主链接播头像")
    private String anchorHeaderUrl;

    @ApiModelProperty(value = "主播简介")
    private String description;

    @ApiModelProperty(value = "是否是特别关注 不是:0 是:1")
    private Boolean isMyfavorite;

    @ApiModelProperty(value = "上一次更新的时间")
    private Date updateTime;

    @ApiModelProperty(value = "0:女 1:男")
    private Boolean gender;

    @ApiModelProperty(value = "置顶")
    private Integer isTop;

    @ApiModelProperty(value = "作品数量")
    private Integer photoCounts;

    @ApiModelProperty(value = "粉丝数量")
    private Integer fans;


}
