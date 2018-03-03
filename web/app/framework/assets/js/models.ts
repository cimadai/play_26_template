export class User {
    constructor(private id: string, private name: string, private age: number) {}
}

export interface IApiError {
    category: number;
    code: number;
    caption: string;
    ret_code: number;
}

export interface IGetUsersResponse {
    users: User[];
}
export interface ICreateUserResponse {
    created: number;
}
export interface IDeleteUserResponse {
    deleted: number;
}
