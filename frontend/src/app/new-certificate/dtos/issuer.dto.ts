export class IssuerDto{
  private _name: string;
  private _surname: string;
  private _email: string;
  private _serialNumber: string;


  constructor(name: string, surname: string, email: string, serialNumber: string) {
    this._name = name;
    this._surname = surname;
    this._email = email;
    this._serialNumber = serialNumber;
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

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get serialNumber(): string {
    return this._serialNumber;
  }

  set serialNumber(value: string) {
    this._serialNumber = value;
  }
}
