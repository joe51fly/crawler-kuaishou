<template>
  <div>
    <el-row type="flex" align="center" :span="10" :id="`imgDiv${theVideoIndex}`">
      <div class="grid-content dataCol">
        <img v-if="showWhat === 'liveList'"
             :src="listData.rtCoverUrl"
             @click="liveUrlPicFun(listData.hlsPlayUrl,listData.playUrls[0].url,$event,theVideoIndex)"
             v-show="isImgShow"
             class="imgClass"/>
        <img v-else-if="showWhat === 'mySpecialFollow'"
             :src="listData.rtCoverUrl"
             @click="liveUrlPicFun(listData.hlsPlayUrl,listData.playUrls,$event,theVideoIndex)"
             v-show="isImgShow"
             class="imgClass"/>
        <br/>

        <span v-if="showWhat === 'liveList'" style="font-size: 1.3rem">
            {{ listData.user.user_name }}
          </span>
        <span v-else-if="showWhat === 'mySpecialFollow'" style="font-size: 1.3rem">
            {{ listData.userName }}
          </span>

        <!--
          mouseover和mouseenter都是鼠标移到元素身上就触发，区别是
          1、mouseover经过自身盒子触发，经过子盒子也触发，拥有冒泡特性
          2、mouseenter只经过自身盒子触发，没有冒泡特性
          mouseenter不会冒泡，搭配鼠标离开事件mouseleave同样不会冒泡
        -->
        <el-dropdown @mouseenter.native="getIsMyfavoriteByEid($event,theVideoIndex)">
          <i class="el-icon-arrow-down"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-show="isMySpecialFavorite?false:true"
                              @click.native="addOrRemoveMyfavorite($event,theVideoIndex,true)">添加
            </el-dropdown-item>
            <el-dropdown-item v-show="isMySpecialFavorite?true:false"
                              @click.native="addOrRemoveMyfavorite($event,theVideoIndex,false)">移除
            </el-dropdown-item>
            <el-dropdown-item
              @click.native="setOrCancelIsTop($event,theVideoIndex,true)">置顶
            </el-dropdown-item>
            <el-dropdown-item
              @click.native="setOrCancelIsTop($event,theVideoIndex,false)">取消置顶
            </el-dropdown-item>

            <el-dropdown-item v-show="theAnchorTopNumber == 9?false:true"
                              @click.native="setOrCancelTheSuperTop($event,theVideoIndex,true)">设置超级置顶
            </el-dropdown-item>
            <el-dropdown-item v-show="theAnchorTopNumber == 9?true:false"
                              @click.native="setOrCancelTheSuperTop($event,theVideoIndex,false)">取消超级置顶
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

      </div>
    </el-row>
  </div>
</template>

<script>
import $ from "jquery";


export default {
  props: {
    listData: {
      type: Object
    },
    /*该视频的索引位置*/
    theVideoIndex: {
      type: Number
    },
    lastLiItemIdex: {
      type: Number
    },
    myVideoPlayer: Object,
    liveListAllData: Array,
    showWhat: String,
    mySpecialFollowData:Array
  },
  data() {
    return {
      //播放视频时隐藏点击的图片
      isImgShow: true,
      // theAnchorInfoByEid:String,
      isMySpecialFavorite: Boolean,
      /*主播置顶值，根据大小排序 0-9 0表示未置顶 置顶选项只能设置0-8 9表示超级置顶 */
      theAnchorTopNumber: Number,
      videoSource: {
        type: '',
        src: '' //你的m3u8地址（必填）
      },
      theVideoPlayer: {},
      isSwitchM3u8: false,
      show: String,
    }
  },
  methods: {
    liveUrlPicFun(m3u8Url, flvUrl, event, theVideoIndex) {
      this.theVideoPlayer = this.myVideoPlayer;

      const thisVideoDivBox = $('#videoDivBox' + theVideoIndex);
      const lastVideoDivBox = $('#videoDivBox' + this.lastLiItemIdex);
      const thisCloseVideoDivImg = $('#videoDivBox' + theVideoIndex + ' + div');
      const lastCloseVideoDivImg = $('#videoDivBox' + this.lastLiItemIdex + ' + div');
      const thisLiveImgDiv = $('#imgDiv' + theVideoIndex);
      const lastLiveImgDiv = $('#imgDiv' + this.lastLiItemIdex);
      // console.log("this.theVideoPlayer", this.theVideoPlayer);
      if (this.theVideoPlayer !== undefined) {
        if (this.isSwitchM3u8) {
          this.theVideoPlayer.dispose();/*!//停止*/
          $('.playIpt').val(m3u8Url);
        } else {
          $('#videoPlayer').hide();/*!//点击关闭*/
          console.log("theVideoPlayer", this.theVideoPlayer);
          this.theVideoPlayer.dispose();/*!//停止*/
          lastVideoDivBox.css('display', 'none');
          lastVideoDivBox.css('zIndex', 4);
          this.$parent.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
          lastCloseVideoDivImg.css('display', 'none');
          lastLiveImgDiv.css('display', 'block');
          $('.playIpt').val(flvUrl);
        }
      } else {
        if (this.isSwitchM3u8) {
          $('.playIpt').val(m3u8Url);
        } else {
          $('.playIpt').val(flvUrl);
        }
      }
      const videoBox = "<div id=\"videoDiv\" ref=\"videoBox2\"><div id=\"videoPlayer\" style=\"width: 20rem\"><video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;' preload=\"none\" controls=\"true\" autoplay=\"autoplay\"></video></div></div>";
      thisVideoDivBox.css('display', 'block');
      this.$parent.$refs.name[theVideoIndex].childNodes[2].innerHTML = videoBox;
      $('.videos').show();/*//视频窗口弹出*/
      if (this.isSwitchM3u8) {
        this.videoSource.src = m3u8Url;
        this.videoSource.type = m3u8Type;
      } else {
        this.videoSource.src = flvUrl;
        this.videoSource.type = flvType;
      }
      this.theVideoPlayer = videojs("video", {
          playbackRates: [0.5, 1.0, 1.5, 2.0, 3.0], //播放速度
          autoplay: true, //如果true,浏览器准备好时开始回放。
          // notSupportedMessage: "此视频暂时无法播放,请稍后再试",
          muted: false, // 默认情况下将会消除任何音频。
          loop: false, // 导致视频一结束就重新开始。
          preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
          language: 'zh-CN',
          aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
          fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
          techOrder: ['html5', 'flvjs'], //html5模式和flvjs模式
          flvjs: { //配置flv相关信息 如果播放flv才配置这个
            mediaDataSource: {
              isLive: true, //是否是直播
              cors: true,//是否跨域
              withCredentials: false,//是否跨站检测
            },
            preload: false,//预加载
            autoplay: true,//是否自动播放
          },
          sources: [
            this.videoSource
          ],
          // width: document.documentElement.clientWidth
          // notSupportedMessage: '此视频暂无法播放，请稍后再试' //允许覆盖Video.js无法播放媒体源时显示的默认信息。
          //  controlBar: {
          //   timeDivider: true,
          //   durationDisplay: true,
          //   remainingTimeDisplay: false,
          //   fullscreenToggle: true //全屏按钮
          //  }
        },
      );
      thisLiveImgDiv.css('display', 'none');
      thisCloseVideoDivImg.css('display', 'block');
      // this.lastLiItemIdex = theVideoIndex;
      this.$emit("setLastLiIndex", theVideoIndex)
      this.$emit("setMyVideoPlayer", this.theVideoPlayer);
      this.$emit("setThePlayingVideoUrl", $('.playIpt').val());
    },

    getIsMyfavoriteByEid(event, theVideoIndex) {
      let eid;
      if (this.showWhat === 'liveList'){
        console.log('liveListAllData-eid', this.liveListAllData[theVideoIndex].user.eid);
        eid = this.liveListAllData[theVideoIndex].user.eid;
      }else if (this.showWhat === 'mySpecialFollow'){
        console.log('mySpecialFollowData-eid', this.mySpecialFollowData[theVideoIndex].userEid);
        eid = this.mySpecialFollowData[theVideoIndex].userEid;
      }
      this.$http.post(baseUrl + '/live/getIsMyfavoriteByEid',
        {
          inputEid: eid,
        },
        {
          emulateJSON: true
        }).then(isMyfavoriteByEidRes => {
        // console.log("isMyfavoriteByEidRes:", isMyfavoriteByEidRes);
        if (isMyfavoriteByEidRes.status !== 200) {
          this.$notify({
            title: '失败',
            message: "出问题了，请联系网站管理员查找原因：" + isMyfavoriteByEidRes.status,
            type: 'error'
          });
        } else if (isMyfavoriteByEidRes.body.success) {
          console.log("isMySpecialFavorite", isMyfavoriteByEidRes.body.data.result.myfavorite);
          this.isMySpecialFavorite = isMyfavoriteByEidRes.body.data.result.myfavorite;
          this.theAnchorTopNumber = isMyfavoriteByEidRes.body.data.result.isTop;
        } else {
          this.$notify({
            title: '失败',
            message: isMyfavoriteByEidRes.body.message,
            type: 'error'
          });
        }
      }).catch(function (isMyfavoriteByEidRes) {
        //出错处理
        console.log(isMyfavoriteByEidRes)
      });
    },
    addOrRemoveMyfavorite(event, theVideoIndex, isMyfavorite) {
      let eid;
      if (this.showWhat === 'liveList'){
        eid = this.liveListAllData[theVideoIndex].user.eid;
      }else if (this.showWhat === 'mySpecialFollow'){
        eid = this.mySpecialFollowData[theVideoIndex].userEid;
      }
      this.$http.post(baseUrl + '/live/updateMyfavLiveInfoByIsMyfavorite',
        {
          inputEid: eid,
          inputIsMyfavorite: isMyfavorite,
        },
        {
          emulateJSON: true
        }).then(mySpecFollowDataRes => {
        console.log("mySpecFollowDataRes:", mySpecFollowDataRes);
        if (mySpecFollowDataRes.status !== 200) {
          this.$notify({
            title: '失败',
            message: "出问题了，请联系网站管理员查找原因：" + mySpecFollowDataRes.status,
            type: 'error'
          });
        } else if (mySpecFollowDataRes.body.success) {
          this.$notify({
            title: mySpecFollowDataRes.data.data.userName,
            message: mySpecFollowDataRes.body.message,
            type: 'success'
          });
        } else {
          this.$notify({
            title: '失败',
            message: mySpecFollowDataRes.body.message,
            type: 'error'
          });
        }
      }).catch(function (mySpecFollowDataRes) {
        //出错处理
        console.log(mySpecFollowDataRes)
      });
    },

    /*设置置顶*/
    setOrCancelIsTop(event, theVideoIndex, isTop) {
      let eid;
      if (this.showWhat === 'liveList'){
        eid = this.liveListAllData[theVideoIndex].user.eid;
      }else if (this.showWhat === 'mySpecialFollow'){
        eid = this.mySpecialFollowData[theVideoIndex].userEid;
      }
      this.$http.post(baseUrl + '/live/updateForTheTopByIsTop',
        {
          inputEid: eid,
          isSetTop: isTop,
        },
        {
          emulateJSON: true
        }).then(res => {
        console.log("res:", res);
        if (res.status !== 200) {
          this.$notify({
            title: '失败',
            message: "出问题了，请联系网站管理员查找原因：" + res.status,
            type: 'error'
          });
        } else if (res.body.success) {
          this.$notify({
            title: res.data.data.userName,
            message: res.body.message,
            type: 'success'
          });
        } else {
          this.$notify({
            title: '失败',
            message: res.body.message,
            type: 'error'
          });
        }
      }).catch(function (mySpecFollowDataRes) {
        //出错处理
        console.log(mySpecFollowDataRes)
      });
    },

    /*设置超级置顶*/
    setOrCancelTheSuperTop(event, theVideoIndex, isSuperTop) {
      // console.log(this.liveListAllData[theVideoIndex].user.eid);
      let eid;
      if (this.showWhat === 'liveList'){
        eid = this.liveListAllData[theVideoIndex].user.eid;
      }else if (this.showWhat === 'mySpecialFollow'){
        eid = this.mySpecialFollowData[theVideoIndex].userEid;
      }
      this.$http.post(baseUrl + '/live/updateSuperSet_top',
        {
          inputEid: eid,
          isSetSuper_top: isSuperTop,
        },
        {
          emulateJSON: true
        }).then(res => {
        // console.log("res:", res);
        if (res.status !== 200) {
          this.$notify({
            title: '失败',
            message: "出问题了，请联系网站管理员查找原因：" + res.status,
            type: 'error'
          });
        } else if (res.body.success) {
          this.$notify({
            title: res.data.data.userName,
            message: res.body.message,
            type: 'success'
          });
        } else {
          this.$notify({
            title: '失败',
            message: res.body.message,
            type: 'error'
          });
        }
      }).catch(function (mySpecFollowDataRes) {
        //出错处理
        console.log(mySpecFollowDataRes)
      });
    },
  },
}
</script>

<style>
.imgClass {
  height: 381.5px;
  width: 231px;
}
</style>
