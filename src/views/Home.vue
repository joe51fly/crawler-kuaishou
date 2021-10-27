<template>
  <div>
    <el-container>
      <el-aside
        width="150px">
        <el-menu :collapse="isCollapse" router >
          <el-menu-item index="/home/login" >
            <i class="el-icon-user-solid" ></i>
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
          <el-menu-item index="/home/my-special-follow">
            <img :src="redHertUrl" class="redHertClass">
            <span slot="title">我的特别关注</span>
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
              <router-view :liveListAllData="list"></router-view>
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
        redHertUrl:"static/pic/redHert-32.ico",
        liveUrl:"static/pic/live-48.ico"
      }
    },
    components: {
      MyHeader, Login
    },
    methods: {

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
    },
  }
</script>

<style>
  /*.redHert{*/
  /*  margin-top: 50%;*/
  /*  bott-left: 50%;*/
  /*}*/
  .redHertClass{
    margin-left: -4px;
  }
  .liveClass{
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
