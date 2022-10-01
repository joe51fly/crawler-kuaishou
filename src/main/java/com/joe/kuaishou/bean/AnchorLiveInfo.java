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
 * 我关注的主播正在直播的信息
 * </p>
 *
 * @author joe
 * @since 2022-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AnchorLiveInfo对象", description="我关注的主播正在直播的信息")
public class AnchorLiveInfo implements Serializable {

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

    @ApiModelProperty(value = "是否正在直播：0:没有直播，1:正在直播")
    private Boolean isLive;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "上次更新的时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否是我的特别关注")
    private Boolean isMyfavorite;

    @ApiModelProperty(value = "置顶")
    private Integer isTop;


}
