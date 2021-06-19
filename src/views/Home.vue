<template>
  <div>
    <el-container>
      <el-header>
          <MyHeader :list="list"></MyHeader>
      </el-header>
      <el-container>
        <el-container>
          <el-main>
            <div>
              <router-view :list="list"></router-view>
            </div>
          </el-main>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import MyHeader from "./MyHeader";
  export default {
    name: "home",
    data() {
      return {
        homeMsg: "home-components",
        list: [],
      }
    },
    components:{
      MyHeader
    },
    mounted() {
      this.$jsonp(baseUrl + '/data', {
        callbackQuery: "callback",
        callbackName: "aaaaa",
        output: "jsonp",
      }).then((res) => {
        // console.log(typeof res);
        // console.log("res:",res)
        if (res.follow === undefined || res.follow === null) {
          this.$message({
            showClose: true,
            message: res.bodyText,
            type: 'warning',
            duration: 10000,
          });
        } else {
          this.list = res.follow;
          this.list != null ? this.isLoading = false : this.isLoading = true;
        }
      }).catch(err => {
        console.log(err);
      })
    },
  }
</script>

<style>
  .el-container{
    /*border: 1px solid #aaa*/
    align-items: center;
    border-radius: 4px;
  }
  .el-header{
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
