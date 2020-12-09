<template>
  <v-card>
    <v-card-title>
      <v-form class="col-12" lazy-validation ref="form">
        <v-container>
          <v-row :gutter="20">
            <v-col cols="2">
              <v-select
                      v-model="search.project"
                      :items="search.projectList"
                      @change="changeProject"
                      item-text="name"
                      item-value="name"
                      label="项目"
                      clearable
                      :rules="[v => !!v || '必选']"
              ></v-select>
            </v-col>
            <v-col cols="2">
              <v-select
                      v-model="search.env"
                      :items="search.envList"
                      @click="selectEnv"
                      item-text="name"
                      item-value="name"
                      label="环境"
                      clearable
                      :rules="[v => !!v || '必选']"
              ></v-select>
            </v-col>
            <v-col cols="2">
              <v-btn
                      small
                      color="primary"
                      class="mt-4"
                      dark
                      @click="searchConfirm"
              >
                查询
              </v-btn>
            </v-col>
          </v-row>
        </v-container>
      </v-form>
    </v-card-title>
    <v-card-text>
      <v-data-table
              :headers="headers"
              :items="dataList"
              item-key="id"
              class="elevation-1"
              :page.sync="page"
              :items-per-page="pageSize"
              hide-default-footer
      >
        <!--改变某个字段的显示方式-->
        <template v-slot:item.envName="{ item }">
          <v-chip
                  class="ma-2"
                  color="cyan"
                  label
                  text-color="white"
          >
            <v-icon left small>
              mdi-label
            </v-icon>
            {{item.envName}}
          </v-chip>
        </template>
        <template v-slot:item.actions="{ item }">
          <v-btn
                  small
                  color="primary"
                  dark
                  class="mr-2"
                  @click="showConfigDialog(item)"
          >
            配置
          </v-btn>
        </template>
      </v-data-table>
      <div class="text-center pt-2">
        <v-pagination
                v-model="page"
                :length="pageCount"
                :total-visible="7"
        ></v-pagination>
      </div>
      <!--配置列表-->
      <config-dialog :config-dialog="config.configDialog" :current-namespace="config.currentNamespace" :namespace-id="config.namespaceId" @close="config.configDialog = false" ref="configDialog"></config-dialog>
    </v-card-text>
  </v-card>
</template>

<script>
  import ConfigDialog from "./ConfigDialog";
  export default {
    name: 'FastSearch',
    components: {ConfigDialog},
    data: () => ({
      page: 1,
      pageCount: 0,
      pageSize: 10,

      search:{
        project:'',
        env:'',
        projectList:[],
        envList:[],
      },
      config:{
        namespaceId: 0,
        currentNamespace: '',
        configDialog: false,
      },
      dataList: [],
    }),
    computed: {
      headers() {
        return [
          {
            text: 'Namespace',
            align: 'start',
            value: 'name',
          },
          {
            text: '项目',
            value: 'projectName',
          },
          {
            text: '环境',
            value: 'envName',
          },
          {
            text: '操作人',
            value: 'username',
          },
          {
            text: '操作时间',
            value: 'updateTimeStr',
          },
          {text: '操作', value: 'actions'}
        ]
      },
    },
    watch:{
      page(){
        this.searchConfirm();
      }
    },
    mounted() {
      this.selectProject();
      window.vue = this;
    },
    methods:{
      selectProject () {
        let me = this;
        let params = {};
        me.$axios.post('/project/list', params)
                // 请求成功后
                .then(function (response) {
                  let data = response.data;
                  let dataList = data.dataList;

                  me.$nextTick(() => {
                    me.search.projectList = dataList;
                    console.log("初始化project列表", me.search.projectList)
                  })
                })
                // 请求失败处理
                .catch(function (error) {
                  console.log(error);
                });
      },
      selectEnv () {
        let me = this;
        let params = {
          projectName: me.search.project
        };
        me.$axios.post('/env/list', params)
        // 请求成功后
                .then(function (response) {
                  let data = response.data;
                  let dataList = data.dataList;
                  me.$nextTick(() => {
                    me.search.envList = dataList;
                    console.log("读取env列表", me.search.envList)
                  })
                })
                // 请求失败处理
                .catch(function (error) {
                  console.log(error);
                });
      },
      changeProject () {
        this.search.envList = [];
      },

      searchConfirm () {
        if (!this.$refs.form.validate()){
          return;
        }
        let params = {
          projectName: this.search.project,
          envName: this.search.env,
          page: this.page,
          pageSize: this.pageSize,
        };
        let me = this;
        this.$axios.post('/namespace/list', params)
                // 请求成功后
                .then(function (response) {
                  let data = response.data;
                  me.$nextTick(() => {
                    me.dataList = data.dataList;
                    me.page = data.page;
                    me.pageCount = data.pageCount;
                    console.log("NameSpace列表", me.dataList, data.pageCount, me.pageCount)
                  })
                })
                // 请求失败处理
                .catch(function (error) {
                  console.log(error);
                });
      },

      showConfigDialog (item) {
        this.config.currentNamespace = item.name;
        this.config.configDialog = true;
        this.config.namespaceId = item.id;

        this.$refs.configDialog.init(item.id);
      },

    }
  }
</script>
