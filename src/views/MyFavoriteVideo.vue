<template>
  <div align="center">

    <div>
      <a href="ks-vue://" >调用cmd</a>
    </div>

    <h2 style="color: hotpink">我点赞的视频</h2>
    <br/>
    <li :id="`liId+${index}`" v-for="(myFavorite,index) in myFavoriteListData"
        style="display:inline-block;width: 327px"
        :key=index
        ref="name">
      <el-row type="flex" align="center" :span="10" :id="`imgDiv${index}`">
        <div id="dataCol" class="grid-content">
          <img :src="myFavorite.photo.coverUrl" v-loading="isLoading"
               @click="liveUrlPicFun(myFavorite.photo.photoUrl,myFavorite.photo.photoUrls[0].url,$event,index)"
               v-show="isImgShow" class="imgClass"/>
          <br/>
          <span style="font-size: 1.3rem">{{ myFavorite.author.name }}</span>
        </div>
      </el-row>
      <div :id="`videoDivBox${index}`" v-show="isVideoDivBoxShow" class="videoDivBoxClass"></div>
      <div v-show="isVideoDivBoxShow">
        <el-button circle size="small" class="videoClose" @click="closeVideoPlayer" type="text">关闭</el-button>
        <!--        <img @click="closeVideoPlayer" class='videoClose' :src="closeImgPath" width='30rem' height='30rem'/>-->
      </div>
    </li>
    <div class="hint" v-show="isShowHint">上拉加载，获取更多内容</div>
  </div>
</template>

<script>
import $ from 'jquery';

let myPlayer;

export default {
  name: 'my-favorite-video',
  props: {
    list: [],
    // myFavoriteList: [],
    // myFavoriteObject: {},
  },
  components: {},
  data() {
    return {
      //项目打包后图片加载不到 所以给前面加上/ks-vue/
      // closeImgPath: '/static/pic/close.jpg',
      // closeImgBuildPath: '/ks-vue/static/pic/close.jpg',

      //这个属性好像没啥用
      isLoading: true,
      inputVideoUrl: '',
      //播放视频后把当前视频的链接设置进来
      playVideoUrl: '',
      //页面初始化隐藏videoboxdiv容器
      isVideoDivBoxShow: false,
      //上一个点击的图片的索引
      lastLiItemIdex: 0,
      //播放视频时隐藏点击的图片
      isImgShow: true,
      //防止底部一次滑动触发多次刷新的方法
      isRefreshBool: true,
      //pcursor : "",
      myFavoriteListData: [],
      _pcursor: "",
      myFavoriteObjectData: Object,
      isShowHint: false,

    }
  },
  methods: {

    runcmd(){

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
      console.dir('this.myFavoriteObjectData:', this.myFavoriteObjectData)
      this._pcursor = this.myFavoriteObjectData.data.visionProfileLikePhotoList.pcursor;
      setTimeout(() => {
        this.$http.post(baseUrl + '/ks/myLikeData',
          // baseUrl + '/ks/test/live-data',
          //参数部分，将会拼接在url后面
          {
            callback: "a",
            pcursor: this._pcursor,
          },
          {
            emulateJSON: true
          }).then(myLikeDataRes => {
          console.log("myLikeData:", myLikeDataRes);
          // console.log(response.body.data.follow);
          if (myLikeDataRes.status !== 200) {
            this.$message({
              showClose: true,
              message: "出问题了，请联系网站管理员查找原因：" + myLikeDataRes.status,
              type: 'error',
              duration: 10000,
            });
          } else if (myLikeDataRes.body.success) {
            // this.myFavoriteList = JSON.parse(myLikeDataRes.body.data.result).data.visionProfileLikePhotoList.feeds;
            this.myFavoriteListData.splice(this.myFavoriteListData.length, 0, ...myLikeDataRes.body.data.data.visionProfileLikePhotoList.feeds);
            this.myFavoriteObjectData = myLikeDataRes.body.data;
          } else {
            this.$message({
              showClose: true,
              message: myLikeDataRes.body.message,
              type: 'error',
              duration: 10000,
            });
          }
        }).catch(function (myLikeDataRes) {
          //出错处理
          console.log(myLikeDataRes)
        });
        this.isRefreshBool = true;
      }, 500);
    },
    liveUrlPicFun(mp4Url, cdnMp4Url, event, index) {
      const thisVideoDivBox = $('#videoDivBox' + index);
      const lastVideoDivBox = $('#videoDivBox' + this.lastLiItemIdex);
      const thisCloseVideoDivImg = $('#videoDivBox' + index + ' + div');
      const lastCloseVideoDivImg = $('#videoDivBox' + this.lastLiItemIdex + ' + div');
      const thisLiveImgDiv = $('#imgDiv' + index);
      const lastLiveImgDiv = $('#imgDiv' + this.lastLiItemIdex);
      if (myPlayer !== undefined) {
        $('#videoPlayer').hide();/*//点击关闭*/
        myPlayer.dispose();/*//停止*/
        lastVideoDivBox.css('display', 'none');
        lastVideoDivBox.css('zIndex', 4);
        this.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
        lastCloseVideoDivImg.css('display', 'none');
        lastLiveImgDiv.css('display', 'block');
      }
      const videoBox = "<div id=\"videoDiv\" ref=\"videoBox2\"><div id=\"videoPlayer\" style=\"width: 20rem\"><video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;' preload=\"none\" controls=\"true\" autoplay=\"autoplay\"></video></div></div>";
      thisVideoDivBox.css('display', 'block');
      this.$refs.name[index].childNodes[2].innerHTML = videoBox;
      $('.videos').show();/*//视频窗口弹出*/
      myPlayer = videojs("video", {
          playbackRates: [0.5, 1.0, 1.5, 2.0, 3.0], //播放速度
          autoplay: true, //如果true,浏览器准备好时开始回放。
          notSupportedMessage: "此视频暂时无法播放,请稍后再试",
          muted: false, // 默认情况下将会消除任何音频。
          loop: false, // 导致视频一结束就重新开始。
          preload: 'auto', // 建议浏览器在<video>加载元素后是否应该开始下载视频数据。auto浏览器选择最佳行为,立即开始加载视频（如果浏览器支持）
          language: 'zh-CN',
          aspectRatio: '16:9', // 将播放器置于流畅模式，并在计算播放器的动态大小时使用该值。值应该代表一个比例 - 用冒号分隔的两个数字（例如"16:9"或"4:3"）
          fluid: true, // 当true时，Video.js player将拥有流体大小。换句话说，它将按比例缩放以适应其容器。
          techOrder: ['html5'], //html5模式和flvjs模式
          sources: [
            {
              type: 'video/mp4',
              src: mp4Url //你的m3u8地址（必填）
            }, {
              type: 'video/mp4',
              src: cdnMp4Url //你的m3u8地址（必填）
            },
          ],
        },
      );
      thisLiveImgDiv.css('display', 'none');
      thisCloseVideoDivImg.css('display', 'block');
      this.lastLiItemIdex = index;
      this.playVideoUrl = mp4Url;
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
    this.$http.post(baseUrl + '/ks/myLikeData',
      // baseUrl + '/ks/test/live-data',
      //参数部分，将会拼接在url后面
      {
        callback: "a",
        pcursor: "",
      },
      {
        emulateJSON: true
      }).then(myLikeDataRes => {
      console.log("myLikeData:", myLikeDataRes);
      // console.log(response.body.data.follow);
      if (myLikeDataRes.status !== 200) {
        this.$message({
          showClose: true,
          message: "出问题了，请联系网站管理员查找原因：" + myLikeDataRes.status,
          type: 'error',
          duration: 10000,
        });
      } else if (myLikeDataRes.body.success) {
        this.myFavoriteListData = myLikeDataRes.body.data.data.visionProfileLikePhotoList.feeds;
        this.myFavoriteObjectData = myLikeDataRes.body.data;
        console.log("myFavoriteObject:", myLikeDataRes.body.data);
      } else {
        this.$message({
          showClose: true,
          message: myLikeDataRes.body.message,
          type: 'error',
          duration: 10000,
        });
      }
    }).catch(function (myLikeDataRes) {
      //出错处理
      console.log(myLikeDataRes)
    });

    //监听页面滑动，执行this.loading函数
    window.addEventListener('scroll', this.loading, true);
    //页面刷新时重置this.endListIndex的值
    window.onbeforeunload = function (e) {
      // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
      this.endListIndex = 10;
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

#playIpt {
  width: 16.5rem;
}

#cookieIpt {
  width: 14rem;
}

.el-row {
  margin: 2rem;
}
</style>
