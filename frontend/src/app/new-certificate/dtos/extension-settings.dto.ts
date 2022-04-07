export class ExtensionSettingsDto{
  private _extensionsIds: number[];
  private _keyUsageIds: number[];
  private _extendedKeyUsageIds: number[];
  private _publicKey: null;
  private _subjectName: null;


  constructor(extensionsIds: number[], keyUsageIds: number[], extendedKeyUsageIds: number[]) {
    this._extensionsIds = extensionsIds;
    this._keyUsageIds = keyUsageIds;
    this._extendedKeyUsageIds = extendedKeyUsageIds;
    this._publicKey = null;
    this._subjectName = null;
  }


  get extensionsIds(): number[] {
    return this._extensionsIds;
  }

  set extensionsIds(value: number[]) {
    this._extensionsIds = value;
  }

  get keyUsageIds(): number[] {
    return this._keyUsageIds;
  }

  set keyUsageIds(value: number[]) {
    this._keyUsageIds = value;
  }

  get extendedKeyUsageIds(): number[] {
    return this._extendedKeyUsageIds;
  }

  set extendedKeyUsageIds(value: number[]) {
    this._extendedKeyUsageIds = value;
  }
}
