export class DateRange{
  private _start: Date;
  private _end: Date;

  constructor(start: Date, end: Date) {
    this._start = start;
    this._end = end;
  }


  get start(): Date {
    return this._start;
  }

  set start(value: Date) {
    this._start = value;
  }

  get end(): Date {
    return this._end;
  }

  set end(value: Date) {
    this._end = value;
  }
}
