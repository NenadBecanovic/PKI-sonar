export class RegisterUsetDto{
  private _type: string;
  private _name: string;
  private _surname: string;
  private _organizationUnit: string;
  private _email: string;
  private _password: string;


  constructor(type: string, name: string, surname: string, organizationUnit: string, email: string, password: string) {
    this._type = type;
    this._name = name;
    this._surname = surname;
    this._organizationUnit = organizationUnit;
    this._email = email;
    this._password = password;
  }


  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get surname(): string {
    return this._surname;
  }

  set surname(value: string) {
    this._surname = value;
  }

  get organizationUnit(): string {
    return this._organizationUnit;
  }

  set organizationUnit(value: string) {
    this._organizationUnit = value;
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
}
