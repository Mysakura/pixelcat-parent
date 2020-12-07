<template>
    <v-navigation-drawer
            v-model="drawer"
            :mini-variant.sync="mini"
            permanent
            clipped
            app
    >
        <v-list-item class="px-2">
            <v-btn
                    icon
                    @click.stop="mini = !mini"
            >
                <v-icon v-show="!mini">mdi-chevron-left</v-icon>
                <v-icon v-show="mini">mdi-chevron-right</v-icon>
            </v-btn>
        </v-list-item>

        <v-divider></v-divider>

        <v-list dense>
            <div v-for="item in menuList" :key="item.title">
                <!--不含子菜单-->
                <v-list-item
                        v-if="item.children == null"
                        :key="item.title"
                        link
                        exact
                        :to="item.url"
                        :color="item.color"
                >
                    <v-list-item-icon>
                        <v-icon>{{ item.icon }}</v-icon>
                    </v-list-item-icon>

                    <v-list-item-content>
                        <v-list-item-title v-text="item.title"></v-list-item-title>
                    </v-list-item-content>
                </v-list-item>
                <!--含有子菜单-->
                <v-list-group
                        v-else
                        no-action
                        :value="true"
                        :prepend-icon="item.icon"
                        :color="item.color"
                >
                    <template v-slot:activator>
                        <v-list-item-content>
                            <v-list-item-title>{{ item.title }}</v-list-item-title>
                        </v-list-item-content>
                    </template>

                    <v-list-item
                            v-for="c in item.children"
                            :key="c.title"
                            link
                            exact
                            :to="c.url"
                            :color="c.color"
                    >
                        <v-list-item-title v-text="c.title"></v-list-item-title>

                        <v-list-item-icon>
                            <v-icon v-text="c.icon"></v-icon>
                        </v-list-item-icon>
                    </v-list-item>
                </v-list-group>
            </div>
        </v-list>
    </v-navigation-drawer>
</template>

<script>
    export default {
        name: "NavBar",
        data: () => ({
            drawer: true,
            menuList: [
                { title: '快速入口', icon: 'mdi-alarm', url: '/', color: 'blue-grey' },
                { title: '项目管理', icon: 'mdi-cog-outline', url: '/project', color: 'teal' },
                { title: '用户管理', icon: 'mdi-account-multiple', color: 'indigo',
                    children:[
                        { title: '用户管理', icon: 'mdi-account', url: '/users', color: 'pink darken-1' },
                        { title: '角色管理', icon: 'mdi-star', url: '/roles', color: 'cyan darken-1' },
                        { title: '权限管理', icon: 'mdi-flag', url: '/auths', color: 'purple darken-1' },
                    ] }
            ],
            mini: true,
        })
    }
</script>

<style scoped>

</style>