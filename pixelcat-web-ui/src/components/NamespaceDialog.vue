<template>
    <div>
        <v-dialog
                v-model="namespaceDialog"
                fullscreen
                hide-overlay
                transition="dialog-bottom-transition"
                scrollable
                persistent
        >
            <v-card tile>
                <v-toolbar
                        dense
                        flat
                        dark
                        color="teal"
                >
                    <v-toolbar-title>项目：【{{currentProject.name}}】  环境：【{{currentEnv.name}}】</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-toolbar-title>NameSpace Settings</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn
                            icon
                            dark
                            @click="$emit('close')"
                    >
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-toolbar>
                <v-card-text>
                    <v-data-table
                            v-model="selected"
                            show-select
                            :headers="namespaceHeaders"
                            :items="dataList"
                            item-key="name"
                            class="elevation-1"
                            :page.sync="page"
                            :items-per-page="pageSize"
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
                                            <v-icon small>fa-plus</v-icon>
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
                                                                v-model="currentProject.name"
                                                                label="项目名称"
                                                                readonly
                                                        ></v-text-field>
                                                    </v-col>
                                                    <v-col cols="12">
                                                        <v-text-field
                                                                v-model="currentEnv.name"
                                                                label="环境"
                                                                readonly
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
                                <v-btn
                                        small
                                        color="red"
                                        dark
                                        class="ml-2 mb-2"
                                        @click="deleteNameSpaceItem()"
                                >
                                    <v-icon small>mdi-delete</v-icon>
                                    删除
                                </v-btn>
                                <v-btn
                                        small
                                        dark
                                        class="ml-2 mb-2"
                                        @click="init(currentProject, currentEnv)"
                                >
                                    <v-icon small>mdi-refresh</v-icon>
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

                </v-card-text>
                <div style="flex: 1 1 auto;"></div>
            </v-card>
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
        </v-dialog>
        <!--配置列表-->
        <config-dialog :config-dialog="configDialog" :current-namespace="currentNamespace" :namespace-id="namespaceId" @close="configDialog = false" ref="configDialog"></config-dialog>
    </div>
</template>

<script>
    import ConfigDialog from "./ConfigDialog";
    export default {
        name: "NamespaceDialog",
        components: {ConfigDialog},
        data: () => ({
            currentNamespace: '',
            namespaceId: 0,
            snackbar: false,
            text: ``,
            page: 1,
            pageCount: 0,
            pageSize: 10,
            dialog: false,
            dialogDelete: false,
            configDialog: false,
            selected: [],
            dataList: [],
            editedIndex: -1,
            editedItem: {
                name: '',
            },
            defaultItem: {
                name: '',
            },

        }),
        props:{
            namespaceDialog: Boolean,
            currentProject: Object,
            currentEnv: Object,
        },
        computed:{
            namespaceHeaders () {
                return [
                    {
                        text: 'NameSpace',
                        align: 'start',
                        value: 'name',
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
                return this.editedIndex === -1 ? '新建namespace' : '修改namespace'
            },

        },
        watch:{
            page(){
                this.init(this.currentProject, this.currentEnv);
            }
        },
        methods: {
            init(project, env){
                let me = this;
                me.getDataForTable(project, env)
                    .then(data => {
                        me.$nextTick(() => {
                            me.dataList = data.dataList;
                            me.page = data.page;
                            me.pageCount = data.pageCount;
                            console.log("NameSpace列表", me.dataList)
                        })
                    })
            },
            showTip(text){
                this.snackbar = true;
                this.text = text;
            },
            getDataForTable (project, env) {
                let me = this;
                return new Promise((resolve, reject) => {
                    let params = {
                        projectId: project.id,
                        envId: env.id,
                        page: me.page,
                        pageSize: me.pageSize,
                    };
                    me.$axios.post('/namespace/list', params)
                    // 请求成功后
                        .then(function (response) {
                            let data = response.data;
                            let dataList = data.dataList;
                            let page = data.page;
                            let pageCount = data.pageCount;
                            resolve({
                                dataList,
                                page,
                                pageCount
                            })
                        })
                        // 请求失败处理
                        .catch(function (error) {
                            console.log(error);
                        });
                })
            },
            deleteNameSpaceItem () {
                if (this.selected == "") {
                    this.showTip("请先选择数据！");
                    return;
                }
                this.dialogDelete = true
            },
            editItem (item) {
                this.editedIndex = this.dataList.indexOf(item)
                this.editedItem = Object.assign({}, item)
                this.dialog = true
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
                me.$axios.post('/namespace/delete', params)
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
                this.init(this.currentProject, this.currentEnv);
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultItem)
                    this.editedIndex = -1
                })
            },

            closeDelete () {
                this.dialogDelete = false
                this.init(this.currentProject, this.currentEnv);
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
                        type: 3,
                        name: me.editedItem.name,
                    };
                    me.$axios.post('/namespace/update', params)
                    // 请求成功后
                        .then(function (response) {
                            let data = response.data;
                            if (data.code === 0) {
                                me.showTip("修改成功！");
                            } else {
                                me.showTip("修改失败！" + data.message);
                            }
                        })
                        // 请求失败处理
                        .catch(function (error) {
                            console.log(error);
                            me.showTip(error);
                        });
                } else {
                    // this.dataList.push(this.editedItem)
                    let params = {
                        type: 3,
                        name: me.editedItem.name,
                        projectId: me.currentProject.id,
                        envId: me.currentEnv.id,
                    };
                    me.$axios.post('/namespace/add', params)
                    // 请求成功后
                        .then(function (response) {
                            let data = response.data;
                            if (data.code === 0){
                                me.showTip("新增成功！");
                            } else {
                                me.showTip("新增失败！" + data.message);
                            }
                        })
                        // 请求失败处理
                        .catch(function (error) {
                            console.log(error);
                            me.showTip(error);
                        });
                }
                this.close()
            },


            //----------------------
            showConfigDialog (item) {
                this.currentNamespace = item.name;
                this.namespaceId = item.id;
                this.configDialog = true;
                this.$refs.configDialog.init(item.id);
            },

        }
    }
</script>

<style scoped>

</style>