<template>
    <v-app>
        <v-toolbar app>
            <v-toolbar-title>Sarafan v.1.09</v-toolbar-title>
            <v-btn flat v-if="profile" :disabled="$route.path === '/'" @click="showMessages">
                Messages
            </v-btn>
            <v-spacer></v-spacer>
            <v-btn flat v-if="profile" :disabled="$route.path === '/profile'" @click="showProfile">
                {{profile.name}}
            </v-btn>
            <v-btn v-if="profile" icon  href="/logout">
                <v-icon>exit_to_app</v-icon>
            </v-btn>
        </v-toolbar>
        <v-content>
            <router-view></router-view>
        </v-content>
    </v-app>
</template>

<script>
    import { mapState, mapMutations } from 'vuex'
    import { addHandler } from "util/ws";

    export default {
        computed: mapState(['profile']),
        methods: {
            ...mapMutations([
                'addMessageMutation',
                'updateMessageMutation',
                'removeMessageMutation',
                'addCommentMutation'
            ]),
            showMessages() {
                this.$router.push('/')
            },
            showProfile() {
                this.$router.push('/profile')
            }
        },
        created() {
            addHandler(data => {
                if (data.objectType === 'MESSAGE'){

                    switch (data.eventType) {
                        case 'CREATE':
                            this.addMessageMutation(data.body);
                            break;
                        case 'UPDATE':
                            this.updateMessageMutation(data.body);
                            break;
                        case 'REMOVE':
                            this.removeMessageMutation(data.body);
                            break;
                        default:
                            console.error(`Looks like the event type is unknown "${data.eventType}"`)
                    }
                }else if (data.objectType === 'COMMENT') {

                    switch (data.eventType) {
                        case 'CREATE':
                            this.addCommentMutation(data.body);
                            break;
                        default:
                            console.error(`Looks like the event type is unknown "${data.eventType}"`)
                    }
                } else {
                    console.error(`Looks like the object type is unknown "${data.objectType}"`)
                }
            })
        },
        beforeMount() {
            if (!this.profile){
                this.$router.replace('/auth')
            }
        }
    }
</script>

<style>

</style>