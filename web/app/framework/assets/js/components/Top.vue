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

    import {IApiError, ICreateUserResponse, IDeleteUserResponse, IGetUsersResponse} from "../models";

    import AxiosFactory from 'axios';
    const axios = AxiosFactory.create({headers: {"X-Requested-With": "XMLHttpRequest"}});

    export default Vue.extend({
        data: () => {
            return {
                greeting: "Example app",
                userName: "",
                users: []
            }
        },
        mounted: function () {
            this.getUsers();
        },
        methods: {
            addUser: function() {
                if (!this.userName || this.userName.length == 0) {
                    this.error({
                        title: "Error",
                        text: "userId must be set."
                    });
                    return;
                }

                axios
                    .post("/api/users", {
                        user: {
                            id: this.userName,
                            name: this.userName,
                            age: 100,
                        }
                    })
                    .then(response => response.data as ICreateUserResponse)
                    .then(() => {
                        this.getUsers();
                    })
                    .catch(e => {
                        const apiError = e.response.data as IApiError;
                        this.error({
                            title: "title1",
                            text: apiError.caption
                        });
                    })
            },
            getUsers: function() {
                axios
                    .get("/api/users")
                    .then(response => response.data as IGetUsersResponse)
                    .then(data => {
                        this.users = data.users;
                    })
                    .catch(e => {
                        const apiError = e.response.data as IApiError;
                        this.error({
                            title: "title2",
                            text: apiError.caption
                        });
                    });
            },
            deleteUser: function(userId: string) {
                if (!userId || userId.length == 0) {
                    this.error({
                        title: "Error",
                        text: "userId must be set."
                    });
                    return;
                }

                axios
                    .delete(`/api/users/${userId}`)
                    .then(response => response.data as IDeleteUserResponse)
                    .then(() => {
                        this.getUsers();
                    })
                    .catch(e => {
                        const apiError = e.response.data as IApiError;
                        this.error({
                            title: "title3",
                            text: apiError.caption
                        });
                    });
            }
        }
    })
</script>
