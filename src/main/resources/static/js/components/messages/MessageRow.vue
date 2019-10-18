<template>
    <v-card class="my-2">
        <v-card-text primary-title>
            <i>({{message.id}})</i>
            {{message.text}}
        </v-card-text>
        <v-card-actions>
            <v-btn value="Edit" @click="edit" small flat round>Edit</v-btn>
            <v-btn icon @click="del" small>
                <v-icon>delete</v-icon>
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        props: ['message', 'editMessage'],
        methods: {
            ...mapActions(['removeMessageActions']),
            edit() {
                this.editMessage(this.message)
            },
            del() {
                this.removeMessageActions(this.message)
            }
        }
    }
</script>

<style scoped>

</style>