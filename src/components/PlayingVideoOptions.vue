<template>
  <div>
    <el-button circle size="small" class="videoClose" type="text"
               @click="switchVideoUrl(listData.hlsPlayUrl,listData.playUrls[0].url,$event,theVideoIndex)">切换源
    </el-button>
    <el-button circle size="small" class="videoClose" @click="closeVideoPlayer" type="text">关闭</el-button>
    <br/>
    <input type="text" class="playIpt" placeholder="正在播放的视频链接"/>
    <el-button  circle  type="text"
                class="playIpt"
                v-clipboard:copy="thePlayingVideoUrl"
                v-clipboard:success="onCopy"
                v-clipboard:error="onError">
      在PotPlayer中播放
    </el-button>
  </div>
</template>

<script>
import $ from "jquery";


let myPlayer;

export default {
  name:"playInPotplayer",
  props:{
    listData: {
      type:Object
    },
    myVideoPlayer:{
      type:Object
    },
    lastLiItemIdex:{
      type:Number
    },
    /*该视频的索引位置*/
    theVideoIndex:{
      type:Number
    },
    thePlayingVideoUrl:{
      type:String
    },
  },
  methods:{
    onCopy (e) {
      //提示框
      this.$notify({
        title: '成功',
        message: '内容已复制到剪切板！',
        type: 'success'
      });
      window.location.href="ks-vue://";
    },
    // 复制失败时的回调函数
    onError (e) {
      this.$notify({
        title: '失败',
        message: '抱歉，复制失败！',
        type: 'error'
      });
    },

    switchVideoUrl(liveUrlFromPic, flvUrl, event, index) {
      this.$emit("switchPlayVideo",liveUrlFromPic, flvUrl,index);
    },

    closeVideoPlayer() {
      $('#videoPlayer').hide();/*//隐藏视频播放器*/
      this.myVideoPlayer.dispose();/*//停止*/
      $('#videoDivBox' + this.lastLiItemIdex).css('display', 'none');
      $('#videoDivBox' + this.lastLiItemIdex + ' + div').css('display', 'none');
      this.$parent.$refs.name[this.lastLiItemIdex].childNodes[2].innerHTML = '';
      this.$emit("changeVideoPlayer");
      $('#imgDiv' + this.lastLiItemIdex).css('display', 'block');
    }
  },
}
</script>

<style>
.videoClose {
  margin-top: 1px;
  margin-bottom: 1px;
  font-size: medium;
}
.playIpt {
  width: 18rem;
}

</style>
