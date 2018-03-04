/**
 * Usage:
 * require("./components/apiMixin");
 * require once in top level script.
 */

import Vue from "vue";

import {IApiError, ICreateUserResponse, IDeleteUserResponse, IGetUsersResponse, User} from "../models";

import AxiosFactory from 'axios';
const axios = AxiosFactory.create({headers: {"X-Requested-With": "XMLHttpRequest"}});

// vueクラスに読み込ませるインターフェース
declare module 'vue/types/vue' {
    export interface Vue {
        API: {
            User: {
                list: () => Promise<IGetUsersResponse>,
                create: (user: User) => Promise<ICreateUserResponse>,
                remove: (userId: string) => Promise<IDeleteUserResponse>,
            }
        };
    }
}

Vue.mixin({
    data: () => { return {
        API: {
            User: {
                list: (): Promise<IGetUsersResponse> => {
                    return new Promise<IGetUsersResponse>((resolve: (value?: IGetUsersResponse) => void, reject: (reason?: IApiError) => void) => {
                        axios
                            .get("/api/users")
                            .then(response => response.data as IGetUsersResponse)
                            .then(data => resolve(data))
                            .catch(e => reject(e.response.data as IApiError));
                    });
                },
                create: (user: User): Promise<ICreateUserResponse> => {
                    return new Promise<ICreateUserResponse>((resolve: (value?: ICreateUserResponse) => void, reject: (reason?: IApiError) => void) => {
                        axios
                            .post("/api/users", {
                                user: user
                            })
                            .then(response => response.data as ICreateUserResponse)
                            .then(data => resolve(data))
                            .catch(e => reject(e.response.data as IApiError));
                    });
                },
                remove: (userId: string): Promise<IDeleteUserResponse> => {
                    return new Promise<IDeleteUserResponse>((resolve: (value?: IDeleteUserResponse) => void, reject: (reason?: IApiError) => void) => {
                        axios
                            .delete(`/api/users/${userId}`)
                            .then(response => response.data as IDeleteUserResponse)
                            .then(data => resolve(data))
                            .catch(e => reject(e.response.data as IApiError));
                    });
                }
            }
        }
    }}
});
