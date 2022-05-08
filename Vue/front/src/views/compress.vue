<template>
  <span>test</span>
</template>

<script>
export default {
  name: "compressView",
  data(){
    return{
      test:''
      }
    },
  methods:{
    testMethod(){
      this.$axios.get("http://localhost:8080/filetest").then(
          response=>{
            console.log("这里")
            this.test = response.data
          }
      )

    }
  },
  created() {
    this.testMethod()
  }
}
</script>

<style scoped>

</style>