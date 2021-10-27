<template>
  <div align="center">
    <h2 style="color: hotpink">我的关注</h2>
    <h3>当前直播人数:<span style="color: red">{{ liveListAllData.length }}</span></h3>
    <br/>
    <div id="inputData">
      <br>
    </div>
      <li :id="`liId+${index}`" v-for="(listData,index) in liveListAllData.slice(startListIndex, endListIndex)"
          style="display:inline-block;width: 327px"
          :key=index
          ref="name"
          v-loading="isLoading">
        <TheVideoPlayer showWhat="liveList"
                        :listData="listData"
                        :theVideoIndex="index"
                        @resetMyPlayer="resetMyPlayer"
                        @setMyVideoPlayer="setMyVideoPlayerFromTheVideoPlayer"
                        @setLastLiIndex="setLastLiIndex"
                        :lastLiItemIdex="lastLiItemIdex"
                        :myVideoPlayer="myVideoPlayer"
                        @setThePlayingVideoUrl="setThePlayingVideoUrl"
                        :liveListAllData="liveListAllData"
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
import PlayingVideoOptions from "../components/PlayingVideoOptions.vue";
import TheVideoPlayer from "../components/TheVideoPlayer";

let myPlayer;

export default {
  name: 'live-list',
  props: {
    liveListAllData: [],
    myFavoriteList: [],
    myFavoriteObject: {},
  },
  components: {
    PlayingVideoOptions,TheVideoPlayer
  },
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
      // theAnchorInfoByEid:String,
      isMySpecialFavorite: Boolean,

      /*主播置顶值，根据大小排序 0-9 0表示未置顶 置顶选项只能设置0-8 9表示超级置顶 */
      theAnchorTopNumber:Number,

      /*播放视频的对象*/
      myVideoPlayer:undefined,
      /*正在播放的视频链接*/
      thePlayingVideoUrl:"",
    }
  },
  methods: {
    setThePlayingVideoUrl(param){
      this.thePlayingVideoUrl = param;
    },

    setLastLiIndex(param){
      this.lastLiItemIdex = param;
    },

    setMyVideoPlayerFromTheVideoPlayer(param){
      myPlayer = this.myVideoPlayer = param;
    },

    /*切换播放的数据源*/
    switchPlayVideo(params){
      this.$refs.theVideoPlayer[0].isSwitchM3u8 = true;
      this.$refs.theVideoPlayer[0].liveUrlPicFun(params[0], params[1], event, params[2]);
      this.$refs.theVideoPlayer[0].isSwitchM3u8 = false;
    },

    /*初始化视频播放的对象*/
    resetMyPlayer(){
      myPlayer = this.myVideoPlayer = undefined;
    },

    /*监听页面滚动上拉刷新*/
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
      this.endListIndex = 20;
    };
  },
  destroyed() {
    window.removeEventListener("scroll", this.loading, true);
  },
}


</script>

<style>
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

#cookieIpt {
  width: 14rem;
}

.el-row {
  margin: 2rem;
}
</style>
