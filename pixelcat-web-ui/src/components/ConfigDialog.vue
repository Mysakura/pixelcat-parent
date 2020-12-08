<template>
    <v-dialog
            v-model="configDialog"
            max-width="900"
            transition="dialog-bottom-transition"
            scrollable
            persistent
    >
        <v-card>
            <v-toolbar
                    dense
                    flat
                    dark
                    color="teal"
            >
                <v-card-title class="headline">
                    【{{currentNamespace}}】配置列表
                </v-card-title>
                <v-spacer></v-spacer>
                <v-btn
                        icon
                        dark
                        @click="$emit('close')"
                >
                    <v-icon>mdi-close</v-icon>
                </v-btn>
            </v-toolbar>
            <v-card-text class="mt-6">
                <!--配置列表详细-->
                <v-data-table
                        v-model="configSelected"
                        show-select
                        :headers="configHeaders"
                        :items="configs"
                        :search="search"
                        item-key="id"
                        class="elevation-1"
                        hide-default-footer
                        disable-pagination
                >
                    <template v-slot:top>
                        <v-toolbar flat>
                            <!--新增弹窗-->
                            <v-dialog
                                    v-model="configAddDialog"
                                    max-width="500px"
                            >
                                <template v-slot:activator="{ on, attrs }">
                                    <v-btn
                                            small
                                            color="primary"
                                            dark
                                            v-bind="attrs"
                                            v-on="on"
                                    >
                                        <v-icon small>fa-plus</v-icon>
                                        新建
                                    </v-btn>
                                </template>
                                <v-card>
                                    <v-card-title>
                                        <span class="headline">{{ configFormTitle }}</span>
                                    </v-card-title>

                                    <v-card-text>
                                        <v-container>
                                            <v-row>
                                                <v-col cols="12" v-show="!textMode">
                                                    <v-text-field
                                                            v-model="configEditedItem.key"
                                                            label="key"
                                                    ></v-text-field>
                                                </v-col>
                                                <v-col cols="12" v-show="!textMode">
                                                    <v-textarea
                                                            v-model="configEditedItem.value"
                                                            :value="configEditedItem.value"
                                                            outlined
                                                            label="value"
                                                    ></v-textarea>
                                                </v-col>
                                                <v-col cols="12" v-show="textMode">
                                                    <v-textarea
                                                            v-model="textModeContent"
                                                            outlined
                                                            label="value"
                                                    ></v-textarea>
                                                </v-col>
                                                <v-col cols="12">
                                                    <v-switch
                                                            v-model="textMode"
                                                            :disabled="configEditedIndex !== -1"
                                                            label="文本模式"
                                                            color="error"
                                                            value="error"
                                                            hide-details
                                                    ></v-switch>
                                                </v-col>
                                            </v-row>
                                        </v-container>
                                    </v-card-text>

                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn
                                                color="blue darken-1"
                                                text
                                                @click="closeConfig"
                                        >
                                            取消
                                        </v-btn>
                                        <v-btn
                                                color="blue darken-1"
                                                text
                                                @click="saveConfig"
                                        >
                                            保存
                                        </v-btn>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                            <!--删除按钮-->
                            <v-btn
                                    small
                                    color="red"
                                    dark
                                    class="ml-2"
                                    @click="deleteConfigItem()"
                            >
                                <v-icon small>mdi-delete</v-icon>
                                删除
                            </v-btn>
                            <!--复制-->
                            <v-dialog max-width="500px">
                                <template v-slot:activator="{ on, attrs }">
                                    <v-btn
                                            small
                                            color="green"
                                            dark
                                            class="ml-2"
                                            @click="copyConfig()"
                                            v-bind="attrs"
                                            v-on="on"
                                    >
                                        <v-icon small>fa-clone</v-icon>
                                        复制配置
                                    </v-btn>
                                </template>
                                <v-card>
                                    <v-card-title>
                                        <span class="headline">复制当页<span class="red--text text--darken-1">{{clipboardConfigCount}}</span>个配置</span>
                                    </v-card-title>

                                    <v-card-text>
                                        <v-container>
                                            <v-row>
                                                <v-col cols="12">
                                                    <v-textarea
                                                            :value="clipboard"
                                                            outlined
                                                            auto-grow
                                                    ></v-textarea>
                                                </v-col>
                                            </v-row>
                                        </v-container>
                                    </v-card-text>
                                </v-card>
                            </v-dialog>

                            <v-spacer></v-spacer>
                            <v-text-field
                                    v-model="search"
                                    label="搜索key名称"
                                    class="mx-4"
                                    single-line
                                    hide-details
                            ></v-text-field>

                            <v-dialog v-model="configDeleteDialog" max-width="500px">
                                <v-card>
                                    <v-card-title class="headline">确定删除此项目？</v-card-title>
                                    <v-card-text>
                                        {{configSelected}}
                                    </v-card-text>
                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="blue darken-1" text @click="closeDeleteConfig">取消</v-btn>
                                        <v-btn color="blue darken-1" text @click="deleteConfigConfirm">确定</v-btn>
                                        <v-spacer></v-spacer>
                                    </v-card-actions>
                                </v-card>
                            </v-dialog>
                        </v-toolbar>
                    </template>
                    <template v-slot:item.value="{ item }">
                        <div style="max-width: 150px;">{{item.value}}</div>
                    </template>
                    <template v-slot:item.actions="{ item }">
                        <v-btn
                                small
                                color="green"
                                dark
                                class="mr-2"
                                @click="editConfig(item)"
                        >
                            修改
                        </v-btn>
                    </template>

                </v-data-table>
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                        color="green darken-1"
                        text
                        @click="$emit('close')"
                >
                    关闭
                </v-btn>
            </v-card-actions>
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
</template>

<script>
    export default {
        name: "ConfigDialog",
        props:{
            configDialog: Boolean,
            currentNamespace: String,
        },
        data: () => ({
            snackbar: false,
            text: ``,
            search: ``,
            textMode: false,    // 文本模式
            textModeContent: '',    // 文本模式下内容
            configs: [
                {
                    id: '1',
                    key: 'timeout',
                    value: '1000',
                    namespace: 'dubbo.properties',
                    userName: 'AAA',
                    updateTime: '2020-12-07 14:53:00',
                },
                {
                    id: '2',
                    key: 'zk',
                    value: '127.0.0.1:2181',
                    namespace: 'dubbo.properties',
                    userName: 'AAA',
                    updateTime: '2020-12-07 14:53:00',
                }
            ],
            configSelected: [],
            configAddDialog: false,
            configDeleteDialog: false,
            configEditedIndex: -1,
            configEditedItem: {
                key: '',
                value: '',
                namespace: '',
            },
            defaultConfig: {
                key: '',
                value: '',
                namespace: '',
            },
            clipboardConfigCount:'',
            clipboard:'',
        }),
        mounted(){
            for(let i = 0; i < 100; i++){
                this.configs.push({
                    id: 'a' + i,
                    key: 'zk',
                    value: '127.0.0.1:2181,127.0.0.1:2181,127.0.0.1:2181,127.0.0.1:2181,127.0.0.1:2181,127.0.0.1:2181',
                    namespace: 'dubbo.properties',
                    userName: 'AAA',
                    updateTime: '2020-12-07 14:53:00',
                });
            }
            window.vue = this;
        },
        computed:{
            configHeaders () {
                return [
                    { text: 'key', value: 'key', },
                    { text: 'Value', value: 'value', },
                    { text: '操作者', value: 'userName', },
                    { text: '操作时间', value: 'updateTime', },
                    { text: '操作', value: 'actions' }
                ]
            },
            configFormTitle () {
                return this.configEditedIndex === -1 ? '新建配置' : '修改配置'
            },
        },
        methods:{
            deleteConfigItem () {
                if (this.configSelected == "") {
                    this.snackbar = true;
                    this.text = "请先选择数据！";
                    return;
                }
                this.configDeleteDialog = true
            },

            editConfig (item) {
                this.configEditedIndex = this.configs.indexOf(item)
                this.configEditedItem = Object.assign({}, item)
                this.configAddDialog = true
            },

            closeConfig () {
                this.configAddDialog = false
                this.$nextTick(() => {
                    this.configEditedItem = Object.assign({}, this.defaultConfig)
                    this.configEditedIndex = -1
                })
            },

            closeDeleteConfig () {
                this.configDeleteDialog = false
                this.$nextTick(() => {
                    this.configEditedItem = Object.assign({}, this.defaultConfig)
                    this.configEditedIndex = -1
                })
            },
            saveConfig () {
                if (this.textMode){
                    let arr = this.textModeContent.split("\n");
                    console.log(arr)
                    arr.forEach((item,index,array)=>{
                        let kv = item.split("=");
                        let newObj = Object.assign({}, this.defaultConfig); // 克隆对象
                        newObj.key = kv[0];
                        newObj.value = kv[1];
                        this.configs.push(newObj);
                    })
                    this.closeConfig()
                    return;
                }
                if (this.configEditedIndex > -1) {
                    Object.assign(this.configs[this.configEditedIndex], this.configEditedItem)
                } else {
                    this.configs.push(this.configEditedItem)
                }
                this.closeConfig()
            },
            deleteConfigConfirm () {
                this.configs.splice(this.configEditedIndex, 1)
                this.closeDeleteConfig()
            },

            copyConfig () {
                let kvArr = '';
                let i = 0;
                this.configs.forEach((item,index,array)=>{
                    kvArr += item.key + "=" + item.value + "\n";
                    i++;
                });
                this.clipboardConfigCount = i;
                this.clipboard = kvArr;
            },

        }
    }
</script>

<style scoped>

</style>