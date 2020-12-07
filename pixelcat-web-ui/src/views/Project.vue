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
                    :items="desserts"
                    item-key="name"
                    class="elevation-1"
                    :search="search"
                    :page.sync="page"
                    :items-per-page="pageSize"
                    hide-default-footer
                    @page-count="pageCount = $event"
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
                  <v-dialog v-model="dialogDelete" max-width="500px">
                    <v-card>
                      <v-card-title class="headline">确定删除此项目？</v-card-title>
                      <v-card-text>
                        {{selected}}
                      </v-card-text>
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
              <template v-slot:item.envNames="{ item }">
                <v-btn
                        class="mr-2"
                        color="primary"
                        elevation="2"
                        small
                        @click="openNamespace(item.name, envName)"
                        v-for="envName in item.envNames"
                        :key="envName"
                >{{envName}}</v-btn>
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
                        @click="manageEnv(item)"
                >
                  管理环境
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
          </v-card-text>
        </v-card>
        <!--namespace弹框-->
        <namespace-dialog :namespace-dialog="namespaceDialog" @close="namespaceDialog = false" :current-project="currentProject" :current-env="currentEnv"></namespace-dialog>
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
        <v-navigation-drawer
                v-model="envDrawer"
                absolute
                right
                temporary
                width="400"
        >
          <v-list-item>
            <v-btn dark small @click="addEnv" color="blue darken-1">
              <v-icon small>fa-plus</v-icon>
              新增环境
            </v-btn>
          </v-list-item>

          <v-divider></v-divider>

          <v-list dense>
            <v-list-item
                    dense
                    v-for="item in envList"
                    :key="item.name"
            >
              <v-list-item-content>
                <v-text-field
                        class="mr-12"
                        :rules="envRules"
                        :value="item.name"
                        :readonly="!item.edit"
                        :loading="!item.edit"
                >
                  <template v-slot:progress>
                    <v-progress-linear absolute height="0"></v-progress-linear>
                  </template>
                  <template v-slot:append>
                    <div v-if="item.edit">
                      <v-btn icon small @click="item.edit = false">
                        <v-icon small color="green">fa-check</v-icon>
                      </v-btn>
                      <v-btn icon small @click="item.edit = false">
                        <v-icon small color="red">fa-times</v-icon>
                      </v-btn>
                    </div>
                  </template>
                </v-text-field>
              </v-list-item-content>
              <v-list-item-action-text>
                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn icon small v-bind="attrs" v-on="on" class="mr-2" @click="item.edit = true">
                      <v-icon small color="green lighten-1">fa-pencil</v-icon>
                    </v-btn>
                  </template>
                  <span>修改</span>
                </v-tooltip>

                <v-tooltip top>
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn icon small v-bind="attrs" v-on="on">
                      <v-icon small color="red lighten-1">fa-trash</v-icon>
                    </v-btn>
                  </template>
                  <span>删除</span>
                </v-tooltip>
              </v-list-item-action-text>
            </v-list-item>
          </v-list>
        </v-navigation-drawer>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
  import NamespaceDialog from "../components/NamespaceDialog";
  export default {
    components: {NamespaceDialog},
    data () {
      return {
        envRules: [
          value => !!value || 'Required.',
          value => (value && value.length >= 3) || 'Min 3 characters',
        ],
        snackbar: false,
        text: ``,

        envDrawer: null,

        currentProject:'',
        currentEnv:'',

        namespaceDialog: false,

        dialog: false,
        dialogDelete: false,
        page: 1,
        pageCount: 0,
        pageSize: 10,

        selected: [],
        search: '',

        desserts: [
          {
            name: 'Frozen Yogurt',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Ice cream sandwich',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Eclair',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Cupcake',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Gingerbread',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Jelly bean',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Lollipop',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Honeycomb',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'Donut',
            envNames: ['测试1','测试2'],
          },
          {
            name: 'KitKat',
            envNames: ['测试1','测试2'],
          },
        ],

        editedIndex: -1,
        editedItem: {
          name: '',
        },
        defaultItem: {
          name: '',
        },

        // 环境列表
        envList: [],

      }
    },
    computed: {
      headers () {
        return [
          {
            text: '项目',
            align: 'start',
            value: 'name',
          },
          {
            text: '环境',
            value: 'envNames',
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
    methods: {
      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem () {
        if (this.selected == "") {
          this.snackbar = true;
          this.text = "请先选择数据！";
          return;
        }
        this.dialogDelete = true
      },


      deleteItemConfirm () {
        this.desserts.splice(this.editedIndex, 1)
        this.closeDelete()
      },

      close () {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      closeDelete () {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save () {
        if (this.editedIndex > -1) {
          Object.assign(this.desserts[this.editedIndex], this.editedItem)
        } else {
          this.desserts.push(this.editedItem)
        }
        this.close()
      },
      openNamespace (projectName, envName) {
        this.namespaceDialog = true;
        this.currentProject = projectName;
        this.currentEnv = envName;

      },

      // --------

      addEnv () {
        this.envList.push({
          id: 2,
          name: '',
          projectName:this.currentProject,
          edit: true
        });
      },

      manageEnv (item) {
        this.envList = [];
        item.envNames.forEach((v,index,arr) => {
          this.envList.push({
            id: 1,
            name: v,
            projectName:item.name,
            edit: false
          });
        })
        this.currentProject = item.name;
        this.envDrawer = true
      },

    },
  }
</script>