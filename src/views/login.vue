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
              <el-input v-model="myCookie.inputShortVideoCookie" placeholder="请输入短视频Cookie"></el-input>
            </td>
          </tr>
          <tr>
            <td style="padding-top: 1rem">直播Cookie</td>
            <td style="padding-top: 1rem">
              <el-input v-model="myCookie.inputLiveCookie" placeholder="请输入直播Cookie"
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
          inputShortVideoCookie: "",
          inputLiveCookie: ""
        }
      }
    },
    methods: {
      doLogin() {
        this.$http.post(
          baseUrl + '/login',
          //参数部分，将会拼接在url后面
          /*params: {
            liveCookie: this.myCookie.inputLiveCookie,
            shortVideoCookie: this.myCookie.inputShortVideoCookie,
          },*/
          {
            liveCookie: this.myCookie.inputLiveCookie,
            shortVideoCookie: this.myCookie.inputShortVideoCookie,
            callback:"aaaaaaa"
          },
          {
            emulateJSON: true
          }).then(response=> {
          // console.log(response.body);
          // console.log(response);
          if (response.status === 200){
            if (response.body.success){
              this.$router.push('/home');
            }else {
              this.$message({
                showClose: true,
                message: response.body.message,
                type: 'error',
                duration: 10000,
              });
            }
          }else {
            this.$message({
              showClose: true,
              message: "出问题了，请联系网站管理员查找原因："+response.status,
              type: 'error',
              duration: 10000,
            });
          }

        }).catch(function (response) {
          //出错处理
          console.log(response)
        })

        /*doLogin() {
        this.$jsonp(baseUrl + '/ks/login', {
          callbackQuery: "callback",
          callbackName: "aaaaa",
          liveCookie:this.myCookie.inputLiveCookie,
          shortVideoCookie:this.myCookie.inputShortVideoCookie,
          output: "jsonp",
        }).then((res) => {
          console.log(res);

        });
        // this.$router.push('/home');
        console.log("login");
      }*/
      }
    }
  }
</script>

<style scoped>
  #loginBox {
    display: flex;
    justify-content: center;
    margin-top: 2rem;
  }

  .box-card {
    width: 30rem;
  }

  table {
    width: 90%;
  }
</style>
