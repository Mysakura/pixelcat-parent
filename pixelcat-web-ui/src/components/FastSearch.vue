<template>
  <v-card>
    <v-card-title   >
      <v-container>
        <v-row :gutter="20">
          <v-col cols="2">
            <v-select
                    v-model="search.project"
                    :items="search.projectList"
                    label="项目"
                    dense
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-select
                    v-model="search.env"
                    :items="search.envList"
                    label="环境"
                    dense
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-select
                    v-model="search.namespace"
                    :items="search.namespaceList"
                    label="Namespace"
                    dense
            ></v-select>
          </v-col>
          <v-col cols="2">
            <v-btn
                    small
                    color="primary"
                    dark
                    @click="search=''"
            >
              查询
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-card-title>
    <v-card-text>
      <v-data-table
              :headers="headers"
              :items="desserts"
              item-key="id"
              class="elevation-1"
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
                    persistent
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
                                v-model="editedItem.projectName"
                                label="项目名称"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                                v-model="editedItem.envName"
                                label="环境名称"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12">
                        <v-text-field
                                v-model="editedItem.name"
                                label="Namespace"
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
            <v-dialog v-model="dialogDelete" max-width="500px">
              <v-card>
                <v-card-title class="headline">Are you sure you want to delete this item?</v-card-title>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn color="blue darken-1" text @click="closeDelete">Cancel</v-btn>
                  <v-btn color="blue darken-1" text @click="deleteItemConfirm">OK</v-btn>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-toolbar>
        </template>
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
                  color="green"
                  dark
                  class="mr-2"
                  @click="editItem(item)"
          >
            修改
          </v-btn>
          <v-btn
                  small
                  color="primary"
                  dark
                  class="mr-2"
                  @click="showConfigDialog(item.name)"
          >
            配置
          </v-btn>
          <v-btn
                  small
                  color="red"
                  dark
                  @click="deleteItem(item)"
          >
            删除
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
      <config-dialog :config-dialog="config.configDialog" :current-namespace="config.currentNamespace" @close="config.configDialog = false"></config-dialog>
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
      dialog: false,
      dialogDelete: false,


      search:{
        project:'',
        env:'',
        namespace:'',
        projectList:['Foo', 'Bar', 'Fizz', 'Buzz'],
        envList:['Foo', 'Bar', 'Fizz', 'Buzz'],
        namespaceList:['Foo', 'Bar', 'Fizz', 'Buzz'],
      },
      config:{
        currentNamespace: '',
        configDialog: false,
      },
      desserts: [
        {
          id: 1,
          projectName: '基金',
          envName: '测试1',
          name: 'zk.properties',
        },
        {
          id: 2,
          projectName: '基金',
          envName: '测试10',
          name: 'db.properties',
        },
        {
          id: 3,
          projectName: '资管',
          envName: '测试5',
          name: 'zk.properties',
        },
      ],
      editedIndex: -1,
      editedItem: {
        projectName: '',
        envName: '',
        name: '',
      },
      defaultItem: {
        projectName: '',
        envName: '',
        name: '',
      },
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
          {text: '操作', value: 'actions'}
        ]
      },
      formTitle () {
        return this.editedIndex === -1 ? '新建Namespace' : '修改Namespace'
      },
    },
    methods:{
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

      editItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem (item) {
        this.editedIndex = this.desserts.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm () {
        this.desserts.splice(this.editedIndex, 1)
        this.closeDelete()
      },


      //-----------
      showConfigDialog (namespace) {
        this.config.currentNamespace = namespace;
        this.config.configDialog = true;
      },

    }
  }
</script>
