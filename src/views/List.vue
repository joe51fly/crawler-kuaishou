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

    <li :id="`liId+${index}`" v-for="(listData,index) in list" style="display:inline-block"
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
      <div :id="`videoDivBox${index}`" v-show="isvideoDivBoxShow" class="videoDivBoxClass"></div>
      <div @click="closeVideoPlayer" v-show="isvideoDivBoxShow">
        <img class='videoClose' :src="closeImgPath" width='30rem' height='30rem'/>
      </div>
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
        closeImgPath:'/static/pic/close.jpg',
        closeImgBuildPath:'/ks-vue/static/pic/close.jpg',
        isLoading: true,
        inputVideoUrl: '',
        playVideoUrl: '',
        isvideoDivBoxShow: false,
        lastLiItemIdex: 0,
        isImgShow: true
      }
    },
    methods: {


      loadErrorMessage() {
        console.log('abc');
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
        const liRef = 'li' + index;
        // console.log(liRef);
        // console.log('li-ref', this.$refs.name[index]);
        const videoBox = "<div id=\"videoDiv\" ref=\"videoBox2\"><div id=\"videoPlayer\" style=\"width: 20rem\"><video id=\"video\" :onerror='loadErrorMessage' class=\"video-js vjs-default-skin\" style='height:100%;width:100%;' preload=\"none\" controls=\"true\" autoplay=\"autoplay\"><source :src=liveUrlFromPic type='application/x-mpegURL'></video></div></div>";
        // this.$refs.videoBox1.innerHTML = videoBox;
        thisVideoDivBox.css('display', 'block');
        // console.log(this.$refs.name[index].childNodes[2]);
        this.$refs.name[index].childNodes[2].innerHTML = videoBox;
        this.$nextTick(function () {

          // DOM 更新了
        });
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
        $('#videoPlayer').hide();/*//点击关闭*/
        myPlayer.dispose();/*//停止*/
        $('#videoDivBox' + this.lastLiItemIdex).css('display', 'none');
        // $('#videoDivBox' + this.lastLiItemIdex).css('zIndex', 4);
        $('#videoDivBox' + this.lastLiItemIdex + ' + div').css('display', 'none');
        this.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
        myPlayer = undefined;
        $('#imgDiv' + this.lastLiItemIdex).css('display', 'block');
      }

    },
    mounted() {
      // console.log('this.$refs:',this.$refs.videoBox1.innerHTML);
      // console.log('this.$refs:',this.$refs.videoBox1.outerHTML);
      const _this = this;
      // (function() {
      //   _this.$refs.testDiv.$createElement('p', null, [
      //     ('span', null, '内容可以是 '),
      //     ('a', {
      //         //普通html特性
      //         attrs: {
      //           href:'aTemp'
      //         },
      //         //相当于`v-bind:style`
      //         style: {
      //           color: 'red',
      //           fontSize: '14px'},
      //       },
      //       '百度'
      //     )
      //   ]);
      //   _this.$createElement('div', {attrs: {id: 'createDiv'}}, '<span>aaaaaa</span>');
      //   console.log('createDiv:', $('#createDiv'));
      // })();

      // (function(){
      //   const h = _this.$createElement;
      //   const aTemp = 'https://www.baidu.com/?tn=98010089_dg&ch=8';
      //   _this.$msgbox({
      //     title: '消息',
      //     message: h('p', null, [
      //       h('span', null, '内容可以是 '),
      //       h('a', {
      //           //普通html特性
      //           attrs: {
      //             href:aTemp
      //           },
      //           //相当于`v-bind:style`
      //           style: {
      //             color: 'red',
      //             fontSize: '14px'},
      //         },
      //         '百度'
      //       )
      //     ]),
      //     showCancelButton: true,
      //     confirmButtonText: '确定',
      //     cancelButtonText: '取消',
      //     beforeClose: (action, instance, done) => {
      //       if (action === 'confirm') {
      //         instance.confirmButtonLoading = true;
      //         instance.confirmButtonText = '执行中...';
      //         setTimeout(() => {
      //           done();
      //           setTimeout(() => {
      //             instance.confirmButtonLoading = false;
      //           }, 300);
      //         }, 3000);
      //       } else {
      //         done();
      //       }
      //     }
      //   }).then(action => {
      //     alert('>>>>>');
      //     _this.$message({
      //       type: 'info',
      //       message: 'action: ' + action
      //     });
      //   });
      // })()
    },

  }


</script>

<style>
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
