export class UserTokenStateDto{
  private _accessToken: string;
  private _roles: [];
  private _expiresIn: number;


  constructor(accessToken: string, roles: [], expiresIn: number) {
    this._accessToken = accessToken;
    this._roles = roles;
    this._expiresIn = expiresIn;
  }


  get accessToken(): string {
    return this._accessToken;
  }

  set accessToken(value: string) {
    this._accessToken = value;
  }

  get roles(): [] {
    return this._roles;
  }

  set roles(value: []) {
    this._roles = value;
  }

  get expiresIn(): number {
    return this._expiresIn;
  }

  set expiresIn(value: number) {
    this._expiresIn = value;
  }
}
