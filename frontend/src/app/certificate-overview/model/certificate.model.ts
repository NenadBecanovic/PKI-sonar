
export class Certificate {
  private _subject: string = "";
  private _issuer: string = "";
  private _startDate: Date = new Date();
  private _endDate: Date = new Date();
  private _type: string = "";
  private _serialNumber: number = 0;


  constructor() {
  }

  get subject(): string {
    return this._subject;
  }

  set subject(value: string) {
    this._subject = value;
  }

  get issuer(): string {
    return this._issuer;
  }

  set issuer(value: string) {
    this._issuer = value;
  }

  get startDate(): Date {
    return this._startDate;
  }

  set startDate(value: Date) {
    this._startDate = value;
  }

  get endDate(): Date {
    return this._endDate;
  }

  set endDate(value: Date) {
    this._endDate = value;
  }

  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }

  get serialNumber(): number {
    return this._serialNumber;
  }

  set serialNumber(value: number) {
    this._serialNumber = value;
  }
}
