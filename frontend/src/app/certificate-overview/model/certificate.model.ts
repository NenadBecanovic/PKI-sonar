import {DateRange} from "./dateRange.model";

export class Certificate {
  private _subject: string;
  private _issuer: string;
  private _validityPeriod: DateRange;
  private _type: string;


  constructor(subject: string, issuer: string, validityPeriod: DateRange, type: string) {
    this._subject = subject;
    this._issuer = issuer;
    this._validityPeriod = validityPeriod;
    this._type = type;
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

  get validityPeriod(): DateRange {
    return this._validityPeriod;
  }

  set validityPeriod(value: DateRange) {
    this._validityPeriod = value;
  }


  get type(): string {
    return this._type;
  }

  set type(value: string) {
    this._type = value;
  }
}
