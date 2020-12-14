<template>
  <v-container>
    <v-row>
      <v-col>
        <!--项目-->
        <v-card>
          <v-card-title>
            项目列表
            <v-spacer></v-spacer>
            <v-text-field
                    v-model="search"
                    label="搜索项目名称"
                    class="mx-4"
                    single-line
                    hide-details
            ></v-text-field>
          </v-card-title>
          <v-card-text>
            <v-data-table
                    v-model="selected"
                    show-select
                    :headers="headers"
                    :items="dataList"
                    item-key="name"
                    class="elevation-1"
                    :search="search"
                    hide-default-footer
            >
              <template v-slot:top>
                <v-toolbar flat>
                  <v-dialog
                          v-model="dialog"
                          max-width="500px"
                  >
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                              small
                              color="primary"
                              dark
                              class="mb-2"
                              v-bind="attrs"
                              v-on="on"
                      >
                        新建
                      </v-btn>
                    </template>
                    <v-card>
                      <v-card-title>
                        <span class="headline">{{ formTitle }}</span>
                      </v-card-title>

                      <v-card-text>
                        <v-container>
                          <v-row>
                            <v-col cols="12">
                              <v-text-field
                                      v-model="editedItem.name"
                                      label="项目名称"
                              ></v-text-field>
                            </v-col>
                          </v-row>
                        </v-container>
                      </v-card-text>

                      <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                                color="blue darken-1"
                                text
                                @click="close"
                        >
                          取消
                        </v-btn>
                        <v-btn
                                color="blue darken-1"
                                text
                                @click="save"
                        >
                          保存
                        </v-btn>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                  <v-btn
                          small
                          color="red"
                          dark
                          class="ml-2 mb-2"
                          @click="deleteItem()"
                  >
                    删除
                  </v-btn>
                  <v-btn
                          small
                          dark
                          class="ml-2 mb-2"
                          @click="init()"
                  >
                    刷新
                  </v-btn>
                  <v-dialog v-model="dialogDelete" max-width="500px">
                    <v-card>
                      <v-card-title class="headline">确定删除选中项目？</v-card-title>
                      <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn color="blue darken-1" text @click="closeDelete">取消</v-btn>
                        <v-btn color="blue darken-1" text @click="deleteItemConfirm">确定</v-btn>
                        <v-spacer></v-spacer>
                      </v-card-actions>
                    </v-card>
                  </v-dialog>
                </v-toolbar>
              </template>
              <template v-slot:item.envList="{ item }">
                <v-btn
                        class="mr-2"
                        color="primary"
                        elevation="2"
                        small
                        @click="openNamespace(item, env)"
                        v-for="env in item.envList"
                        :key="env.name"
                >{{env.id}}:{{env.name}}</v-btn>
              </template>
              <template v-slot:item.actions="{ item }">
                <v-btn
                        small
                        color="green"
                        dark
                        class="mr-2"
                        @click="editItem(item)"
                >
                  修改
                </v-btn>
                <v-btn
                        small
                        color="green"
                        dark
                        class="mr-2"
                        @click="openEnv(item)"
                >
                  管理环境
                </v-btn>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
        <!--namespace弹框-->
        <namespace-dialog :namespace-dialog="namespaceDialog" @close="namespaceDialog = false" :current-project="currentProject" :current-env="currentEnv" ref="namespaceDialog"></namespace-dialog>
        <!--提示-->
        <v-snackbar
                v-model="snackbar"
                top
                timeout="2000"
        >
          {{ text }}
          <template v-slot:action="{ attrs }">
            <v-btn
                    icon
                    color="red"
                    text
                    v-bind="attrs"
                    @click="snackbar = false"
            >
              <v-icon>fa-times</v-icon>
            </v-btn>
          </template>
        </v-snackbar>
        <!--修改环境-->
        <env-dialog :env-dialog="envDialog" @close="envDialog = false" :current-project="currentProject" ref="envDialog"></env-dialog>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
  import NamespaceDialog from "../components/NamespaceDialog";
  import EnvDialog from "../components/EnvDialog";
  export default {
    components: {EnvDialog, NamespaceDialog},
    data () {
      return {
        envRules: [
          value => !!value || 'Required.',
          value => (value && value.length >= 3) || 'Min 3 characters',
        ],
        snackbar: false,
        text: ``,

        currentProject:{},
        currentEnv:{},

        namespaceDialog: false,
        envDialog: false,

        dialog: false,
        dialogDelete: false,

        selected: [], // 选中数据
        search: '',

        dataList: [
          // {
          //   name: 'Frozen Yogurt',
          //   envList: ['测试1','测试2'],
          //   username: 'AAA',
          //   updateTimeStr: '2020-12-07 14:53:00',
          // }
        ],

        editedIndex: -1,
        editedItem: {
          name: '',
        },
        defaultItem: {
          name: '',
        },

      }
    },
    computed: {
      headers () {
        return [
          {
            text: 'ID',
            align: 'start',
            value: 'id',
          },
          {
            text: '项目',
            align: 'start',
            value: 'name',
          },
          {
            text: 'ID:环境',
            value: 'envList',
          },
          {
            text: '操作人',
            value: 'username',
          },
          {
            text: '操作时间',
            value: 'updateTimeStr',
          },
          { text: '操作', value: 'actions' }
        ]
      },

      formTitle () {
        return this.editedIndex === -1 ? '新建项目' : '修改项目'
      },
    },
    watch: {
      dialog (val) {
        val || this.close()
      },
      dialogDelete (val) {
        val || this.closeDelete()
      },
    },
    mounted(){
      this.init();
    },
    methods: {
      init(){
        let me = this;
        me.getDataForTable()
                .then(data => {
                  me.dataList = data.dataList;
                  console.log(me.dataList)
                })
      },
      showTip(text){
        this.snackbar = true;
        this.text = text;
      },
      getDataForTable () {
        let me = this;
        return new Promise((resolve, reject) => {
          // 额外的参数，比如headers
          // let options = {
          //     headers: {
          //         'token': '00000'
          //     }
          // }
          // 参数
          let params = {};
          me.$axios.post('/project/list', params/*,options*/)
          // 请求成功后
                  .then(function (response) {
                    let data = response.data;
                    let dataList = data.dataList;
                    resolve({
                      dataList
                    })
                  })
                  // 请求失败处理
                  .catch(function (error) {
                    console.log(error);
                  });
        })
      },



      editItem (item) {
        this.editedIndex = this.dataList.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem () {
        if (this.selected == "") {
          this.showTip("请先选择数据！");
          return;
        }
        this.dialogDelete = true
      },


      deleteItemConfirm () {
        // this.dataList.splice(this.editedIndex, 1)
        let me = this;
        let ids = new Array();
        me.selected.forEach((item, index, arr) => {
          ids.push(item.id);
        })

        let params = {
          ids: ids,
        };
        me.$axios.post('/project/delete', params)
        // 请求成功后
                .then(function (response) {
                  let data = response.data;
                  if (data.code === 0)
                    me.showTip("删除成功！");
                  else
                    me.showTip("删除失败！" + data.message);
                })
                // 请求失败处理
                .catch(function (error) {
                  console.log(error);
                  me.showTip(error);
                });
        this.closeDelete();
      },

      close () {
        this.dialog = false
        this.init();
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.init();
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save () {
        let me = this;

        if (this.editedIndex > -1) {
          // Object.assign(this.dataList[this.editedIndex], this.editedItem)
          let params = {
            id: me.editedItem.id,
            type: 1,
            name: me.editedItem.name,
          };
          me.$axios.post('/project/update', params)
          // 请求成功后
                  .then(function (response) {
                    let data = response.data;
                    if (data.code === 0)
                      me.showTip("修改成功！");
                    else
                      me.showTip("修改失败！" + data.message);
                  })
                  // 请求失败处理
                  .catch(function (error) {
                    console.log(error);
                    me.showTip(error);
                  });
        } else {
          // this.dataList.push(this.editedItem)
          let params = {
            type: 1,
            name: me.editedItem.name,
          };
          me.$axios.post('/project/add', params)
          // 请求成功后
                  .then(function (response) {
                    let data = response.data;
                    if (data.code === 0)
                      me.showTip("新增成功！");
                    else
                      me.showTip("新增失败！" + data.message);
                  })
                  // 请求失败处理
                  .catch(function (error) {
                    console.log(error);
                    me.showTip(error);
                  });
        }
        this.close()
      },
      openNamespace (item, env) {
        this.namespaceDialog = true;
        this.currentProject = item;
        this.currentEnv = env;
        this.$refs.namespaceDialog.init(item, env);
      },

      openEnv (project) {
        this.currentProject = project;
        this.envDialog = true;
        // 调用子组件的初始化方法
        this.$refs.envDialog.init(project);
      },

    },
  }
</script>