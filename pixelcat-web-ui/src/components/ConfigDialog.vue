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
                        v-model="selected"
                        show-select
                        :headers="configHeaders"
                        :items="dataList"
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
                                                            v-model="editedItem.key"
                                                            label="key"
                                                    ></v-text-field>
                                                </v-col>
                                                <v-col cols="12" v-show="!textMode">
                                                    <v-textarea
                                                            v-model="editedItem.value"
                                                            :value="editedItem.value"
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
                                                            :disabled="editedIndex !== -1"
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
                            <v-btn
                                    small
                                    dark
                                    class="ml-2"
                                    @click="init(namespaceId)"
                            >
                                <v-icon small>mdi-refresh</v-icon>
                                刷新
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
                                    <v-card-title class="headline">确定删除选中项目？</v-card-title>
                                    <v-card-actions>
                                        <v-spacer></v-spacer>
                                        <v-btn color="blue darken-1" text @click="closeDelete">取消</v-btn>
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
            namespaceId: Number,
        },
        data: () => ({
            snackbar: false,
            text: ``,
            search: ``,
            textMode: false,    // 文本模式
            textModeContent: '',    // 文本模式下内容
            dataList: [
                // {
                //     id: '1',
                //     key: 'timeout',
                //     value: '1000',
                //     namespaceId: '22',
                //     username: 'AAA',
                //     updateTimeStr: '2020-12-07 14:53:00',
                // },
            ],
            selected: [],
            configAddDialog: false,
            configDeleteDialog: false,
            editedIndex: -1,
            editedItem: {
                key: '',
                value: '',
            },
            defaultConfig: {
                key: '',
                value: '',
            },
            clipboardConfigCount:'',
            clipboard:'',
        }),
        mounted(){
            window.vue = this;
        },
        computed:{
            configHeaders () {
                return [
                    { text: 'key', value: 'key', },
                    { text: 'Value', value: 'value', },
                    { text: '操作者', value: 'username', },
                    { text: '操作时间', value: 'updateTimeStr', },
                    { text: '操作', value: 'actions' }
                ]
            },
            configFormTitle () {
                return this.editedIndex === -1 ? '新建配置' : '修改配置'
            },
        },
        methods:{
            init(namespaceId){
                let me = this;
                me.getDataForTable(namespaceId)
                    .then(data => {
                        me.$nextTick(() => {
                            me.dataList = data.dataList;
                            me.page = data.page;
                            me.pageCount = data.pageCount;
                            console.log("Config列表", me.dataList)
                        })
                    })
            },
            showTip(text){
                this.snackbar = true;
                this.text = text;
            },
            getDataForTable (namespaceId) {
                let me = this;
                return new Promise((resolve, reject) => {
                    let params = {
                        namespaceId: namespaceId,
                        page: me.page,
                        pageSize: me.pageSize,
                    };
                    me.$axios.post('/config/list', params)
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
            deleteConfigItem () {
                if (this.selected == "") {
                    this.snackbar = true;
                    this.text = "请先选择数据！";
                    return;
                }
                this.configDeleteDialog = true
            },

            editConfig (item) {
                this.editedIndex = this.dataList.indexOf(item)
                this.editedItem = Object.assign({}, item)
                this.configAddDialog = true
            },

            closeConfig () {
                this.configAddDialog = false
                this.init(this.namespaceId);
                this.$nextTick(() => {
                    this.editedItem = Object.assign({}, this.defaultConfig)
                    this.editedIndex = -1
                })
            },

            closeDelete () {
                this.configDeleteDialog = false
                this.init(this.namespaceId);
                this.$nextTick(() => {
                    this.selected = []
                    this.editedItem = Object.assign({}, this.defaultConfig)
                    this.editedIndex = -1
                })
            },
            saveConfig() {
                let me = this;
                if (me.textMode){
                    let arr = me.textModeContent.split("\n");
                    console.log("文本模式", arr)
                    // arr.forEach((item,index,array)=>{
                    //     let kv = item.split("=");
                    //     let newObj = Object.assign({}, this.defaultConfig); // 克隆对象
                    //     newObj.key = kv[0];
                    //     newObj.value = kv[1];
                    //     this.dataList.push(newObj);
                    // })
                    let params = {
                        namespaceId: me.namespaceId,
                        textModeContent: me.textModeContent,
                    };
                    me.$axios.post('/config/batchAdd', params)
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
                    me.closeConfig();
                    return;
                }

                if (me.editedIndex > -1) {
                    // Object.assign(this.dataList[this.editedIndex], this.editedItem)
                    let params = {
                        id: me.editedItem.id,
                        key: me.editedItem.key,
                        value: me.editedItem.value,
                    };
                    me.$axios.post('/config/update', params)
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
                        namespaceId: me.namespaceId,
                        key: me.editedItem.key,
                        value: me.editedItem.value,
                    };
                    me.$axios.post('/config/add', params)
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
                me.closeConfig();
            },
            deleteConfigConfirm () {
                let me = this;
                let ids = new Array();
                me.selected.forEach((item, index, arr) => {
                    ids.push(item.id);
                })

                let params = {
                    ids: ids,
                };
                me.$axios.post('/config/delete', params)
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

            copyConfig () {
                let kvArr = '';
                let i = 0;
                this.dataList.forEach((item,index,array)=>{
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