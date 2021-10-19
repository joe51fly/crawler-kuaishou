<template>
  <div align="center">
    <h2 style="color: hotpink">我的关注</h2>
    <h3>当前直播人数:<span style="color: red">{{ list.length }}</span></h3>
    <br/>
    <div id="inputData">
      <br>
    </div>
      <li :id="`liId+${index}`" v-for="(listData,index) in list.slice(startListIndex, endListIndex)"
          style="display:inline-block;width: 327px"
          :key=index
          ref="name"
          v-loading="isLoading">
        <el-row type="flex" align="center" :span="10" :id="`imgDiv${index}`">
          <div id="dataCol" class="grid-content">
            <img :src="listData.rtCoverUrl"
                 @click="liveUrlPicFun(listData.hlsPlayUrl,listData.playUrls[0].url,$event,index)"
                 v-show="isImgShow"
                 class="imgClass"/>
            <br/>
            <span style="font-size: 1.3rem">{{ listData.user.user_name }}</span>

            <!--
              mouseover和mouseenter都是鼠标移到元素身上就触发，区别是
              1、mouseover经过自身盒子触发，经过子盒子也触发，拥有冒泡特性
              2、mouseenter只经过自身盒子触发，没有冒泡特性
              mouseenter不会冒泡，搭配鼠标离开事件mouseleave同样不会冒泡
            -->
            <el-dropdown @mouseenter.native="getIsMyfavoriteByEid(listData.user.user_name,$event,index)">
              <i class="el-icon-arrow-down"></i>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-show="isMySpecialFavorite?false:true"
                                  @click.native="addMyfavorite(listData.user.user_name,$event,index)">添加
                </el-dropdown-item>
                <el-dropdown-item v-show="isMySpecialFavorite?true:false"
                                  @click.native="removeMyfavorite(listData.user.user_name,$event,index)">移除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>

          </div>
        </el-row>
        <div :id="`videoDivBox${index}`" v-show="isVideoDivBoxShow" class="videoDivBoxClass"></div>

        <div v-show="isVideoDivBoxShow">
          <el-button circle size="small" class="videoClose" type="text"
                     @click="switchVideoUrl(listData.hlsPlayUrl,listData.playUrls[0].url,$event,index)">切换源
          </el-button>
          <el-button circle size="small" class="videoClose" @click="closeVideoPlayer" type="text">关闭</el-button>
          <!--        <img class='videoClose'  @click="closeVideoPlayer" :src="closeImgPath" />-->
          <br/>
          <input type="text" class="playIpt" placeholder="正在播放的视频链接"/>
        </div>
      </li>
    <div class="hint" v-show="isShowHint">上拉加载，获取更多内容</div>
  </div>
</template>

<script>
import $ from 'jquery';

let myPlayer;

export default {
  name: 'live-list',
  props: {
    list: [],
    myFavoriteList: [],
    myFavoriteObject: {},
  },
  components: {},
  data() {
    return {
      //项目打包后图片加载不到 所以给前面加上/ks-vue/
      // closeImgPath: '/static/pic/close.jpg',
      // closeImgBuildPath: '/ks-vue/static/pic/close.jpg',
      // closeImgPath,
      //这个属性好像没啥用
      isLoading: false,
      inputVideoUrl: '',
      //播放视频后把当前视频的链接设置进来
      // playVideoUrl: '',
      //页面初始化隐藏videoboxdiv容器
      isVideoDivBoxShow: false,
      //上一个点击的图片的索引
      lastLiItemIdex: 0,
      //播放视频时隐藏点击的图片
      isImgShow: true,
      //限制初始化页面的时候数据的条数
      startListIndex: 0,
      endListIndex: 20,
      //防止底部一次滑动触发多次刷新的方法
      isRefreshBool: true,
      isShowHint: false,
      // isShowVideoUrl:false
      videoSource: {
        type: '',
        src: '' //你的m3u8地址（必填）
      },
      isSwitchM3u8: false,
      // theAnchorInfoByEid:String,
      isMySpecialFavorite: Boolean,
    }
  },
  methods: {
    switchVideoUrl(liveUrlFromPic, flvUrl, event, index) {
      this.isSwitchM3u8 = true;
      this.$options.methods.liveUrlPicFun.bind(this)(liveUrlFromPic, flvUrl, event, index);
      this.isSwitchM3u8 = false;
    },

    getIsMyfavoriteByEid(user_name, event, index) {
      console.log('eid', this.list[index].user.eid);
      console.log('user_name', user_name);
      const eid = this.list[index].user.eid;
      this.$http.post(baseUrl + '/live/getIsMyfavoriteByEid',
        {
          inputEid: eid,
        },
        {
          emulateJSON: true
        }).then(isMyfavoriteByEidRes => {
        // console.log("isMyfavoriteByEidRes:", isMyfavoriteByEidRes);
        if (isMyfavoriteByEidRes.status !== 200) {
          this.$message({
            showClose: true,
            message: "出问题了，请联系网站管理员查找原因：" + isMyfavoriteByEidRes.status,
            type: 'error',
            duration: 10000,
          });
        } else if (isMyfavoriteByEidRes.body.success) {
          // console.log("isMySpecialFavorite",isMyfavoriteByEidRes.body.data.result.myfavorite);
          this.isMySpecialFavorite = isMyfavoriteByEidRes.body.data.result.myfavorite;
        } else {
          this.$message({
            showClose: true,
            message: isMyfavoriteByEidRes.body.message,
            type: 'error',
            duration: 10000,
          });
        }
      }).catch(function (isMyfavoriteByEidRes) {
        //出错处理
        console.log(isMyfavoriteByEidRes)
      });
    },

    addMyfavorite(user_name, event, index) {
      // console.log(this.list[index].user.eid);
      const eid = this.list[index].user.eid;
      this.$http.post(baseUrl + '/live/updateMyfavLiveInfoByIsMyfavorite',
        {
          inputEid: eid,
          inputIsMyfavorite: true,
        },
        {
          emulateJSON: true
        }).then(mySpecFollowDataRes => {
        // console.log("mySpecFollowDataRes:", mySpecFollowDataRes);
        if (mySpecFollowDataRes.status !== 200) {
          this.$message({
            showClose: true,
            message: "出问题了，请联系网站管理员查找原因：" + mySpecFollowDataRes.status,
            type: 'error',
            duration: 10000,
          });
        } else if (mySpecFollowDataRes.body.success) {
          this.$message({
            showClose: true,
            message: mySpecFollowDataRes.body.message,
            type: 'info',
            duration: 10000,
          });
        } else {
          this.$message({
            showClose: true,
            message: mySpecFollowDataRes.body.message,
            type: 'error',
            duration: 10000,
          });
        }
      }).catch(function (mySpecFollowDataRes) {
        //出错处理
        console.log(mySpecFollowDataRes)
      });
    },
    removeMyfavorite(user_name, event, index) {
      console.log(this.list[index].user.eid);
      const eid = this.list[index].user.eid;
      this.$http.post(baseUrl + '/live/updateMyfavLiveInfoByIsMyfavorite',
        {
          inputEid: eid,
          inputIsMyfavorite: false,
        },
        {
          emulateJSON: true
        }).then(mySpecFollowDataRes => {
        // console.log("mySpecFollowDataRes:", mySpecFollowDataRes);
        if (mySpecFollowDataRes.status !== 200) {
          this.$message({
            showClose: true,
            message: "出问题了，请联系网站管理员查找原因：" + mySpecFollowDataRes.status,
            type: 'error',
            duration: 10000,
          });
        } else if (mySpecFollowDataRes.body.success) {
          this.$message({
            showClose: true,
            message: mySpecFollowDataRes.body.message,
            type: 'info',
            duration: 10000,
          });
        } else {
          this.$message({
            showClose: true,
            message: mySpecFollowDataRes.body.message,
            type: 'error',
            duration: 10000,
          });
        }
      }).catch(function (mySpecFollowDataRes) {
        //出错处理
        console.log(mySpecFollowDataRes)
      });
    },
    /**
     * 监听页面滚动上拉刷新
     */
    loading() {
      //变量scrollTop是滚动条滚动时，距离顶部的距离
      const scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
      //变量windowHeight是可视区的高度
      const windowHeight = document.documentElement.clientHeight || document.body.clientHeight;
      //变量scrollHeight是滚动条的总高度
      const scrollHeight = document.documentElement.scrollHeight || document.body.scrollHeight;
      //滚动条到底部的条件
      // console.log(scrollTop + windowHeight + "--" + scrollHeight);
      if (scrollTop + windowHeight >= scrollHeight - 20 && this.isRefreshBool) {
        // false防止refresh()加载数据函数多次触发
        this.isRefreshBool = false;
        this.loadList();
      }
    },
    loadList() {
      this.isShowHint = true;
      setTimeout(() => {
        this.endListIndex = this.endListIndex + 40;
        this.isRefreshBool = true;
      }, 500);
    },
    liveUrlPicFun(liveUrlFromPic, flvUrl, event, index) {
      const thisVideoDivBox = $('#videoDivBox' + index);
      const lastVideoDivBox = $('#videoDivBox' + this.lastLiItemIdex);
      const thisCloseVideoDivImg = $('#videoDivBox' + index + ' + div');
      const lastCloseVideoDivImg = $('#videoDivBox' + this.lastLiItemIdex + ' + div');
      const thisLiveImgDiv = $('#imgDiv' + index);
      const lastLiveImgDiv = $('#imgDiv' + this.lastLiItemIdex);

      if (myPlayer !== undefined) {
        if (this.isSwitchM3u8) {
          myPlayer.dispose();/*!//停止*/
          // $('.playIpt').val(flvUrl);
          $('.playIpt').val(liveUrlFromPic);
        } else {
          $('#videoPlayer').hide();/*!//点击关闭*/
          myPlayer.dispose();/*!//停止*/
          lastVideoDivBox.css('display', 'none');
          lastVideoDivBox.css('zIndex', 4);
          this.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
          lastCloseVideoDivImg.css('display', 'none');
          lastLiveImgDiv.css('display', 'block');
          // $('.playIpt').val(liveUrlFromPic);
          $('.playIpt').val(flvUrl);
        }
      } else {
        if (this.isSwitchM3u8) {
          // $('.playIpt').val(flvUrl);
          $('.playIpt').val(liveUrlFromPic);
        } else {
          // $('.playIpt').val(liveUrlFromPic);
          $('.playIpt').val(flvUrl);
        }
      }
      const videoBox = "<div id=\"videoDiv\" ref=\"videoBox2\"><div id=\"videoPlayer\" style=\"width: 20rem\"><video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;' preload=\"none\" controls=\"true\" autoplay=\"autoplay\"></video></div></div>";
      thisVideoDivBox.css('display', 'block');
      this.$refs.name[index].childNodes[2].innerHTML = videoBox;
      $('.videos').show();/*//视频窗口弹出*/
      if (this.isSwitchM3u8) {
        this.videoSource.src = liveUrlFromPic;
        this.videoSource.type = m3u8Type;
      } else {
        this.videoSource.src = flvUrl;
        this.videoSource.type = flvType;
      }
      myPlayer = videojs("video", {
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
      this.lastLiItemIdex = index;
      // this.playVideoUrl = liveUrlFromPic;
    },
    inputVideoBtn(event) {
      const inputVideoUrl = $('#inputVideoId').val();

      //被点击的btn
      const clkBtn = event.currentTarget;
      const videoDiv = $('videoDiv');

      $('#videoPlayer').show();/*!//视频窗口弹出*/
      console.log('videoPlayer:', $('#videoPlayer'));/*!//视频窗口弹出*/
      myPlayer = videojs("video");
      myPlayer.ready(function () {
        myPlayer.play();
      });
    },
    closeVideoPlayer() {
      $('#videoPlayer').hide();/*//隐藏视频播放器*/
      myPlayer.dispose();/*//停止*/
      $('#videoDivBox' + this.lastLiItemIdex).css('display', 'none');
      $('#videoDivBox' + this.lastLiItemIdex + ' + div').css('display', 'none');
      this.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
      myPlayer = undefined;
      $('#imgDiv' + this.lastLiItemIdex).css('display', 'block');
    }
  },
  mounted() {
    //监听页面滑动，执行this.loading函数
    window.addEventListener('scroll', this.loading, true);
    //页面刷新时重置this.endListIndex的值
    window.onbeforeunload = function (e) {
      // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
      this.endListIndex = 20;
    };
  },
  destroyed() {
    window.removeEventListener("scroll", this.loading, true);
  },
}


</script>

<style>
.videoClose {
  margin-top: 1px;
  margin-bottom: 1px;
  font-size: medium;
}

.hint {
  margin-top: 1rem;
}

.imgClass {
  height: 381.5px;
  width: 231px;
}

#inputData {
  margin-bottom: 2px;
}

#inputVideoId {
  width: 16.5rem;
}

.playIpt {
  width: 18rem;
}

#cookieIpt {
  width: 14rem;
}

.el-row {
  margin: 2rem;
}
</style>
