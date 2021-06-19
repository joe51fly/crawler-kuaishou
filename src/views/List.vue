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
      <div>
        <div id="videoDiv">
          <div id="videoPlayer" style="width: 20rem">
            <video id="video" class="video-js vjs-default-skin" style='height:100%;width:100%;' preload="none"
                   controls="true" autoplay="autoplay">
<!--              <source :src="inputVideoUrl" type='video/mp4'>-->
              <source :src="inputVideoUrl" type='application/x-mpegURL'>
            </video>
            <img @click="close2" class='videoClose' src='static/pic/close.jpg' width='30rem' height='30rem'/>
          </div>
        </div>
      </div>
    </div>

    <li :id="`liId+${index}`" v-for="(listData,index) in list" style="display:inline-block"
        @click="liveUrlPicFun(listData.hlsPlayUrl,$event,index)" :key=index>
      <el-row type="flex" align="center" :span="10">
        <div id="dataCol" class="grid-content">
          <img id="imgId" :src="listData.rtCoverUrl" v-loading="isLoading"></img>
          <br/>
          <span style="font-size: 1.3rem">{{listData.user.user_name}}</span>
        </div>
      </el-row>
    </li>
  </div>
</template>

<script>
  import $ from 'jquery';

  let myPlayer;

  export default {
    name: 'list',
    props: {
      insertAfter: Function,
      list: Array,
    },
    data() {
      return {
        isLoading: true,
        inputVideoUrl: '',
        playVideoUrl: '',
      }
    },
    methods: {
      liveUrlPicFun(liveUrlFromPic, event, index) {
        //被点击的li
        var clkLi = event.currentTarget;
        // 存放视频
        var appDom = event.path[4];
        // app.$options.methods.insertAfter(video, clkLi);
        $('#playIpt').val(liveUrlFromPic);
        $('video').append("<source src=" + liveUrlFromPic + " type=\'application\/x-mpegURL\'>");
        $('.videos').show();/*//视频窗口弹出*/
        videojs("video").ready(function () {
          myPlayer = this;
          myPlayer.play();
        });
      },
      inputVideoBtn(event) {
        var inputVideoUrl = $('#inputVideoId').val();

        //被点击的btn
        const clkBtn = event.currentTarget;
        const videoDiv = $('videoDiv');
        this.$emit('insertAfter', videoDiv, clkBtn);
        // $('.video').html("<div class=\"videos\" style=\"width: 20rem\"></div>");
        // $('.videos').html("<video id=\"video\" class=\"video-js vjs-default-skin\" style='height:100%;width:100%;'  preload=\"none\" controls=\"true\" autoplay=\"autoplay\"></video><img onClick=\"close2();return false;\" class=\'vclose\' src=\'pic/close.jpg\' width=\'30rem\' height=\'30rem\'/>");
        // // document.getElementById("video").innerHTML += "<source src=" + inputVideoUrl + " type=\'video\/mp4\'>";
        // $('#video').append("<source src=" + inputVideoUrl + " type=\'video\/mp4\'>");
        // console.log($('#video').html());
        // console.log(event);
        // console.log($('.videos').html());
        $('#videoPlayer').show();/*!//视频窗口弹出*/
        myPlayer = videojs("video");
        myPlayer.ready(function () {
          myPlayer.play();
        });
      },
      close2() {
        $('#videoPlayer').hide();/*//点击关闭*/
        myPlayer.dispose();/*//停止*/
        // $('#videoPlayer').html();
        // $('.video').remove();
      },
    },

  }
</script>

<style>
  #imgId {
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
