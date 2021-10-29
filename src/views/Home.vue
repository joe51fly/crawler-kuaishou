<template>
  <div>
    <el-container>
      <el-aside
        width="150px">
        <el-menu :collapse="isCollapse" router>
          <el-menu-item index="/home/login">
            <i class="el-icon-user-solid"></i>
            <span slot="title">登录</span>
          </el-menu-item>
          <el-menu-item index="/home/live-list">
            <img :src="liveUrl" class="liveClass">
            <span slot="title">直播</span>
          </el-menu-item>
          <el-menu-item index="/home/my-favorite-video">
            <img :src="redHertUrl" class="redHertClass">
            <span slot="title">点赞的短视频</span>
          </el-menu-item>
          <el-menu-item index="/home/my-special-follow" @click="changeToMyfollow">
            <img :src="redHertUrl" class="redHertClass">
            <span slot="title">我的特别关注</span>
          </el-menu-item>
          <el-menu-item index="/home/my-special-follow" @click="changeToNotIsMyfollow">
            <img :src="redHertUrl" class="redHertClass">
            <span slot="title">未特别关注</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-header>
        <MyHeader :list="list"></MyHeader>
      </el-header>
      <el-container>
        <el-container>
          <el-main>
            <div>
              <!--              <router-view :list="list" :myFavoriteList="myFavoriteList" :myFavoriteObject="myFavoriteObject"></router-view>-->
              <router-view :liveListAllData="list"
                           :theMyFollowListdata="theMyFollowListdata"
                           :theTitle="theTitle">
              </router-view>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import MyHeader from "./MyHeader";
import Login from "./login";

export default {
  name: "home",
  data() {
    return {
      homeMsg: "home-components",
      list: [],
      isCollapse: true,
      redHertUrl: "static/pic/redHert-32.ico",
      liveUrl: "static/pic/live-48.ico",
      myNewSpecFollowRes: [],
      notIsMySpecialFollow: [],
      /*切换列表数据*/
      theMyFollowListdata: [],
      /*MySpecialFollow页面的标题*/
      theTitle: "特别关注",
    }
  },
  components: {
    MyHeader, Login
  },
  methods: {
    changeToNotIsMyfollow() {
      this.theMyFollowListdata = this.notIsMySpecialFollow;
      this.theTitle = "还未特别关注的主播";
      console.log("theMyFollowListdata",this.theMyFollowListdata);
    },
    changeToMyfollow() {
      this.theMyFollowListdata = this.myNewSpecFollowRes;
      this.theTitle = "特别关注的主播";
      console.log("theMyFollowListdata",this.theMyFollowListdata);
    },
  },

  mounted() {



    this.$http.post(baseUrl + '/ks/data',
      // baseUrl + '/ks/test/live-data',
      //参数部分，将会拼接在url后面
      {
        callback: "a"
      },
      {
        emulateJSON: true
      }).then(response => {
      // console.log(response);
      // console.log(response.body.data.follow);
      if (response.status !== 200) {
        this.$message({
          showClose: true,
          message: "出问题了，请联系网站管理员查找原因：" + response.status,
          type: 'error',
          duration: 10000,
        });
      } else if (response.body.success) {
        this.list = response.body.data.follow;
        // console.log(response.body.data.result.follow);
        // this.list = response.body.data.result.follow;
        this.list != null ? this.isLoading = false : this.isLoading = true;
      } else {
        this.$message({
          showClose: true,
          message: response.body.message,
          type: 'error',
          duration: 10000,
        });
      }
    }).catch(function (response) {
      //出错处理
      console.log(response)
    });
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
        if(this.myNewSpecFollowRes.length !== 0){
          window.sessionStorage.setItem("myNewSpecFollowRes_key",JSON.stringify(this.myNewSpecFollowRes));
        }
        //提示框
        this.$notify({
          title: '特别关注',
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
        console.log("notIsMySpecialFollowRes", notIsMySpecialFollowRes.body.data.result);
        this.notIsMySpecialFollow = notIsMySpecialFollowRes.body.data.result;
        if(this.notIsMySpecialFollow.length !== 0){
          window.sessionStorage.setItem("notIsMySpecialFollow_key",JSON.stringify(this.notIsMySpecialFollow));
        }
        //提示框
        this.$notify({
          title: '不是特别关注',
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
  },
  watch:{
    theMyFollowListdata:{
      deep:true,
      handler:function (newValue) {
        console.log("aaa");
        if (newValue.length === 0){
          if (this.theTitle === '还未特别关注的主播') {
            this.theMyFollowListdata = JSON.parse( window.sessionStorage.getItem("notIsMySpecialFollow_key"));
            this.$options.methods.changeToNotIsMyfollow();
          }else if (this.theTitle === '特别关注的主播'){
            this.theMyFollowListdata = JSON.parse( window.sessionStorage.getItem("myNewSpecFollowRes_key"));
            this.$options.methods.changeToMyfollow();
          }
        }
      }
    }
  },
}
</script>

<style>
/*.redHert{*/
/*  margin-top: 50%;*/
/*  bott-left: 50%;*/
/*}*/
.redHertClass {
  margin-left: -4px;
}

.liveClass {
  margin-left: -10px;
}

.el-aside {
  /*display: flex;*/
  z-index: 2;
  float: left;
  position: fixed;
  top: 100px;

}

.el-container {
  /*border: 1px solid #aaa*/
  /*align-items: center;*/
  border-radius: 4px;
}

.el-header {
  /*border-radius: 4px;*/
  height: auto;
  font-size: 1.1rem;
  color: #333;
  width: 100%;
  line-height: 60px;
  background-color: #DCDFE6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
}

.el-main {
  border-radius: 4px;
  padding: 2px;
  overflow: hidden;
}
</style>
