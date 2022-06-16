export class LoginUser2faDto {
    private _email: string;
    private _password: string;
    private _code: string;


    constructor(email: string, password: string, code: string) {
        this._email = email;
        this._password = password;
        this._code = code;
    }


    get email(): string {
        return this._email;
    }

    set email(value: string) {
        this._email = value;
    }

    get password(): string {
        return this._password;
    }

    set password(value: string) {
        this._password = value;
    }

    get code(): string {
        return this._code;
    }

    set code(value: string) {
        this._code = value;
    }
}
