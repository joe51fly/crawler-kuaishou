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
@ApiModel(value="AnchorLiveInfoTemp对象", description="")
public class AnchorLiveInfoTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "对应myfavorite表里的anchor_id")
    private String userEid;

    @ApiModelProperty(value = "直播封面")
    private String rtCoverUrl;

    @ApiModelProperty(value = "m3u8直播视频链接")
    private String hlsPlayUrl;

    @ApiModelProperty(value = "flv直播视频链接")
    private String playUrls;

    @ApiModelProperty(value = "上次更新的时间")
    private Date updateTime;


}
