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
                    <v-toolbar-title>项目：【{{currentProject}}】  环境：【{{currentEnv}}】</v-toolbar-title>
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
                            v-model="namespaceSelected"
                            show-select
                            :headers="namespaceHeaders"
                            :items="namespaces"
                            item-key="name"
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
                                                                v-model="currentProject"
                                                                label="项目名称"
                                                                readonly
                                                        ></v-text-field>
                                                    </v-col>
                                                    <v-col cols="12">
                                                        <v-text-field
                                                                v-model="currentEnv"
                                                                label="环境"
                                                                readonly
                                                        ></v-text-field>
                                                    </v-col>
                                                    <v-col cols="12">
                                                        <v-text-field
                                                                v-model="editedItem.namespace"
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

                                <v-dialog v-model="dialogDelete" max-width="500px">
                                    <v-card>
                                        <v-card-title class="headline">确定删除此项目？</v-card-title>
                                        <v-card-text>
                                            {{namespaceSelected}}
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
                                    @click="showConfigDialog(item.namespace)"
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
        <config-dialog :config-dialog="configDialog" :current-namespace="currentNamespace" @close="configDialog = false"></config-dialog>
    </div>
</template>

<script>
    import ConfigDialog from "./ConfigDialog";
    export default {
        name: "NamespaceDialog",
        components: {ConfigDialog},
        data: () => ({
            currentNamespace: '',
            snackbar: false,
            text: ``,
            page: 1,
            pageCount: 0,
            pageSize: 10,
            dialog: false,
            dialogDelete: false,
            configDialog: false,
            namespaceSelected: [],
            namespaces: [
                {
                    name: 'Frozen Yogurt',
                    envName: ['测试1','测试2'],
                    namespace: 'dubbo.properties',
                },
                {
                    name: 'Ice cream sandwich',
                    envName: ['测试1','测试2'],
                    namespace: 'zk.properties',
                }
            ],
            editedIndex: -1,
            editedItem: {
                name: '',
                envName: 0,
                namespace: '',
            },
            defaultItem: {
                name: '',
                envName: 0,
                namespace: '',
            },

        }),
        props:{
            namespaceDialog: Boolean,
            currentProject: String,
            currentEnv: String,
        },
        computed:{
            namespaceHeaders () {
                return [
                    {
                        text: 'NameSpace',
                        align: 'start',
                        value: 'namespace',
                    },
                    { text: '操作', value: 'actions' }
                ]
            },

            formTitle () {
                return this.editedIndex === -1 ? '新建namespace' : '修改namespace'
            },

        },
        methods: {
            deleteNameSpaceItem () {
                if (this.namespaceSelected == "") {
                    this.snackbar = true;
                    this.text = "请先选择数据！";
                    return;
                }
                this.dialogDelete = true
            },
            editItem (item) {
                this.editedIndex = this.namespaces.indexOf(item)
                this.editedItem = Object.assign({}, item)
                this.dialog = true
            },
            deleteItemConfirm () {
                this.namespaces.splice(this.editedIndex, 1)
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
                    Object.assign(this.namespaces[this.editedIndex], this.editedItem)
                } else {
                    this.namespaces.push(this.editedItem)
                }
                this.close()
            },


            //----------------------
            showConfigDialog (namespace) {
                this.currentNamespace = namespace;
                this.configDialog = true;
            },

        }
    }
</script>

<style scoped>

</style>