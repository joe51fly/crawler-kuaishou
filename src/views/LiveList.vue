<template>
  <div align="center">
    <h2 style="color: hotpink">我的关注</h2>
    <h3>当前直播人数:<span style="color: red">{{list.length}}</span></h3>
    <br/>
    <div id="inputData">
      视频播放:<input type="text" id="inputVideoId" v-model="inputVideoUrl"/>
      <input type="button" id="playBtn" value="播放" @click="inputVideoBtn($event)"/>
      <br/>
      播放链接:<input type="text" id="playIpt" v-model="playVideoUrl"/>
      <br>
      <div ref="videoBox1" id="videoBox1"></div>
    </div>

    <li :id="`liId+${index}`" v-for="(listData,index) in list.slice(startListIndex, endListIndex)"
        style="display:inline-block"
        :key=index ref="name">
      <el-row type="flex" align="center" :span="10" :id="`imgDiv${index}`">
        <div id="dataCol" class="grid-content">
          <img :src="listData.rtCoverUrl" v-loading="isLoading"
               @click="liveUrlPicFun(listData.hlsPlayUrl,$event,index)"
               v-show="isImgShow" class="imgClass"/>
          <br/>
          <span style="font-size: 1.3rem">{{listData.user.user_name}}</span>
        </div>
      </el-row>
      <div :id="`videoDivBox${index}`" v-show="isVideoDivBoxShow" class="videoDivBoxClass"></div>
      <div v-show="isVideoDivBoxShow">
        <img @click="closeVideoPlayer" class='videoClose' :src="closeImgPath" width='30rem' height='30rem'/>
      </div>
    </li>
    <div class="hint">上拉加载，获取更多内容</div>
  </div>
</template>

<script>
  import $ from 'jquery';

  let myPlayer;

  export default {
    name: 'list',
    props: {
      list: Array,
    },
    data() {
      return {
        //项目打包后图片加载不到 所以给前面加上/ks-vue/
        closeImgPath: '/static/pic/close.jpg',
        closeImgBuildPath: '/ks-vue/static/pic/close.jpg',
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
        //限制初始化页面的时候数据的条数
        startListIndex: 0,
        endListIndex: 10,
        //防止底部一次滑动触发多次刷新的方法
        isRefreshBool: true
      }
    },
    methods: {
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
          console.log('aaaaaa');
          // false防止refresh()加载数据函数多次触发
          this.isRefreshBool = false;
          this.loadList();
        }
      },
      loadList() {
        setTimeout(() => {
          this.endListIndex = this.endListIndex + 10;
          this.isRefreshBool = true;
        }, 500);
      },
      liveUrlPicFun(liveUrlFromPic, event, index) {
        const thisVideoDivBox = $('#videoDivBox' + index);
        const lastVideoDivBox = $('#videoDivBox' + this.lastLiItemIdex);
        const thisCloseVideoDivImg = $('#videoDivBox' + index + ' + div');
        const lastCloseVideoDivImg = $('#videoDivBox' + this.lastLiItemIdex + ' + div');
        const thisLiveImgDiv = $('#imgDiv' + index);
        const lastLiveImgDiv = $('#imgDiv' + this.lastLiItemIdex);
        // console.log('thisCloseVideoDivImg', thisCloseVideoDivImg);
        // console.log('lastCloseVideoDivImg', lastCloseVideoDivImg);
        // console.log('thisVideoDivBox:', thisVideoDivBox);
        // console.log('thisLiveImg:', thisLiveImgDiv);
        // console.log('lastLiveImg:', lastLiveImgDiv);
        // console.log(myPlayer);
        if (myPlayer !== undefined) {
          // console.log('lastVideoDivBox:', this.$refs.name[this.lastLiItemIdex].childNodes[2]);
          $('#videoPlayer').hide();/*//点击关闭*/
          myPlayer.dispose();/*//停止*/
          lastVideoDivBox.css('display', 'none');
          lastVideoDivBox.css('zIndex', 4);
          this.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
          lastCloseVideoDivImg.css('display', 'none');
          lastLiveImgDiv.css('display', 'block');
        }
        // const liRef = 'li' + index;
        // console.log(liRef);
        // console.log('li-ref', this.$refs.name[index]);
        const videoBox = "<div id=\"videoDiv\" ref=\"videoBox2\"><div id=\"videoPlayer\" style=\"width: 20rem\"><video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;' preload=\"none\" controls=\"true\" autoplay=\"autoplay\"><source :src=liveUrlFromPic type='application/x-mpegURL'></video></div></div>";
        // this.$refs.videoBox1.innerHTML = videoBox;
        thisVideoDivBox.css('display', 'block');
        // console.log(this.$refs.name[index].childNodes[2]);
        this.$refs.name[index].childNodes[2].innerHTML = videoBox;
        // console.log('videoDiv:', $('#videoDiv'));
        // app.$options.methods.insertAfter(video, clkLi);
        $('#playIpt').val(liveUrlFromPic);
        $('video').append("<source src=" + liveUrlFromPic + " type=\'application\/x-mpegURL\'>");
        $('.videos').show();/*//视频窗口弹出*/
        videojs("video").ready(function () {
          myPlayer = this;
          myPlayer.play();
          // console.log('myPlayer', myPlayer);
        });
        thisLiveImgDiv.css('display', 'none');
        thisCloseVideoDivImg.css('display', 'block');
        this.lastLiItemIdex = index;
        this.playVideoUrl = liveUrlFromPic;
      },
      inputVideoBtn(event) {
        const inputVideoUrl = $('#inputVideoId').val();

        //被点击的btn
        const clkBtn = event.currentTarget;
        const videoDiv = $('videoDiv');
        // this.$emit('insertAfter', videoDiv, clkBtn);


        // $('.video').html("<div class=\"videos\" style=\"width: 20rem\"></div>");
        // $('.videos').html("<video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;'  preload=\"none\" controls=\"true\" autoplay=\"autoplay\"></video><img onClick=\"close2();return false;\" class=\'vclose\' src=\'pic/close.jpg\' width=\'30rem\' height=\'30rem\'/>");
        // // document.getElementById("video").innerHTML += "<source src=" + inputVideoUrl + " type=\'video\/mp4\'>";
        // $('#video').append("<source src=" + inputVideoUrl + " type=\'video\/mp4\'>");
        // console.log($('#video').html());
        // console.log(event);
        // console.log($('.videos').html());
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
        this.endListIndex = 10;
      };
    },
    destroyed() {
      window.removeEventListener("scroll", this.loading, true);
    },

  }


</script>

<style>
  .hint{
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
    margin-top: 2rem;
  }
</style>
