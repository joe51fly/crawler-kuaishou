<template>
  <div>
    <div id="loginBox">
      <el-card class="box-card">
        <div slot="header" class="clearfix" align="center">
          <h2>登录</h2>
        </div>
        <table>
          <tr>
            <td>短视频Cookie</td>
            <td>
              <el-input v-model="myCookie.shortVideoCookie" placeholder="请输入短视频Cookie"></el-input>
            </td>
          </tr>
          <tr>
            <td style="padding-top: 1rem">直播Cookie</td>
            <td style="padding-top: 1rem">
              <el-input type="password" v-model="myCookie.liveCookie" placeholder="请输入直播Cookie"
                        @keydown.enter.native="doLogin"></el-input>
              <!-- @keydown.enter.native="doLogin"当按下enter键的时候也会执行doLogin方法-->
            </td>
          </tr>
        </table>
        <div style="margin-top: 2rem" align="center">
          <el-button style="width: 20rem" type="primary" @click="doLogin">登录</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
  export default {
    name: "login",
    data() {
      return {
        myCookie: {
          shortVideoCookie: 'aaa',
          liveCookie: 'bbb'
        }
      }
    },
    methods: {
      doLogin() {
        this.$router.push('/home');
        console.log("login");
      }
    }
  }
</script>

<style scoped>
  #loginBox{
    display: flex;
    justify-content: center;
    margin-top: 10rem;
  }
  .box-card{
    width: 30rem;
  }
  table{
    width: 90%;
  }
</style>
