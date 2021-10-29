<template>
  <div align="center">
    <h2 style="color: hotpink">{{ theTitle }}</h2>
    <h3>当前直播人数:<span style="color: red">{{ theMyFollowListdata.length }}</span></h3>
    <br/>
    <div id="inputData">
      <br>
    </div>
    <li :id="`liId+${index}`" v-for="(listData,index) in theMyFollowListdata.slice(startListIndex, endListIndex)"
        style="display:inline-block;width: 327px"
        :key=index
        ref="name"
        v-loading="isLoading">
      <TheVideoPlayer showWhat="mySpecialFollow"
                      :listData="listData"
                      :theVideoIndex="index"
                      @resetMyPlayer="resetMyPlayer"
                      @setMyVideoPlayer="setMyVideoPlayerFromTheVideoPlayer"
                      @setLastLiIndex="setLastLiIndex"
                      :lastLiItemIdex="lastLiItemIdex"
                      :myVideoPlayer="myVideoPlayer"
                      @setThePlayingVideoUrl="setThePlayingVideoUrl"
                      :liveListAllData="liveListAllData"
                      :mySpecialFollowData="theMyFollowListdata"
                      ref="theVideoPlayer"

      />
      <div :id="`videoDivBox${index}`" v-show="isVideoDivBoxShow" class="videoDivBoxClass"></div>

      <div v-show="isVideoDivBoxShow">
        <PlayingVideoOptions :listData="listData"
                             :myVideoPlayer="myVideoPlayer"
                             :lastLiItemIdex="lastLiItemIdex"
                             @resetMyPlayer="resetMyPlayer"
                             :theVideoIndex="index"
                             @switchPlayVideo="switchPlayVideo(arguments)"
                             :thePlayingVideoUrl="thePlayingVideoUrl"
        />
      </div>
    </li>
    <div class="hint" v-show="isShowHint">上拉加载，获取更多内容</div>
  </div>
</template>

<script>
import $ from 'jquery';
import PlayingVideoOptions from "../components/PlayingVideoOptions";
import TheVideoPlayer from "../components/TheVideoPlayer";

let myPlayer;

export default {
  name: 'mySpecialFollow',
  props: {
    liveListAllData: [],
    myFavoriteList: [],
    myFavoriteObject: {},
    theTitleFromHome: ""
  },
  components: {PlayingVideoOptions, TheVideoPlayer},
  data() {
    return {
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
      mySpecialFollow: [],
      /*主播置顶值，根据大小排序 0-9 0表示未置顶 置顶选项只能设置0-8 9表示超级置顶 */
      theAnchorTopNumber: Number,
      /*播放视频的对象*/
      myVideoPlayer: undefined,
      /*正在播放的视频链接*/
      thePlayingVideoUrl: "",
      /*页面标题*/
      theTitle: window.sessionStorage.getItem("theMyFollowListdata_key"),
      /*切换列表数据*/
      theMyFollowListdata: [],
      /*特别关注的正在直播的主播的列表*/
      myNewSpecFollowRes: [],
      /*未特别关注但是正在直播的主播列表*/
      notIsMySpecialFollow: [],
    }
  },
  methods: {
    setThePlayingVideoUrl(param) {
      this.thePlayingVideoUrl = param;
    },

    setLastLiIndex(param) {
      this.lastLiItemIdex = param;
    },

    setMyVideoPlayerFromTheVideoPlayer(param) {
      myPlayer = this.myVideoPlayer = param;
    },
    /*初始化视频播放的对象*/
    resetMyPlayer() {
      myPlayer = this.myVideoPlayer = undefined;
    },

    /*切换播放的数据源*/
    switchPlayVideo(params) {
      this.$refs.theVideoPlayer[0].isSwitchM3u8 = true;
      this.$refs.theVideoPlayer[0].liveUrlPicFun(params[0], params[1], event, params[2]);
      this.$refs.theVideoPlayer[0].isSwitchM3u8 = false;
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
  },
  mounted() {
    //监听页面滑动，执行this.loading函数
    window.addEventListener('scroll', this.loading, true);
    //页面刷新时重置this.endListIndex的值
    window.onbeforeunload = function (e) {
      // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
      this.endListIndex = 10;
    };

    /**
     * 加载正在直播的特别关注的主播
     */
    this.$http.post(baseUrl + '/live/getNewMySpecialFollowFromTemp',
      {},
      {
        emulateJSON: true
      }).then(myNewSpecFollowRes => {
      // console.log("myNewSpecFollowRes:", myNewSpecFollowRes);
      if (myNewSpecFollowRes.status !== 200) {
        this.$message({
          showClose: true,
          message: "出问题了，请联系网站管理员查找原因：" + myNewSpecFollowRes.status,
          type: 'error',
          duration: 10000,
        });
      } else if (myNewSpecFollowRes.body.success) {
        // console.log("myNewSpecFollowRes",myNewSpecFollowRes.body.data.result);
        // this.myNewSpecFollowRes = myNewSpecFollowRes.body.data.result;
        this.myNewSpecFollowRes = myNewSpecFollowRes.body.data.result;
        if (this.myNewSpecFollowRes.length !== 0) {
          window.sessionStorage.setItem("myNewSpecFollowRes_key", JSON.stringify(this.myNewSpecFollowRes));
        }
        //提示框
        this.$notify({
          title: specialFollow,
          message: '加载完成',
          type: 'success'
        });
      } else {
        this.$message({
          showClose: true,
          message: myNewSpecFollowRes.body.message,
          type: 'error',
          duration: 10000,
        });
      }
    }).catch(function (myNewSpecFollowRes) {
      //出错处理
      console.log(myNewSpecFollowRes)
    });

    this.$http.post(baseUrl + '/live/getNotIsMySpecialFollow',
      {},
      {
        emulateJSON: true
      }).then(notIsMySpecialFollowRes => {
      // console.log("notIsMySpecialFollowRes:", notIsMySpecialFollowRes);
      if (notIsMySpecialFollowRes.status !== 200) {
        this.$message({
          showClose: true,
          message: "出问题了，请联系网站管理员查找原因：" + notIsMySpecialFollowRes.status,
          type: 'error',
          duration: 10000,
        });
      } else if (notIsMySpecialFollowRes.body.success) {
        // console.log("notIsMySpecialFollowRes", notIsMySpecialFollowRes.body.data.result);
        this.notIsMySpecialFollow = notIsMySpecialFollowRes.body.data.result;
        if (this.notIsMySpecialFollow.length !== 0) {
          window.sessionStorage.setItem("notIsMySpecialFollow_key", JSON.stringify(this.notIsMySpecialFollow));
        }
        //提示框
        this.$notify({
          title: notSpecialFollow,
          message: '正在直播的主播列表加载完成',
          type: 'success'
        });
      } else {
        this.$message({
          showClose: true,
          message: notIsMySpecialFollowRes.body.message,
          type: 'error',
          duration: 10000,
        });
      }
    }).catch(function (myNewSpecFollowRes) {
      //出错处理
      console.log(myNewSpecFollowRes)
    });

    if (this.theTitle === notSpecialFollow) {
      this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("notIsMySpecialFollow_key"));
    } else if (this.theTitle === specialFollow) {
      this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("myNewSpecFollowRes_key"));
    }
  },
  destroyed() {
    window.removeEventListener("scroll", this.loading, true);
  },
  watch: {
    theTitleFromHome: {
      deep: true,
      handler: function (newValue) {
        this.theTitle = window.sessionStorage.getItem("theMyFollowListdata_key");
        console.log("theTitle", this.theTitle);
        if (this.theTitle === notSpecialFollow) {
          this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("notIsMySpecialFollow_key"));
        } else if (this.theTitle === specialFollow) {
          this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("myNewSpecFollowRes_key"));
        }
      }
    },
    myNewSpecFollowRes: {
      deep: true,
      handler: function () {
        if (this.theTitle === specialFollow) {
          this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("myNewSpecFollowRes_key"));
        }
      }
    },
    notIsMySpecialFollow: {
      deep: true,
      handler: function () {
        if (this.theTitle === notSpecialFollow) {
          this.theMyFollowListdata = JSON.parse(window.sessionStorage.getItem("notIsMySpecialFollow_key"));
        }
      }
    }
  }
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
