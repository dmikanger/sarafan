<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write something"
                v-model="text"/>
        <v-btn @click="save">
            Save
        </v-btn>
    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'

    export default {
        props: ['messageAttr'],
        data(){
            return {
                text: '',
                id: ''
            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text;
                this.id = newVal.id
            }
        },

        methods: {
            ...mapActions(['addMessageActions', 'updateMessageActions']),
            save() {
                let message = {
                    id: this.id,
                    text: this.text
                };

                if(this.id){
                    this.updateMessageActions(message)
                }else{
                    this.addMessageActions(message)
                }

                this.id = '';
                this.text = '';
            }
        }
    }
</script>

<style scoped>

</style>