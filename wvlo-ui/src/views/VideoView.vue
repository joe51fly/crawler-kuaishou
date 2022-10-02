<template>
  <div class="liveView" style="height: 20rem;width: 20rem">
    <video-player class="vjs-custom-skin"
                  ref="videoPlayer"
                  :options="playerOptions"
                  @ready="onPlayerReadied"
                  @timeupdate="onTimeupdate"
                  :playsinline="playsinline"
                  style="width: 100%;height: 100%">
    </video-player>
  </div>
</template>

<script>
  import {videoPlayer} from 'vue-video-player'
  import 'video.js/dist/video-js.css'
  import 'vue-video-player/src/custom-theme.css'
  import 'videojs-contrib-hls'
    export default {
      components: {
        videoPlayer
      },
      data(){
        return{
          initialized: false,
          currentTech: '',
          streams: {
            rtmp: '',
            hls: ''
          },
          playerOptions: {
            overNative: true,
            autoplay: false,
            controls: true,
            techOrder: ['html5'],
            sourceOrder: true,
            flash: {
              hls: {withCredentials: false}
            },
            html5: {hls: {withCredentials: false}},
            sources: [
              {
                withCredentials: false,
                type: 'application/x-mpegURL',
                src: 'https://ali-origin.hlspull.yximgs.com/gifshow/xSA-3KG31Mg_ma1500.m3u8?auth_key=1624285547-0-0-da3c8156e88bca8775cfba07a80b266d&tsc=origin&oidc=alihbtsc=origin'
              }
            ]
          }
        }
      },
      computed: {
        player() {
          return this.$refs.videoPlayer.player
        },
        currentStream() {
          return this.currentTech === 'Flash' ? 'RTMP' : 'HLS'
        },
        playsinline() {
          let ua = navigator.userAgent.toLocaleLowerCase()
          // x5内核
          if (ua.match(/tencenttraveler/) != null || ua.match(/qqbrowse/) != null) {
            return false
          } else {
            // ios端
            return true
          }
        }
      },
      methods:{
        onPlayerReadied() {
          if (!this.initialized) {
            this.initialized = true
            this.currentTech = this.player.techName_
          }
        },
        // record current time
        onTimeupdate(e) {
          console.log('currentTime', e.cache_.currentTime)
        },
      }
    }
</script>

<style>
  .liveView {
    /*position: relative;*/
  }

</style>
