<template>
    <div id="top_vue">
        <div class="jumbotron">
            <div class="container">
                <h1 v-cloak>{{ greeting }}</h1>
            </div>
        </div>
        <div class="container">
            <form class="form-horizontal">
                <div class="form-group">
                    <div class="col-md-4">
                        <input type="text" class="form-control" v-model="userName"/>
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-default" v-on:click.prevent="addUser">Add User</button>
                    </div>
                </div>
            </form>
            <div class="row">
                <div class="col-md-4">
                    <ul class="list-group">
                        <li class="list-group-item clearfix" v-for="user in users">
                            {{ user.name }} - {{ user.age }}
                            <div v-on:click="deleteUser(user.id)" class="btn btn-danger btn-xs pull-right"><i class="fa fa-trash"></i> Remove</div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <notifications :speed="400" position="top right" />
    </div>
</template>

<script lang="ts" type="text/tsx">
    import Vue from "vue";
    import {User} from "../models";
    import Component from "vue-class-component";

    @Component
    export default class TopPageComponent extends Vue {
        greeting: string = "Example app";
        userName: string = "";
        users: User[] = [];

        // lifecycle hook
        mounted () {
            this.getUsers();
        }

        // methods
        addUser () {
            if (!this.userName || this.userName.length == 0) {
                this.error({
                    title: "Error",
                    text: "userId must be set."
                });
                return;
            }

            this.API.User.create(new User(this.userName, this.userName, 100))
                .then(data => {
                    this.getUsers();
                })
                .catch(error => {
                    this.error({title: "title1", text: error.caption});
                });
        }

        getUsers () {
            this.API.User.list()
                .then(data => {
                    this.users = data.users;
                })
                .catch(error => {
                    this.error({title: "title2", text: error.caption});
                });
        }

        deleteUser (userId: string) {
            if (!userId || userId.length == 0) {
                this.error({
                    title: "Error",
                    text: "userId must be set."
                });
                return;
            }

            this.API.User.remove(userId)
                .then(data => {
                    this.getUsers();
                })
                .catch(error => {
                    this.error({title: "title3", text: error.caption});
                });
        }
    }
</script>
